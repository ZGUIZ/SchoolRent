package com.example.amia.schoolrent.Bean;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;


/**
 * <p>
 * 资金提现请求
 * </p>
 *
 * @author Yanghu
 * @since 2018-10-30
 */
public class CapitalCash {
	/**
	 * 提现编号
	 */
	private String cashId;
	/**
	 * 请求提现用户的ID
	 */
	private String userId;
	/**
	 * 提现源
	 */
	private Integer source;
	/**
	 * 请求提现的金额
	 */
	private Float capital;
	/**
	 * 提现状态：
	 0，请求提交
	 1，管理员审核中
	 2，同意
	 3，提现完成
	 */
	private Integer status;
	/**
	 * 请求提现时间
	 */
	private Date requestTime;
	/**
	 * 管理员审批时间
	 */
	private Date responseTime;
	/**
	 * 审批人
	 */
	private String responsePerson;

	private String account;


	public String getCashId() {
		return cashId;
	}

	public void setCashId(String cashId) {
		this.cashId = cashId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Float getCapital() {
		return capital;
	}

	public void setCapital(Float capital) {
		this.capital = capital;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public String getResponsePerson() {
		return responsePerson;
	}

	public void setResponsePerson(String responsePerson) {
		this.responsePerson = responsePerson;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
