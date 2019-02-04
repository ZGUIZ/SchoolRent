package com.example.amia.schoolrent.Bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;


/**
 * <p>
 * 商品类别
 * </p>
 *
 * @author Yanghu
 * @since 2018-10-30
 */
public class Classify extends LitePalSupport implements Serializable{

    private static final long serialVersionUID = 1L;

	private String classifyId;
	private String classifyName;
	private Integer weight;
	private String imageUrl;


	public String getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(String classifyId) {
		this.classifyId = classifyId;
	}

	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
