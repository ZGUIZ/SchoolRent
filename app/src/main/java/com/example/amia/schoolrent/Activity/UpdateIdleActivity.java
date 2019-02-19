package com.example.amia.schoolrent.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.amia.schoolrent.Activity.ActivityInterface.IdleInfoInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.PushIdleInterface;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.UpdateIdleFragment;
import com.example.amia.schoolrent.Presenter.PersenterImpl.UpdateIdleContractImpl;
import com.example.amia.schoolrent.Presenter.UpdateIdleContract;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.IdleTask;
import com.example.amia.schoolrent.Task.TaskImpl.IdleTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

import static com.example.amia.schoolrent.Activity.PushActivity.ACCESS_NETWORK_STATE_FLAG;
import static com.example.amia.schoolrent.Activity.PushActivity.ACCESS_WIFI_STATE_FLAG;
import static com.example.amia.schoolrent.Activity.PushActivity.CHOOSE_PICTURE_FLAG;

public class UpdateIdleActivity extends AppCompatActivity implements IdleInfoInterface, PushIdleInterface {

    private IdleInfo idleInfo;
    private Student student;
    private UpdateIdleFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_idle);
        init();
    }

    private void init(){
        Intent intent  = getIntent();
        idleInfo = (IdleInfo) intent.getSerializableExtra("idleInfo");
        student = (Student) intent.getSerializableExtra("student");

        setToolBar();
        getPermission();

        fragment = (UpdateIdleFragment) getSupportFragmentManager().findFragmentById(R.id.update_fragment);
        if(fragment == null){
            fragment = UpdateIdleFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.update_fragment);
        }

        //设置Presenter
        IdleTask idleTask = new IdleTaskImpl();
        UpdateIdleContract.Presenter presenter = new UpdateIdleContractImpl(fragment,idleTask);
        fragment.setPresenter(presenter);
    }

    /**
     * 获取需要的权限
     */
    protected void getPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_WIFI_STATE},ACCESS_WIFI_STATE_FLAG);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_NETWORK_STATE},ACCESS_NETWORK_STATE_FLAG);
        }
    }

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.alter_idle);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UpdateIdleActivity.this.finish();
        }
    };

    @Override
    public IdleInfo getIdleInfo() {
        return idleInfo;
    }

    @Override
    public void choosePic() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CHOOSE_PICTURE_FLAG);
    }

    @Override
    public Student getStudent() {
        return student;
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
}
