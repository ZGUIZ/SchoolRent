package com.example.amia.schoolrent.Activity.ActivityInterface;

import com.example.amia.schoolrent.Bean.PassWord;

public interface ForgotPassInterface {
    void codeValidateSuccess(PassWord passWord);
    String getMail();
    PassWord getPassWord();
    void setSendCodeFragment();
}
