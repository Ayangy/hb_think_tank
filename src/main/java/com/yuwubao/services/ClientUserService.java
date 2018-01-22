package com.yuwubao.services;

import com.yuwubao.entities.ClientUserEntity;

import java.util.List;

/**
 * Created by yangyu on 2017/12/20.
 */
public interface ClientUserService {
    ClientUserEntity add(ClientUserEntity clientUserEntity);

    ClientUserEntity findByEmail(String email);

    ClientUserEntity findOne(int id);

    List<ClientUserEntity> findByEmailAndPwd(String email, String pwd);

    ClientUserEntity findByNameAndPwd(String name, String pwd);

    List<ClientUserEntity> findByNameAndPwdAndStatus(String username, String pwd, int status);
}
