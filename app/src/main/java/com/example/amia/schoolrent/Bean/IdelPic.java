package com.example.amia.schoolrent.Bean;

import java.io.Serializable;


/**
 * <p>
 * 闲置信息相关图片
 * </p>
 *
 * @author Yanghu
 * @since 2018-10-30
 */
public class IdelPic implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 图片ID
     */
	private String picId;
    /**
     * 图片地址
     */
	private String picUrl;
    /**
     * 闲置物品ID
     */
	private String idelId;

	private String beanStatus;


	public String getPicId() {
		return picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getIdelId() {
		return idelId;
	}

	public void setIdelId(String idelId) {
		this.idelId = idelId;
	}

	public String getBeanStatus() {
		return beanStatus;
	}

	public void setBeanStatus(String beanStatus) {
		this.beanStatus = beanStatus;
	}
}
