package com.example.applayout.core.exercise;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseUnit1BasicActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvQuantity;
    private TextView tvQuestion;
    private TextView tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4;
    private ImageView icVoice;
    private String correct_answer, answer1, answer2, answer3, answer4;
    private Unit unit;
    private int currentQuestion = 1;
    public int result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.exercise_unit1_basic_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Đánh dấu activity hiện tại trên thanh menu
        ImageView imV_exercise = findViewById(R.id.imV_exercise);
        TextView tv_exercise = findViewById(R.id.tv_exercise);
        imV_exercise.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_exercise.setImageResource(R.drawable.icon_exercise2);
        tv_exercise.setTextAppearance(R.style.menu_text);

        //back trang home
        ImageView ic_back = findViewById(R.id.ic_back);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExerciseMain.class);
                startActivity(intent);
                finish();
            }
        });

        //ket noi unit
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }

        unit = (Unit) bundle.get("object_unit");
        //---------------------

        initUi();
        GetData(currentQuestion);
    }

    private void initUi() {
        tvQuantity = findViewById(R.id.tv_quantity);
        tvQuestion = findViewById(R.id.tv_question);
        icVoice = findViewById(R.id.imV_volume);
        tvAnswer1 = findViewById(R.id.tv_answer1);
        tvAnswer2 = findViewById(R.id.tv_answer2);
        tvAnswer3 = findViewById(R.id.tv_answer3);
        tvAnswer4 = findViewById(R.id.tv_answer4);
    }
    private void GetData(int currentQuestion){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Exercise/Basic/" + unit.getUnit() + "/" + currentQuestion);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String number = snapshot.getKey();
                tvQuantity.setText(number + "/10");

                Question question = snapshot.getValue(Question.class);
                tvQuestion.setText(question.getQuestion());
                tvAnswer1.setText(question.getAnswer1());
                tvAnswer2.setText(question.getAnswer2());
                tvAnswer3.setText(question.getAnswer3());
                tvAnswer4.setText(question.getAnswer4());

                answer1 = question.getAnswer1();
                answer2 = question.getAnswer2();
                answer3 = question.getAnswer3();
                answer4 = question.getAnswer4();

                correct_answer = question.getCorrect_answer();

                String audioUrl = question.getVoice();
                MediaPlayer voice = new MediaPlayer();
                try {
                    voice.setDataSource(audioUrl);
                    voice.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                icVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        voice.start();
                    }
                });

                tvAnswer1.setBackgroundResource(R.drawable.ex_bg_while_border_corner_15);
                tvAnswer2.setBackgroundResource(R.drawable.ex_bg_while_border_corner_15);
                tvAnswer3.setBackgroundResource(R.drawable.ex_bg_while_border_corner_15);
                tvAnswer4.setBackgroundResource(R.drawable.ex_bg_while_border_corner_15);

                tvAnswer1.setOnClickListener(ExerciseUnit1BasicActivity.this);
                tvAnswer2.setOnClickListener(ExerciseUnit1BasicActivity.this);
                tvAnswer3.setOnClickListener(ExerciseUnit1BasicActivity.this);
                tvAnswer4.setOnClickListener(ExerciseUnit1BasicActivity.this);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.tv_answer1){
            tvAnswer1.setBackgroundResource(R.drawable.ex_bg_yellow_1_corner_30);
            checkAnswer(tvAnswer1, answer1);
        } else if (i == R.id.tv_answer2) {
            tvAnswer2.setBackgroundResource(R.drawable.ex_bg_yellow_1_corner_30);
            checkAnswer(tvAnswer2, answer2);
        } else if (i == R.id.tv_answer3) {
            tvAnswer3.setBackgroundResource(R.drawable.ex_bg_yellow_1_corner_30);
            checkAnswer(tvAnswer3, answer3);
        } else if (i == R.id.tv_answer4) {
            tvAnswer4.setBackgroundResource(R.drawable.ex_bg_yellow_1_corner_30);
            checkAnswer(tvAnswer4, answer4);
        }

    }

    private void checkAnswer(TextView textView, String answer) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Toast toast = Toast.makeText(getApplicationContext(),correct_answer, Toast.LENGTH_LONG);
//                toast.show();

                if (answer.equals(correct_answer)){
                    textView.setBackgroundResource(R.drawable.ex_bg_green_1_corner_30);
                    result ++;
                    nextQuestion();
                } else {
                    textView.setBackgroundResource(R.drawable.ex_bg_red_1_corner_30);
                    showAnswerCorrect();
                    nextQuestion();
                }
            }
        },1000);
    }

    private void showAnswerCorrect() {

        if (answer1.equals(correct_answer)){
            tvAnswer1.setBackgroundResource(R.drawable.ex_bg_green_1_corner_30);
        } else if (answer2.equals(correct_answer)){
            tvAnswer2.setBackgroundResource(R.drawable.ex_bg_green_1_corner_30);
        } else if (answer3.equals(correct_answer)){
            tvAnswer3.setBackgroundResource(R.drawable.ex_bg_green_1_corner_30);
        } else if (answer4.equals(correct_answer)){
            tvAnswer4.setBackgroundResource(R.drawable.ex_bg_green_1_corner_30);
        }
    }

    private void nextQuestion() {
        if(currentQuestion == 10){
            Intent intent = new Intent(this, ExerciseResultActivity.class);
            intent.putExtra("result", result);
            startActivity(intent);
        } else {
            currentQuestion++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GetData(currentQuestion);
                }
            },1000);

        }
    }

}