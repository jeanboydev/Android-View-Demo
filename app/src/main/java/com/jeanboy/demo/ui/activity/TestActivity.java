package com.jeanboy.demo.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeanboy.demo.R;
import com.jeanboy.demo.ui.widget.pulltorefresh.RefreshHandler;
import com.jeanboy.demo.ui.widget.pulltorefresh.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {


    private RefreshLayout refresh_layout;
    private ListView lv_container;

    private List<String> dataList = new ArrayList<>();
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        refresh_layout = (RefreshLayout) findViewById(R.id.refresh_layout);
        lv_container = (ListView) findViewById(R.id.lv_container);


        for (int i = 0; i < 20; i++) {
            dataList.add("第" + i + "条数据");
        }

        myAdapter = new MyAdapter(this);
        lv_container.setAdapter(myAdapter);
        refresh_layout.setHandler(new RefreshHandler() {
            @Override
            public void onRefreshBegin() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refresh_layout.refreshComplete();
                            }
                        });
                    }
                }).start();
            }
        });

    }

    class MyAdapter extends BaseAdapter {
        private Context mContext;

        public MyAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.refresh_item, parent, false);
            }
            TextView idView = (TextView) convertView.findViewById(R.id.id);
            TextView contentView = (TextView) convertView.findViewById(R.id.content);

            idView.setText(String.valueOf(position));
            contentView.setText(dataList.get(position));
            return convertView;
        }
    }


}
