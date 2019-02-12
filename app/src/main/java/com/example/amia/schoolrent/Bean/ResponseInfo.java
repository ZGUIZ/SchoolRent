package com.example.amia.schoolrent.Bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * 租赁信息回复
 * </p>
 *
 * @author Yanghu
 * @since 2018-10-30
 */
public class ResponseInfo implements Serializable {

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
	private String alertUser;

	/**
	 * 状态
	 */
	private Integer status;

	private Student student;

    private List<SecondResponseInfo> secondResponseInfos;

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

	public String getAlertUser() {
		return alertUser;
	}

	public void setAlertUser(String alertUser) {
		this.alertUser = alertUser;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

    public List<SecondResponseInfo> getSecondResponseInfos() {
        return secondResponseInfos;
    }

    public void setSecondResponseInfos(List<SecondResponseInfo> secondResponseInfos) {
        this.secondResponseInfos = secondResponseInfos;
    }
}
