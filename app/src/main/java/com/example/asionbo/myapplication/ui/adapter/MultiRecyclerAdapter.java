package com.example.asionbo.myapplication.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.DigitsKeyListener;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.asionbo.myapplication.R;
import com.example.asionbo.myapplication.event.Event;
import com.example.asionbo.myapplication.utils.LogUtils;
import com.example.asionbo.myapplication.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asionbo on 2017/12/6.
 */

public class MultiRecyclerAdapter extends RecyclerView.Adapter<MultiRecyclerAdapter.DataViewHolder> {


    private Context context;
    private List<String> mData;
    private List<String> selectDatas=new ArrayList<>();
    private boolean isMulti;
    private SparseBooleanArray booleanArray;

    public MultiRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> news){
        this.mData = news;
        initArray();
        this.notifyDataSetChanged();
    }

    private void initArray() {
        if (booleanArray == null){
            booleanArray = new SparseBooleanArray(0);
        }
        for (int i=0;i<mData.size();i++){
            booleanArray.append(i,false);
        }
    }

    public List<String> getData(){
        return mData;
    }

    public List<String> getSelectItems() {
        selectDatas.clear();
        for (int j = 0; j < mData.size(); j++) {
            if (booleanArray.get(j)) {
                selectDatas.add(mData.get(j));
            }
        }
        return selectDatas;
    }
    public void setMulti(boolean multi){
        isMulti = multi;
        this.notifyDataSetChanged();
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_multi_select,parent,false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        String s = getData().get(position);
        holder.tvItem.setText(s);
        holder.checkBox.setTag(position);
        if (isMulti){//多选时单机
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(booleanArray.get(position));
            holder.rootView.setOnClickListener(l->{
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
                RxBus.INSTANCE.post(new Event(3,getSelectItems().size()));
            });
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int i = (int) buttonView.getTag();
                booleanArray.append(i,isChecked);
            });

        }else{
            //单选时单机
            holder.checkBox.setVisibility(View.GONE);
            holder.rootView.setOnClickListener(l->{
                RxBus.INSTANCE.post(new Event(1,s));
            });
        }
        holder.rootView.setOnLongClickListener(l->{
            initArray();
            RxBus.INSTANCE.post(new Event(2,s));
            return false;
        });
    }

    public void selectAll(){
        for (int i=0;i<mData.size();i++){
            if (!booleanArray.get(i)){
                booleanArray.append(i,true);
            }
        }
        notifyDataSetChanged();
    }

    public void unSelectAll(){
        for (int i=0;i<mData.size();i++){
            if (booleanArray.get(i)){
                booleanArray.append(i,false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData==null ?0:mData.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder{

        View rootView;
        @BindView(R.id.tv_item)
        TextView tvItem;
        @BindView(R.id.checkbox)
        CheckBox checkBox;

        public DataViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ButterKnife.bind(this,itemView);
        }
    }
}
