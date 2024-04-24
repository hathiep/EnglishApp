package com.example.applayout.core.exercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;
import com.example.applayout.core.MainActivity;
import com.example.applayout.core.Profile;
import com.example.applayout.core.exam.ExamMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.support.SupportMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExerciseResultActivity extends AppCompatActivity {

    private ImageView imV_home, imV_learn, imV_exercise, imV_exam, imV_support, imV_profile;
    private TextView tv_exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.exercise_result_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUi();

        //next Menu
        try {
            setOnClickListener();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        //Đánh dấu activity hiện tại trên thanh menu
        imV_exercise.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_exercise.setImageResource(R.drawable.icon_exercise2);
        tv_exercise.setTextAppearance(R.style.menu_text);

        //back next home
        ImageView icNext = findViewById(R.id.ic_next);
        icNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExerciseMain.class);
                startActivity(intent);
                finish();
            }
        });
        //---------------------

        setDataResult();

    }
    private void initUi(){
        tv_exercise = findViewById(R.id.tv_exercise);
        imV_home = findViewById(R.id.imV_home);
        imV_learn = findViewById(R.id.imV_learn);
        imV_exercise = findViewById(R.id.imV_exercise);
        imV_exam = findViewById(R.id.imV_exam);
        imV_support = findViewById(R.id.imV_support);
        imV_profile = findViewById(R.id.imV_profile);
    }
    private void setOnClickListener() throws IllegalAccessException, InstantiationException {
        // Menu dưới màn hình
        onClickImVMenu(imV_learn, LearnMain.class.newInstance());
        onClickImVMenu(imV_home, MainActivity.class.newInstance());
        onClickImVMenu(imV_exam, ExamMain.class.newInstance());
        onClickImVMenu(imV_support, SupportMain.class.newInstance());
        onClickImVMenu(imV_profile, Profile.class.newInstance());
    }
    private void onClickImVMenu(ImageView imV, Context context){
        imV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), context.getClass());
                startActivity(intent);
            }
        });
    }

    private void setDataResult() {
        TextView tvResult = findViewById(R.id.tv_result);
        Intent intent = getIntent();
        int result = intent.getIntExtra("result", 0);
        setPoint(result);
        String dataResult = "Chúc mừng bạn đã hoàn thành " + result*10 + "%";
        tvResult.setText(dataResult);
    }

    private String getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();
    }

    private void setPoint(int point){
        String uid = getUid();
        Intent intent = getIntent();
        String link = intent.getStringExtra("unit");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("User/" + uid + "/exercise/" + link); // thêm đường dẫn của từng người đến chỗ cần ghi điểm
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pointed = snapshot.getValue(Integer.class);
                if (pointed < point) {
                    ref.setValue(point);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}