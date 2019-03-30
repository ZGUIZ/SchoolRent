package com.example.amia.schoolrent.Bean;

import java.util.Date;
import java.io.Serializable;


/**
 * <p>
 * 评价信息
 * </p>
 *
 * @author Yanghu
 * @since 2018-12-16
 */
public class Eval {

    /**
     * 评价ID
     */
	private String evalId;
    /**
     * 评价用户
     */
	private String userId;
    /**
     * 闲置商品ID
     */
	private String idleId;
    /**
     * 评价内容
     */
	private String content;
    /**
     * 评价时间
     */
	private Date evalDate;
    /**
     * 评价等级
     */
	private float level;
    /**
     * 状态
     */
	private Integer status;

	private IdleInfo idleInfo;


	public String getEvalId() {
		return evalId;
	}

	public void setEvalId(String evalId) {
		this.evalId = evalId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIdleId() {
		return idleId;
	}

	public void setIdleId(String idleId) {
		this.idleId = idleId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getEvalDate() {
		return evalDate;
	}

	public void setEvalDate(Date evalDate) {
		this.evalDate = evalDate;
	}

	public float getLevel() {
		return level;
	}

	public void setLevel(float level) {
		this.level = level;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public IdleInfo getIdleInfo() {
		return idleInfo;
	}

	public void setIdleInfo(IdleInfo idleInfo) {
		this.idleInfo = idleInfo;
	}
}
