package com.yuwubao.entities;

import javax.persistence.*;
import java.util.Date;

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
     *  头像
     */
    private String img;
    /**
     *  姓名
     */
    private String name;
    /**
     *  性别
     */
    private String sex;
    /**
     *  籍贯
     */
    private String nativePlace;
    /**
     *  出生日期
     */
    private Date dateOfBirth;
    /**
     *  职称
     */
    private String jobTitle;
    /**
     *  职务
     */
    private String duty;
    /**
     *  专家称号
     */
    private String expertsTitles;
    /**
     *  所在单位
     */
    private String company;
    /**
     *  学历
     */
    private String education;
    /**
     *  学位
     */
    private String degree;
    /**
     *  简历
     */
    private String resume;
    /**
     *  研究领域
     */
    private String researchField;
    /**
     *  决策咨询成果
     */
    private String decisionConsultingResult;
    /**
     *  学术成果
     */
    private String academicWorks;

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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
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

    public String getResearchField() {
        return researchField;
    }

    public void setResearchField(String researchField) {
        this.researchField = researchField;
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
}
