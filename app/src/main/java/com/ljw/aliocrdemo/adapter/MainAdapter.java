package com.ljw.aliocrdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ljw.aliocrdemo.R;

import java.util.ArrayList;

/**
 * 首页列表adapter
 * @author Android(JiaWei)
 * @date 2017/10/26.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MianViewHolder>{
    private Context mContext;
    private ArrayList<String> contentList;
    private OnRecyclerViewItemClickListener mRecyclerViewClick;

    public MainAdapter(Context mContext,ArrayList<String> contentList){
        this.mContext = mContext;
        this.contentList = contentList;
    }


    @Override
    public MianViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main,parent,false);
        return new MianViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MianViewHolder holder, final int position) {
        holder.textView.setText(contentList.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerViewClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contentList==null?0:contentList.size();
    }

    public class MianViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public MianViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_main_tv);
        }
    }

    public void setOnClickListener(OnRecyclerViewItemClickListener clickListener){
        this.mRecyclerViewClick = clickListener;
    }

    public interface OnRecyclerViewItemClickListener{
       void onItemClick(int position);
    }
}
