package com.example.jr.face_application_view.Thread;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import com.example.jr.face_application_view.Img_Json_Bean;
import com.example.jr.face_application_view.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Get_Bitmap extends Thread {
    private Handler handler;
    private List<Img_Json_Bean.FileInfoBean> img_list;
    private List<Map<String,Object>> image_map = new ArrayList<>();
    private Context context;
    private int COUNT,offset;
    private static final int GET_DATA_SUCCESS = 1;
    public Get_Bitmap(Handler handler,int offset,int count,List<Img_Json_Bean.FileInfoBean> img_list,Context context){
        this.img_list = img_list;
        this.offset = offset;
        this.handler = handler;
        this.context = context;
        this.COUNT = count;
    }
    @Override
    public void run() {
        for(int i=0;i<COUNT;i++){
            if(i+offset+1>img_list.size()){
                handler.sendEmptyMessage(4);
                break;
            }
            String image_url = "http://193.112.122.120/"+this.img_list.get(i+offset).getFile_address();
            String image_time = this.img_list.get(i+offset).getFile_time();
            Http_Get http_get = new Http_Get(image_url);
            InputStream is = http_get.Get_InputStream();
            Map<String,Object> tmp_map = new HashMap<>();
            if(is!=null){
                System.out.println(http_get.Get_Net_Status());
                if(http_get.Get_Net_Status()==200){
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    tmp_map.put("image_time",image_time);
                    tmp_map.put("image_bitmap",bitmap);
                    this.image_map.add(i,tmp_map);
                }else{
                    unable_get(i, tmp_map);
                }
            }else{
                unable_get(i, tmp_map);
            }
        }
        handler.obtainMessage(GET_DATA_SUCCESS,image_map).sendToTarget();
    }

    private void unable_get(int i, Map<String, Object> tmp_map) {
        Resources res = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_launcher_background);
        tmp_map.put("image_time","获取失败");
        tmp_map.put("image_bitmap",bitmap);
        this.image_map.add(i,tmp_map);
    }

}
