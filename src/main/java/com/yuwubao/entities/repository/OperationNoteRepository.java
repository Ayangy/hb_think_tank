package com.yuwubao.entities.repository;

import com.yuwubao.entities.OperationNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by yangyu on 2018/1/9.
 */
public interface OperationNoteRepository extends JpaRepository<OperationNoteEntity, Integer>, JpaSpecificationExecutor<OperationNoteEntity> {
}
