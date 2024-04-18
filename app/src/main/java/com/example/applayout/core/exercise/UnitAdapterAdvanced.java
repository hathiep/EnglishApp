package com.example.applayout.core.exercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applayout.R;

import java.util.List;

public class UnitAdapterAdvanced extends RecyclerView.Adapter<UnitAdapterAdvanced.UnitViewHolder> {

    private List<Unit> mListUnits;
    private Context  mContext;

    public UnitAdapterAdvanced(Context mContext, List<Unit> mListUnit) {
        this.mListUnits = mListUnit;
        this.mContext = mContext;
    }

//    private List<Unit> mListUnits;
//    private Context mContext;
//    private int[] mColorIds;
//
//    // Constructor
//    public UnitAdapter(Context context, List<Unit> listUnits) {
//        this.mContext = context;
//        this.mListUnits = listUnits;
//        this.mColorIds = new int[]{R.drawable.ex_bg_blue_1_corner_30, R.drawable.ex_bg_yellow_1_corner_30, R.drawable.ex_bg_red_1_corner_30, R.drawable.ex_bg_purple_1_corner_30, R.drawable.ex_bg_green_1_corner_30}; // Chỉnh sửa với màu trong colors.xml
//    }
    @NonNull
    @Override
    public UnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item_unit, parent, false);
        return new UnitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnitViewHolder holder, int position) {
        Unit unit = mListUnits.get(position);
        if (unit == null){
            return;
        }

//        // Lấy màu từ colors.xml
//        int colorId = mColorIds[position % mColorIds.length]; // Lặp lại màu nếu hết mảng màu
//        int color = ContextCompat.getColor(mContext, colorId);
//        holder.itemView.setBackgroundColor(color);
        holder.tvUnit.setText(unit.getUnit());
        holder.layoutUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGoToUnit(unit);
            }
        });
    }

    private void onClickGoToUnit(Unit unit) {
        Intent intent = new Intent(mContext, ExerciseUnit1AdvancedActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_unit",unit);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mListUnits != null){
            return mListUnits.size();
        }
        return 0;
    }

    public class UnitViewHolder extends RecyclerView.ViewHolder{
        private TextView tvUnit;
        private LinearLayout layoutUnit;
        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUnit = itemView.findViewById(R.id.tv_unit);
            tvUnit.setBackgroundResource(R.drawable.ex_bg_purple_1_corner_30);
            layoutUnit = itemView.findViewById(R.id.layout_unit);
        }
    }
}
