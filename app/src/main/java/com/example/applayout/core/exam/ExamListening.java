package com.example.applayout.core.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ExamListening extends AppCompatActivity {
    int question = 1;
    List<EditText> list_editText = new ArrayList<>();
    List<RelativeLayout> list_reLayout = new ArrayList<>();
    List<String[]> list_sentence = new ArrayList<>();
    List<String> list_userAnswer = new ArrayList<>();;
    Button btn_reset, btn_answer;
    int status_reset = 0;
    int status_answer = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.exam_listening);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
        tv_part.setText("Part A3");
        tv_exam_name.setText("Listening");
        tv_question_num.setText(String.valueOf(question) + "/10");

        //Đánh dấu activity hiện tại trên thanh menu
        ImageView imV_exam = findViewById(R.id.imV_exam);
        TextView tv_exam = findViewById(R.id.tv_exam);
        imV_exam.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_exam.setImageResource(R.drawable.icon_exam2);
        tv_exam.setTextAppearance(R.style.menu_text);

        set_list_reLayout();
        set_list_editText();
        list_sentence = get_sentence(question);
        set_editText();

        btn_reset = findViewById(R.id.btn_reset);
        btn_answer = findViewById(R.id.btn_answer);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status_reset == 0){
                    for(int i=0; i<list_sentence.size(); i++){
                        if(list_sentence.get(i)[2] == "1"){
                            list_editText.get(i).setText("");
                        }
                    }
                    list_userAnswer.clear();
                }
                else if(status_reset == 1) {
                    set_userAnswer();
                    btn_reset.setText("Đáp án");
                    btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamListening.this, R.color.green));
                    status_reset = 2;
                }
                else {
                    set_correctAnswer();
                    btn_reset.setText("Xem lại");
                    btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamListening.this, R.color.red));
                    status_reset = 1;
                }
            }
        });

        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status_answer == 0){
                    get_userAnswer();
                    if(check_user_filled()){
                        set_correctAnswer();
                        set_edt_enable(false);
                        if(question == 10) btn_answer.setText("Kết thúc");
                        else btn_answer.setText("Tiếp theo");
                        btn_reset.setText("Xem lại");
                        status_reset = status_answer = 1;
                    }
                    else{
                        list_userAnswer.clear();
                        Toast toast = Toast.makeText(getApplicationContext(), "Hãy điền đầy đủ các câu!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else {
                    if(question==10) {
                        Intent intent = new Intent(getApplicationContext(), ExamPartFinal.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        question++;
                        set_list_reLayout();
                        set_list_editText();
                        list_sentence = get_sentence(question);
                        set_edt_enable(true);
                        set_editText();
                        list_userAnswer.clear();
                        btn_reset.setText("Reset");
                        btn_answer.setText("Đáp án");
                        btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamListening.this, R.color.red));
                        tv_question_num.setText(String.valueOf(question) + "/10");
                        status_reset = status_answer = 0;
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

    private void set_list_reLayout(){
        list_reLayout.add(findViewById(R.id.reLay_a1));
        list_reLayout.add(findViewById(R.id.reLay_b1));
        list_reLayout.add(findViewById(R.id.reLay_a2));
        list_reLayout.add(findViewById(R.id.reLay_b2));
        list_reLayout.add(findViewById(R.id.reLay_a3));
        list_reLayout.add(findViewById(R.id.reLay_b3));
    }
    private void set_list_editText(){
        list_editText.add(findViewById(R.id.edt_a1));
        list_editText.add(findViewById(R.id.edt_b1));
        list_editText.add(findViewById(R.id.edt_a2));
        list_editText.add(findViewById(R.id.edt_b2));
        list_editText.add(findViewById(R.id.edt_a3));
        list_editText.add(findViewById(R.id.edt_b3));
    }

    private void set_editText(){
        for(int i=0; i<6; i++){
            if(i<list_sentence.size()){
                list_reLayout.get(i).setVisibility(View.VISIBLE);
                if(list_sentence.get(i)[2] == "0"){
                    list_editText.get(i).setFocusable(false);
                    list_editText.get(i).setText(list_sentence.get(i)[0]);
                }
                else{
                    list_editText.get(i).setFocusable(true);
                    list_editText.get(i).setText("");
                }
            }
            else list_reLayout.get(i).setVisibility(View.GONE);
        }
    }
    private void set_edt_enable(boolean x){
        for(int i=0; i<list_sentence.size(); i++){
            list_editText.get(i).setFocusable(x);
        }
    }
    private void set_userAnswer(){
        int t=0;
        for(int i=0; i<list_sentence.size(); i++){
            if(list_sentence.get(i)[2]=="1"){
                list_editText.get(i).setText(list_userAnswer.get(t));
                t++;
            }
        }
    }

    private void get_userAnswer(){
        for(int i=0; i<list_sentence.size(); i++){
            if(list_sentence.get(i)[2]=="1"){
                list_userAnswer.add(String.valueOf(list_editText.get(i).getText()));
            }
        }
    }
    private boolean check_user_filled(){
        for(int i=0; i<list_userAnswer.size(); i++){
            if(list_userAnswer.get(i).trim().length()==0){
                return false;
            }
        }
        return true;
    }

    private void set_correctAnswer(){
        for(int i=0; i<list_sentence.size(); i++){
            list_editText.get(i).setText(list_sentence.get(i)[0]);
        }
    }

    private List<String[]> get_sentence(int question_num){
        List<String[]> list_sentence = new ArrayList<>();

        list_sentence.add(new String[]{"Person 1 say 1", "1", "0"});
        list_sentence.add(new String[]{"Person 2 say 1", "2", "1"});
        list_sentence.add(new String[]{"Person 1 say 2", "1", "0"});
        list_sentence.add(new String[]{"Person 2 say 2", "2", "1"});
        list_sentence.add(new String[]{"Person 1 say 3", "1", "1"});
//        list_sentence.add(new String[]{"Person 2 say 3", "2", "0"});

        return list_sentence;
    }
}