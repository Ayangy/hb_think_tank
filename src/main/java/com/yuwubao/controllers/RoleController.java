package com.yuwubao.controllers;

import com.yuwubao.entities.OperationNoteEntity;
import com.yuwubao.entities.RoleEntity;
import com.yuwubao.entities.UserEntity;
import com.yuwubao.services.OperationNoteService;
import com.yuwubao.services.RoleService;
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
import java.util.List;
import java.util.Map;

/**
 * Created by yangyu on 2017/10/17.
 */
@RestController
@RequestMapping("/sys/role")
@Transactional
@CrossOrigin
public class RoleController {

    private final static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private OperationNoteService operationNoteService;

    /**
     *  查询角色
     * @param index  第几页
     * @param size  每页几条
     * @param field  查询字段
     * @param keyword  查询值
     * @return
     */
    @GetMapping("/findAll")
    public RestApiResponse<Page<RoleEntity>> findAll(@RequestParam(defaultValue = "1", required = false)int index,
                                                     @RequestParam(defaultValue = "10", required = false)int size,
                                                     @RequestParam(required = false, defaultValue = "")String field,
                                                     @RequestParam(required = false, defaultValue = "")String keyword){
        RestApiResponse<Page<RoleEntity>> result = new RestApiResponse<Page<RoleEntity>>();
        try {
            Map<String, String> map = new HashMap();
            map.put("field", field);
            map.put("keyword", keyword);
            Pageable pageAble = new PageRequest(index - 1, size);
            Page<RoleEntity> list = roleService.findAll(map, pageAble);
            result.successResponse(Const.SUCCESS, list, "查询角色成功");
        } catch (Exception e) {
            logger.warn("角色列表查询异常", e);
            result.failedApiResponse(Const.FAILED, "角色列表查询异常");
        }
        return result;
    }

    /**
     * 新增角色
     */
    @PostMapping("/add")
    public RestApiResponse<RoleEntity> add(@RequestBody RoleEntity roleEntity, @RequestParam int userId) {
        RestApiResponse<RoleEntity> result = new RestApiResponse<RoleEntity>();
        try {
            RoleEntity entity = roleService.findByName(roleEntity.getName());
            if (entity != null) {
                result.failedApiResponse(Const.FAILED, "角色名已存在");
                return result;
            }
            if (!StringUtils.isNotBlank(roleEntity.getName())) {
                result.failedApiResponse(Const.FAILED, "未设置角色名");
                return result;
            }
            RoleEntity role = roleService.add(roleEntity);
            if (role == null) {
                result.failedApiResponse(Const.FAILED, "添加角色失败");
                return result;
            }

            //保存当前用户的操作记录
            UserEntity userEntity = userService.findById(userId);

            OperationNoteEntity noteEntity = new OperationNoteEntity();
            noteEntity.setUserName(userEntity.getUsername());
            noteEntity.setOperationLog(userEntity.getUsername()+"新增角色:" + role.getName());
            noteEntity.setOperationTime(new Date());

            operationNoteService.save(noteEntity);

            result.successResponse(Const.SUCCESS, role, "添加角色成功");
        } catch (Exception e) {
            logger.warn("添加角色异常", e);
            result.failedApiResponse(Const.FAILED, "添加角色异常");
        }
        return result;
    }

    /**
     * 删除角色
     * @param id  角色Id
     * @return
     */
    //@DeleteMapping("/delete")
    @PostMapping("/delete")
    public RestApiResponse<RoleEntity> delete(@RequestParam(required = true) int id, @RequestParam int userId) {
        RestApiResponse<RoleEntity> result = new RestApiResponse<RoleEntity>();
        try {
            RoleEntity role = roleService.delete(id);
            if (role == null) {
                result.failedApiResponse(Const.FAILED, "该角色不存在，删除失败");
                return result;
            }

            //保存当前用户的操作记录
            UserEntity userEntity = userService.findById(userId);

            OperationNoteEntity noteEntity = new OperationNoteEntity();
            noteEntity.setUserName(userEntity.getUsername());
            noteEntity.setOperationLog(userEntity.getUsername()+"删除角色:" + role.getName());
            noteEntity.setOperationTime(new Date());

            operationNoteService.save(noteEntity);

            result.successResponse(Const.SUCCESS, role, "删除成功");
        } catch (Exception e) {
            logger.warn("删除角色失败", e);
            result.failedApiResponse(Const.FAILED, "删除角色异常");
        }
        return result;
    }

    /**
     * 修改角色
     */
    //@PutMapping("/update")
    @PostMapping("/update")
    public RestApiResponse<RoleEntity> update(@RequestBody RoleEntity roleEntity, @RequestParam int userId) {
        RestApiResponse<RoleEntity> result = new RestApiResponse<RoleEntity>();
        try {
            if (!StringUtils.isNotBlank(roleEntity.getName())) {
                result.failedApiResponse(Const.FAILED, "未设置角色名");
                return result;
            }
            RoleEntity oldEntity = roleService.findOne(roleEntity.getId());
            if (!roleEntity.getName().equals(oldEntity.getName())) {
                RoleEntity entity = roleService.findByName(roleEntity.getName());
                if (entity != null) {
                    result.failedApiResponse(Const.FAILED, "角色名已存在");
                    return result;
                }
            }
            if (oldEntity == null) {
                result.failedApiResponse(Const.FAILED, "修改失败，角色不存在");
                return result;
            }
            //保存当前用户的操作记录
            UserEntity userEntity = userService.findById(userId);

            OperationNoteEntity noteEntity = new OperationNoteEntity();
            noteEntity.setUserName(userEntity.getUsername());
            if (oldEntity.getName().equals(roleEntity)) {
                noteEntity.setOperationLog(userEntity.getUsername()+"修改角色:" + roleEntity.getName());
            } else {
                noteEntity.setOperationLog(userEntity.getUsername()+"修改角色:【"+oldEntity.getName()+ "】为【" + roleEntity.getName()+"】");
            }

            noteEntity.setOperationTime(new Date());

            operationNoteService.save(noteEntity);

            RoleEntity role = roleService.update(roleEntity);
            result.successResponse(Const.SUCCESS, role, "修改成功");
        } catch (Exception e) {
            logger.warn("修改角色异常", e);
            result.failedApiResponse(Const.FAILED, "修改角色异常");
        }
        return result;

    }

    /**
     * 无分页角色列表
     */
    @GetMapping("/getAll")
    public RestApiResponse<List<RoleEntity>> getAll() {
        RestApiResponse<List<RoleEntity>> result = new RestApiResponse<List<RoleEntity>>();
        try {
            List<RoleEntity> list = roleService.getAll();
            result.successResponse(Const.SUCCESS, list, "获取角色列表成功");
        } catch (Exception e) {
            logger.warn("获取角色列表异常：", e);
            result.failedApiResponse(Const.FAILED, "获取角色列表异常");
        }
        return result;
    }
}
