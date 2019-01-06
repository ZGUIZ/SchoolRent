package com.example.amia.schoolrent.Presenter;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Province;
import com.example.amia.schoolrent.Bean.School;
import com.example.amia.schoolrent.Bean.Student;

import java.util.List;

public interface LoginContract {
    interface Presenter{
        void getProvince(Handler handler);
        void getCity(Province province,Handler handler);
        void getSchool(Province province,Handler handler);
        void login(Context context,Student student, Handler handler);
    }

    interface View extends BaseView<Presenter>{
        void linkError();
    }

    interface CallBack extends BaseCallBack{
        void toListPage(Student student);
        void passwordError();
        void setProvinceList(List<Province> provinceList);
        void setCityList(List<Province> cityList);
        void setSchoolList(List<School> school);
    }
}
