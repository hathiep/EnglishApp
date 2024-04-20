package com.example.applayout.core.exam.vocabulary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;
import com.example.applayout.core.Profile;
import com.example.applayout.core.RandomArray;
import com.example.applayout.core.exam.ExamMain;
import com.example.applayout.core.exam.ExamPartFinal;
import com.example.applayout.core.exam.Result;
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
import java.util.Stack;

public class ExamVocabulary extends AppCompatActivity {
    ImageView imV_back, imV_home, imV_learn, imV_exercise, imV_exam, imV_support, imV_profile;
    TextView tv_exam, tv_part, tv_exam_name, tv_question_num, tv_status;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database;
    int question = 1;
    List<TextView> list_tv_q = new ArrayList<>(), list_tv_a = new ArrayList<>();;
    int[] word_used = new int[40];
    int[] word_index;
    int[] colors_dark = {R.color.exam_green_dark, R.color.exam_yellow_dark, R.color.exam_purple_dark, R.color.exam_blue_dark};
    int[] colors_light = {R.color.exam_green_light, R.color.exam_yellow_light, R.color.exam_purple_light, R.color.exam_blue_light};
    int[] click_left = {0,0,0,0};
    int[] click_right = {-1,-1,-1,-1};
    int[] click_btn = {0, 0};
    Stack<Integer> st = new Stack<>();
    int point = 0;
    Button btn_reset, btn_answer;
    //Tạo ma trận màu ngẫu nhiên các chỉnh hợp chập 4 của 4 đáp án
    RandomArray random = new RandomArray(4);
    List<List<Integer>> colors_matrix = random.generateRandomPermutations();

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
        getWordFromDataBase();
        showDialogConfirm();
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
        tv_exam = findViewById(R.id.tv_exam);
        imV_exam.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_exam.setImageResource(R.drawable.icon_exam2);
        tv_exam.setTextAppearance(R.style.menu_text);
        // Ánh xạ view list Textview từ và list Textview nghĩa
        list_tv_q.add(findViewById(R.id.tv_q1));
        list_tv_q.add(findViewById(R.id.tv_q2));
        list_tv_q.add(findViewById(R.id.tv_q3));
        list_tv_q.add(findViewById(R.id.tv_q4));
        list_tv_a.add(findViewById(R.id.tv_a1));
        list_tv_a.add(findViewById(R.id.tv_a2));
        list_tv_a.add(findViewById(R.id.tv_a3));
        list_tv_a.add(findViewById(R.id.tv_a4));
        // Ánh xạ view button
        btn_answer = findViewById(R.id.btn_answer);
        btn_reset = findViewById(R.id.btn_reset);
    }
    // Gán giá trị cho view
    private void setUi(){
        tv_part.setText("Part A1");
        tv_exam_name.setText("Vocabulary");
        tv_question_num.setText(String.valueOf(question) + "/10");
        word_index = new int[4];
    }
    // Hàm lấy dữ liệu word từ RealtimeDatabase
    private void getWordFromDataBase(){
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref_vocabulary = database.getReference("Exam/Vocabulary");
        ref_vocabulary.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int size = (int) snapshot.getChildrenCount();
                RandomArray random = new RandomArray();
                word_index = random.generateRandomNumbersNotInArray(word_used, size);
                database = FirebaseDatabase.getInstance();
                for(int i=0; i<4; i++){
                    setWord(i, colors_matrix.get(question).get(i));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    // Hàm set giá trị word vào TextView
    private void setWord(int i, int x){
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref_word = database.getReference("Exam/Vocabulary/" + word_index[i]);
        ref_word.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Word word = snapshot.getValue(Word.class);
                list_tv_q.get(i).setText("" + (i+1) + ". " + word.getWord());
                list_tv_a.get(x).setText((char)('A'+ x) + ". " + word.getMeaning());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    // Hàm onClickListener
    private void setOnClickListener() throws IllegalAccessException, InstantiationException {
        // Các từ trong body
        onClickWord();
        onClickMeaning();
        onClickButtonAnswer();
        onClickButtonReset();
        // Menu dưới màn hình
        onClickImVMenu(imV_back, ExamMain.class.newInstance());
        onClickImVMenu(imV_home, ExamMain.class.newInstance());
        onClickImVMenu(imV_learn, LearnMain.class.newInstance());
        onClickImVMenu(imV_exercise, ExerciseMain.class.newInstance());
        onClickImVMenu(imV_support, SupportMain.class.newInstance());
        onClickImVMenu(imV_profile, Profile.class.newInstance());
    }
    // Hàm onClick Word bên trái
    private void onClickWord(){
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
    }
    // Hàm onClick Meaning bên phải
    private void onClickMeaning(){
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
    }
    // Hàm onClick Button Đáp án
    private void onClickButtonAnswer(){
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
                    int quantity_correct_word = 0;
                    for(int i=0; i<4; i++){
                        TextView tv_q = list_tv_q.get(i);
                        int x = colors_matrix.get(question).get(i);
                        TextView tv_a = list_tv_a.get(x);
                        if(click_right[x] == i) quantity_correct_word++;
                        tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_dark[i]));
                        tv_q.setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                        tv_q.setTextColor(getColor(R.color.white));
                        tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_dark[i]));
                        tv_a.setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                        tv_a.setTextColor(getColor(R.color.white));
                    }
                    if(quantity_correct_word == 4){
                        point+= 1;
                        showDialogNextQuestion("Câu trả lời của bạn hoàn toàn chính xác. Bạn được cộng 1 điểm!");
                    }
                    else showDialogNextQuestion("Bạn đã trả lời đúng " + quantity_correct_word + "/4 từ!");
                    tv_status.setText("Đáp án");
                    btn_reset.setText("Xem lại");
                    if(question==10) btn_answer.setText("Kết thúc");
                    else btn_answer.setText("Tiếp theo");
                    click_btn[0] = click_btn[1] = 1;
                }
                else {
                    if(question==10) {
                        sendToFinal();
                    }
                    else {
                        click_btn[0] = click_btn[1] = 0;
                        question++;
                        getWordFromDataBase();
                        st.clear();
                        tv_question_num.setText(String.valueOf(question) + "/10");

                        for(int i=0; i<4; i++) {
                            TextView tv_q = list_tv_q.get(i);
                            TextView tv_a = list_tv_a.get(colors_matrix.get(question).get(i));
                            tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_light[i]));
                            tv_q.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                            tv_q.setTextColor(getColor(R.color.black));
                            tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, R.color.white));
                            tv_a.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                            tv_a.setTextColor(getColor(R.color.black));
                            click_left[i] = 0;
                            click_right[i] = -1;
                        }
                        btn_reset.setText("Reset");
                        btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, R.color.red));
                        btn_answer.setText("Đáp án");
                    }
                }
            }
        });
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
        DatabaseReference ref = database.getReference("User/" + uid + "/exam/vocabulary");
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
    // Hàm onClick Button Reset
    private void onClickButtonReset(){
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click_btn[0]==0){
                    for(int i=0; i<4; i++) {
                        TextView tv_q = list_tv_q.get(i);
                        TextView tv_a = list_tv_a.get(colors_matrix.get(question).get(i));
                        tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_light[i]));
                        tv_q.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                        tv_q.setTextColor(getColor(R.color.black));
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
                    tv_status.setText("Câu trả lời của bạn");
                    btn_reset.setText("Đáp án");
                    btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, R.color.green));
                    click_btn[0] = 2;
                }
                else{
                    for(int i=0; i<4; i++){
                        TextView tv_q = list_tv_q.get(i);
                        TextView tv_a = list_tv_a.get(colors_matrix.get(question).get(i));
                        tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_dark[i]));
                        tv_q.setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                        tv_q.setTextColor(getColor(R.color.white));
                        tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_dark[i]));
                        tv_a.setBackgroundTintMode(PorterDuff.Mode.SRC_IN);
                        tv_a.setTextColor(getColor(R.color.white));
                    }
                    tv_status.setText("Đáp án");
                    btn_reset.setText("Xem lại");
                    btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, R.color.red));
                    click_btn[0] = 1;
                }
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
        builder.setMessage("Bài kiểm tra Vocabulary sẽ bao gồm 10 câu, mỗi câu có thể xem đáp án và xem lại câu trả lời của mình. Các câu sẽ lần lượt hiển thị sau khi click vào Tiếp theo và không được quay lại câu trước đó. Chúc bạn hoàn thành tốt bài kiểm tra!");

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
                    click_btn[0] = click_btn[1] = 0;
                    question++;
                    getWordFromDataBase();
                    st.clear();
                    tv_question_num.setText(String.valueOf(question) + "/10");

                    for(int i=0; i<4; i++) {
                        TextView tv_q = list_tv_q.get(i);
                        TextView tv_a = list_tv_a.get(colors_matrix.get(question).get(i));
                        tv_q.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, colors_light[i]));
                        tv_q.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                        tv_q.setTextColor(getColor(R.color.black));
                        tv_a.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, R.color.white));
                        tv_a.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                        tv_a.setTextColor(getColor(R.color.black));
                        click_left[i] = 0;
                        click_right[i] = -1;
                    }
                    btn_reset.setText("Reset");
                    btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamVocabulary.this, R.color.red));
                    btn_answer.setText("Đáp án");
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