package com.example.amia.schoolrent.Bean;

import java.util.Date;
import java.io.Serializable;


/**
 * <p>
 * 用户发布的租赁需求
 * </p>
 *
 * @author Yanghu
 * @since 2018-10-30
 */
public class RentNeeds {

    private static final long serialVersionUID = 1L;

    /**
     * 需求信息编号
     */
	private String infoId;
    /**
     * 发布的信息
     */
	private String idelInfo;
    /**
     * 发布状态
     */
	private Integer status;
    /**
     * 发布日期
     */
	private Date createDate;
    /**
     * 截至日期
     */
	private Date endDate;

	/**
	 * 标题
	 */
	private String title;
	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 学校ID
	 */
	private String schoolId;

	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	public String getIdelInfo() {
		return idelInfo;
	}

	public void setIdelInfo(String idelInfo) {
		this.idelInfo = idelInfo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

}
