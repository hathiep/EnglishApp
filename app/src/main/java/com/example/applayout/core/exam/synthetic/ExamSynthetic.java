package com.example.applayout.core.exam.synthetic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;
import com.example.applayout.core.MainActivity;
import com.example.applayout.core.Profile;
import com.example.applayout.core.exam.ExamMain;
import com.example.applayout.core.exam.ExamPartFinal;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExamSynthetic extends AppCompatActivity {
    int question = 1;
    TextView tv_question,  tv_answer1, tv_answer2, tv_answer3, tv_answer4;
    List<TextView> list_tv_ans = new ArrayList<>();
    Button btn_answer;

    List<String> list_answer = new ArrayList<>();
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.exam_synthetic);
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
        tv_part.setText("Part A5");
        tv_exam_name.setText("Synthetic");
        tv_question_num.setText(String.valueOf(question) + "/10");

        //Đánh dấu activity hiện tại trên thanh menu
        ImageView imV_exam = findViewById(R.id.imV_exam);
        TextView tv_exam = findViewById(R.id.tv_exam);
        imV_exam.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_exam.setImageResource(R.drawable.icon_exam2);
        tv_exam.setTextAppearance(R.style.menu_text);

        init_UI();
        get_question(question);

        String[] list_question = new String[20];
//        list_question[0] = "We _____ the homework for this subject last weekend the homework";
//        for(int i=1; i<20; i++) {
//            list_question[i] = "This is question " + String.valueOf(i+1);
//        }
        List<String[]> list_answer = new ArrayList<>();
//        list_answer.add(new String[]{"finish", "are finishing", "finished", "were finished", "3"});
//        for(int i=1; i<20; i++){
//            list_answer.add(new String[]{"Answer1", "Answer2" , "Answer3", "Answer4", "" + String.valueOf(new RandomArray().nextInt(4) + 1)});
//        }
//
//
        int[] result = {0};
        int[] choice = {-1};
//
//        tv_question.setText(String.valueOf(question) + ". " + list_question[question-1]);
//        for(int i=0; i<4; i++){
//            int index = i;
//            TextView tv_a = list_tv_ans.get(i);
//            tv_a.setText((char) ('A' + i) + ". " + list_answer.get(question-1)[i]);
//            tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamSynthetic.this, R.color.exam_blue3));
//            tv_a.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(result[0] == 0 ){
//                        for(int i=0; i<4; i++){
//                            TextView tv_a2 = list_tv_ans.get(i);
//                            tv_a2.setBackgroundTintList(ContextCompat.getColorStateList(ExamSynthetic.this, R.color.exam_blue3));
//                        }
//                        choice[0] = index;
//                        tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamSynthetic.this, R.color.exam_yellow_light));
//                    }
//                }
//            });
//        }

        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result[0] == 0){
                    if(choice[0] > -1){
                        int ans = Integer.parseInt(list_answer.get(question-1)[4]);
                        list_tv_ans.get(ans-1).setBackgroundTintList(ContextCompat.getColorStateList(ExamSynthetic.this, R.color.exam_green_light));
                        if(choice[0] + 1 != ans)
                            list_tv_ans.get(choice[0]).setBackgroundTintList(ContextCompat.getColorStateList(ExamSynthetic.this, R.color.exam_red_light));
                        if(question == 20) btn_answer.setText("Kết thúc");
                        else btn_answer.setText("Tiếp theo");
                        result[0] = 1;
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Bạn chưa chọn câu trả lời!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else {
                    if(question == 20){
                        Intent intent = new Intent(getApplicationContext(), ExamPartFinal.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        question++;
                        tv_question_num.setText(question + "/20");
                        tv_question.setText(String.valueOf(question) + ". " + list_question[question-1]);
                        choice[0] = -1;
                        for(int i=0; i<4; i++) {
                            TextView tv_a = list_tv_ans.get(i);
                            tv_a.setText((char) ('A' + i) + ". " + list_answer.get(question-1)[i]);
                            tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamSynthetic.this, R.color.exam_blue3));
                        }
                        result[0] = 0;
                        btn_answer.setText("Đáp án");
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

    private void init_UI(){
        tv_question = findViewById(R.id.tv_question);
        tv_answer1 = findViewById(R.id.tv_a1);
        tv_answer2 = findViewById(R.id.tv_a2);
        tv_answer3 = findViewById(R.id.tv_a3);
        tv_answer4 = findViewById(R.id.tv_a4);
        btn_answer = findViewById(R.id.btn_answer);
    }

    private void show_question(Question question){
        tv_question.setText(question.getContext());
        tv_answer1.setText(question.getAnswer1());
        tv_answer2.setText(question.getAnswer2());
        tv_answer3.setText(question.getAnswer3());
        tv_answer4.setText(question.getAnswer4());
    }

    private void create_database(int size){
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Exam/Synthetic/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Tìm ID của `Question` mới nhất
                long latestId = snapshot.getChildrenCount();
                // Thêm `size` `Question` mới với ID là `latestId + 1`, `latestId + 2`,...
                for (int i = 1; i <= size; i++) {
                    long nextId = latestId + i;
                    Question question = new Question("This is question " + i + "?", "This is answer1", "This is answer2", "This is answer3", "This is answer4", "This is answer1");
                    ref.child(String.valueOf(nextId)).setValue(question);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void get_question(int question_num){
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Exam/Synthetic/" + question);
//
//        Question question = new Question("a", "x", "y" , "z", "t", "x");
//        ref.setValue(question);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Question question = snapshot.getValue(Question.class);
                show_question(question);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private String getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getUid();
    }
    private void setPoint(){
        String uid = getUid();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("User/" + uid + "/..."); // thêm đường dẫn của từng người đến chỗ cần ghi điểm
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int point = 0; // Đây là điểm sau khi làm bài xong
                ref.setValue(point);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}