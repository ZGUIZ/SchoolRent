package com.example.amia.schoolrent.Bean;

import java.util.Date;
import java.util.List;


/**
 * <p>
 * 订单投诉
 * </p>
 *
 * @author Yanghu
 * @since 2018-12-16
 */
public class OrderComplian {

    /**
     * 投诉ID
     */
	private String complainId;
    /**
     * 投诉内容
     */
	private String context;
    /**
     * 投诉用户
     */
	private String userId;
    /**
     * 状态
     */
	private Integer status;
    /**
     * 投诉日期
     */
	private Date complainDate;
    /**
     * 管理员回复
     */
	private String result;
    /**
     * 投诉闲置ID
     */
	private String infoId;
	private String responsePerson;

	private List<String> urls;

	/**
	 * 赔偿金额
	 */
	private Float money;


	public String getComplainId() {
		return complainId;
	}

	public void setComplainId(String complainId) {
		this.complainId = complainId;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getComplainDate() {
		return complainDate;
	}

	public void setComplainDate(Date complainDate) {
		this.complainDate = complainDate;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	public String getResponsePerson() {
		return responsePerson;
	}

	public void setResponsePerson(String responsePerson) {
		this.responsePerson = responsePerson;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}
}
