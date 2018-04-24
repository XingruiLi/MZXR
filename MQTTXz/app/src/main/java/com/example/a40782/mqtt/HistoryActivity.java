package com.example.a40782.mqtt;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.sql.Timestamp;

public class HistoryActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OkGo.<String>get("http://gateserver01.irricontro.com:8088/sanji/alertor/history.do?alertorId=TID01I0FA&size=10&page=1").execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                JSONObject data = JSON.parseObject(response.body());
                HistoryActivity.this.setContentView(R.layout.history_box);
                ((ListView)findViewById(R.id.box)).setAdapter(new MyAdapter(getApplicationContext(),R.layout.history,data.getIntValue("totalCOunt"),data.getJSONArray("datas")));
            }
        });
    }
    class MyAdapter extends ArrayAdapter {
        int resourceId;
        int total;
        JSONArray datas;
        private MyAdapter(Context context,int resourceId,int total, JSONArray datas){
            super(context,resourceId);
            this.resourceId = resourceId;
            this.total = total;
            this.datas = datas;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return total;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView==null) {
                convertView = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            }
            if(position>=datas.size()) {
                OkGo.<String>get("http://gateserver01.irricontro.com:8088/sanji/alertor/history.do?alertorId=TID01I0FA&size=10&page=" + ((int) Math.ceil(position / 10.0))).execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject data = JSON.parseObject(response.body());
                        datas.addAll(data.getJSONArray("datas"));
                    }
                });
            }
            JSONObject data = datas.getJSONObject(position); //获得当前项的Hero数据
            Log.e("数据", data.toJSONString());
            TextView start = convertView.findViewById(R.id.startTV);
            start.setText(new Timestamp(data.getLong("start_time")).toString());
            TextView end = convertView.findViewById(R.id.endTV);
            end.setText(new Timestamp(data.getLong("end_time")).toString());
            return convertView;
        }
    }
}
