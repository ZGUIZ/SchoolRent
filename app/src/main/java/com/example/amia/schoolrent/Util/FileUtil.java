package com.example.amia.schoolrent.Util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class FileUtil {
    private static final String packageName = Environment.getExternalStorageDirectory().getPath()+"/SchoolRent/";

    /**
     * @param titleHead 头部标识符，建议学生的学号，以免重复
     * @param path 文件的url
     * @return
     */
    public static String copyToUpload(String titleHead,String path){
        File file = new File(path);
        if(!file.exists()){
            return null;
        }
        String fileName = packageName + titleHead+ (new Date()).getTime();
        copyFile(path,fileName);
        return fileName;
    }

    /**
     * 文件复制
     * @param srcPathStr
     * @param desPathStr
     */
    private static void copyFile(String srcPathStr, String desPathStr) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try{
            //2.创建输入输出流对象
            fis = new FileInputStream(srcPathStr);
            fos = new FileOutputStream(desPathStr);

            //创建搬运工具
            byte datas[] = new byte[1024*8];
            //创建长度
            int len = 0;
            //循环读取数据
            while((len = fis.read(datas))!=-1){
                fos.write(datas,0,len);
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                //3.释放资源
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
