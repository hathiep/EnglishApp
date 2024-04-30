package com.example.applayout.core.exercise;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseUnit1AdvancedActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imV_home, imV_learn, imV_exercise, imV_exam, imV_support, imV_profile,ic_back;
    private TextView tv_exercise;
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
        setContentView(R.layout.exercise_unit1_advanced_activity);
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

        //ket noi unit
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }

        unit = (Unit) bundle.get("object_unit");
        //---------------------

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
        tv_exercise = findViewById(R.id.tv_exercise);
        imV_home = findViewById(R.id.imV_home);
        imV_learn = findViewById(R.id.imV_learn);
        imV_exercise = findViewById(R.id.imV_exercise);
        imV_exam = findViewById(R.id.imV_exam);
        imV_support = findViewById(R.id.imV_support);
        imV_profile = findViewById(R.id.imV_profile);
        ic_back = findViewById(R.id.ic_back);
    }

    private void setOnClickListener() throws IllegalAccessException, InstantiationException {
        // Menu dưới màn hình
        onClickImVMenu(imV_learn, LearnMain.class.newInstance());
        onClickImVMenu(imV_home, MainActivity.class.newInstance());
        onClickImVMenu(imV_exam, ExamMain.class.newInstance());
        onClickImVMenu(imV_support, SupportMain.class.newInstance());
        onClickImVMenu(imV_profile, Profile.class.newInstance());
        onClickImVMenu(ic_back, ExerciseMain.class.newInstance());
    }
    private void onClickImVMenu(ImageView imV, Context context){
        imV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogOutExercise(context);
            }
        });
    }

    private void GetData(int currentQuestion){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Exercise/Advanced/" + unit.getUnit() + "/" + currentQuestion);

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

                tvAnswer1.setOnClickListener(ExerciseUnit1AdvancedActivity.this);
                tvAnswer2.setOnClickListener(ExerciseUnit1AdvancedActivity.this);
                tvAnswer3.setOnClickListener(ExerciseUnit1AdvancedActivity.this);
                tvAnswer4.setOnClickListener(ExerciseUnit1AdvancedActivity.this);
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
            intent.putExtra("unit", "advanced/" + unit.getUnit().toLowerCase());
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

    private void showDialogOutExercise(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn chưa hoàn thành bài tập. Bài làm sẽ bị huỷ nếu bạn chuyển sang chức năng khác. Bạn có chắc chắn muốn tiếp tục?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Xử lý khi người dùng nhấn Yes
                Intent intent = new Intent(getApplicationContext(), context.getClass());
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Xử lý khi người dùng nhấn Cancel
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}