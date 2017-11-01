package com.ljw.aliocrdemo;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ljw.aliocrdemo.activity.BankCardRecognitionActivity;
import com.ljw.aliocrdemo.activity.IdCardRecognitionActivity;
import com.ljw.aliocrdemo.adapter.MainAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainAdapter.OnRecyclerViewItemClickListener {
    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private ArrayList<String> contentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatas();
        initViews();
    }

    private void initDatas() {
        contentList = new ArrayList<>();
        contentList.add("身份证识别");
        contentList.add("银行卡识别");

    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MainAdapter(this,contentList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(this);

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = null;
        switch (position){
            case 0:
                //身份证识别
                intent = new Intent(MainActivity.this,IdCardRecognitionActivity.class);
                break;
            case 1:
                //银行卡识别
                intent = new Intent(MainActivity.this,BankCardRecognitionActivity.class);
                break;
            default:
                break;
        }
        if (intent!=null){
            startActivity(intent);
        }
    }
}
