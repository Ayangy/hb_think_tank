package com.yuwubao.services.impl;

import com.yuwubao.entities.OperationNoteEntity;
import com.yuwubao.entities.repository.OperationNoteRepository;
import com.yuwubao.services.OperationNoteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by yangyu on 2018/1/9.
 */
@Service
public class OperationNoteServiceImpl implements OperationNoteService {

    @Autowired
    private OperationNoteRepository operationNoteRepository;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<OperationNoteEntity> findAll(Map<String, String> map, Pageable pageAble) {
        String field = map.get("field");
        String keyword = map.get("keyword");
        String start = map.get("beginTime");
        String end = map.get("endTime");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Specification<OperationNoteEntity> spec = new Specification<OperationNoteEntity>() {
            @Override
            public Predicate toPredicate(Root<OperationNoteEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
                Predicate predict = criteriaBuilder.conjunction();
                if (StringUtils.isNotBlank(field)) {
                    Path<String> exp1 = root.get(field);
                    if (StringUtils.isNotBlank(keyword)) {
                        predict.getExpressions().add(criteriaBuilder.like(exp1, "%" + keyword + "%"));
                    }
                }

                if (StringUtils.isNotBlank(start)) {
                    Path<Date> incept = root.get("operationTime");
                    try {
                        predict.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(incept, dateFormat.parse(start)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                if (StringUtils.isNotBlank(end)) {
                    Path<Date> finish = root.get("operationTime");
                    try {
                        predict.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(finish, dateFormat.parse(end)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return predict;
            }
        };
        return operationNoteRepository.findAll(spec, pageAble);
    }

    @Override
    public OperationNoteEntity save(OperationNoteEntity noteEntity) {
        return operationNoteRepository.save(noteEntity);
    }
}
