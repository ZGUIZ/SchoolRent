package com.example.amia.schoolrent.Bean;

import java.util.Date;


/**
 * <p>
 * 账单
 * </p>
 *
 * @author Yanghu
 * @since 2019-03-12
 */
public class CheckStatement {

    private static final long serialVersionUID = 1L;

    /**
     * 账单编号
     */
	private String stateId;
    /**
     * 用户
     */
	private String userId;
    /**
     * 性质:0.转入  1.转出
     */
	private Integer type;
    /**
     * 金额
     */
	private Float amount;
    /**
     * 描述
     */
	private String memo;
    /**
     * 日期
     */
	private Date createDate;


	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
