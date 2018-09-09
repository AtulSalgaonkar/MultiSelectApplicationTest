package com.example.multiselectapplicationtest;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ScanLogRvAdapter extends RecyclerView.Adapter<ScanLogRvAdapter.ViewHolder> {

    private Context mContext;
    private List<DataModel> mData;

    public ScanLogRvAdapter(Context mContext, List<DataModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModel dataModel = mData.get(position);


        String text1 = dataModel.getTest1();
        String text2 = dataModel.getTest2();


        holder.mTextOne.setText(text1);
        holder.mTextTwo.setText(text2);

        if (dataModel.isSelected()) {
            holder.mRelLay.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.selected_color, null));
        } else {
            holder.mRelLay.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.transparent, null));
        }

        /*holder.mRelLay.setOnClickListener(v -> {
            dataModel.toggle();
        });*/

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextOne, mTextTwo;
        private RelativeLayout mRelLay;

        ViewHolder(View itemView) {
            super(itemView);
            mTextOne = itemView.findViewById(R.id.text_one);
            mTextTwo = itemView.findViewById(R.id.test_two);
            mRelLay = itemView.findViewById(R.id.rel_lay);
        }
    }

}
