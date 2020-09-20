package com.example.photorecognition.uploadphoto.ui.dashboard;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.photorecognition.R;

import androidx.annotation.NonNull;

public class newsDialog extends Dialog {

    private TextView textview;//消息提示文本
    private ImageView imageview;
    private Button exit;
    private String message;
    private Bitmap image;


    public newsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        //空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    private void initView() {
        imageview = (ImageView) findViewById(R.id.dialog_image);
        textview = (TextView) findViewById(R.id.dialog_TextView);
        exit = (Button) findViewById(R.id.dialog_button);
    }

    private void initData() {
        if(message!=null){
            textview.setText(message);
        }
        if(image!=null){
            imageview.setImageBitmap(image);
        }
    }
    private void initEvent(){
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
