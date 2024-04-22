package com.example.applayout.core.exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;
import com.example.applayout.core.MainActivity;
import com.example.applayout.core.Profile;
import com.example.applayout.core.exam.grammar.ExamGrammar;
import com.example.applayout.core.exam.listening.ExamListening;
import com.example.applayout.core.exam.synthetic.ExamSynthetic;
import com.example.applayout.core.exam.vocabulary.ExamVocabulary;
import com.example.applayout.core.exam.writing.ExamWriting;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.support.SupportMain;

public class ExamMain extends AppCompatActivity {
    ImageView imV_back, imV_home, imV_learn, imV_exercise, imV_exam, imV_support, imV_profile;
    TextView tv_exam, tv_vocabulary, tv_grammar, tv_listening, tv_writing, tv_synthetic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.exam_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Ánh xạ view
        initUi();
        // Gọi hàm onClick
        try {
            setOnClickListener();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
    // Hàm ánh xạ view
    private void initUi() {
        tv_vocabulary = findViewById(R.id.tv_vocabulary);
        tv_grammar = findViewById(R.id.tv_grammar);
        tv_listening = findViewById(R.id.tv_listening);
        tv_writing = findViewById(R.id.tv_writing);
        tv_synthetic = findViewById(R.id.tv_synthetic);
        imV_back = findViewById(R.id.imV_back);
        imV_home = findViewById(R.id.imV_home);
        imV_learn = findViewById(R.id.imV_learn);
        imV_exercise = findViewById(R.id.imV_exercise);
        imV_support = findViewById(R.id.imV_support);
        imV_profile = findViewById(R.id.imV_profile);
        //Đánh dấu activity hiện tại trên thanh menu
        imV_exam = findViewById(R.id.imV_exam);
        tv_exam = findViewById(R.id.tv_exam);
        imV_exam.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_exam.setImageResource(R.drawable.icon_exam2);
        tv_exam.setTextAppearance(R.style.menu_text);
    }
    // Hàm onClickListener
    private void setOnClickListener() throws IllegalAccessException, InstantiationException {
        // Menu giữa màn hình
        onClickTVMenu(tv_vocabulary, ExamVocabulary.class.newInstance());
        onClickTVMenu(tv_grammar, ExamGrammar.class.newInstance());
        onClickTVMenu(tv_listening, ExamListening.class.newInstance());
        onClickTVMenu(tv_writing, ExamWriting.class.newInstance());
        onClickTVMenu(tv_synthetic, ExamSynthetic.class.newInstance());
        // Menu dưới màn hình
        onClickImVMenu(imV_back, MainActivity.class.newInstance(), false);
        onClickImVMenu(imV_home, MainActivity.class.newInstance(), false);
        onClickImVMenu(imV_learn, LearnMain.class.newInstance(), true);
        onClickImVMenu(imV_exercise, ExerciseMain.class.newInstance(), true);
        onClickImVMenu(imV_support, SupportMain.class.newInstance(), true);
        onClickImVMenu(imV_profile, Profile.class.newInstance(), true);
    }
    // Hàm onClickImageView
    private void onClickImVMenu(ImageView imV, Context context, boolean ok){
        imV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), context.getClass());
                startActivity(intent);
                if(!ok) finish();
            }
        });
    }
    // Hàm onClickTextView
    private void onClickTVMenu(TextView tv, Context context){
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), context.getClass());
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}