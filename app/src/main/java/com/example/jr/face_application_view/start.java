package com.example.jr.face_application_view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.jr.face_application_view.Thread.Get_Img_Json;

public class start extends AppCompatActivity {
    private static final int GET_DATA_SUCCESS = 1;
    private static final int NETWORK_ERROR = 2;
    private static final int SERVER_ERROR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new Get_Img_Json(handler).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    Intent intent = new Intent(start.this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("JsonData",msg.obj+"");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(start.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(start.this,"服务器发生错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


}
