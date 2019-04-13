package com.example.amia.schoolrent.Presenter.PersenterImpl;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.ResponseInfo;
import com.example.amia.schoolrent.Bean.SecondResponseInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.BaseView;
import com.example.amia.schoolrent.Presenter.RentNeedsContract;
import com.example.amia.schoolrent.Task.RefuseTask;
import com.example.amia.schoolrent.Task.StudentTask;


public class RentNeedsContractImpl implements RentNeedsContract.Presenter {

    private BaseView view;
    private RefuseTask refuseTask;
    private StudentTask studentTask;

    public RentNeedsContractImpl(BaseView view, RefuseTask refuseTask,StudentTask studentTask) {
        this.view = view;
        this.refuseTask = refuseTask;
        this.studentTask = studentTask;
    }

    @Override
    public void loadResponseInfo(RentNeeds rentNeeds, Handler handler) {
        refuseTask.getReufseList(view.getContext(),rentNeeds,handler);
    }

    @Override
    public void addRefuse(ResponseInfo responseInfo, android.os.Handler handler) {
        refuseTask.addRefuse(view.getContext(),responseInfo,handler);
    }

    @Override
    public void addSecondRefuse(SecondResponseInfo secondResponseInfo, android.os.Handler handler) {
        refuseTask.addSecondRefuse(view.getContext(),secondResponseInfo,handler);
    }

    @Override
    public void getStudentInfo(Student student, Handler handler) {
        studentTask.getBaseInfo(view.getContext(),student,handler);
    }
}
