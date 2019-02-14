package com.example.amia.schoolrent.Bean;

import java.util.Date;
import java.io.Serializable;


/**
 * <p>
 * 用于审核身份的图片
 * </p>
 *
 * @author Yanghu
 * @since 2018-10-30
 */
public class AuthPicture implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 图片ID
     */
	private String picId;
    /**
     * 用户ID
     */
	private String userId;
    /**
     * 图片URL
     */
	private String picUrl;
    /**
     * 信息创建日期
     */
	private Date createDate;

	/**
	 * 类型
	 */
	private Integer type;

	private Integer status;

	public String getPicId() {
		return picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
