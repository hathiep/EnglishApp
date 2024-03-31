package com.example.applayout.core.exercise;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;

import java.util.ArrayList;
import java.util.List;

public class ExerciseUnit1BasicActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvQuantity;
    private TextView tvQuestion;
    private TextView tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4;
    private List<Question> mListQuestion;
    private Question mQuestion;
    private int currentQuestion = 0;
    public int result = 0;

    private ImageView icVoice;

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
        //---------------------

        initUi();

        mListQuestion = getListQuestion();
        if (mListQuestion.isEmpty()){
            return;
        }
        setDataQuestion(mListQuestion.get(currentQuestion));
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
    private void setDataQuestion(Question question) {
        if (question == null){
            return;
        }

        mQuestion = question;

        tvAnswer1.setBackgroundResource(R.drawable.ex_bg_while_border_corner_15);
        tvAnswer2.setBackgroundResource(R.drawable.ex_bg_while_border_corner_15);
        tvAnswer3.setBackgroundResource(R.drawable.ex_bg_while_border_corner_15);
        tvAnswer4.setBackgroundResource(R.drawable.ex_bg_while_border_corner_15);

        String quantityQuestion = question.getNumber() + "/5";
        tvQuantity.setText(quantityQuestion);
        tvQuestion.setText(question.getContent());
        tvAnswer1.setText(question.getListAnswer().get(0).getContent());
        tvAnswer2.setText(question.getListAnswer().get(1).getContent());
        tvAnswer3.setText(question.getListAnswer().get(2).getContent());
        tvAnswer4.setText(question.getListAnswer().get(3).getContent());

        tvAnswer1.setOnClickListener(this);
        tvAnswer2.setOnClickListener(this);
        tvAnswer3.setOnClickListener(this);
        tvAnswer4.setOnClickListener(this);
        icVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.getVoice().start();
            }
        });
    }

    private List<Question> getListQuestion() {
        List<Question> list = new ArrayList<>();

        List<Answer> answerList1 = new ArrayList<>();
        answerList1.add(new Answer("your", true));
        answerList1.add(new Answer("is", false));
        answerList1.add(new Answer("my", false));
        answerList1.add(new Answer("are", false));

        List<Answer> answerList2 = new ArrayList<>();
        answerList2.add(new Answer("is", false));
        answerList2.add(new Answer("are", false));
        answerList2.add(new Answer("you", false));
        answerList2.add(new Answer("your", true));

        List<Answer> answerList3 = new ArrayList<>();
        answerList3.add(new Answer("am", false));
        answerList3.add(new Answer("your", true));
        answerList3.add(new Answer("are", false));
        answerList3.add(new Answer("is", false));

        List<Answer> answerList4 = new ArrayList<>();
        answerList4.add(new Answer("am", false));
        answerList4.add(new Answer("your", true));
        answerList4.add(new Answer("are", false));
        answerList4.add(new Answer("is", false));

        List<Answer> answerList5 = new ArrayList<>();
        answerList5.add(new Answer("am", false));
        answerList5.add(new Answer("your", true));
        answerList5.add(new Answer("are", false));
        answerList5.add(new Answer("is", false));

        list.add(new Question(1, "What's .... name?", MediaPlayer.create(this, R.raw.ex_q1), answerList1));
        list.add(new Question(2, "What's .... name?", MediaPlayer.create(this, R.raw.ex_q1), answerList2));
        list.add(new Question(3, "What's .... name?", MediaPlayer.create(this, R.raw.ex_q1), answerList3));
        list.add(new Question(4, "What's .... name?", MediaPlayer.create(this, R.raw.ex_q1), answerList4));
        list.add(new Question(5, "What's .... name?", MediaPlayer.create(this, R.raw.ex_q1), answerList5));


        return list;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.tv_answer1){
            tvAnswer1.setBackgroundResource(R.drawable.ex_bg_yellow_1_corner_30);
            checkAnswer(tvAnswer1, mQuestion, mQuestion.getListAnswer().get(0));
        } else if (i == R.id.tv_answer2) {
            tvAnswer2.setBackgroundResource(R.drawable.ex_bg_yellow_1_corner_30);
            checkAnswer(tvAnswer2, mQuestion, mQuestion.getListAnswer().get(1));
        } else if (i == R.id.tv_answer3) {
            tvAnswer3.setBackgroundResource(R.drawable.ex_bg_yellow_1_corner_30);
            checkAnswer(tvAnswer3, mQuestion, mQuestion.getListAnswer().get(2));
        } else if (i == R.id.tv_answer4) {
            tvAnswer4.setBackgroundResource(R.drawable.ex_bg_yellow_1_corner_30);
            checkAnswer(tvAnswer4, mQuestion, mQuestion.getListAnswer().get(3));
        }

    }

    private void checkAnswer(TextView textView, Question question, Answer answer) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (answer.isCorrect()){
                    textView.setBackgroundResource(R.drawable.ex_bg_green_1_corner_30);
                    result ++;
                    nextQuestion();
                } else {
                    textView.setBackgroundResource(R.drawable.ex_bg_red_1_corner_30);
                    showAnswerCorrect(question);
                    nextQuestion();
                }
            }
        },1000);
    }

    private void showAnswerCorrect(Question question) {
        if (question == null || question.getListAnswer() == null || question.getListAnswer().isEmpty()){
            return;
        }

        if (question.getListAnswer().get(0).isCorrect()){
            tvAnswer1.setBackgroundResource(R.drawable.ex_bg_green_1_corner_30);
        } else if (question.getListAnswer().get(1).isCorrect()){
            tvAnswer2.setBackgroundResource(R.drawable.ex_bg_green_1_corner_30);
        } else if (question.getListAnswer().get(2).isCorrect()){
            tvAnswer3.setBackgroundResource(R.drawable.ex_bg_green_1_corner_30);
        } else if (question.getListAnswer().get(3).isCorrect()){
            tvAnswer4.setBackgroundResource(R.drawable.ex_bg_green_1_corner_30);
        }
    }

    private void nextQuestion() {
        if(currentQuestion == mListQuestion.size()-1){
            Intent intent = new Intent(this, ExerciseResultActivity.class);
            intent.putExtra("result", result);
            startActivity(intent);
        } else {
            currentQuestion++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setDataQuestion(mListQuestion.get(currentQuestion));
                }
            },1000);

        }
    }

}