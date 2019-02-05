package com.example.amia.schoolrent.Bean;

import java.util.Date;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 闲置信息
 * </p>
 *
 * @author Yanghu
 * @since 2018-10-30
 */
public class IdleInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 闲置信息编号
     */
	private String infoId;
    /**
     * 类别
     */
	private String classifyId;
    /**
     * 闲置信息内容
     */
	private String idelInfo;
    /**
     * 损毁描述
     */
	private String destoryInfo;
    /**
     * 最低押金
     */
	private Float deposit;
    /**
     * 租金
     */
	private Float retal;
    /**
     * 日租：1;固定租金：2
     */
	private Integer retalType;
    /**
     * 状态：0：未租赁，1：正在租赁，2：已完成
     */
	private Integer status;
    /**
     * 发布日期
     */
	private Date createDate;
    /**
     * 租赁最迟归还日期
     */
	private Date endDate;

	private String userId;

	/**
	 * 学校ID
	 */
	private String schoolId;
	/**
	 * 标题
	 */
	private String title;

	private List<IdelPic> picList;

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	public String getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(String classifyId) {
		this.classifyId = classifyId;
	}

	public String getIdelInfo() {
		return idelInfo;
	}

	public void setIdelInfo(String idelInfo) {
		this.idelInfo = idelInfo;
	}

	public String getDestoryInfo() {
		return destoryInfo;
	}

	public void setDestoryInfo(String destoryInfo) {
		this.destoryInfo = destoryInfo;
	}

	public Float getDeposit() {
		return deposit;
	}

	public void setDeposit(Float deposit) {
		this.deposit = deposit;
	}

	public Float getRetal() {
		return retal;
	}

	public void setRetal(Float retal) {
		this.retal = retal;
	}

	public Integer getRetalType() {
		return retalType;
	}

	public void setRetalType(Integer retalType) {
		this.retalType = retalType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<IdelPic> getPicList() {
		return picList;
	}

	public void setPicList(List<IdelPic> picList) {
		this.picList = picList;
	}
}
