package com.example.amia.schoolrent.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.amia.schoolrent.Activity.ActivityInterface.ComplainInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.IdleInfoInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.PicInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.RentNeedsInterface;
import com.example.amia.schoolrent.Bean.Complain;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.RentNeeds;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.ComplainFragment;
import com.example.amia.schoolrent.Fragment.MyPushFragment;
import com.example.amia.schoolrent.Fragment.PushComplainFragment;
import com.example.amia.schoolrent.Presenter.ComplainContract;
import com.example.amia.schoolrent.Presenter.OtherComplainContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.ComplainContractImpl;
import com.example.amia.schoolrent.Presenter.PersenterImpl.OtherComplainContractImpl;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.ComplainTask;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.TaskImpl.ComplainTaskImpl;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

import static com.example.amia.schoolrent.Activity.PushActivity.CHOOSE_PICTURE_FLAG;

public class ComplainActivity extends AppCompatActivity implements IdleInfoInterface, PicInterface, ComplainInterface, RentNeedsInterface {

    private IdleInfo idleInfo;
    private RentNeeds rentNeeds;
    private PushComplainFragment fragment;
    private ComplainFragment complainFragment;

    private Complain complain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        setToolBar();
        init();
    }

    private void init(){
        Intent intent = getIntent();
        idleInfo = (IdleInfo) intent.getSerializableExtra("idleInfo");
        rentNeeds = (RentNeeds) intent.getSerializableExtra("rentNeeds");
        complain = (Complain) intent.getSerializableExtra("complain");
        if(complain!=null){
            setOtherComplainFragment();
        } else {
            setPushComplainFragment();
        }
    }

    /**
     * 设置为信息举报/投诉的Fragment
     */
    private void setOtherComplainFragment(){
        try{
            complainFragment = (ComplainFragment) getSupportFragmentManager().findFragmentById(R.id.complain_layout);
            if(complainFragment == null){
                complainFragment = ComplainFragment.newInstance();
                ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),complainFragment,R.id.complain_layout);
            }
        } catch (Exception e){
            complainFragment = ComplainFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),complainFragment,R.id.complain_layout);
        }
        ComplainTask task = new ComplainTaskImpl();
        OtherComplainContract.Presenter presenter = new OtherComplainContractImpl(complainFragment,task);
        complainFragment.setPresenter(presenter);
    }

    /**
     * 设置为租赁投诉的fragment
     */
    private void setPushComplainFragment(){
        try{
            fragment = (PushComplainFragment) getSupportFragmentManager().findFragmentById(R.id.complain_layout);
            if(fragment == null){
                fragment = PushComplainFragment.newInstance();
                ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.complain_layout);
            }
        } catch (Exception e){
            fragment = PushComplainFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.complain_layout);
        }

        //设置Presenter
        IdleTask idleTask = new IdleTaskImpl();
        ComplainContract.Presenter presenter = new ComplainContractImpl(fragment,idleTask);
        fragment.setPresenter(presenter);
    }

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);
    }

    @Override
    public IdleInfo getIdleInfo() {
        return idleInfo;
    }

    @Override
    public RentNeeds getRentNeeds() {
        return rentNeeds;
    }

    @Override
    public Student getStudent() {
        return null;
    }

    @Override
    public void choosePic() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CHOOSE_PICTURE_FLAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CHOOSE_PICTURE_FLAG:
                if(resultCode == RESULT_OK){
                    //读取图片的路径
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String path = cursor.getString(columnIndex);
                    cursor.close();
                    fragment.addPic(path);
                }
                break;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    @Override
    public Complain getComplain() {
        return complain;
    }
}
