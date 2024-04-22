package com.example.applayout.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;
import com.example.applayout.core.exam.ExamMain;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.support.SupportMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database;
    TextView tv_user_name, tv_home, tv_learn, tv_exercise, tv_exam, tv_support;
    ImageView imV_home, imV_learn, imV_exercise, imV_exam, imV_support, imV_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUi();
        setUserName();
        try {
            setOnClickListener();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
    private void initUi() {
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_learn = findViewById(R.id.tv_learn);
        tv_exercise = findViewById(R.id.tv_exercise);
        tv_exam = findViewById(R.id.tv_exam);
        tv_support = findViewById(R.id.tv_support);
        imV_home = findViewById(R.id.imV_home);
        tv_home = findViewById(R.id.tv_home);
        imV_home.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_home.setImageResource(R.drawable.icon_home2);
        tv_home.setTextAppearance(R.style.menu_text);
        imV_learn = findViewById(R.id.imV_learn);
        imV_exercise = findViewById(R.id.imV_exercise);
        imV_exam = findViewById(R.id.imV_exam);
        imV_support = findViewById(R.id.imV_support);
        imV_profile = findViewById(R.id.imV_profile);
    }
    private void setOnClickListener() throws IllegalAccessException, InstantiationException {
        // Menu giữa màn hình
        onClickTVMenu(tv_learn, LearnMain.class.newInstance());
        onClickTVMenu(tv_exercise, ExerciseMain.class.newInstance());
        onClickTVMenu(tv_exam, ExamMain.class.newInstance());
        onClickTVMenu(tv_support, SupportMain.class.newInstance());
        // Menu dưới màn hình
        onClickImVMenu(imV_learn, LearnMain.class.newInstance());
        onClickImVMenu(imV_exercise, ExerciseMain.class.newInstance());
        onClickImVMenu(imV_exam, ExamMain.class.newInstance());
        onClickImVMenu(imV_support, SupportMain.class.newInstance());
        onClickImVMenu(imV_profile, Profile.class.newInstance());
    }
    private void setUserName(){
        String uid = user.getUid();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("User/" + uid + "/name");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_user_name.setText(snapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void onClickImVMenu(ImageView imV, Context context){
        imV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), context.getClass());
                startActivity(intent);
                finish();
            }
        });
    }
    private void onClickTVMenu(TextView tv, Context context){
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), context.getClass());
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn muốn thoát ứng dụng?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onRestart();
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}