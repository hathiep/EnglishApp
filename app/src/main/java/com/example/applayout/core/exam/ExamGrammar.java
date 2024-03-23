package com.example.applayout.core.exam;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;
import com.example.applayout.core.MainActivity;
import com.example.applayout.core.Profile;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.support.SupportMain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExamGrammar extends AppCompatActivity {

    String[] list_question = {"We are doing a project for this subject","We are doing a super big project for this subject", "We are doing a super big Java Mobile project for this subject", "I am", "He is an engineer", "She is a teacher", "They are happy", "We had finish the project", "We got 10 points for this project", "We got an A in this subject "};
    String[] answers = {};

    public static int[] random(int[] numbers) {
        int n = numbers.length;
        Random random = new Random();

        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }

        return numbers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.exam_grammar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int[] question = {1};
        // Bắt sự kiện cho nút back và gán giá trị cho header
        ImageView imV_back = findViewById(R.id.imV_back);
        TextView tv_part = findViewById(R.id.tv_part);
        TextView tv_exam_name = findViewById(R.id.tv_exam_name);
        TextView tv_question_num = findViewById(R.id.tv_question_num);
        imV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExamMain.class);
                startActivity(intent);
                finish();
            }
        });
        tv_part.setText("Part A2");
        tv_exam_name.setText("Grammar");
        tv_question_num.setText(String.valueOf(question[0]) + "/10");

        //Đánh dấu activity hiện tại trên thanh menu
        ImageView imV_exam = findViewById(R.id.imV_exam);
        TextView tv_exam = findViewById(R.id.tv_exam);
        imV_exam.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_exam.setImageResource(R.drawable.icon_exam2);
        tv_exam.setTextAppearance(R.style.menu_text);

        answers = list_question[question[0]-1].split(" ");

        Button btn_reset = findViewById(R.id.btn_reset);
        Button btn_answer = findViewById(R.id.btn_answer);

        int[] arr = new int[14];
        List<TextView> list_word = new ArrayList<>();
        list_word.add(findViewById(R.id.tv_q1));
        list_word.add(findViewById(R.id.tv_q2));
        list_word.add(findViewById(R.id.tv_q3));
        list_word.add(findViewById(R.id.tv_q4));
        list_word.add(findViewById(R.id.tv_q5));
        list_word.add(findViewById(R.id.tv_q6));
        list_word.add(findViewById(R.id.tv_q7));
        list_word.add(findViewById(R.id.tv_q8));
        list_word.add(findViewById(R.id.tv_q9));
        list_word.add(findViewById(R.id.tv_q10));
        list_word.add(findViewById(R.id.tv_q11));
        list_word.add(findViewById(R.id.tv_q12));

        int[] numbers = new int[answers.length];
        for(int i=0; i<answers.length; i++) numbers[i] = i;
        int[] words_random = random(numbers);
        for(int i=0; i<12; i++){
            if(i<answers.length) list_word.get(i).setText(answers[words_random[i]]);
            else list_word.get(i).setVisibility(View.INVISIBLE);
        }

        List<TextView> list_ans = new ArrayList<>();
        list_ans.add(findViewById(R.id.tv_a1));
        list_ans.add(findViewById(R.id.tv_a2));
        list_ans.add(findViewById(R.id.tv_a3));
        list_ans.add(findViewById(R.id.tv_a4));
        list_ans.add(findViewById(R.id.tv_a5));
        list_ans.add(findViewById(R.id.tv_a6));
        list_ans.add(findViewById(R.id.tv_a7));
        list_ans.add(findViewById(R.id.tv_a8));
        list_ans.add(findViewById(R.id.tv_a9));
        list_ans.add(findViewById(R.id.tv_a10));
        list_ans.add(findViewById(R.id.tv_a11));
        list_ans.add(findViewById(R.id.tv_a12));

        ArrayList<String> sentence = new ArrayList<>();

        for(int i=0; i<list_word.size(); i++){
            TextView tv_x = list_word.get(i);
            int k = i;
            tv_x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String word = tv_x.getText().toString();
                    if(arr[k] == 0){
                        sentence.add(word);
                        tv_x.setBackgroundTintList(ContextCompat.getColorStateList(ExamGrammar.this, R.color.exam_yellow_light));
                        list_ans.get(sentence.size()-1).setText(word);
                        list_ans.get(sentence.size()-1).setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                        arr[k] = 1;
                    }
                    else if(arr[k] == 1){
                        tv_x.setBackgroundTintList(ContextCompat.getColorStateList(ExamGrammar.this, R.color.white));
                        for(int j=sentence.size()-1; j>=0; j--){
                            if(sentence.get(j).equals(word)){
                                sentence.remove(j);
                                break;
                            }
                        }
                        for(int j=0; j<12; j++){
                            TextView tv_a = list_ans.get(j);
                            if(j<sentence.size()) tv_a.setText(sentence.get(j));
                            else{
                                tv_a.setBackgroundTintMode(PorterDuff.Mode.ADD);
                                tv_a.setText("");
                            }
                        }
                        arr[k] = 0;
                    }
                }
            });
        }

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answers = list_question[question[0]-1].split("\\s+");
                if(arr[12]==0){
                    for(int i=0; i<12; i++){
                        TextView tv_a = list_ans.get(i);
                        TextView tv_q = list_word.get(i);
                        tv_a.setBackgroundTintMode(PorterDuff.Mode.ADD);
                        tv_a.setText("");
                        tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamGrammar.this, R.color.white));
                        arr[i] = 0;
                        sentence.clear();
                    }
                }
                else {
                    for(int i=0; i<12; i++){
                        TextView tv_a = list_ans.get(i);
                        if(i<sentence.size()){
                            tv_a.setText(sentence.get(i));
                            tv_a.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                            if(!sentence.get(i).equals(answers[i]))
                                tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamGrammar.this, R.color.exam_red_light));
                            else{
                                tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamGrammar.this, R.color.exam_green_light));
                            }
                            for(int t=0; t<12; t++){
                                TextView tv_q = list_word.get(t);
                                if(tv_q.getText()==tv_a.getText()){
                                    tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamGrammar.this, R.color.exam_yellow_light));
                                    break;
                                }
                            }
                        }
                        else{
                            tv_a.setBackgroundTintMode(PorterDuff.Mode.ADD);
                            tv_a.setText("");
                        }
                        arr[i] = 2;
                    }
                }
            }
        });

        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answers = list_question[question[0]-1].split("\\s+");
                if(arr[13]==0){
                    for(int i=0; i<12; i++){
                        TextView tv_a = list_ans.get(i);
                        TextView tv_q = list_word.get(i);
                        if(i<answers.length){
                            tv_a.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                            tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamGrammar.this, R.color.white));
                            tv_a.setText(answers[i]);
                            tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamGrammar.this, R.color.white));
                        }
                        btn_reset.setText("Xem lại");
                        if(question[0]==10) btn_answer.setText("Kết thúc");
                        else btn_answer.setText("Tiếp theo");
                    }
                    arr[12] = arr[13] = 1;
                }
                else {
                    if (question[0] == 10) {
                        Intent intent = new Intent(getApplicationContext(), ExamPartFinal.class);
                        startActivity(intent);
                        finish();
                    } else {
                        for(int i=0; i<12; i++){
                            list_ans.get(i).setText("");
                            list_ans.get(i).setBackgroundTintMode(PorterDuff.Mode.ADD);
                            list_ans.get(i).setBackgroundTintList(ContextCompat.getColorStateList(ExamGrammar.this, R.color.white));
                            list_word.get(i).setBackgroundTintList(ContextCompat.getColorStateList(ExamGrammar.this, R.color.white));
                            arr[i] = 0;
                        }
                        btn_reset.setText("Reset");
                        btn_answer.setText("Đáp án");
                        arr[12] = arr[13] = 0;
                        question[0]++;
                        answers = list_question[question[0]-1].split(" ");
                        int[] numbers = new int[answers.length];
                        for(int i=0; i<answers.length; i++) numbers[i] = i;
                        int[] words_random = random(numbers);
                        for(int i=0; i<12; i++){
                            if(i<answers.length) {
                                list_word.get(i).setText(answers[words_random[i]]);
                                list_word.get(i).setVisibility(View.VISIBLE);
                            }
                            else list_word.get(i).setVisibility(View.INVISIBLE);
                        }
                        sentence.clear();
                        tv_question_num.setText(String.valueOf(question[0]) + "/10");
                    }
                }
            }
        });

        //Bắt sự kiện thanh menu
        ImageView imV_home = findViewById(R.id.imV_home);
        ImageView imV_learn = findViewById(R.id.imV_learn);
        ImageView imV_exercise = findViewById(R.id.imV_exercise);
        ImageView imV_support = findViewById(R.id.imV_support);
        ImageView imV_profile = findViewById(R.id.imV_profile);

        imV_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imV_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LearnMain.class);
                startActivity(intent);
                finish();
            }
        });
        imV_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExerciseMain.class);
                startActivity(intent);
                finish();
            }
        });
        imV_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SupportMain.class);
                startActivity(intent);
                finish();
            }
        });
        imV_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
