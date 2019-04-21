package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.CapitalCash;
import com.example.amia.schoolrent.Bean.Charge;

public interface ChargeTask {

    int ADD_CHARGE = 1101;
    int ADD_CAPITALCASH = 1102;

    void addCharge(Context context, Charge charge, Handler handler);
    void addCapitalCash(Context context, CapitalCash capitalCash,Handler handler);
}
