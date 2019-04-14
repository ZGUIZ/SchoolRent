package com.example.amia.schoolrent.Bean;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 异常申诉
 * </p>
 *
 * @author Yanghu
 * @since 2018-10-30
 */
public class Complain implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 投诉编号
	 */
	private String complainId;
	/**
	 * 投诉用户
	 */
	private String userId;
	/**
	 * 投诉内容
	 */
	private String msg;
	/**
	 * 投诉类型：
	 1：系统功能不合理
	 2：系统功能异常
	 3：提现异常
	 */
	private Integer complainType;
	/**
	 * 投诉状态：
	 0，提交投诉请求
	 1，管理员审核完成并回应
	 2，已关闭
	 */
	private Integer status;
	/**
	 * 审核人
	 */
	private String responsePerson;
	/**
	 * 投诉时间
	 */
	private Date complainTime;
	/**
	 * 审核时间
	 */
	private Date responseTime;
	/**
	 * 管理员回应信息
	 */
	private String responseMsg;
	private String infoId;


	public String getComplainId() {
		return complainId;
	}

	public void setComplainId(String complainId) {
		this.complainId = complainId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getComplainType() {
		return complainType;
	}

	public void setComplainType(Integer complainType) {
		this.complainType = complainType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getResponsePerson() {
		return responsePerson;
	}

	public void setResponsePerson(String responsePerson) {
		this.responsePerson = responsePerson;
	}

	public Date getComplainTime() {
		return complainTime;
	}

	public void setComplainTime(Date complainTime) {
		this.complainTime = complainTime;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
}
