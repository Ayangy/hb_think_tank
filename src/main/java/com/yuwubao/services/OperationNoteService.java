package com.yuwubao.services;

import com.yuwubao.entities.OperationNoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by yangyu on 2018/1/9.
 */
public interface OperationNoteService {
    Page<OperationNoteEntity> findAll(Map<String, String> map, Pageable pageAble);

    OperationNoteEntity save(OperationNoteEntity noteEntity);
}
