package com.example.amia.schoolrent.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.amia.schoolrent.Activity.ActivityInterface.CapitalInterface;
import com.example.amia.schoolrent.Activity.ActivityInterface.PicInterface;
import com.example.amia.schoolrent.Bean.Capital;
import com.example.amia.schoolrent.Bean.Student;
import com.example.amia.schoolrent.Fragment.AddCashFragment;
import com.example.amia.schoolrent.Fragment.AddChargeFragment;
import com.example.amia.schoolrent.Presenter.CashContract;
import com.example.amia.schoolrent.Presenter.ChargeContract;
import com.example.amia.schoolrent.Presenter.PersenterImpl.CashContractImpl;
import com.example.amia.schoolrent.Presenter.PersenterImpl.ChargeContractImpl;
import com.example.amia.schoolrent.R;
import com.example.amia.schoolrent.Task.ChargeTask;
import com.example.amia.schoolrent.Task.TaskImpl.ChargeTaskImpl;
import com.example.amia.schoolrent.Util.ActivityUtil;

import static com.example.amia.schoolrent.Activity.PushActivity.CHOOSE_PICTURE_FLAG;

public class AddChargeActivity extends AppCompatActivity implements CapitalInterface, PicInterface {

    protected int type;
    protected Capital capital;

    private ChargeTask task;

    private AddChargeFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_charge);
        setToolBar();
        init();
    }

    protected void setToolBar(){
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(onClickListener);

        getSupportActionBar().setTitle(R.string.cash);
    }

    protected void init(){
        Intent intent = getIntent();
        type = intent.getIntExtra("type",0);
        capital = (Capital) intent.getSerializableExtra("capital");
        if(type == 0 || capital == null){
            return;
        }
        task = new ChargeTaskImpl();
        switch (type){
            case R.id.recharge_btn:
                loadChargeFragment();
                break;
            case R.id.cash:
                loadCashFragemnt();
                break;
        }
    }

    protected void loadChargeFragment(){
        AddChargeFragment fragment = AddChargeFragment.newInstance();
        ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.add_fragment);
        ChargeContract.Presenter presenter = new ChargeContractImpl(fragment,task);
        fragment.setPresenter(presenter);
        this.fragment = fragment;
    }

    protected void loadCashFragemnt(){
        AddCashFragment fragment = AddCashFragment.newInstance();
        ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.add_fragment);
        CashContract.Presenter presenter = new CashContractImpl(fragment,task);
        fragment.setPresenter(presenter);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                default:
                    AddChargeActivity.this.finish();
                    break;
            }
        }
    };

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

    @Override
    public Capital getCapital() {
        return capital;
    }
}
