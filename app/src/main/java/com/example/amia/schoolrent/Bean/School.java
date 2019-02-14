package com.example.amia.schoolrent.Bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 学校
 * </p>
 *
 * @author Yanghu
 * @since 2018-10-30
 */
public class School implements Serializable{

    private static final long serialVersionUID = 1L;

	private String schoolId;
	private String schoolName;
	private String city;
	private String province;


	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public static School getSchool(JSONObject object) throws JSONException {
		School school = new School();
		school.setSchoolId(object.getString("id"));
		school.setSchoolName(object.getString("text"));
		return school;
	}

	public static List<School> getSchool(JSONArray jsonArray) throws JSONException {
		List<School> schools = new ArrayList<>();
		for(int i=0;i<jsonArray.length();i++){
			JSONObject object = (JSONObject) jsonArray.get(i);
			School province=getSchool(object);
			schools.add(province);
		}
		return schools;
	}
}
