package com.example.photorecognition.uploadphoto.ui.home;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.photorecognition.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private static final int GALLERY_REQUEST_CODE = 0;    // 相册选图标记
    private static final int CAMERA_REQUEST_CODE = 1;    // 相机拍照标记


    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;

    private String mTempPhotoPath;
    private String mPhotoPath;


    private  ImageView photo_iv;
    private Button modify_bt;
    private Button upload_bt;
    private Spinner spinner_list;


    protected Context mContext 	= null;
    protected static String TAG = "0001";


    private Bitmap bitmapResult=null;

    private long  totalSeconds;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        photo_iv=root.findViewById(R.id.main_frag_picture_iv);
        modify_bt = root.findViewById(R.id.modify_button);
        upload_bt = root.findViewById(R.id.upload_button);
        spinner_list=root.findViewById(R.id.spinner1);
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
        mPhotoPath=Environment.getExternalStorageDirectory()  + "/Tmp_photo.jpeg";
        init();
//        mTempPhotoPath= savePicToSdcard(image,Environment.getExternalStorageDirectory().getPath(),System.currentTimeMillis() + ".jpg");




        OnClickListener modify_listener=new OnClickListener() {
            @Override
            public void onClick(View v){
//                BitmapUtils.saveMyBitmap(mTempPhotoPath,BitmapUtils.getImage(mTempPhotoPath));
                doPost(mPhotoPath);
                Toast.makeText(getActivity(),"123",Toast.LENGTH_SHORT).show();
//                try{
//                    Thread.sleep(5000);
//                }catch (Exception e ){
//
//                }
                photo_iv.setImageBitmap(getImage("http://117.114.240.111:9999/ImageUploadServer/files/out.jpg"));

                    try{
                        Thread.sleep(3000);
                        photo_iv.setImageBitmap(getImage("http://117.114.240.111:9999/ImageUploadServer/files/out.jpg"));
                        Log.d("getImage", "onClick: "+(photo_iv.getDrawable()==null));
                    }catch (Exception e ){

                }
                new File(mPhotoPath).delete();

                Log.d("getImage", "onClick: "+photo_iv.getDrawable());

            }
        };


        OnClickListener upload_listener=new OnClickListener() {
            @Override
            public void onClick(View v) {


                String base64_image=imageToBase64(mTempPhotoPath);
                base64_image = "data:image/jpeg;base64," + base64_image;
                String urlPath = "http://117.114.240.111:9999/machineRoomManage-1.0-SNAPSHOT/device/uploadNew";
                Map<String,String> postData = new HashMap<>();
                postData.put("fileBase64",base64_image);
                postData.put("modelNum",spinner_list.getSelectedItem().toString());
                Log.d("data", postData.get("fileBase64")+" : "+ postData.get("modelNum"));
                HttpUtil.submitPostData(urlPath,postData,"UTF-8");

                Toast.makeText(getActivity(),"上传图片成功",Toast.LENGTH_SHORT).show();

            }
        };

        OnClickListener photoiv_listener=new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),"789",Toast.LENGTH_SHORT).show()
                showBottomDialog();





//                selectPicture();
            }
        };



        modify_bt.setOnClickListener(modify_listener);
        upload_bt.setOnClickListener(upload_listener);
        photo_iv.setOnClickListener(photoiv_listener);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        return root;
    }
    private void showBottomDialog(){
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(getActivity(),R.style.DialogTheme);   ///////
        //2、设置布局
        View view = View.inflate(getActivity(),R.layout.fragment_select_picture,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.picture_selector_take_photo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.picture_selector_pick_picture_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.picture_selector_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }





    //激活相机操作
    private void takePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //下面这句指定调用相机拍照后的照片存储的路径
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTempPhotoPath)));
            startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);
        }

    }


    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
            // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
        }
    }

    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (shouldShowRequestPermissionRationale(permission)) {

        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap = BitmapFactory.decodeFile((imagePath));
            qualityCompress(bitmap,new File(mPhotoPath));
            photo_iv.setImageBitmap(bitmap);
        }else{
            Toast.makeText(getActivity(),"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:   // 调用相机拍照
//                    File temp = new File(mTempPhotoPath);
                    displayImage(mTempPhotoPath);
                    break;
                case GALLERY_REQUEST_CODE:  // 直接从相册获取
                    Uri uri=data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        qualityCompress(bitmap,new File(mPhotoPath));
                        photo_iv.setImageBitmap(bitmap);
                        if(uri!=null){
                            mTempPhotoPath=RealPathFromUriUtils.getRealPathFromUri(getContext(), data.getData());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    ImageView mPictureIv;
    /**
     * 上传图片
     * @param imagePath
     */
    private void uploadImage(String imagePath) {
        new NetworkTask().execute(imagePath);
    }

    /**
     * 访问网络AsyncTask,访问网络在子线程进行并返回主线程通知访问的结果
     */
    class NetworkTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return doPost(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if(!"error".equals(result)) {
                Log.i(TAG, "图片地址 " + Constant.BASE_URL + result);
                Glide.with(mContext)
                        .load(Constant.BASE_URL + result)
                        .into(mPictureIv);
            }
        }
    }

    private String doPost(String imagePath) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(30000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .build();

        String result = "error";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        // 这里演示添加用户ID
//        builder.addFormDataPart("userId", "20160519142605");
        builder.addFormDataPart("image", imagePath,
                RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePath)));

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(Constant.BASE_URL + "/uploadimage")
                .post(requestBody)
                .build();

        Log.d(TAG, "请求地址 " + Constant.BASE_URL + "/uploadimage");
        try{
            Response response = client.newCall(request).execute();

            Log.d(TAG, "响应码 " + response.code());
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                Log.d(TAG, "响应体 " + resultValue);
                return resultValue;
            }
        } catch (SocketTimeoutException e) {
            Log.i(TAG, "ontask rerun: "+e.getCause());
//            client.dispatcher().cancelAll();
//            client.connectionPool().evictAll();
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
    public static String imageToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;

        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }



    public static Bitmap getImage(String path){
        Bitmap bitmap=null;
        InputStream inputStream;


            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
//            System.out.println("tdw1");
                if(conn.getResponseCode() == 200){

                        inputStream = conn.getInputStream();


                        bitmap = BitmapFactory.decodeStream(inputStream);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }



        return bitmap;


//        return null;
    }

    /**
     * 质量压缩
     * 设置bitmap options属性，降低图片的质量，像素不会减少
     * 第一个参数为需要压缩的bitmap图片对象，第二个参数为压缩后图片保存的位置
     * 设置options 属性0-100，来实现压缩（因为png是无损压缩，所以该属性对png是无效的）
     *
     * @param bmp
     * @param file
     */
    public void qualityCompress(Bitmap bmp, File file) {
        // 0-100 100为不压缩
        int quality = 30;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDialog(String message,Bitmap bitmap) {
        final CommonDialog dialog = new CommonDialog(getContext());
        dialog.setMessage("这是一个自定义Dialog。")
                .setImageBitmap(bitmap)
                .setMessage(message)
//                .setTitle("系统提示")
                .setSingle(true).setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                dialog.dismiss();
                Toast.makeText(getContext(),"xxxx",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNegtiveClick() {
                dialog.dismiss();
                Toast.makeText(getContext(),"ssss",Toast.LENGTH_SHORT).show();
            }
        }).show();
    }

    public static Bitmap scaleMatrix(Bitmap bitmap, int width, int height){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float scaleW = (float)width/w;
        float scaleH = (float)height/h;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleW, scaleH); // 长和宽放大缩小的比例
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    public void init(){
        BufferedReader reader=null;
        try {

            String path = "http://117.114.240.111:9999/machineRoomManage-1.0-SNAPSHOT/device/index";

            URL url = new URL(path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(500);

            conn.setRequestMethod("GET");


            // 准备要传输的数据
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("User-Agent", Other.getUserAgent(context));
            // 获取所有响应头字段
            InputStream in=conn.getInputStream();
            reader = new BufferedReader( new InputStreamReader(in));
            StringBuilder response =new StringBuilder();
            String line;
            while ((line=reader.readLine())!=null){
                response.append(line);
            }

            System.out.println(response);

            JSONObject jsonObject = JSONObject.parseObject(response.toString());
            List<Integer> res =new ArrayList<>();


//            StringBuilder model_num_result_rea=(StringBuilder)jsonObject.get("model_num_result");


            StringBuilder model_num_result = new StringBuilder(jsonObject.getString("model_num_result"));
            Log.d("model_num_result",model_num_result.toString());


            model_num_result.delete(0,1);
            model_num_result.delete(model_num_result.length()-1,model_num_result.length());


            List<String> sList=Arrays.asList(model_num_result.toString().replaceAll("\"", "").split(","));
//            JSON.parse(response.toString());



            ArrayAdapter adapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,sList);
            spinner_list.setAdapter(adapter);


//            conn.connect();
//            int responseCode = conn.getResponseCode();
//            if(responseCode == HttpURLConnection.HTTP_OK) {
//                InputStream inptStream = conn.getInputStream();
//
//
//            }



        } catch (Exception e) {

            e.printStackTrace();

        }
    }



}