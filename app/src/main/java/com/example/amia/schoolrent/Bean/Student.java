package com.example.amia.schoolrent.Bean;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.LitePalSupport;

import java.util.Date;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 学生学号
 * </p>
 *
 * @author Yanghu
 * @since 2018-10-30
 */
public class Student extends LitePalSupport implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
	private String userId;
    /**
     * 学生学号
     */
	private String studentId;
    /**
     * 学校编号
     */
	private String schoolId;
    /**
     * 显示的用户名
     */
	private String userName;
    /**
     * 用户真实姓名
     */
	private String realName;
    /**
     * 登录密码
     */
	private String password;

	private String confirmPassword;
    /**
     * 支付密码
     */
	private String payPassword;

	private String confirmPayPassword;
    /**
     * 认证等级
     */
	private Integer authLevel;
    /**
     * 用户信誉
     */
	private Integer credit;
    /**
     * 用户头像URL
     */
	private String userIcon;
    /**
     * 绑定手机号码
     */
	private String telephone;
    /**
     * 绑定邮箱
     */
	private String email;
    /**
     * 用户性别
     */
	private String sex;
    /**
     * 账号状态：0：未认证，1：正常，2：禁止登陆
     */
	private Integer status;
    /**
     * 注册日期
     */
	private Date createDate;

	private School school;

	/**
	 * “10000”表示为当前登录用户
	 */
	private String beanStatus;

	private List<AuthPicture> authPictureList;

	/**
	 * 验证码
	 */
	private String code;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public Integer getAuthLevel() {
		return authLevel;
	}

	public void setAuthLevel(Integer authLevel) {
		this.authLevel = authLevel;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getBeanStatus() {
		if(beanStatus == null || "".equals(beanStatus)){
			return "edit";
		}
		return beanStatus;
	}

	public void setBeanStatus(String beanStatus) {
		this.beanStatus = beanStatus;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getConfirmPayPassword() {
		return confirmPayPassword;
	}

	public void setConfirmPayPassword(String confirmPayPassword) {
		this.confirmPayPassword = confirmPayPassword;
	}

	public List<AuthPicture> getAuthPictureList() {
		return authPictureList;
	}

	public void setAuthPictureList(List<AuthPicture> authPictureList) {
		this.authPictureList = authPictureList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 从JSON数据获取对象
	 * @param object
	 * @return
	 */
	public static Student getObjectFromJsonObject(JSONObject object) throws JSONException {
		Student student=new Student();
		student.setUserId(object.getString("userId"));
		student.setStudentId("".equals(object.getString("studentId"))? null: object.getString("studentId"));
		student.setSchoolId("".equals(object.getString("schoolId"))? null: object.getString("schoolId"));
		student.setUserName("".equals(object.getString("userName"))? null: object.getString("userName"));
		student.setRealName("".equals(object.getString("realName"))? null: object.getString("realName"));
		student.setAuthLevel(object.getInt("authLevel"));
		student.setCredit(object.getInt("credit"));
		student.setUserIcon(object.getString("userIcon"));
		student.setTelephone(object.getString("telephone"));
		student.setEmail(object.getString("email"));
		student.setSex(object.getString("sex"));
		student.setStatus(object.getInt("status"));
		student.setBeanStatus(object.getString("beanStatus"));
		return student;
	}

	public static Student getObjectFromJsonObject(String json) throws JSONException {
		JSONObject jsonObject = new JSONObject(json);
		return getObjectFromJsonObject(jsonObject);
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
