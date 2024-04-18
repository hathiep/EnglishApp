package com.example.applayout.core.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applayout.R;
import com.example.applayout.core.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExerciseMain extends AppCompatActivity {

    private RecyclerView rcvUnits;
    private UnitAdapterBasic mUnitAdapterBasic;

    private List<Unit> mListUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.exercise_main_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvUnit1 = findViewById(R.id.tv_unit);
        TextView tvNangCao = findViewById(R.id.tv_nangcao);
        ImageView ic_back = findViewById(R.id.ic_back);

        //RCV
        rcvUnits = findViewById(R.id.rcv_units);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvUnits.setLayoutManager(linearLayoutManager);

        mListUnits = new ArrayList<>();
        mUnitAdapterBasic = new UnitAdapterBasic(this, mListUnits);

        rcvUnits.setAdapter(mUnitAdapterBasic);

        getListUnitsFromRealtimeDatabase(tvUnit1);

        //Đánh dấu activity hiện tại trên thanh menu
        ImageView imV_exercise = findViewById(R.id.imV_exercise);
        TextView tv_exercise = findViewById(R.id.tv_exercise);
        imV_exercise.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_exercise.setImageResource(R.drawable.icon_exercise2);
        tv_exercise.setTextAppearance(R.style.menu_text);

        //next trang nang cao
        tvNangCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExerciseAdvancedActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //back trang home
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void getListUnitsFromRealtimeDatabase(TextView tvUnit1){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Exercise/Basic/Units");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mListUnits != null){
                    mListUnits.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Unit unit = dataSnapshot.getValue(Unit.class);
                    mListUnits.add(unit);
//                    Toast toast = Toast.makeText(getApplicationContext(), unit.getUnit(), Toast.LENGTH_LONG);
//                    toast.show();
//                    System.out.println(unit);

                 }

                mUnitAdapterBasic.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ExerciseMain.this, "Get list Units faild", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //next trang unit1 co ban
//        tvUnit1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ExerciseUnit1BasicActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
}