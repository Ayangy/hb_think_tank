package com.yuwubao.controllers;

import com.yuwubao.entities.OperationNoteEntity;
import com.yuwubao.entities.OtherConfigurationEntity;
import com.yuwubao.entities.UserEntity;
import com.yuwubao.services.OperationNoteService;
import com.yuwubao.services.OtherConfigurationService;
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

/**
 * Created by yangyu on 2018/1/12.
 */
@RestController
@RequestMapping("/otherConfiguration")
@Transactional
@CrossOrigin
public class OtherConfigurationController {

    private final static Logger logger = LoggerFactory.getLogger(OtherConfigurationController.class);

    @Autowired
    private OtherConfigurationService otherConfigurationService;

    @Autowired
    private UserService userService;

    @Autowired
    private OperationNoteService operationNoteService;

    /**
     * 查询其它配置列表
     * @param index  第几页
     * @param size  每页几条
     * @param  type  配置类型0(关于我们),1(加入联盟),2(法律顾问),3(广告服务),4(网站声明)
     * @return
     */
    @GetMapping("/findAll")
    public RestApiResponse<Page<OtherConfigurationEntity>> findAll(@RequestParam(defaultValue = "1", required = false)int index,
                                                                   @RequestParam(defaultValue = "10", required = false)int size,
                                                                   @RequestParam(required = false, defaultValue = "-1")int type){
        RestApiResponse<Page<OtherConfigurationEntity>> result = new RestApiResponse<Page<OtherConfigurationEntity>>();
        try {
            Pageable pageAble = new PageRequest(index - 1, size);
            Page<OtherConfigurationEntity> list = otherConfigurationService.findAll(type, pageAble);
            result.successResponse(Const.SUCCESS, list, "获取其它配置列表成功");
        } catch (Exception e) {
            logger.warn("获取其它配置列表异常", e);
            result.failedApiResponse(Const.FAILED, "获取其它配置列表异常");
        }
        return result;
    }

    /**
     * 新增其它配置
     * @param otherConfigurationEntity
     * @param userId  后台当前登录用户id
     * @return
     */
    @PostMapping("/add")
    public RestApiResponse<OtherConfigurationEntity> add(@RequestBody OtherConfigurationEntity otherConfigurationEntity, @RequestParam int userId) {
        RestApiResponse<OtherConfigurationEntity> result = new RestApiResponse<OtherConfigurationEntity>();
        try {
            OtherConfigurationEntity entity = otherConfigurationService.findByType(otherConfigurationEntity.getType());
            if (entity != null) {
                result.failedApiResponse(Const.FAILED, "已添加此类配置");
                return result;
            }
            OtherConfigurationEntity otherConfiguration = otherConfigurationService.add(otherConfigurationEntity);
            if (otherConfiguration == null) {
                result.failedApiResponse(Const.FAILED, "添加其它配置失败");
                return result;
            }

            //保存当前用户的操作记录
            String configurationType = "";
            switch (otherConfigurationEntity.getType()) {
                case 0:
                    configurationType = "关于我们";
                    break;
                case 1:
                    configurationType = "加入联盟";
                    break;
                case 2:
                    configurationType = "法律顾问";
                    break;
                case 3:
                    configurationType = "广告服务";
                    break;
                case 4:
                    configurationType = "网站声明";
                    break;
                case 5:
                    configurationType = "联系我们";
                    break;
            }
            UserEntity userEntity = userService.findById(userId);

            OperationNoteEntity noteEntity = new OperationNoteEntity();
            noteEntity.setUserName(userEntity.getUsername());
            noteEntity.setOperationLog(userEntity.getUsername()+"新增:" + configurationType);
            noteEntity.setOperationTime(new Date());

            operationNoteService.save(noteEntity);
            result.successResponse(Const.SUCCESS, otherConfiguration, "添加其它配置成功");
        } catch (Exception e) {
            logger.warn("新增其它配置异常", e);
            result.failedApiResponse(Const.FAILED, "新增其它配置异常");
        }
        return result;
    }

    /**
     * 删除其它配置
     * @param id  配置id
     * @param userId  后台当前登录的id
     * @return
     */
    //@DeleteMapping("/delete")
    @PostMapping("/delete")
    public RestApiResponse<OtherConfigurationEntity> delete(@RequestParam(required = true) int id, @RequestParam int userId) {
        RestApiResponse<OtherConfigurationEntity> result = new RestApiResponse<OtherConfigurationEntity>();
        try {
            OtherConfigurationEntity otherConfigurationEntity = otherConfigurationService.delete(id);

            if (otherConfigurationEntity == null) {
                result.failedApiResponse(Const.FAILED, "该类配置不存在，删除失败");
                return result;
            }

            //保存当前用户的操作记录
            String configurationType = "";
            switch (otherConfigurationEntity.getType()) {
                case 0:
                    configurationType = "关于我们";
                    break;
                case 1:
                    configurationType = "加入联盟";
                    break;
                case 2:
                    configurationType = "法律顾问";
                    break;
                case 3:
                    configurationType = "广告服务";
                    break;
                case 4:
                    configurationType = "网站声明";
                    break;
                case 5:
                    configurationType = "联系我们";
                    break;
            }
            UserEntity userEntity = userService.findById(userId);

            OperationNoteEntity noteEntity = new OperationNoteEntity();
            noteEntity.setUserName(userEntity.getUsername());
            noteEntity.setOperationLog(userEntity.getUsername()+"删除:" + configurationType);
            noteEntity.setOperationTime(new Date());

            operationNoteService.save(noteEntity);
            result.successResponse(Const.SUCCESS, otherConfigurationEntity, "删除配置成功");
        } catch (Exception e) {
            logger.warn("删除配置异常", e);
            result.failedApiResponse(Const.FAILED, "删除配置异常");
        }
        return result;
    }

    /**
     * 修改其它配置
     */
    //@PutMapping("/update")
    @PostMapping("/update")
    public RestApiResponse<OtherConfigurationEntity> update(@RequestBody OtherConfigurationEntity otherConfigurationEntity, @RequestParam int userId) {
        RestApiResponse<OtherConfigurationEntity> result = new RestApiResponse<OtherConfigurationEntity>();
        try {
            OtherConfigurationEntity otherConfiguration = otherConfigurationService.findOne(otherConfigurationEntity.getId());
            if (otherConfiguration == null) {
                result.failedApiResponse(Const.FAILED, "修改失败，配置不存在");
                return result;
            }
            if (otherConfigurationEntity.getType() != otherConfiguration.getType()) {
                OtherConfigurationEntity configurationEntity = otherConfigurationService.findByType(otherConfigurationEntity.getType());
                if (configurationEntity != null) {
                    result.failedApiResponse(Const.FAILED, "修改失败，已存在此类配置");
                    return result;
                }
            }
            OtherConfigurationEntity entity = otherConfigurationService.update(otherConfigurationEntity);

            //保存当前用户的操作记录
            String configurationType = "";
            switch (otherConfigurationEntity.getType()) {
                case 0:
                    configurationType = "关于我们";
                    break;
                case 1:
                    configurationType = "加入联盟";
                    break;
                case 2:
                    configurationType = "法律顾问";
                    break;
                case 3:
                    configurationType = "广告服务";
                    break;
                case 4:
                    configurationType = "网站声明";
                    break;
                case 5:
                    configurationType = "联系我们";
                    break;
            }
            UserEntity userEntity = userService.findById(userId);

            OperationNoteEntity noteEntity = new OperationNoteEntity();
            noteEntity.setUserName(userEntity.getUsername());
            noteEntity.setOperationLog(userEntity.getUsername()+"修改:" + configurationType);
            noteEntity.setOperationTime(new Date());

            operationNoteService.save(noteEntity);

            result.successResponse(Const.SUCCESS, entity, "修改配置成功");
        } catch (Exception e) {
            logger.warn("修改配置异常", e);
            result.failedApiResponse(Const.FAILED, "修改配置异常");
        }
        return result;

    }
}
