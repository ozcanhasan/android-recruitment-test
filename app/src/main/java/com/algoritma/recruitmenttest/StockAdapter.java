package com.algoritma.recruitmenttest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.algoritma.recruitmenttest.model.StockInfo;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
    private List<StockInfo> dataList;
    private Context context;

    public StockAdapter(Context context, List<StockInfo> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(StockAdapter.ViewHolder holder, int position) {
        StockInfo stockInfo = dataList.get(position);

        if (stockInfo.getIndicator().equalsIgnoreCase("down")) {
            holder.imgIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.red_down_b_arrow_icon));
        } else {
            holder.imgIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.green_up_arrow_icon));
        }

        String[] valueList ={stockInfo.getValue1(), stockInfo.getValue2(), stockInfo.getValue3(), stockInfo.getValue4(), stockInfo.getValue5()};
        holder.tvCurrencyName.setText(stockInfo.getCurrencyName());
        holder.tvValues.setText(TextUtils.join(" # ", valueList));
        holder.tvDateTime.setText(stockInfo.getDateTime());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIndicator;
        private TextView tvValues, tvCurrencyName;
        private TextView tvDateTime;

        public ViewHolder(View itemView) {
            super(itemView);

            imgIndicator = itemView.findViewById(R.id.imgIndicator);
            tvCurrencyName = itemView.findViewById(R.id.tvCurrencyName);
            tvValues = itemView.findViewById(R.id.tvValues);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
        }
    }

    public void setData(List<StockInfo> dataList) {
        this.dataList = dataList;
        this.notifyDataSetChanged();
    }
}
