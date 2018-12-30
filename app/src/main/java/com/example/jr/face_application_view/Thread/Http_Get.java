package com.example.jr.face_application_view.Thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class Http_Get {
    private static final int GET_DATA_SUCCESS = 1;
    private static final int NETWORK_ERROR = 2;
    private static final int SERVER_ERROR = 3;
    private String Str_URL;
    private int Net_Status,Status;
    private InputStream inputStream;
    Http_Get(String Str_url){
        this.Str_URL = Str_url;Get_Data(); }
    int Get_Status(){
        return this.Status;
    }
    int Get_Net_Status(){
        return this.Net_Status;
    }
    InputStream Get_InputStream(){
        return this.inputStream;
    }
    private void Get_Data(){
        try{
            System.out.println(Str_URL);
            URL url = new URL(Str_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("Charset", "utf-8");
            this.Net_Status = connection.getResponseCode();
            if(this.Net_Status==200){
                this.Status = GET_DATA_SUCCESS;
                InputStream is = connection.getInputStream();
                if(is!=null){
                    this.inputStream = is; }
            }else{ this.Status = SERVER_ERROR; }
        }catch (IOException e){
            e.printStackTrace();
            this.Status = NETWORK_ERROR;
        }
    }
}
