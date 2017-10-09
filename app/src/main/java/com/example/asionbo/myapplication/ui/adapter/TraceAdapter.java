package com.example.asionbo.myapplication.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asionbo.myapplication.R;
import com.example.asionbo.myapplication.model.Trace;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asionbo on 2017/10/8.
 */

public class TraceAdapter extends RecyclerView.Adapter<TraceAdapter.DataViewHolder> {

    private Context context;
    private List<Trace> mData = new ArrayList<>();
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;

    public void setData(List<Trace> list){
        this.mData = list;
        this.notifyDataSetChanged();
    }

    public List<Trace> getData(){
        return mData;
    }
    public TraceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trace,parent,false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {

        Trace t = mData.get(position);
        if (getItemViewType(position) == TYPE_TOP){
            holder.topLine.setVisibility(View.INVISIBLE);
            holder.time.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.content.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.dot.setBackgroundResource(R.drawable.timelline_dot_now);
        }else if (getItemViewType(position) == TYPE_NORMAL){
            holder.topLine.setVisibility(View.VISIBLE);
            holder.time.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.content.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.dot.setBackgroundResource(R.drawable.timelline_dot_normal);
        }
        holder.time.setText(t.getTime());
        holder.content.setText(t.getContent());
    }

    @Override
    public int getItemCount() {
        return mData == null?0:mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_TOP;
        return TYPE_NORMAL;
    }

    class DataViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_time) TextView time;
        @BindView(R.id.tv_content) TextView content;
        @BindView(R.id.tv_topLine) TextView topLine;
        @BindView(R.id.tv_dot) TextView dot;

        private DataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
