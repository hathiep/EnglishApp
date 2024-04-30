package com.example.applayout.core.exercise;

import static com.github.mikephil.charting.utils.Utils.convertDpToPixel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.applayout.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class UnitAdapterBasic extends RecyclerView.Adapter<UnitAdapterBasic.UnitViewHolder> {

    private List<Unit> mListUnits;
    private Context  mContext;

    public UnitAdapterBasic(Context mContext, List<Unit> mListUnit) {
        this.mListUnits = mListUnit;
        this.mContext = mContext;
    }

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


        holder.tvUnit.setText(unit.getUnit());
        Glide.with(mContext).load(unit.getImg()).transform(
                new CenterCrop(),
                new GranularRoundedCorners(40, 40, 40, 40)
        ).into(holder.imV_unit);
        holder.layoutUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGoToUnit(unit);
            }
        });
    }

    private void onClickGoToUnit(Unit unit) {
        Intent intent = new Intent(mContext, ExerciseUnit1BasicActivity.class);
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
        private ImageView imV_unit;
        private RelativeLayout layoutUnit;
        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUnit = itemView.findViewById(R.id.tv_unit);
            layoutUnit = itemView.findViewById(R.id.layout_unit);
            imV_unit = itemView.findViewById(R.id.imV_unit);
        }
    }
}
