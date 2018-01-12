package com.yuwubao.services.impl;

import com.yuwubao.entities.ExpertEntity;
import com.yuwubao.entities.repository.ExpertRepository;
import com.yuwubao.services.ExpertService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyu on 2017/11/3.
 */
@Service
public class ExpertServiceImpl implements ExpertService {

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<ExpertEntity> findAll(Map<String, String> map, Pageable pageAble) {
        String field = map.get("field");
        String keyword = map.get("keyword");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Specification<ExpertEntity> spec = new Specification<ExpertEntity>() {
            @Override
            public Predicate toPredicate(Root<ExpertEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
                Predicate predict = criteriaBuilder.conjunction();
                if (StringUtils.isNotBlank(field)) {
                    Path<String> exp1 = root.get(field);
                    if (StringUtils.isNotBlank(keyword)) {
                        predict.getExpressions().add(criteriaBuilder.like(exp1, "%" + keyword + "%"));
                    }
                }
                return predict;
            }
        };
        return expertRepository.findAll(spec, pageAble);
    }

    @Override
    public ExpertEntity add(ExpertEntity expertEntity) {
        return expertRepository.save(expertEntity);
    }

    @Override
    public ExpertEntity delete(int id) {
        ExpertEntity entity = expertRepository.findOne(id);
        if (entity == null) {
            return null;
        }
        expertRepository.delete(id);
        return entity;
    }

    @Override
    public ExpertEntity findOne(int id) {
        return expertRepository.findOne(id);
    }

    @Override
    public ExpertEntity update(ExpertEntity expertEntity) {
        ModelMapper mapper = new ModelMapper();
        ExpertEntity entity = mapper.map(expertEntity, ExpertEntity.class);
        return expertRepository.save(entity);
    }

    @Override
    public ExpertEntity findById(int id) {
        return expertRepository.findOne(id);
    }

    @Override
    public List<ExpertEntity> getAll() {
        List<ExpertEntity> list = expertRepository.findAll();
        return list;
    }

    @Override
    public List<ExpertEntity> findExpertByLetter(String letter) {
        String sql = "select * from zk_expert where shield=0 AND get_first_pinyin_char(name) = ?";
        RowMapper<ExpertEntity> rowMapper = new BeanPropertyRowMapper<>(ExpertEntity.class);
        List<ExpertEntity> list = jdbcTemplate.query(sql, rowMapper, letter);
        return list;
    }

    @Override
    public List<ExpertEntity> findByShield(int shield) {
        String sql = "select * from zk_expert where shield=? ORDER BY sortIndex";
        RowMapper<ExpertEntity> rowMapper = new BeanPropertyRowMapper<>(ExpertEntity.class);
        List<ExpertEntity> list = jdbcTemplate.query(sql, rowMapper, shield);
        return list;
    }

    @Override
    public List<ExpertEntity> queryAnExpert(int id) {
        String sql = "select * from zk_expert where shield=0 AND id = ?";
        RowMapper<ExpertEntity> rowMapper = new BeanPropertyRowMapper<>(ExpertEntity.class);
        List<ExpertEntity> list = jdbcTemplate.query(sql, rowMapper, id);
        return list;
    }

    @Override
    public List<ExpertEntity> findExpertByCondition(Map<String, String> map, int fieldType) {
        String field = map.get("field");
        String keyword = map.get("keyword");
        Specification<ExpertEntity> spec = new Specification<ExpertEntity>() {
            @Override
            public Predicate toPredicate(Root<ExpertEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
                Predicate predict = criteriaBuilder.conjunction();
                if (StringUtils.isNotBlank(field)) {
                    Path<String> exp1 = root.get(field);
                    if (StringUtils.isNotBlank(keyword)) {
                        predict.getExpressions().add(criteriaBuilder.like(exp1, "%" + keyword + "%"));
                    }
                }
                Path<Integer> path = root.get("shield");
                predict.getExpressions().add(criteriaBuilder.equal(path, String.valueOf(0)));
                if (fieldType >= 0) {
                    Path<Integer> path1 = root.get("fieldType");
                    predict.getExpressions().add(criteriaBuilder.equal(path1, String.valueOf(fieldType)));
                }
                return predict;
            }
        };
        return expertRepository.findAll(spec);
    }
}
