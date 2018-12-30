package com.example.jr.face_application_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private List<Map<String,Object>> Data;
    //布局解析器
    private LayoutInflater mInflater;
    MyRecyclerAdapter(Context context, List<Map<String, Object>> Data){
        this.Data = Data;
        this.mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.img_layout,viewGroup,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tv.setText(Data.get(i).get("image_time").toString());
        viewHolder.imageView.setImageBitmap((Bitmap) Data.get(i).get("image_bitmap"));
    }
    @Override
    public int getItemCount(){
        return Data==null ? 0 : Data.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv;
        private ImageView imageView;
        ViewHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.faceview);
            tv = (TextView) view.findViewById(R.id.img_nametab);
        }
    }
}
