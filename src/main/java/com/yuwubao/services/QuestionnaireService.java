package com.yuwubao.services;

import com.yuwubao.entities.QuestionnaireEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by yangyu on 2017/12/26.
 */
public interface QuestionnaireService {

    QuestionnaireEntity findByClientUserIdAndArticleId(int clientUserId, int articleId);

    QuestionnaireEntity save(QuestionnaireEntity questionnaireEntity);

    Page<QuestionnaireEntity> findAll(Map<String, String> map, Pageable pageAble);
}
