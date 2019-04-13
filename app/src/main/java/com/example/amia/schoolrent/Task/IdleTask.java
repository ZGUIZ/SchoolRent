package com.example.amia.schoolrent.Task;

import android.content.Context;
import android.os.Handler;

import com.example.amia.schoolrent.Bean.Classify;
import com.example.amia.schoolrent.Bean.Eval;
import com.example.amia.schoolrent.Bean.EvalExtend;
import com.example.amia.schoolrent.Bean.IdleInfo;
import com.example.amia.schoolrent.Bean.IdleInfoExtend;
import com.example.amia.schoolrent.Bean.Rent;
import com.example.amia.schoolrent.Bean.RentExtend;
import com.example.amia.schoolrent.Bean.Student;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IdleTask extends BaseTask{
    //所有类别的ID
    String allId = "0000000";

    int ERRORWITHMESSAGE=-2;
    int ERROR = -1;
    int INDEX_CLASSIFY = 1;
    int ALL_CLASSIFY = 2;
    int CLASSIFY_ICON = 3;
    int CLASSIFY_ALL = 4;


    int PUSH_SUCCESS = 500;
    int PUSH_ERROR = 501;
    int IDLE_SUCESS = 502;
    int IDLE_ERROR = 503;
    int PIC_LOAD_SUCCESS = 504;
    int LOAD_MORE_SUCCESS = 505;
    int LOAD_MORE_ERROR = 506;
    int LOAD_RELATION_SUCCESS = 507;
    int LOAD_RELATION_ERROR = 508;
    int RENT_SUCCESS = 509;
    int RENT_ERROR = 510;
    int IDLE_RENT_LIST_SUCCESS = 511;
    int IDLE_RENT_LIST_ERROR = 512;
    int RENT_AGREE_SUCCESS = 513;
    int RENT_AGREE_ERROR = 514;
    int RENT_REFUSE_SUCCESS = 515;
    int RENT_REFUSE_ERROR = 516;
    int MY_IDLE_SUCCESS = 517;
    int MY_IDLE_ERROR =518;
    int CLOSE_IDLE_SUCCESS = 519;
    int CANCLE_SUCCESS = 520;
    int UPDATE_IDLE_SUCCESS = 521;
    int DEL_SUCCESS = 522;
    int CLASS_NAME_SUCCESS = 523;
    int LOAD_MINE_REQUEST = 524;
    int LOAD_AGREE = 525;
    int LOAD_RENTING = 526;
    int LOAD_FINISH = 527;
    int LOAD_DISAGREE = 528;
    int CANCEL_RENT = 529;
    int FIND_BY_ID = 530;
    int START_RENT = 531;
    int DEL_RENT = 532;
    int DIS_RENT = 533;
    int USER_PUSH = 534;
    int ADD_EVAL = 535;
    int GET_EVAL = 536;
    int ADD_DESTORY = 537;

    /**
     * 获取首页分类
     * @param context
     * @param handler
     */
    void getIndexClassify(Context context,IdleInfoExtend idleInfoExtend,Handler handler);

    /**
     * 从缓存中获取分类
     * @return
     */
    List<Classify> getIndexClassifyFromCache();

    /**
     * 获取所有分类
     * @param context
     * @param handler
     */
    void getAllClassify(Context context,Handler handler);

    /**
     * 上传图片
     * @param context
     * @param student
     * @param srcPath
     * @param handler
     * @return task的id
     */
    String uploadImage(Context context,Student student, String srcPath, Handler handler);
    void stopUpload(String id);
    /**
     * 获取商品列表
     * @return
     */
    void getListInfo(Context context, IdleInfoExtend idleInfo, Handler handler);

    /**
     * 发布闲置
     * @param context
     * @param idleInfo
     * @param handler
     */
    void pushIdle(Context context,IdleInfo idleInfo,Handler handler);

    void loadImage(String id,String url,Handler handler);

    /**
     * 加载商品和用户关系
     * @param context
     * @param idleInfo
     * @param handler
     */
    void getRelation(Context context,IdleInfo idleInfo,Handler handler);

    /**
     * 提交租赁请求
     * @param context
     * @param rent
     * @param handler
     */
    void addRent(Context context, Rent rent, Handler handler);

    void getIdleRentList(Context context,IdleInfo idleInfo,Handler handler);

    void agree(Context context,Rent rent,Handler handler);

    void getMyPushList(Context context,IdleInfoExtend idleInfoExtend,Handler handler);

    void closeIdle(Context context,IdleInfo idleInfo,Handler handler);

    void cancelRent(Context context,IdleInfo idleInfo,Handler handler);

    /**
     * 更新闲置信息
     * @param context
     * @param idleInfo
     * @param handler
     */
    void updateIdleInfo(Context context,IdleInfo idleInfo,Handler handler);

    void delIdleInfo(Context context,IdleInfo idleInfo,Handler handler);

    void getClassifyName(Context context,Classify classify,Handler handler);

    void loadMineRent(Context context, RentExtend rentExtend,Handler handler,int flag);

    void cancelRent(Context context,Rent rent,Handler handler);

    void findIdleById(Context context,String id,Handler handler);

    void startRent(Context context,Rent rent,Handler handler);
    void startRent(Context context,IdleInfo idleInfo,Handler handler);

    void delRent(Context context,Rent rent,Handler handler);

    void disagreeRent(Context context, Rent rent, final Handler handler);

    void getUserPush(Context context, IdleInfoExtend extend,Handler handler);

    void addEval(Context context, Eval eval,Handler handler) throws UnsupportedEncodingException;

    void getEval(Context context, EvalExtend extend,Handler handler);

    void addDestory(Context context,IdleInfo idleInfo,Handler handler);
}
