package com.yuwubao.entities.repository;

import com.yuwubao.entities.ClientUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by yangyu on 2017/12/20.
 */
public interface ClientUserRepository extends JpaRepository<ClientUserEntity, Integer>, JpaSpecificationExecutor<ClientUserEntity> {
    ClientUserEntity findByEmail(String email);

    List<ClientUserEntity> findByEmailAndPwd(String email, String pwd);

    ClientUserEntity findByNameAndPwd(String name, String pwd);

}
