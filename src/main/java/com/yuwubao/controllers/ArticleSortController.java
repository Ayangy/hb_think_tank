package com.yuwubao.controllers;

import com.yuwubao.entities.ArticleEntity;
import com.yuwubao.entities.ArticleSortEntity;
import com.yuwubao.entities.OperationNoteEntity;
import com.yuwubao.entities.UserEntity;
import com.yuwubao.services.ArticleService;
import com.yuwubao.services.ArticleSortService;
import com.yuwubao.services.OperationNoteService;
import com.yuwubao.services.UserService;
import com.yuwubao.util.Const;
import com.yuwubao.util.RestApiResponse;
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
import java.util.List;
import java.util.Map;

/**
 * Created by yangyu on 2017/10/24.
 */
@RestController
@RequestMapping("/articleSort")
@Transactional
@CrossOrigin
public class ArticleSortController {

    private final static Logger logger = LoggerFactory.getLogger(ArticleSortController.class);

    @Autowired
    private ArticleSortService articleSortService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private OperationNoteService operationNoteService;

    /**
     * 查询文章分类
     * @param index
     * @param size
     * @param field
     * @param keyword
     * @return
     */
    @GetMapping("/findAll")
    public RestApiResponse<Page<ArticleSortEntity>> findAll(@RequestParam(defaultValue = "1", required = false)int index,
                                @RequestParam(defaultValue = "10", required = false)int size,
                                @RequestParam(required = false, defaultValue = "")String field,
                                @RequestParam(required = false, defaultValue = "")String keyword,
                                @RequestParam(required = false, defaultValue = "0") int parentId){
        RestApiResponse<Page<ArticleSortEntity>> result = new RestApiResponse<Page<ArticleSortEntity>>();
        try {
            Map<String, String> map = new HashMap();
            map.put("field", field);
            map.put("keyword", keyword);
            Pageable pageAble = new PageRequest(index - 1, size);
            Page<ArticleSortEntity> list = articleSortService.findAll(map, pageAble, parentId);
            result.successResponse(Const.SUCCESS, list, "获取文章列表成功");
        } catch (Exception e) {
            logger.warn("文章分类条件查询异常：", e);
            result.failedApiResponse(Const.FAILED, "文章分类条件查询异常");
        }
        return result;
    }

    /**
     * 新增文章分类
     */
    @PostMapping("/add")
    public RestApiResponse<ArticleSortEntity> add(@RequestBody ArticleSortEntity articleSortEntity, @RequestParam int userId) {
        RestApiResponse<ArticleSortEntity> result = new RestApiResponse<ArticleSortEntity>();
        try {
            int parentId = articleSortEntity.getParentId();
            if (parentId != 0) {
                ArticleSortEntity sortEntity = articleSortService.findById(parentId);
                if (sortEntity == null) {
                    result.failedApiResponse(Const.FAILED, "指定的父级类型不存在");
                    return result;
                }
            }
            ArticleSortEntity articleSort = articleSortService.add(articleSortEntity);
            if (articleSort == null) {
                result.failedApiResponse(Const.FAILED, "添加失败");
                return result;
            }

            //保存当前用户的操作记录
            UserEntity userEntity = userService.findById(userId);

            OperationNoteEntity noteEntity = new OperationNoteEntity();
            noteEntity.setUserName(userEntity.getUsername());
            noteEntity.setOperationLog(userEntity.getUsername()+"新增了文章类型:" + articleSort.getName());
            noteEntity.setOperationTime(new Date());

            operationNoteService.save(noteEntity);

            result.successResponse(Const.SUCCESS, articleSort, "添加成功");
        } catch (Exception e) {
            logger.warn("新增文章分类异常", e);
            result.failedApiResponse(Const.FAILED, "新增文章分类异常");
        }
        return result;
    }

    /**
     * 删除文章分类
     */
    //@DeleteMapping("/delete")
    @PostMapping("/delete")
    public RestApiResponse<ArticleSortEntity> delete(@RequestParam int id, @RequestParam int userId) {
        RestApiResponse<ArticleSortEntity> result = new RestApiResponse<ArticleSortEntity>();
        try {
            List<ArticleEntity> articleList = articleService.findByTextTypeId(id);
            if (articleList.size() > 0) {
                result.failedApiResponse(Const.FAILED, "此类文章未删净，删除失败");
                return result;
            }
            ArticleSortEntity sortEntity = articleSortService.findById(id);
            if (sortEntity == null) {
                result.failedApiResponse(Const.FAILED, "删除的文章分类不存在");
                return result;
            }
            //保存当前用户的操作记录
            UserEntity userEntity = userService.findById(userId);
            OperationNoteEntity noteEntity = new OperationNoteEntity();
            if (sortEntity.getParentId() > 0) {
                ArticleSortEntity parentSort = articleSortService.findById(sortEntity.getParentId());
                noteEntity.setOperationLog(userEntity.getUsername() + "删除一级文章类型【" + parentSort.getName() + "】下的子类型【" + sortEntity.getName() + "】");
            } else {
                noteEntity.setOperationLog(userEntity.getUsername() + "删除一级文章类型【" + sortEntity.getName() + "】");
            }
            noteEntity.setUserName(userEntity.getUsername());
            noteEntity.setOperationTime(new Date());
            operationNoteService.save(noteEntity);

            ArticleSortEntity articleSort = articleSortService.delete(id);
            if (articleSort == null) {
                result.failedApiResponse(Const.FAILED, "删除失败");
                return result;
            }
            result.successResponse(Const.SUCCESS, articleSort, "删除成功");
        } catch (Exception e) {
            logger.warn("删除文章分类异常", e);
            result.failedApiResponse(Const.FAILED, "删除文章分类异常");
        }
        return result;
    }

    /**
     * 修改文章分类
     */
    //@PutMapping("/update")
    @PostMapping("/update")
    public RestApiResponse<ArticleSortEntity> update(@RequestBody ArticleSortEntity articleSortEntity, @RequestParam int userId) {
        RestApiResponse<ArticleSortEntity> result = new RestApiResponse<ArticleSortEntity>();
        try {
            ArticleSortEntity sortEntity = articleSortService.findById(articleSortEntity.getId());
            if (sortEntity == null) {
                result.failedApiResponse(Const.FAILED, "修改的类型不存在");
                return result;
            }

            if (!sortEntity.getName().equals(articleSortEntity.getName())) {
                //保存当前用户的操作记录
                UserEntity userEntity = userService.findById(userId);
                OperationNoteEntity noteEntity = new OperationNoteEntity();
                if (articleSortEntity.getParentId() > 0) {
                    ArticleSortEntity parentSort = articleSortService.findById(articleSortEntity.getParentId());
                    noteEntity.setOperationLog(userEntity.getUsername() + "修改一级文章类型【"+ parentSort.getName() +"】下的子类型【" + sortEntity.getName() + "】为【" + articleSortEntity.getName() + "】");
                } else {
                    noteEntity.setOperationLog(userEntity.getUsername() + "修改一级文章类型【" + sortEntity.getName() + "】为【" + articleSortEntity.getName() + "】");
                }
                noteEntity.setUserName(userEntity.getUsername());
                noteEntity.setOperationTime(new Date());
                operationNoteService.save(noteEntity);
            }
            ArticleSortEntity articleSort = articleSortService.update(articleSortEntity);
            if (articleSort == null) {
                result.failedApiResponse(Const.FAILED, "修改失败");
                return result;
            }
            result.successResponse(Const.SUCCESS, articleSort, "修改成功");
        } catch (Exception e) {
            logger.warn("修改文章分类异常", e);
            result.failedApiResponse(Const.FAILED, "修改文章分类异常");
        }
        return result;

    }

    /**
     * 无分页文章分类列表
     */
    @GetMapping("getAll")
    public RestApiResponse<List<ArticleSortEntity>> getAll() {
        RestApiResponse<List<ArticleSortEntity>> result = new RestApiResponse<List<ArticleSortEntity>>();
        try {
            List<ArticleSortEntity> list = articleSortService.getAll();
            result.failedApiResponse(Const.SUCCESS, "列表获取成功");
            result.setResult(list);
        } catch (Exception e) {
            logger.warn("获取列表异常", e);
            result.failedApiResponse(Const.FAILED, "获取列表异常");
        }
        return result;
    }

    /**
     * 获取当前分类的子级类型
     */
    @GetMapping("/getArticleSort")
    public RestApiResponse<List<ArticleSortEntity>> getSort(@RequestParam(required = false, defaultValue = "0") int parentId) {
        RestApiResponse<List<ArticleSortEntity>> result = new RestApiResponse<List<ArticleSortEntity>>();
        try {
            List<ArticleSortEntity> list = articleSortService.getArticleSort(parentId);
            result.successResponse(Const.SUCCESS, list);
        } catch (Exception e) {
            logger.warn("获取子级文章分类列表异常", e);
            result.failedApiResponse(Const.FAILED, "获取子级文章分类列表异常");
        }
        return result;
    }

    /**
     * 获取当前分类的子级类型
     */
    @GetMapping("getArticleSonSort")
    public RestApiResponse<List<ArticleSortEntity>> getSonSort(@RequestParam(required = false, defaultValue = "0") int parentId) {
        RestApiResponse<List<ArticleSortEntity>> result = new RestApiResponse<List<ArticleSortEntity>>();
        try {
            List<ArticleSortEntity> list = articleSortService.getArticleSort(parentId);
            result.successResponse(Const.SUCCESS, list);
        } catch (Exception e) {
            logger.warn("获取子级文章分类列表异常", e);
            result.failedApiResponse(Const.FAILED, "获取子级文章分类列表异常");
        }
        return result;
    }

    /**
     * 获取所有的二级文章分类
     */
    @GetMapping("/getJuniorAll")
    public RestApiResponse<List<ArticleSortEntity>> getJuniorAll(){
        RestApiResponse<List<ArticleSortEntity>> result = new RestApiResponse<List<ArticleSortEntity>>();
        try {
            List<ArticleSortEntity> list = articleSortService.getJuniorAll();
            result.successResponse(Const.SUCCESS, list);
        } catch (Exception e) {
            logger.warn("获取文章二级分类异常");
            result.failedApiResponse(Const.FAILED, "获取文章二级分类异常");
        }
        return result;
    }
}
