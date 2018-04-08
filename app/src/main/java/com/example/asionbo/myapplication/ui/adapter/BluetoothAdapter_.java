package com.example.asionbo.myapplication.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asionbo.myapplication.R;
import com.example.asionbo.myapplication.event.Event;
import com.example.asionbo.myapplication.model.BluetoothAddress;
import com.example.asionbo.myapplication.utils.Constant;
import com.example.asionbo.myapplication.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BluetoothAdapter_ extends RecyclerView.Adapter<BluetoothAdapter_.DataViewHolder> {

    private Context context;
    private List<BluetoothAddress> mData = new ArrayList<>();

    public BluetoothAdapter_(Context context) {
        this.context = context;
    }

    public void addData(List<BluetoothAddress> news) {
        this.mData.addAll(news);
        this.notifyDataSetChanged();
    }

    public void addOne(BluetoothAddress news) {
        this.mData.add(news);
    }

    public void setData(List<BluetoothAddress> news) {
        this.mData = news;
        this.notifyDataSetChanged();
    }

    public List<BluetoothAddress> getData() {
        return mData;
    }

    public void clear() {
        this.mData.clear();
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bluetooth, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        BluetoothAddress aNew = mData.get(position);
        bindMovieListHolder(aNew, holder);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private void bindMovieListHolder(final BluetoothAddress o, DataViewHolder holder) {
        holder.addressName.setText(o.getName());
        holder.rootView.setOnClickListener(v -> RxBus.INSTANCE.post(new Event(Constant.MESSAGE_CONNECT, o)));
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1) TextView addressName;
        View rootView;

        public DataViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
