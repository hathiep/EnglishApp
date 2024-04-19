package com.example.applayout.core.exam.grammar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.example.applayout.core.exam.Result;
import com.example.applayout.core.exam.vocabulary.ExamVocabulary;
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

public class ExamGrammar extends AppCompatActivity {

    ImageView imV_back, imV_home, imV_learn, imV_exercise, imV_exam, imV_support, imV_profile;
    TextView tv_grammar, tv_part, tv_exam_name, tv_question_num, tv_status;
    Button btn_reset, btn_answer;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database;
    int question = 1;
    int point = 0;

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
        setUi();

        answers = list_question[question-1].split(" ");

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
                answers = list_question[question-1].split("\\s+");
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
                answers = list_question[question-1].split("\\s+");
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
                        if(question==10) btn_answer.setText("Kết thúc");
                        else btn_answer.setText("Tiếp theo");
                    }
                    arr[12] = arr[13] = 1;
                }
                else {
                    if (question == 10) {
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
                        question++;
                        answers = list_question[question-1].split(" ");
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
                        tv_question_num.setText(String.valueOf(question) + "/10");
                    }
                }
            }
        });
    }
    // Hàm ánh xạ view
    private void initUi() {
        // Ánh xạ view header
        tv_part = findViewById(R.id.tv_part);
        tv_exam_name = findViewById(R.id.tv_exam_name);
        tv_question_num = findViewById(R.id.tv_question_num);
        tv_status = findViewById(R.id.tv_status);
        // Ánh xạ view menu
        imV_back = findViewById(R.id.imV_back);
        imV_home = findViewById(R.id.imV_home);
        imV_learn = findViewById(R.id.imV_learn);
        imV_exercise = findViewById(R.id.imV_exercise);
        imV_support = findViewById(R.id.imV_support);
        imV_profile = findViewById(R.id.imV_profile);
        //Đánh dấu activity hiện tại trên thanh menu
        imV_exam = findViewById(R.id.imV_exam);
        tv_grammar = findViewById(R.id.tv_exam);
        imV_exam.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_exam.setImageResource(R.drawable.icon_exam2);
        tv_grammar.setTextAppearance(R.style.menu_text);

        // Ánh xạ view button
        btn_answer = findViewById(R.id.btn_answer);
        btn_reset = findViewById(R.id.btn_reset);
    }
    // Gán giá trị cho view
    private void setUi(){
        tv_part.setText("Part A2");
        tv_exam_name.setText("Grammar");
        tv_question_num.setText(String.valueOf(question) + "/10");
    }
    // Hàm onClickListener
    private void setOnClickListener() throws IllegalAccessException, InstantiationException {

        // Menu dưới màn hình
        onClickImVMenu(imV_back, ExamMain.class.newInstance());
        onClickImVMenu(imV_home, ExamMain.class.newInstance());
        onClickImVMenu(imV_learn, LearnMain.class.newInstance());
        onClickImVMenu(imV_exercise, ExerciseMain.class.newInstance());
        onClickImVMenu(imV_support, SupportMain.class.newInstance());
        onClickImVMenu(imV_profile, Profile.class.newInstance());
    }
    // Hàm start Final Activity
    private void sendToFinal(){
        setPoint();
        Result.point = "" + point + "/10";
        Result.part = tv_part.getText().toString().trim();
        Result.exam_name = tv_exam_name.getText().toString().trim();
        Intent intent = new Intent(getApplicationContext(), ExamPartFinal.class);
        startActivity(intent);
        finish();
    }
    // Hàm lưu điểm lên Uid trên RealtimeDatabase
    private void setPoint(){
        String uid = user.getUid();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("User/" + uid + "/exam/vocabulary"); // thêm đường dẫn của từng người đến chỗ cần ghi điểm
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref.setValue(point);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    // Hàm onClickImageView
    private void onClickImVMenu(ImageView imV, Context context){
        imV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogOutExam(context);
            }
        });
    }
    // Hàm hiển thị Dialog xác nhận làm bài
    private void showDialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bài kiểm tra Vocabulary sẽ bao gồm 10 câu, các câu sẽ lần lượt hiển thị sau khi làm xong và không được quay lại. Chúc bạn hoàn thành tốt bài kiểm tra!");

        // Nếu người dùng chọn Yes
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Thực hiện hành động khi người dùng chọn Yes
                dialog.dismiss();
            }
        });

        // Hiển thị Dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // Hàm hiển thị Dialog xác nhận sang câu tiếp theo
    private void showDialogNextQuestion(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage(s);
        String str = "Tiếp theo";
        if(question == 10) str = "Kết thúc";

        // Nếu người dùng chọn Yes
        builder.setPositiveButton(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Thực hiện hành động khi người dùng chọn Yes
                dialog.dismiss();
                if(question == 10){
                    sendToFinal();
                }
                else{

                }
            }
        });
        // Nếu người dùng chọn Cancel hoặc nhấn back
        builder.setNegativeButton("Xem lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Thực hiện đóng Dialog khi người dùng chọn Cancel
                dialog.dismiss();
            }
        });

        // Hiển thị Dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // Hàm hiển thị Dialog xác nhận chuyển màn hình
    private void showDialogOutExam(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn chưa hoàn thành bài kiểm tra. Bài làm sẽ bị huỷ nếu bạn chuyển sang chức năng khác. Bạn có chắc chắn muốn tiếp tục?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Xử lý khi người dùng nhấn Yes
                Intent intent = new Intent(getApplicationContext(), context.getClass());
                startActivity(intent);
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
