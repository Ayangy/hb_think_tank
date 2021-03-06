package com.yuwubao.controllers;

import com.yuwubao.entities.OperationNoteEntity;
import com.yuwubao.entities.RoleEntity;
import com.yuwubao.entities.UserEntity;
import com.yuwubao.entities.UserRoleEntity;
import com.yuwubao.services.OperationNoteService;
import com.yuwubao.services.RoleService;
import com.yuwubao.services.UserRoleService;
import com.yuwubao.services.UserService;
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
 * Created by yangyu on 2017/10/17.
 */
@RestController
@RequestMapping("/sys/user")
@Transactional
@CrossOrigin
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private OperationNoteService operationNoteService;

    /**
     * 分页查询用户
     * @param index 第几页
     * @param size  每页几条
     * @param field  查询字段
     * @param keyword  查询值
     * @param beginTime 开始时间
     * @param endTime  结束时间
     * @return
     */
    @GetMapping("/findAll")
    public RestApiResponse<Page<UserEntity>> findAll(@RequestParam(defaultValue = "1", required = false)int index,
                                                    @RequestParam(defaultValue = "10", required = false)int size,
                                                    @RequestParam(required = false, defaultValue = "")String field,
                                                    @RequestParam(required = false, defaultValue = "")String keyword,
                                                    @RequestParam(required = false, defaultValue = "")String beginTime,
                                                    @RequestParam(required = false, defaultValue = "")String endTime){
        RestApiResponse<Page<UserEntity>> result = new RestApiResponse<Page<UserEntity>>();
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("field", field);
            map.put("keyword", keyword);
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            Pageable pageAble = new PageRequest(index - 1, size);
            Page<UserEntity> list = userService.findAll(map, pageAble);
            result.successResponse(Const.SUCCESS, list);
        } catch (Exception e) {
            logger.warn("查询用户列表异常", e);
            result.failedApiResponse(Const.FAILED, "用户列表查询异常");
        }
        return result;
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    public RestApiResponse<UserEntity> add(@RequestBody UserEntity userEntity,
                                           @RequestParam(required = false, defaultValue = "0")int type,
                                           @RequestParam int userId) {
        RestApiResponse<UserEntity> result = new RestApiResponse<UserEntity>();
        try {
            RoleEntity roleEntity = roleService.findOne(userEntity.getRoleId());
            if (roleEntity == null) {
                result.failedApiResponse(Const.FAILED, "指定的权限不存在");
                return result;
            }
            if (!StringUtils.isNotBlank(userEntity.getUsername())) {
                result.failedApiResponse(Const.FAILED, "未设置用户名");
                return result;
            }
            if (!StringUtils.isNotBlank(userEntity.getPassword())) {
                result.failedApiResponse(Const.FAILED, "未设置密码");
                return result;
            }
            UserEntity entity = userService.findByUsername(userEntity.getUsername());
            if (entity != null) {
                result.failedApiResponse(Const.FAILED, "用户名已存在");
                return result;
            }
            userEntity.setCreateTime(new Date());
            userEntity.setType(type);
            UserEntity user = userService.add(userEntity);
            if (user == null) {
                result.failedApiResponse(Const.FAILED, "添加失败");
                return result;
            }

            //保存当前用户的操作记录
            UserEntity loginUserEntity = userService.findById(userId);

            OperationNoteEntity noteEntity = new OperationNoteEntity();
            noteEntity.setUserName(loginUserEntity.getUsername());
            noteEntity.setOperationLog(loginUserEntity.getUsername()+"新增后台用户:" + user.getUsername());
            noteEntity.setOperationTime(new Date());

            operationNoteService.save(noteEntity);

            result.successResponse(Const.SUCCESS, user, "添加成功");
        } catch (Exception e) {
            logger.warn("新增用户异常:", e);
            result.failedApiResponse(Const.FAILED, "新增用户异常");
        }
        return result;
    }

    /**
     * 删除用户
     * @param id  用户Id
     * @return
     */
    //@DeleteMapping("/delete")
    @PostMapping("/delete")
    public RestApiResponse<UserEntity> delete(@RequestParam(required = true) int id, @RequestParam int userId) {
        RestApiResponse<UserEntity> result = new RestApiResponse<UserEntity>();
        try {
            UserEntity userEntity = userService.delete(id);
            if (userEntity == null) {
                result.failedApiResponse(Const.FAILED, "该用户不存在，删除失败");
                return result;
            }

            //保存当前用户的操作记录
            UserEntity loginUserEntity = userService.findById(userId);

            OperationNoteEntity noteEntity = new OperationNoteEntity();
            noteEntity.setUserName(loginUserEntity.getUsername());
            noteEntity.setOperationLog(loginUserEntity.getUsername()+"删除后台用户:" + userEntity.getUsername());
            noteEntity.setOperationTime(new Date());

            operationNoteService.save(noteEntity);

            result.successResponse(Const.SUCCESS, userEntity, "删除成功");
        } catch (Exception e) {
            logger.warn("删除用户异常:", e);
            result.failedApiResponse(Const.FAILED, "删除用户异常");
        }
        return result;
    }

    /**
     * 修改用户
     */
    //@PutMapping("/update")
    @PostMapping("/update")
    public RestApiResponse<UserEntity> update(@RequestBody UserEntity userEntity, @RequestParam int userId) {
        RestApiResponse<UserEntity> result = new RestApiResponse<UserEntity>();
        try {
            if (!StringUtils.isNotBlank(userEntity.getUsername())) {
                result.failedApiResponse(Const.FAILED, "未设置用户名");
                return result;
            }
            if (!StringUtils.isNotBlank(userEntity.getPassword())) {
                result.failedApiResponse(Const.FAILED, "未设置密码");
                return result;
            }
            UserEntity entity = userService.findById(userEntity.getId());
            if (entity == null) {
                result.failedApiResponse(Const.FAILED, "修改失败，用户不存在");
                return result;
            }
            if (!entity.getUsername().equals(userEntity.getUsername())) {
                UserEntity elseUser = userService.findByUsername(userEntity.getUsername());
                if (elseUser != null) {
                    result.failedApiResponse(Const.FAILED, "修改失败，用户名已存在");
                    return result;
                }
            }
            userEntity.setCreateTime(entity.getCreateTime());
            UserEntity user = userService.update(userEntity);

            //保存当前用户的操作记录
            UserEntity loginUserEntity = userService.findById(userId);

            OperationNoteEntity noteEntity = new OperationNoteEntity();
            noteEntity.setUserName(loginUserEntity.getUsername());
            noteEntity.setOperationLog(loginUserEntity.getUsername()+"修改后台用户:" + user.getUsername());
            noteEntity.setOperationTime(new Date());

            operationNoteService.save(noteEntity);

            result.successResponse(Const.SUCCESS, user, "修改成功");
        } catch (Exception e) {
            logger.warn("修改用户异常", e);
            result.failedApiResponse(Const.FAILED, "修改用户异常");
        }
        return result;

    }

    /**
     * 给用户指定角色
     * @param userId  用户Id
     * @param roleId  角色Id
     * @return
     */
    @PostMapping("/updateAuthedUserRole")
    public RestApiResponse<UserRoleEntity> editUserRole(@RequestParam int userId,@RequestParam int roleId) {
        RestApiResponse<UserRoleEntity> result = new RestApiResponse<UserRoleEntity>();
        try {
            UserEntity user = userService.findById(userId);
            if (user == null) {
                result.failedApiResponse(Const.FAILED, "用户不存在，无法指定角色");
                return result;
            }
            RoleEntity roleEntity = roleService.findOne(roleId);
            if (roleEntity == null) {
                result.failedApiResponse(Const.FAILED, "无此角色，请重新指定角色");
                return result;
            }
            UserRoleEntity entity = userRoleService.findByUserId(userId);
            if (entity == null) {
                UserRoleEntity userRole = new UserRoleEntity();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleService.save(userRole);
                result.successResponse(Const.SUCCESS, userRole, "指定用户角色成功");
                return result;
            }
            entity.setRoleId(roleId);
            UserRoleEntity userRoleEntity = userRoleService.save(entity);
            result.successResponse(Const.SUCCESS, userRoleEntity, "修改用户角色成功");

        } catch (Exception e) {
            logger.warn("指定用户角色异常：", e);
            result.failedApiResponse(Const.FAILED, "指定用户角色异常");
        }
        return result;
    }

}
