package com.example.amia.schoolrent.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.amia.schoolrent.Activity.ActivityInterface.BaseInfoInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.StudentInterface;
import com.example.amia.schoolrent.Activity.ModifyActivity;
import com.example.amia.schoolrent.Activity.SplashActivity;
import com.example.amia.schoolrent.Bean.AuthPicture;
import com.example.amia.schoolrent.Bean.School;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Presenter.BaseInfoContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Util.ActivityUtil;
import com.example.amia.schoolrent.Util.COSUtil;
import com.example.amia.schoolrent.Util.NetUtils;
import com.example.amia.schoolrent.View.SexDialog;

import java.util.Date;
import java.util.List;

import static com.example.amia.schoolrent.Task.StudentTask.BASE_INFO_ERROR;
import static com.example.amia.schoolrent.Task.StudentTask.BASE_INFO_SUCCESS;
import static com.example.amia.schoolrent.Task.StudentTask.UPDATE_STUDENT_ERROR;
import static com.example.amia.schoolrent.Task.StudentTask.UPDATE_STUDENT_SUCCESS;
import static com.example.amia.schoolrent.Util.COSUtil.RESULT_ERROR;
import static com.example.amia.schoolrent.Util.COSUtil.RESULT_SUCCESS;

public class BaseInfoFragement extends Fragment implements BaseInfoContract.View {
    private View view;
    private BaseInfoContract.Presenter presenter;
    private Student student;
    private Student updateStudent;

    public static BaseInfoFragement newInstance(){
        BaseInfoFragement baseInfoFragement = new BaseInfoFragement();
        return baseInfoFragement;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.base_info_fragment,container,false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        presenter.loadBaseInfo(getActivity(),student,handler);
    }

    private void init(){
        StudentInterface studentInterface = (StudentInterface) getActivity();
        student = studentInterface.getStudent();
        showStudentInfo(student);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    private void showStudentInfo(Student student){
        Context context = getActivity();

        ImageView userIcon = view.findViewById(R.id.user_cion);
        Glide.with(context).load(student.getUserIcon()).into(userIcon);
        userIcon.setOnClickListener(onClickListener);

        TextView userName = view.findViewById(R.id.user_name_tv);
        userName.setText(student.getUserName());

        userName.setOnClickListener(onClickListener);

        TextView realName = view.findViewById(R.id.real_name_tv);
        realName.setText(student.getRealName());
        realName.setOnClickListener(onClickListener);

        TextView sex = view.findViewById(R.id.sex);
        sex.setText(student.getSex());
        view.findViewById(R.id.sex_ll).setOnClickListener(onClickListener);

        School school = student.getSchool();
        if(school!=null) {
            TextView schoolTextView = view.findViewById(R.id.school_tv);
            schoolTextView.setText(school.getSchoolName());
        }

        TextView mailTextView = view.findViewById(R.id.mail_tv);
        mailTextView.setText(student.getEmail());
        mailTextView.setOnClickListener(onClickListener);

        TextView phoneTextView = view.findViewById(R.id.telephone_tv);
        phoneTextView.setText(student.getTelephone());
        phoneTextView.setOnClickListener(onClickListener);

        view.findViewById(R.id.school_cared_validate_layout).setOnClickListener(onClickListener);
        view.findViewById(R.id.id_card_validate_layout).setOnClickListener(onClickListener);

        List<AuthPicture> authPictures = student.getAuthPictureList();
        if(authPictures != null && authPictures.size() > 0){
            //设置验证信息
            for(AuthPicture authPicture:authPictures){
                int type = authPicture.getType();
                int status =authPicture.getStatus();
                if( type == 1){
                    ImageView schoolIcon = view.findViewById(R.id.validated_icon);
                    TextView schoolTV = view.findViewById(R.id.school_validate);
                   switch (status){
                       case 0:
                           break;
                       case 1:
                           schoolIcon.setImageResource(R.drawable.validate);
                           schoolTV.setText(R.string.validated);
                           break;
                       case 2:
                           schoolIcon.setImageResource(R.drawable.unvalidate);
                           schoolTV.setText(R.string.validate_no_pass);
                           break;
                       case 3:
                           schoolIcon.setImageResource(R.drawable.validate);
                           schoolTV.setText(R.string.validating);
                           break;
                   }
                } else if(type == 2){
                    ImageView idIcon = view.findViewById(R.id.id_validated_icon);
                    TextView idValTv = view.findViewById(R.id.id_card_validate);
                    switch (status){
                        case 0:
                            break;
                        case 1:
                            idIcon.setImageResource(R.drawable.validate);
                            idValTv.setText(R.string.validated);
                            break;
                        case 2:
                            idIcon.setImageResource(R.drawable.unvalidate);
                            idValTv.setText(R.string.validate_no_pass);
                            break;
                        case 3:
                            idIcon.setImageResource(R.drawable.validate);
                            idValTv.setText(R.string.validating);
                            break;
                    }
                }
            }
        }

        view.findViewById(R.id.password_reset_ll).setOnClickListener(onClickListener);
        view.findViewById(R.id.pay_password_reset_ll).setOnClickListener(onClickListener);
        view.findViewById(R.id.exit_tv).setOnClickListener(onClickListener);
    }

    private void showStudentInfo(Object o){
        try{
            Student student = (Student) o;
            this.student = student;
            showStudentInfo(student);
        } catch (ClassCastException e){
            e.printStackTrace();
            linkError();
        }
    }

    private void showModifyActivity(int flag){
        Intent intent = new Intent(getActivity(), ModifyActivity.class);
        intent.putExtra("type",flag);
        intent.putExtra("student",student);
        startActivity(intent);
    }

    private void showSexDidalog(){
        final SexDialog.Builder builder = new SexDialog.Builder(getActivity());
        builder.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sex = builder.getValue();
                updateSex(sex);
                builder.cancleDialog();
            }
        });
        SexDialog sexDialog = builder.createDialog();
        sexDialog.show();
    }

    protected void updateSex(String sex){
        Student student = new Student();
        student.setSex(sex);
        presenter.updateStudentInfo(getContext(),student,handler);
    }

    protected void exit(){
        presenter.exit();
        Activity activity = getActivity();
        Intent intent = new Intent(activity, SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case BASE_INFO_SUCCESS:
                    showStudentInfo(msg.obj);
                    break;
                case BASE_INFO_ERROR:
                    linkError();
                    break;
                case UPDATE_STUDENT_SUCCESS:
                    updateStudent = null;
                    presenter.loadBaseInfo(getContext(),student,handler);
                    break;
                case UPDATE_STUDENT_ERROR:
                    updateStudent = null;
                    Toast.makeText(getContext(),R.string.update_error,Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_SUCCESS:
                    presenter.updateStudentInfo(getContext(),updateStudent,handler);
                    break;
                case RESULT_ERROR:
                    student.setUserIcon(null);
                    linkError();
                    break;
            }
        }
    };

    @Override
    public void linkError() {
        Toast.makeText(getActivity(),R.string.link_error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(BaseInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setUserIcon(String path) {
        COSUtil cosUtil = new COSUtil(getActivity());
        String url = cosUtil.uploadFile(student,path,handler,String.valueOf(new Date().getTime()));
        updateStudent = new Student();
        updateStudent.setUserId(student.getUserId());
        updateStudent.setUserIcon(ActivityUtil.getString(getActivity(),R.string.image_host)+url);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.user_cion:
                    ((BaseInfoInterface)getActivity()).chooseUserIcon();
                    break;
                case R.id.sex_ll:
                    showSexDidalog();
                    break;
                case R.id.user_name_tv:
                    showModifyActivity(R.id.user_name_tv);
                    break;
                case R.id.real_name_tv:
                    showModifyActivity(R.id.real_name_tv);
                    break;
                case R.id.mail_tv:
                    showModifyActivity(R.id.mail_tv);
                    break;
                case R.id.telephone_tv:
                    showModifyActivity(R.id.telephone_tv);
                    break;
                case R.id.school_cared_validate_layout:

                    break;
                case R.id.id_card_validate_layout:

                    break;
                case R.id.password_reset_ll:
                    showModifyActivity(R.id.password_reset_ll);
                    break;
                case R.id.pay_password_reset_ll:
                    showModifyActivity(R.id.pay_password_reset_ll);
                    break;
                case R.id.exit_tv:
                    exit();
                    break;
            }
        }
    };
}
