package com.example.amia.schoolrent.Presenter;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.ResponseInfo;
import com.example.amia.schoolrent.Bean.SecondResponseInfo;



public interface RentNeedsContract {
    interface Presenter{
        void loadResponseInfo(RentNeeds rentNeeds, Handler handler);
        void addRefuse(ResponseInfo responseInfo, android.os.Handler handler);
        void addSecondRefuse(SecondResponseInfo secondResponseInfo, android.os.Handler handler);
    }

    interface View extends BaseView<Presenter>{

    }
}
