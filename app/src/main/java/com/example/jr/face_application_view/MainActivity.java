package com.example.jr.face_application_view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jr.face_application_view.Thread.Get_Bitmap;
import com.example.jr.face_application_view.Thread.Get_Img_Json;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar probar;
    private TextView tv;
    private SmartRefreshLayout smartRefreshLayout;
    private List<Map<String,Object>> Datalist = new ArrayList<>();
    private List<Img_Json_Bean.FileInfoBean> img_list;
    private MyRecyclerAdapter myRecyclerAdapter;
    private static final int GET_DATA_SUCCESS = 1;
    private static final int NETWORK_ERROR = 2;
    private static final int SERVER_ERROR = 3;
    private int offset=0,load_count=30;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        myRecyclerAdapter = new MyRecyclerAdapter(MainActivity.this, Datalist);
        String JsonStr = new Bundle(getIntent().getExtras()).getString("JsonData");
        Get_list_from_json(JsonStr);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setAdapter(myRecyclerAdapter);
        set_smarfresh();
        set_probar(true);
        new Get_Bitmap(Image_handler,offset,load_count,img_list,MainActivity.this).start();
    }
    public void Get_list_from_json(String json){
        Gson gson = new Gson();
        Img_Json_Bean img_json_bean = null;
        try{
            img_json_bean = gson.fromJson(json,Img_Json_Bean.class);
        }catch (JsonSyntaxException e){
            Toast.makeText(MainActivity.this,"识别列表获取失败，请检查网络连接",Toast.LENGTH_LONG).show();
            finish();
        }
        if (img_json_bean != null) {
            img_list = img_json_bean.getFile_Info();
        }
    }

    private void set_smarfresh(){
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                set_probar(true);
                new Get_Bitmap(Image_handler,offset,load_count,img_list,MainActivity.this).start();
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                set_probar(true);
                offset=0;
                Datalist.clear();
                img_list.clear();
                new Get_Img_Json(handler1).start();
            }
        });
    }

    private void init(){
        recyclerView = (RecyclerView) findViewById(R.id.rec_vid);
        probar = (ProgressBar) findViewById(R.id.probar);
        tv = (TextView) findViewById(R.id.probartv);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.srl_fresh);
    }

    private void set_probar(Boolean b){
        if(b){
            probar.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
        }else{
            probar.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler Image_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    List<Map<String,Object>> tmplist = (List<Map<String, Object>>) msg.obj;
                    for(int i=0;i<tmplist.size();i++){
                        Map<String,Object> tmp_map = new HashMap<>();
                        String image_time = (String) tmplist.get(i).get("image_time");
                        Bitmap bitmap = (Bitmap) tmplist.get(i).get("image_bitmap");
                        tmp_map.put("image_time",image_time);
                        tmp_map.put("image_bitmap",bitmap);
                        Datalist.add(offset+i,tmp_map);
                    }
                    offset+=load_count;
                    myRecyclerAdapter.notifyDataSetChanged();
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                    set_probar(false);
                    break;
                case 4:
                    set_probar(false);
                    Toast.makeText(MainActivity.this,"没有更多了哦",Toast.LENGTH_LONG).show();
                    smartRefreshLayout.finishLoadMore();
                    break;
                    case NETWORK_ERROR:
                    Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(MainActivity.this,"服务器发生错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_DATA_SUCCESS:
                    Get_list_from_json(msg.obj+"");
                    new Get_Bitmap(Image_handler,offset,load_count,img_list,MainActivity.this).start();
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(MainActivity.this,"服务器发生错误",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
