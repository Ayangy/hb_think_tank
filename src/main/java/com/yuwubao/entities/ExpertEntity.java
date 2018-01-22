package com.yuwubao.entities;

import javax.persistence.*;

/**
 * Created by yangyu on 2017/11/3.
 * 智库专家实体
 */
@Entity
@Table( name = "zk_expert")
public class ExpertEntity {
    /**
     * 主键、自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * 专家照片
     */
    private String img;

    /**
     * 专家姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 出生日期
     */
    private String dateOfBirth;

    /**
     * 职称
     */
    private String position;

    /**
     * 职务
     */
    private String duty;

    /**
     * 专家称号
     */
    private String expertsTitles;

    /**
     * 所属机构id
     */
    private int organizationId;

    /**
     * 所属机构名
     */
    private String organizationName;

    /**
     * 学历
     */
    private String educational;

    /**
     * 学位
     */
    private String degree;

    /**
     * 简历
     */
    private String resume;

    /**
     * 研究领域
     */
    private String research;

    /**
     * 研究领域类型
     */
    private int fieldType;

    /**
     * 决策咨询成果
     */
    private String decisionConsultingResult;

    /**
     * 学术成果
     */
    private String academicWorks;

    /**
     * 0(不屏蔽), 1(屏蔽)
     */
    private int shield;

    /**
     * 排序索引
     */
    private int sortIndex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getExpertsTitles() {
        return expertsTitles;
    }

    public void setExpertsTitles(String expertsTitles) {
        this.expertsTitles = expertsTitles;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getEducational() {
        return educational;
    }

    public void setEducational(String educational) {
        this.educational = educational;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getResearch() {
        return research;
    }

    public void setResearch(String research) {
        this.research = research;
    }

    public int getFieldType() {
        return fieldType;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public String getDecisionConsultingResult() {
        return decisionConsultingResult;
    }

    public void setDecisionConsultingResult(String decisionConsultingResult) {
        this.decisionConsultingResult = decisionConsultingResult;
    }

    public String getAcademicWorks() {
        return academicWorks;
    }

    public void setAcademicWorks(String academicWorks) {
        this.academicWorks = academicWorks;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }
}
