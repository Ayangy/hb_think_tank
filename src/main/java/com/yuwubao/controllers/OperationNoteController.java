package com.yuwubao.controllers;

import com.yuwubao.entities.OperationNoteEntity;
import com.yuwubao.services.OperationNoteService;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangyu on 2018/1/9.
 */
@RestController
@RequestMapping("/operationNote")
@Transactional
@CrossOrigin
public class OperationNoteController {

    private final static Logger logger = LoggerFactory.getLogger(OperationNoteController.class);

    @Autowired
    private OperationNoteService operationNoteService;

    /**
     * 查询操作记录列表
     * @param index  第几页
     * @param size  每页几条
     * @param field  查询字段
     * @param keyword  查询值
     * @param beginTime  开始时间
     * @param endTime  结束时间
     *
     */
    @GetMapping("/findAll")
    public RestApiResponse<Page<OperationNoteEntity>> findAll(@RequestParam(defaultValue = "1", required = false)int index,
                                                              @RequestParam(defaultValue = "10", required = false)int size,
                                                              @RequestParam(required = false, defaultValue = "")String field,
                                                              @RequestParam(required = false, defaultValue = "")String keyword,
                                                              @RequestParam(required = false, defaultValue = "")String beginTime,
                                                              @RequestParam(required = false, defaultValue = "")String endTime){
        RestApiResponse<Page<OperationNoteEntity>> result = new RestApiResponse<Page<OperationNoteEntity>>();
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("field", field);
            map.put("keyword", keyword);
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            Pageable pageAble = new PageRequest(index - 1, size);
            Page<OperationNoteEntity> list = operationNoteService.findAll(map, pageAble);
            result.successResponse(Const.SUCCESS, list, "获取列表成功");
        } catch (Exception e) {
            logger.warn("操作记录列表查询异常", e);
            result.failedApiResponse(Const.FAILED, "操作记录列表查询异常");
        }
        return result;
    }
}
