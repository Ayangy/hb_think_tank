package com.yuwubao.entities;

import javax.persistence.*;

/**
 * Created by yangyu on 2018/1/12.
 * 其它配置实体
 */
@Entity
@Table(name = "other_configuration")
public class OtherConfigurationEntity {

    /**
     * 主键、自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * 内容
     */
    private String content;

    /**
     * 配置类型0(关于我们),1(法律顾问),2(广告服务),3(网站声明)
     */
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
