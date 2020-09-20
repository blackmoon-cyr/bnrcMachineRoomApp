package com.example.photorecognition.uploadphoto.ui.dashboard;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.photorecognition.R;

import com.example.photorecognition.uploadphoto.ui.dashboard.DashboardViewModel;
import com.example.photorecognition.uploadphoto.ui.dashboard.MessageAdapter;
import com.example.photorecognition.uploadphoto.ui.dashboard.message;
import com.example.photorecognition.uploadphoto.ui.home.HttpUtil;
import com.example.photorecognition.uploadphoto.ui.home.MyStringCallBack;
import com.lzy.imagepicker.bean.ImageItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class DashboardFragment extends Fragment implements AbsListView.OnScrollListener {

    private DashboardViewModel dashboardViewModel;

    private List<message> messageList=new ArrayList<>();
    private MessageAdapter adapter;

    private int visibleLastIndex = 0; //最后的可视项索引
    private int visibleItemCount; // 当前窗口可见项总数
    private boolean isLoading = false;
    private int num = 1;

    private TextView textView;

    private ListView listView;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        textView = root.findViewById(R.id.text_dashboard);
        listView=root.findViewById((R.id.list_view));

        messageList=initMessages(num);
        adapter=new MessageAdapter(getActivity(),R.layout.message_item,messageList);

        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                messageList.get((int) id).getTime();
                BufferedReader reader=null;
                String response =null;
                try {
                    response = GetData.getResponse("http://117.114.240.111:9999/machineRoomManage-1.0-SNAPSHOT/device/getResult?time="+messageList.get((int)id).getTime());
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Map<String, List<String>> mRes = (Map<String, List<String>>) JSON.parse((response));
                System.out.println(mRes);
                showDialog(mRes.toString(),null);





            }
        });

        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }





    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.visibleItemCount = visibleItemCount;
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
        System.out.println(visibleLastIndex);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int itemsLastIndex = adapter.getCount() - 1; //数据集最后一项的索引
        int lastIndex = itemsLastIndex; //加上底部的loadMoreView项
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
//如果是自动加载,可以在这里放置异步加载数据的代码
            List<message> res = initMessages(++num);
//            System.out.println(num);
            for (message re : res) {
                messageList.add(re);
            }
            adapter.notifyDataSetChanged();

        }
    }

    public List<message> initMessages(int num){
        BufferedReader reader=null;
        List<message> messList=new ArrayList<>();
        try {
            String path =String.format( "http://117.114.240.111:9999/machineRoomManage-1.0-SNAPSHOT/device/paging?page=%d",num);

            URL url = new URL(path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(500);

            conn.setRequestMethod("GET");

            InputStream in=conn.getInputStream();
            reader = new BufferedReader( new InputStreamReader(in));
            StringBuilder response =new StringBuilder();
            String line;
            while ((line=reader.readLine())!=null){
                response.append(line);
            }
            Map<String, List<Map<String, String>>> mRes = (Map<String, List<Map<String, String>>>) JSON.parse((response).toString());

            List<Map<String, String>> lRes = mRes.get("list");

            for (Map<String, String> lRe : lRes) {
                JSONObject data= (JSONObject) JSON.parse(lRe.toString());
                messList.add(new message(data.get("brandResult").toString(), data.get("requestTime").toString()));
            }








        } catch (Exception e) {

            e.printStackTrace();

        }
        return messList;
    }

    private void showDialog(String messages, Bitmap image) {
        final newsDialog dialog = new newsDialog(getActivity());
        dialog.setMessage(messages);
        dialog.show();

    }

}