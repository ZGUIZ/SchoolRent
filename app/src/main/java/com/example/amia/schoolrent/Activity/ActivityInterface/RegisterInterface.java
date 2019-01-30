package com.example.amia.schoolrent.Activity.ActivityInterface;

import com.example.amia.schoolrent.Bean.School;
import com.example.amia.schoolrent.Bean.Student;

public interface RegisterInterface {
    void setEMail(String mail);
    void setSchool(School school);
    void setUserName(String userName);
    void setPassword(String password);
    void setConfirmPassword(String confirmPassword);
    void setConfirmPayPassword(String confirmPayPassword);
    Student getStudent();
    String getEMail();
    void loadMailFragment();
}
