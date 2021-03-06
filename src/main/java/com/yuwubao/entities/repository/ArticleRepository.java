package com.yuwubao.entities.repository;

import com.yuwubao.entities.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by yangyu on 2017/10/20.
 */
public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer>, JpaSpecificationExecutor<ArticleEntity>{
    List<ArticleEntity> findByOrganizationId(int id);

    List<ArticleEntity> findByTextTypeId(int id);

    List<ArticleEntity> findByTextTypeIdAndShield(int textTypeId, int shield);
}
