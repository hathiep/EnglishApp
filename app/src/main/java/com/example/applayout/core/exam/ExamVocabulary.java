package com.example.applayout.core.exam;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Random;
import java.util.Stack;

public class ExamVocabulary extends AppCompatActivity {

    public static List<List<Integer>> generateRandomPermutations(int[] numbers) {
        List<List<Integer>> permutations = generatePermutations(numbers);

        // Sử dụng thuật toán Fisher-Yates để hoán đổi vị trí các chỉnh hợp
        Random random = new Random();
        for (int i = permutations.size() - 1; i >= 0; i--) {
            int j = random.nextInt(i + 1);
            List<Integer> temp = permutations.get(i);
            permutations.set(i, permutations.get(j));
            permutations.set(j, temp);
        }

        return permutations;
    }

    public static List<List<Integer>> generatePermutations(int[] numbers) {
        List<List<Integer>> permutations = new ArrayList<>();
        generatePermutationsHelper(numbers, new ArrayList<>(), permutations);
        return permutations;
    }

    public static void generatePermutationsHelper(int[] numbers, List<Integer> currentPermutation, List<List<Integer>> permutations) {
        if (currentPermutation.size() == 4) {
            permutations.add(new ArrayList<>(currentPermutation));
            return;
        }

        for (int i = 0; i < numbers.length; i++) {
            if (!currentPermutation.contains(numbers[i])) {
                currentPermutation.add(numbers[i]);
                generatePermutationsHelper(numbers, currentPermutation, permutations);
                currentPermutation.remove(currentPermutation.size() - 1);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.exam_vocabulary);
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
        tv_part.setText("Part A1");
        tv_exam_name.setText("Vocabulary");
        tv_question_num.setText(String.valueOf(question[0]) + "/10");

        //Đánh dấu activity hiện tại trên thanh menu
        ImageView imV_exam = findViewById(R.id.imV_exam);
        TextView tv_exam = findViewById(R.id.tv_exam);
        imV_exam.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_exam.setImageResource(R.drawable.icon_exam2);
        tv_exam.setTextAppearance(R.style.menu_text);

        //Tạo ma trận màu ngẫu nhiên các chỉnh hợp chập 4 của 4 đáp án
        int[] numbers = {0, 1, 2, 3};
        List<List<Integer>> colors_matrix = generateRandomPermutations(numbers);

        List<String[]> list_word_q = new ArrayList<>();
        List<String[]> list_word_a = new ArrayList<>();
        list_word_q.add(new String[]{"chicken", "pineapple", "shoes", "truck"});
        list_word_q.add(new String[]{"chicken", "pineapple", "shoes", "truck"});
        list_word_q.add(new String[]{"chicken", "pineapple", "shoes", "truck"});
        list_word_q.add(new String[]{"chicken", "pineapple", "shoes", "truck"});
        list_word_q.add(new String[]{"chicken", "pineapple", "shoes", "truck"});
        list_word_q.add(new String[]{"chicken", "pineapple", "shoes", "truck"});
        list_word_q.add(new String[]{"chicken", "pineapple", "shoes", "truck"});
        list_word_q.add(new String[]{"chicken", "pineapple", "shoes", "truck"});
        list_word_q.add(new String[]{"chicken", "pineapple", "shoes", "truck"});
        list_word_q.add(new String[]{"chicken", "pineapple", "shoes", "truck"});
        list_word_a.add(new String[]{"con gà", "quả dứa", "đôi giày", "xe tải"});
        list_word_a.add(new String[]{"con gà", "quả dứa", "đôi giày", "xe tải"});
        list_word_a.add(new String[]{"con gà", "quả dứa", "đôi giày", "xe tải"});
        list_word_a.add(new String[]{"con gà", "quả dứa", "đôi giày", "xe tải"});
        list_word_a.add(new String[]{"con gà", "quả dứa", "đôi giày", "xe tải"});
        list_word_a.add(new String[]{"con gà", "quả dứa", "đôi giày", "xe tải"});
        list_word_a.add(new String[]{"con gà", "quả dứa", "đôi giày", "xe tải"});
        list_word_a.add(new String[]{"con gà", "quả dứa", "đôi giày", "xe tải"});
        list_word_a.add(new String[]{"con gà", "quả dứa", "đôi giày", "xe tải"});
        list_word_a.add(new String[]{"con gà", "quả dứa", "đôi giày", "xe tải"});


        List<TextView> list_tv_q = new ArrayList<>();
        list_tv_q.add(findViewById(R.id.tv_q1));
        list_tv_q.add(findViewById(R.id.tv_q2));
        list_tv_q.add(findViewById(R.id.tv_q3));
        list_tv_q.add(findViewById(R.id.tv_q4));
        for(int i=0; i<4; i++){
            list_tv_q.get(i).setText((i+1) + ". " + list_word_q.get(question[0])[i]);
        }
        List<TextView> list_tv_a = new ArrayList<>();
        list_tv_a.add(findViewById(R.id.tv_a1));
        list_tv_a.add(findViewById(R.id.tv_a2));
        list_tv_a.add(findViewById(R.id.tv_a3));
        list_tv_a.add(findViewById(R.id.tv_a4));
        for(int i=0; i<4; i++){
            list_tv_a.get(i).setText((char)('A'+i) + ". " + list_word_a.get(question[0])[colors_matrix.get(question[0]).get(i)]);
        }
        Button btn_answer = findViewById(R.id.btn_answer);
        Button btn_reset = findViewById(R.id.btn_reset);

        int[] click_left = {0,0,0,0};
        int[] click_right = {-1,-1,-1,-1};
        int[] click_btn = {0, 0};
        int[] colors_dark = {R.color.exam_green_dark, R.color.exam_yellow_dark, R.color.exam_purple_dark, R.color.exam_blue_dark};
        int[] colors_light = {R.color.exam_green_light, R.color.exam_yellow_light, R.color.exam_purple_light, R.color.exam_blue_light};
        Stack<Integer> st = new Stack<>();

        for(int i=0; i<4; i++){
            TextView tv_q = list_tv_q.get(i);
            int index = i;
            if(click_btn[1] == 0)
                tv_q.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(click_left[index] == 0) {
                            tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_dark[index]));
                            tv_q.setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                            tv_q.setTextColor(getColor(R.color.white));
                            click_left[index] = 1;
                            st.push(index);
                        }
                        else {
                            tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_light[index]));
                            tv_q.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                            tv_q.setTextColor(getColor(R.color.black));
                            for(int i=0; i<4; i++){
                                if(click_right[i] == index){
                                    list_tv_a.get(i).setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, R.color.white));
                                    list_tv_a.get(i).setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                                    list_tv_a.get(i).setTextColor(getColor(R.color.black));
                                    click_right[i] = -1;
                                    break;
                                }
                            }
                            click_left[index] = 0;
                            if(!st.empty()) st.pop();
                        }
                    }
                });
        }

        for(int i=0; i<4; i++) {
            TextView tv_a = list_tv_a.get(i);
            int index = i;
            if(click_btn[1] == 0)
                tv_a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(click_right[index] != -1){
                            TextView tv_q = list_tv_q.get(click_right[index]);
                            tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_light[click_right[index]]));
                            tv_q.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                            tv_q.setTextColor(getColor(R.color.black));
                            tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, R.color.white));
                            tv_a.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                            tv_a.setTextColor(getColor(R.color.black));
                            click_left[click_right[index]] = 0;
                            click_right[index] = -1;
                        }
                        else if(st.empty()){
                            Toast toast = Toast.makeText(getApplicationContext(), "Hãy chọn từ bên trái trước!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else {
                            tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_dark[st.peek()]));
                            tv_a.setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                            tv_a.setTextColor(getColor(R.color.white));
                            click_right[index] = st.peek();
                            st.pop();
                        }
                    }
                });
        }

        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int full = 0;
                for(int i=0; i<4; i++){
                    if(click_right[i]==-1){
                        full = 1;
                        break;
                    }
                }
                if(full == 1){
                    Toast toast = Toast.makeText(getApplicationContext(), "Hãy chọn đầy đủ các từ!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(click_btn[1] == 0) {
                    for(int i=0; i<4; i++){
                        TextView tv_q = list_tv_q.get(i);
                        TextView tv_a = list_tv_a.get(i);
                        tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_dark[i]));
                        tv_q.setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                        tv_q.setTextColor(getColor(R.color.white));
                        tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_dark[colors_matrix.get(question[0]).get(i)]));
                        tv_a.setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                        tv_a.setTextColor(getColor(R.color.white));
                    }
                    btn_reset.setText("Xem lại");
                    if(question[0]==10) btn_answer.setText("Kết thúc");
                    else btn_answer.setText("Tiếp theo");
                    click_btn[0] = click_btn[1] = 1;
                }
                else {
                    if(question[0]==10) {
                        Intent intent = new Intent(getApplicationContext(), ExamPartFinal.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        for(int i=0; i<4; i++) {
                            TextView tv_q = list_tv_q.get(i);
                            TextView tv_a = list_tv_a.get(i);
                            tv_q.setText((i+1) + ". " + list_word_q.get(question[0]+1)[i]);
                            tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_light[i]));
                            tv_q.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                            tv_q.setTextColor(getColor(R.color.black));
                            tv_a.setText((char)('A'+i) + ". " + list_word_a.get(question[0])[colors_matrix.get(question[0]+1).get(i)]);
                            tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, R.color.white));
                            tv_a.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                            tv_a.setTextColor(getColor(R.color.black));
                            click_left[i] = 0;
                            click_right[i] = -1;
                        }
                        btn_reset.setText("Reset");
                        btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, R.color.red));
                        btn_answer.setText("Đáp án");
                        click_btn[0] = click_btn[1] = 0;
                        question[0]++;
                        st.clear();
                        tv_question_num.setText(String.valueOf(question[0]) + "/10");
                    }
                }
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click_btn[0]==0){
                    for(int i=0; i<4; i++) {
                        TextView tv_q = list_tv_q.get(i);
                        TextView tv_a = list_tv_a.get(i);
                        tv_q.setText((i+1) + ". " + list_word_q.get(question[0])[i]);
                        tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_light[i]));
                        tv_q.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                        tv_q.setTextColor(getColor(R.color.black));
                        tv_a.setText((char)('A'+i) + ". " + list_word_a.get(question[0])[colors_matrix.get(question[0]).get(i)]);
                        tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, R.color.white));
                        tv_a.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                        tv_a.setTextColor(getColor(R.color.black));
                        click_left[i] = 0;
                        click_right[i] = -1;
                    }
                    st.clear();
                }
                else if(click_btn[0]==1){
                    for(int i=0; i<4; i++){
                        list_tv_a.get(i).setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_dark[click_right[i]]));
                    }
                    btn_reset.setText("Đáp án");
                    btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, R.color.green));
                    click_btn[0] = 2;
                }
                else{
                    for(int i=0; i<4; i++){
                        TextView tv_q = list_tv_q.get(i);
                        TextView tv_a = list_tv_a.get(i);
                        tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_dark[i]));
                        tv_q.setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                        tv_q.setTextColor(getColor(R.color.white));
                        tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_dark[colors_matrix.get(question[0]).get(i)]));
                        tv_a.setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                        tv_a.setTextColor(getColor(R.color.white));
                    }
                    btn_reset.setText("Xem lại");
                    btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, R.color.red));
                    click_btn[0] = 1;
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