package com.example.amia.schoolrent.Bean;

import java.util.Date;
import java.io.Serializable;


/**
 * <p>
 * 租赁信息关联表
 * </p>
 *
 * @author Yanghu
 * @since 2018-10-30
 */
public class Rent{


    /**
     * 租赁ID
     */
	private String rentId;
    /**
     * 租赁用户ID
     */
	private String userId;
    /**
     * 闲置信息ID
     */
	private String idelId;
    /**
     * 状态：0.待确认 1.确认 2.拒绝
     */
	private Integer status;
    /**
     * 申请租赁开始日期
     */
	private Date startDate;
    /**
     * 申请结束日期
     */
	private Date endDate;

	private Student student;

	/**
	 * 剩余押金
	 */
	private Double lastRental;

	/**
	 * 支付密码
	 */
	private String payPassword;

	public String getRentId() {
		return rentId;
	}

	public void setRentId(String rentId) {
		this.rentId = rentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIdelId() {
		return idelId;
	}

	public void setIdelId(String idelId) {
		this.idelId = idelId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Double getLastRental() {
		return lastRental;
	}

	public void setLastRental(Double lastRental) {
		this.lastRental = lastRental;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
}
