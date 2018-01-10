package com.yuwubao.controllers;

import com.yuwubao.entities.*;
import com.yuwubao.services.*;
import com.yuwubao.services.impl.ArticleSearchService;
import com.yuwubao.util.Const;
import com.yuwubao.util.RestApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyu on 2017/10/20.
 */
@RestController
@RequestMapping("/article")
@Transactional
@CrossOrigin
public class ArticleController {

    private final static Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleSearchService articleSearchService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ArticleSortService articleSortService;

    @Autowired
    private FileUploadController fileUploadController;

    @Autowired
    private OperationNoteService operationNoteService;

    @Autowired
    private UserService userService;

    /**
     * 查询文章
     * @param index  第几页
     * @param size  每页几条
     * @param field  查询字段
     * @param keyword  查询值
     * @param beginTime  开始时间
     * @param endTime  结束时间
     * @param textTypeId  文章分类Id
     * @param organizationId  机构Id
     *
     */
    @GetMapping("/findAll")
    public RestApiResponse<Page<ArticleEntity>> findAll(@RequestParam(defaultValue = "1", required = false)int index,
                                @RequestParam(defaultValue = "10", required = false)int size,
                                @RequestParam(required = false, defaultValue = "")String field,
                                @RequestParam(required = false, defaultValue = "")String keyword,
                                @RequestParam(required = false, defaultValue = "")String beginTime,
                                @RequestParam(required = false, defaultValue = "")String endTime,
                                @RequestParam(required = false, defaultValue = "0")int textTypeId,
                                @RequestParam(required = false, defaultValue = "0")int organizationId){
        RestApiResponse<Page<ArticleEntity>> result = new RestApiResponse<Page<ArticleEntity>>();
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("field", field);
            map.put("keyword", keyword);
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            Pageable pageAble = new PageRequest(index - 1, size);
            Page<ArticleEntity> list = articleService.findAll(map, pageAble, textTypeId, organizationId);
            result.successResponse(Const.SUCCESS, list, "获取列表成功");
        } catch (Exception e) {
            logger.warn("文章列表查询异常", e);
            result.failedApiResponse(Const.FAILED, "文章列表查询异常");
        }
        return result;
    }

    /**
     * 新增文章
     */
    @PostMapping("/add")
    public RestApiResponse<ArticleEntity> add(@RequestBody ArticleEntity articleEntity, @RequestParam int userId) {
        RestApiResponse<ArticleEntity> result = new RestApiResponse<ArticleEntity>();
        try {
            OrganizationEntity organizationEntity = organizationService.findOne(articleEntity.getOrganizationId());
            if (organizationEntity == null) {
                result.failedApiResponse(Const.FAILED, "指定的机构不存在");
                return result;
            }
            ArticleSortEntity articleSortEntity = articleSortService.findById(articleEntity.getTextTypeId());
            if (articleSortEntity == null) {
                result.failedApiResponse(Const.FAILED, "文章类型不存在，请重新指定");
                return result;
            }
            //articleEntity.setAddTime(new Date());
            ArticleEntity article = articleService.add(articleEntity);
            if (article == null) {
                result.failedApiResponse(Const.FAILED, "添加失败");
                return result;
            }
            if (articleEntity.getShield() == 0) {
                articleSearchService.createDoc(article);
            }

            //保存当前用户的操作记录
            UserEntity userEntity = userService.findById(userId);

            OperationNoteEntity noteEntity = new OperationNoteEntity();
            noteEntity.setUserName(userEntity.getUsername());
            noteEntity.setOperationLog(userEntity.getUsername()+"添加了文章:" + article.getTitle());
            noteEntity.setOperationTime(new Date());

            operationNoteService.save(noteEntity);

            result.successResponse(Const.SUCCESS, article, "添加成功");
        } catch (Exception e) {
            logger.warn("新增文章异常", e);
            result.failedApiResponse(Const.FAILED, "新增文章异常");
        }
        return result;
    }

    /**
     *  删除文章
     * @param id 文章Id
     *
     */
    //@DeleteMapping("/delete")
    @PostMapping("/delete")
    public RestApiResponse<ArticleEntity> delete(@RequestParam int id, @RequestParam int userId) {
        RestApiResponse<ArticleEntity> result = new RestApiResponse<ArticleEntity>();
        try {
            ArticleEntity articleEntity = articleService.delete(id);
            if (articleEntity == null) {
                result.failedApiResponse(Const.FAILED, "删除失败,文章不存在");
                return result;
            }
            articleSearchService.delete(String.valueOf(id));
            if (StringUtils.isNotBlank(articleEntity.getImgUrl())) {
                fileUploadController.deleteFile(articleEntity.getImgUrl());
            }

            //保存当前用户的操作记录
            UserEntity userEntity = userService.findById(userId);

            OperationNoteEntity noteEntity = new OperationNoteEntity();
            noteEntity.setUserName(userEntity.getUsername());
            noteEntity.setOperationLog(userEntity.getUsername()+"删除了文章:" + articleEntity.getTitle());
            noteEntity.setOperationTime(new Date());

            operationNoteService.save(noteEntity);

            result.successResponse(Const.SUCCESS, articleEntity, "删除成功");
        } catch (Exception e) {
            logger.warn("删除文章异常", e);
            result.failedApiResponse(Const.FAILED, "删除文章异常");
        }
        return result;
    }

    /**
     * 修改文章
     */
    //@PutMapping("/update")
    @PostMapping("/update")
    public RestApiResponse<ArticleEntity> update(@RequestBody ArticleEntity articleEntity, @RequestParam int userId) {
        RestApiResponse<ArticleEntity> result = new RestApiResponse<ArticleEntity>();
        try {
            ArticleEntity oldEntity = articleService.findById(articleEntity.getId());
            articleEntity.setAddTime(oldEntity.getAddTime());
            if (oldEntity.getOrganizationId() != articleEntity.getOrganizationId()) {
                OrganizationEntity organizationEntity = organizationService.findOne(articleEntity.getOrganizationId());
                if (organizationEntity == null) {
                    result.failedApiResponse(Const.FAILED, "指定的机构不存在");
                    return result;
                }
            }

            if (oldEntity.getTextTypeId() != articleEntity.getTextTypeId()) {
                ArticleSortEntity articleSortEntity = articleSortService.findById(articleEntity.getTextTypeId());
                if (articleSortEntity == null) {
                    result.failedApiResponse(Const.FAILED, "文章类型不存在，请重新指定");
                    return result;
                }
            }
            ArticleEntity article = articleService.update(articleEntity);
            if (article == null) {
                result.failedApiResponse(Const.FAILED, "修改失败，文章不存在");
                return result;
            }

            if (article.getShield() == 0) {
                articleSearchService.createDoc(article);
            } else {
                articleSearchService.delete(String.valueOf(article.getId()));
            }

            //保存当前用户的操作记录
            UserEntity userEntity = userService.findById(userId);

            OperationNoteEntity noteEntity = new OperationNoteEntity();
            noteEntity.setUserName(userEntity.getUsername());
            noteEntity.setOperationLog(userEntity.getUsername()+"修改了文章:" + articleEntity.getTitle());
            noteEntity.setOperationTime(new Date());

            operationNoteService.save(noteEntity);
            result.successResponse(Const.SUCCESS, article, "修改成功");
        } catch (Exception e) {
            logger.warn("修改文章异常", e);
            result.failedApiResponse(Const.FAILED, "修改文章异常");
        }
        return result;

    }

    /**
     * 获取文章详情
     * @param id  文章id
     * @return
     */
    @GetMapping("/articleDetails")
    public RestApiResponse<ArticleEntity> findArticleById(@RequestParam int id) {
        RestApiResponse<ArticleEntity> result = new RestApiResponse<ArticleEntity>();
        try {
            ArticleEntity articleEntity = articleService.findById(id);
            result.successResponse(Const.SUCCESS, articleEntity);
        } catch (Exception e) {
            logger.warn("文章获取异常", e);
            result.failedApiResponse(Const.FAILED, "文章获取异常");
        }
        return result;
    }

}
