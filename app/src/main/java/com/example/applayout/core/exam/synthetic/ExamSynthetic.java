package com.example.applayout.core.exam.synthetic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.applayout.core.main_class.RandomArray;
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
import java.util.Random;

public class ExamSynthetic extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database;
    int question = 1;
    TextView tv_exam, tv_part, tv_exam_name, tv_question_num, tv_question;
    List<TextView> list_tv_ans = new ArrayList<>();
    private Question current_question = new Question();
    Button btn_answer;
    int choice, result, click_answer;
    ImageView imV_back, imV_home, imV_learn, imV_exercise, imV_exam, imV_support, imV_profile;
    //Tạo ma trận ngẫu nhiên các chỉnh hợp chập 4 của 4 đáp án
    RandomArray random_array = new RandomArray(4);
    List<List<Integer>> random_matrix = random_array.generateRandomPermutations();
    int snapshot_size;
    int point = 0;

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
        // Ánh xạ view
        initUi();
        // Get dữ liệu từ Database
        getQuestionFromDatabase();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Khởi tạo giá trị các biến toàn cục
                initVariable();
                // Gán giá trị textview
                setUi();
            }
        }, 1000);
        // Gọi hàm xác nhận thể lệ bài test
        showDialogConfirm();
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
        // Ánh xạ view header
        tv_part = findViewById(R.id.tv_part);
        tv_exam_name = findViewById(R.id.tv_exam_name);
        tv_question_num = findViewById(R.id.tv_question_num);
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

        tv_question = findViewById(R.id.tv_question);
        list_tv_ans.add(findViewById(R.id.tv_a1));
        list_tv_ans.add(findViewById(R.id.tv_a2));
        list_tv_ans.add(findViewById(R.id.tv_a3));
        list_tv_ans.add(findViewById(R.id.tv_a4));

        // Ánh xạ view button
        btn_answer = findViewById(R.id.btn_answer);
    }
    private void getQuestionFromDatabase(){
        database = FirebaseDatabase.getInstance();
//        getSapshotSize();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Random random = new Random();
//                DatabaseReference ref = database.getReference("Exam/Synthetic/" + random.nextInt(8));
//
//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        current_question = snapshot.getValue(Question.class);
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                    }
//                });
//            }
//        }, 2000);
        Random random = new Random();
        DatabaseReference ref = database.getReference("Exam/Synthetic/" + random.nextInt(70));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                current_question = snapshot.getValue(Question.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    // Khởi tạo giá trị các biến
    private void initVariable(){
        click_answer = 0;
        choice = -1;
        result = -1;
    }
    // Gán giá trị cho view
    private void setUi(){
        tv_part.setText("Part A5");
        tv_exam_name.setText("Synthetic");
        tv_question_num.setText(String.valueOf(question) + "/20");
        tv_question.setText(String.valueOf(question) + ". " + current_question.getContext());
        List<String> list_answer = new ArrayList<>();
        list_answer.add(current_question.getAnswer1());
        list_answer.add(current_question.getAnswer2());
        list_answer.add(current_question.getAnswer3());
        list_answer.add(current_question.getAnswer4());
        for(int i=0; i<4; i++){
            int x = random_matrix.get(question).get(i);
            if(current_question.getCorrect_answer().equals(list_answer.get(i))) result = x;
            list_tv_ans.get(x).setText((char)('A' + x) + ". " + list_answer.get(i));
            list_tv_ans.get(x).setBackgroundTintList(ContextCompat.getColorStateList(ExamSynthetic.this, R.color.exam_blue3));
        }
    }
    // Hàm onClickListener
    private void setOnClickListener() throws IllegalAccessException, InstantiationException {
        onClickButtonAnswer();
        for(int i=0; i<4; i++) onClickAnswer(list_tv_ans.get(i), i);

        // Menu dưới màn hình
        onClickImVMenu(imV_back, ExamMain.class.newInstance());
        onClickImVMenu(imV_home, MainActivity.class.newInstance());
        onClickImVMenu(imV_learn, LearnMain.class.newInstance());
        onClickImVMenu(imV_exercise, ExerciseMain.class.newInstance());
        onClickImVMenu(imV_support, SupportMain.class.newInstance());
        onClickImVMenu(imV_profile, Profile.class.newInstance());
    }
    // Hàm onClick từng đáp án
    private void onClickAnswer(TextView tv, int i){
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click_answer == 0 ){
                    choice = i;
                    for(int i=0; i<4; i++){
                        list_tv_ans.get(i).setBackgroundTintList(ContextCompat.getColorStateList(ExamSynthetic.this, R.color.exam_blue3));
                    }
                    tv.setBackgroundTintList(ContextCompat.getColorStateList(ExamSynthetic.this, R.color.exam_yellow_light));
                }
            }
        });
    }
    // Hàm onClick button Đáp án
    private void onClickButtonAnswer(){
        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click_answer == 0){
                    if(choice != -1){
                        if(choice != result){
                            for(int i=0; i<4; i++){
                                TextView tv = list_tv_ans.get(i);
                                if(choice == i)
                                    tv.setBackgroundTintList(ContextCompat.getColorStateList(ExamSynthetic.this, R.color.exam_red_light));
                                if(result == i)
                                    tv.setBackgroundTintList(ContextCompat.getColorStateList(ExamSynthetic.this, R.color.exam_green_light));
                            }
                            showDialogNextQuestion("Câu trả lời của bạn không chính xác!");
                        }
                        else{
                            for(int i=0; i<4; i++){
                                if(result == i){
                                    list_tv_ans.get(i).setBackgroundTintList(ContextCompat.getColorStateList(ExamSynthetic.this, R.color.exam_green_light));
                                    point+= 1;
                                    showDialogNextQuestion("Câu trả lời của bạn hoàn toàn chính xác. Bạn được cộng 1 điểm!");
                                }
                            }
                        }
                        if(question == 20) btn_answer.setText("Kết thúc");
                        else btn_answer.setText("Tiếp theo");
                        click_answer = 1;
                    }
                    else {
                        show_dialog("Bạn chưa chọn câu trả lời!", 2);
                    }
                }
                else {
                    if(question == 20){
                        sendToFinal();
                    }
                    else{
                        setTextNextQuestion();
                    }
                }
            }
        });
    }
    // Hàm set giá trị cho câu hỏi tiếp theo
    private void setTextNextQuestion(){
        // Sang câu hỏi tiếp theo
        question++;
        // Get dữ liệu câu từ Database
        getQuestionFromDatabase();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Khởi tạo giá trị các biến toàn cục
                initVariable();
                // Set giá trị
                setUi();
                // Set lại text cho button
                btn_answer.setText("Đáp án");
            }
        }, 1000);
    }
    // Hàm start Final Activity
    private void sendToFinal(){
        setPoint();
        Result.point = "" + point + "/20";
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
        DatabaseReference ref = database.getReference("User/" + uid + "/exam/synthetic");
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
        builder.setMessage("Bài kiểm tra Synthetic sẽ bao gồm 20 câu với chủ đề ngẫu nhiên, mỗi câu có 1 đáp án đúng và 3 đáp án sai. Các câu sẽ lần lượt hiển thị sau khi click vào Tiếp theo và không được quay lại câu trước đó. Chúc bạn hoàn thành tốt bài kiểm tra!");

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
        if(question == 20) str = "Kết thúc";

        // Nếu người dùng chọn Yes
        builder.setPositiveButton(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Thực hiện hành động khi người dùng chọn Yes
                dialog.dismiss();
                if(question == 20){
                    sendToFinal();
                }
                else{
                    setTextNextQuestion();
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
    private void show_dialog(String s, int time){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Thông báo");
        progressDialog.setMessage(s);
        progressDialog.show();

        // Sử dụng Handler để gửi một tin nhắn hoạt động sau một khoảng thời gian
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ẩn Dialog sau khi đã qua một khoảng thời gian nhất định
                progressDialog.dismiss();
            }
        }, time * 1000); // Số milliseconds bạn muốn Dialog biến mất sau đó
    }
    private void getSapshotSize(){
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Exam/Synthetic");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot_size = (int) snapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}