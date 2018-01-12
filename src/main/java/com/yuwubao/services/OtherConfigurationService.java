package com.yuwubao.services;

import com.yuwubao.entities.OtherConfigurationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by yangyu on 2018/1/12.
 */
public interface OtherConfigurationService {
    Page<OtherConfigurationEntity> findAll(int type, Pageable pageAble);

    OtherConfigurationEntity add(OtherConfigurationEntity otherConfigurationEntity);

    OtherConfigurationEntity findByType(int type);

    OtherConfigurationEntity delete(int id);

    OtherConfigurationEntity findOne(int id);

    OtherConfigurationEntity update(OtherConfigurationEntity otherConfigurationEntity);
}
