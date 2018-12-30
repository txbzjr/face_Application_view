package com.example.jr.face_application_view.Thread;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Get_Img_Json extends Thread {
    private static final int GET_DATA_SUCCESS = 1;
    private static final int NETWORK_ERROR = 2;
    private static final int SERVER_ERROR = 3;
    private Handler handler;
    public Get_Img_Json(Handler handler){
        this.handler = handler;
    }
    @Override
    public void run() {
        Http_Get http_get = new Http_Get("http://193.112.122.120/get-file-list");
        InputStream is = http_get.Get_InputStream();
        switch (http_get.Get_Status()){
            case GET_DATA_SUCCESS:
                if(is!=null){
                    BufferedReader reader=new BufferedReader(new InputStreamReader(http_get.Get_InputStream()));
                    StringBuilder data = new StringBuilder();
                    String line;
                    try{
                        while ((line = reader.readLine()) != null) {
                            data.append(line);
                        }
                    }catch (IOException ignored){ }
                    handler.obtainMessage(GET_DATA_SUCCESS,data).sendToTarget();
                }else{ handler.sendEmptyMessage(GET_DATA_SUCCESS); }
                break;
            case NETWORK_ERROR:
                handler.sendEmptyMessage(NETWORK_ERROR);
                break;
            case SERVER_ERROR:
                handler.sendEmptyMessage(SERVER_ERROR);
                break;
        }
    }
}
