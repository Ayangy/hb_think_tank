package com.yuwubao.services;

import com.yuwubao.entities.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by yangyu on 2017/10/23.
 */

public interface OrganizationService {
    Page<OrganizationEntity> findAll(Map<String, String> map, Pageable pageAble, int organizationType);

    OrganizationEntity add(OrganizationEntity organizationEntity);

    OrganizationEntity delete(int id);

    OrganizationEntity update(OrganizationEntity organizationEntity);


    List<OrganizationEntity> getAll();

    OrganizationEntity findOne(int id);

    List<OrganizationEntity> finByLetter(String letter, int type, int organizationType);

    List<OrganizationEntity> finByNameAndTypeAndShield(String query, int type, int shield);

    List<OrganizationEntity> findByName(String name);

    List<OrganizationEntity> findByCondition(Map<String, String> map, int organizationType, int type);

    List<OrganizationEntity> findByShield(int shield);

    OrganizationEntity findByType(int i);

    List<OrganizationEntity> findByShieldAndType(int shield, int type);

    List<OrganizationEntity> findByOrganizationType(int type, int organizationType);

    List<OrganizationEntity> findByTerritoryNameNotNull(int type, int shield, int organizationType);
}
