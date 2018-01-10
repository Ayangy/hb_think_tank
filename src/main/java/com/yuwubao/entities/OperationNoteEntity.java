package com.yuwubao.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yangyu on 2018/1/9.
 */
@Entity
@Table(name = "operation_note")
public class OperationNoteEntity {

    /**
     * 主键、自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * 操作的用户
     */
    private String userName;

    /**
     * 操作日志
     */
    private String operationLog;

    /**
     * 操作日期
     */
    private Date operationTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOperationLog() {
        return operationLog;
    }

    public void setOperationLog(String operationLog) {
        this.operationLog = operationLog;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }
}
