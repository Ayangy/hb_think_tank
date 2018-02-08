package com.yuwubao.util;

import com.yuwubao.entities.ClientUserEntity;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by yangyu on 2018/1/19.
 */
public class Sendmail extends Thread {

    private String myEmailSMTPHost;

    private String myEmailAccount;

    private String myEmailPwd;

    private String emailContent;

    private String emailType;

    private ClientUserEntity clientUserEntity;

    public Sendmail(ClientUserEntity clientUserEntity,
                    String myEmailSMTPHost,
                    String myEmailAccount,
                    String myEmailPwd,
                    String emailType,
                    String emailContent) {
        this.clientUserEntity = clientUserEntity;
        this.myEmailSMTPHost = myEmailSMTPHost;
        this.myEmailAccount = myEmailAccount;
        this.myEmailPwd = myEmailPwd;
        this.emailContent = emailContent;
        this.emailType = emailType;
    }

     /* 重写run方法的实现，在run方法中发送邮件给指定的用户
      * @see java.lang.Thread#run()
      */
     @Override
    public void run() {
        try{
            //创建参数配置, 用于连接邮件服务器的参数配置
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.host", myEmailSMTPHost);
            props.setProperty("mail.smtp.auth", "true");
            Session session = Session.getInstance(props);;
            session.setDebug(true);

            //创建邮件
            MimeMessage message = new MimeMessage(session);

            // From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
            message.setFrom(new InternetAddress(myEmailAccount, "湖北智库网", "UTF-8"));

            //To: 收件人（可以增加多个收件人、抄送、密送）
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(clientUserEntity.getEmail(), clientUserEntity.getName(), "UTF-8"));

            //163邮箱需要抄送给发送方
            message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(myEmailAccount, "UTF-8"));

            //邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
            message.setSubject(emailType, "UTF-8");

            //邮件正文
            message.setContent(emailContent, "text/html;charset=UTF-8");

            //设置发件时间
            message.setSentDate(new Date());

            // 7. 保存设置
            message.saveChanges();

            //根据 Session 获取邮件传输对象
            Transport transport = session.getTransport();
            transport.connect(myEmailAccount, myEmailPwd);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
     }



}
