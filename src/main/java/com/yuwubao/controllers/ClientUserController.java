package com.yuwubao.controllers;

import com.yuwubao.entities.ClientUserEntity;
import com.yuwubao.services.ClientUserService;
import com.yuwubao.util.Const;
import com.yuwubao.util.MD5;
import com.yuwubao.util.RestApiResponse;
import com.yuwubao.util.Sendmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by yangyu on 2017/12/20.
 */
@RestController
@RequestMapping("/clientUser")
@Transactional
@CrossOrigin
public class ClientUserController {

    private final static Logger logger = LoggerFactory.getLogger(ClientUserController.class);

    @Value("${myEmailSMTPHost}")
    private String myEmailSMTPHost;

    @Value("${myEmailAccount}")
    private String myEmailAccount;

    @Value("${myEmailPwd}")
    private String myEmailPwd ;

    @Value("${serverIp}")
    private String serverIp;

    @Autowired
    private ClientUserService clientUserService;

    /**
     * 注册用户
     */
    @PostMapping(value = "/registeredUser",consumes="application/json;charset=UTF-8")
    public RestApiResponse<Boolean> registeredUser(@RequestBody ClientUserEntity clientUserEntity) {
        RestApiResponse<Boolean> result = new RestApiResponse<Boolean>();
        try {
            /*boolean emailValid = CheckEmailValidityUtil.isEmailValid(clientUserEntity.getEmail());
            if(emailValid){
                result.failedApiResponse(Const.FAILED, "注册失败,请检查邮箱是否书写正确");
                return result;
            }*/
            ClientUserEntity clientUser = clientUserService.findByEmail(clientUserEntity.getEmail());
            if (clientUser != null) {
                result.failedApiResponse(Const.FAILED, "已使用此邮箱注册");
                return result;
            }



            String uuid = UUID.randomUUID().toString().replace("-", "");
            clientUserEntity.setActivationCode(uuid);
            clientUserEntity.setPwd(MD5.md5(clientUserEntity.getPwd()));
            ClientUserEntity entity = clientUserService.add(clientUserEntity);
            if (entity != null) {

                String emailType = "湖北智库网新用户激活";
                String emailContent = "您好：\n" +
                        "感谢您注册湖北智库网，点击或复制到浏览器打开下方链接，激活您的账号"+
                        "<a href='http://localhost/active.html?id=" + entity.getId() + "&ActivationCode=" +entity.getActivationCode()+
                        "' _act='check_domail'>http://localhost/active.html?id="+ entity.getId() +"&ActivationCode=" +entity.getActivationCode()+ "</a>";

                //发送邮件到邮箱
                Sendmail send = new Sendmail(entity, myEmailSMTPHost, myEmailAccount, myEmailPwd, emailType, emailContent);
                send.start();
                result.successResponse(Const.SUCCESS, true, "注册成功,请到邮箱激活!");
                return result;
            }
            result.failedApiResponse(Const.FAILED, "注册失败");
            return result;
        } catch (Exception e) {
            logger.warn("注册异常", e);
            result.failedApiResponse(Const.ERROR, "注册异常");
        }
        return result;
    }

    /**
     * 激活账号
     * @param id  前端用户id
     * @param ActivationCode  激活码
     * @return
     */
    @RequestMapping("/activateUser")
    public RestApiResponse<Boolean> activateUser(@RequestParam int id,
                                                 @RequestParam String ActivationCode) {
        RestApiResponse<Boolean> result = new RestApiResponse<Boolean>();
        try {
            ClientUserEntity entity = clientUserService.findOne(id);
            if (entity != null) {
                if(entity.getStatus() == 1){
                    result.failedApiResponse(Const.FAILED, "账号已激活");
                    return result;
                }
                if(entity.getActivationCode().equals(ActivationCode)){
                    entity.setStatus(1);
                    clientUserService.add(entity);
                    result.successResponse(Const.SUCCESS, true, "激活成功");
                    return result;
                }
            }
            result.failedApiResponse(Const.FAILED, "激活失败");
        } catch (Exception e) {
            logger.warn("激活异常", e);
            result.failedApiResponse(Const.FAILED, "激活异常");
        }
        return result;
    }

}