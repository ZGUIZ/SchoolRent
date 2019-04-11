package com.example.amia.schoolrent.Bean;

import java.util.Date;

import org.litepal.crud.LitePalSupport;

/**
 * <p>
 * 消息
 * </p>
 *
 * @author Yanghu
 * @since 2019-04-01
 */
public class Message extends LitePalSupport {

    /**
     * 消息ID
     */
	private String msgId;
    /**
     * 消息内容
     */
	private String content;
    /**
     * 标题
     */
	private String title;
    /**
     * 状态
     */
	private Integer status;
    /**
     * 日期
     */
	private Date createDate;
	private String userId;


	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
