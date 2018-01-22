package com.yuwubao.services.impl;

import com.yuwubao.entities.OrganizationEntity;
import com.yuwubao.entities.OtherConfigurationEntity;
import com.yuwubao.entities.repository.OtherConfigurationRepository;
import com.yuwubao.services.OtherConfigurationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

/**
 * Created by yangyu on 2018/1/12.
 */
@Service
public class OtherConfigurationServiceImpl implements OtherConfigurationService {

    @Autowired
    private OtherConfigurationRepository otherConfigurationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<OtherConfigurationEntity> findAll(int type, Pageable pageAble) {
        Specification<OtherConfigurationEntity> spec = new Specification<OtherConfigurationEntity>() {
            @Override
            public Predicate toPredicate(Root<OtherConfigurationEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder){
                Predicate predict = criteriaBuilder.conjunction();
                if (type != -1) {
                    Path<Integer> path = root.get("type");
                    predict.getExpressions().add(criteriaBuilder.equal(path, type));
                }
                return predict;
            }
        };
        return otherConfigurationRepository.findAll(spec, pageAble);
    }

    @Override
    public OtherConfigurationEntity add(OtherConfigurationEntity otherConfigurationEntity) {
        return otherConfigurationRepository.save(otherConfigurationEntity);
    }

    @Override
    public OtherConfigurationEntity findByType(int type) {
        OtherConfigurationEntity entity = otherConfigurationRepository.findByType(type);
        return entity;
    }

    @Override
    public OtherConfigurationEntity delete(int id) {
        OtherConfigurationEntity entity = otherConfigurationRepository.findOne(id);
        if (entity != null) {
            otherConfigurationRepository.delete(id);
            return entity;
        }
        return null;
    }

    @Override
    public OtherConfigurationEntity findOne(int id) {
        return otherConfigurationRepository.findOne(id);
    }

    @Override
    public OtherConfigurationEntity update(OtherConfigurationEntity otherConfigurationEntity) {
        ModelMapper mapper = new ModelMapper();
        OtherConfigurationEntity entity = mapper.map(otherConfigurationEntity, OtherConfigurationEntity.class);
        return otherConfigurationRepository.save(entity);
    }
}
