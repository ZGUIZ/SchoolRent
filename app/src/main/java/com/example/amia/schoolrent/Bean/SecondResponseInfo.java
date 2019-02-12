package com.example.amia.schoolrent.Bean;

import java.util.Date;
import java.io.Serializable;


/**
 * <p>
 * 二级回复信息
 * </p>
 *
 * @author Yanghu
 * @since 2019-02-12
 */
public class SecondResponseInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 回复ID
     */
	private String responseId;
    /**
     * 发表回复的用户ID
     */
	private String userId;
    /**
     * 发布回复的信息ID
     */
	private String infoId;
    /**
     * 回复内容
     */
	private String responseInfo;
    /**
     * 回复日期
     */
	private Date responseDate;
    /**
     * 回复某个用户并推送给对方
     */
	private String alterUser;
    /**
     * 0:禁止显示  1：正常显示
     */
	private Integer status;
    /**
     * 父级ID
     */
	private String parentId;


	public String getResponseId() {
		return responseId;
	}

	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	public String getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(String responseInfo) {
		this.responseInfo = responseInfo;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	public String getAlterUser() {
		return alterUser;
	}

	public void setAlterUser(String alterUser) {
		this.alterUser = alterUser;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}
