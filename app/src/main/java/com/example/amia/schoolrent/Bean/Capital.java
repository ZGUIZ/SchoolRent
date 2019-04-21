package com.example.amia.schoolrent.Bean;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * 用户资金信息
 * </p>
 *
 * @author Yanghu
 * @since 2018-10-30
 */
public class Capital implements Serializable {

	private String capitalId;
	private String userId;
	private Float capital;
	private Date updateTime;


	public String getCapitalId() {
		return capitalId;
	}

	public void setCapitalId(String capitalId) {
		this.capitalId = capitalId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Float getCapital() {
		return capital;
	}

	public void setCapital(Float capital) {
		this.capital = capital;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
