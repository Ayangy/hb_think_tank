package com.yuwubao.entities.repository;

import com.yuwubao.entities.OtherConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by yangyu on 2018/1/12.
 */
public interface OtherConfigurationRepository extends JpaRepository<OtherConfigurationEntity, Integer>, JpaSpecificationExecutor<OtherConfigurationEntity> {
    OtherConfigurationEntity findByType(int type);
}
