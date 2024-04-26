package com.example.applayout.core.exam.writing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.applayout.core.exam.grammar.ExamGrammar;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.support.SupportMain;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExamWriting extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database;
    int question = 1;
    TextView tv_exam, tv_part, tv_exam_name, tv_question_num, tv_question, tv_status;
    private Question current_question = new Question();
    private LinearLayout layout_input;
    List<TextInputEditText> list_edt;
    List<String> list_user_input;
    List<Integer> list_index_correct, list_index_wrong;
    Button btn_reset, btn_answer;
    int click_reset, click_answer;
    ImageView imV_back, imV_home, imV_learn, imV_exercise, imV_exam, imV_support, imV_profile;
    int point = 0;
    private ProgressDialog progressDialog;
    private CountDownTimer countDownTimer;
    private Integer got, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.exam_writing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        initUi();
        // Get dữ liệu từ Database
        getQuestionFromDatabase();
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
        imV_exam.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_exam.setImageResource(R.drawable.icon_exam2);
        tv_exam = findViewById(R.id.tv_exam);
        tv_exam.setTextAppearance(R.style.menu_text);

        tv_question = findViewById(R.id.tv_question);
        layout_input = findViewById(R.id.layout_input);
        // Ánh xạ view button
        btn_answer = findViewById(R.id.btn_answer);
        btn_reset = findViewById(R.id.btn_reset);
    }
    private void getQuestionFromDatabase(){
        // Gọi hàm đếm ngược sau 1 giây nếu không get được dữ liệu thì hiển thị thông báo
        countDown();

        database = FirebaseDatabase.getInstance();
        Random random = new Random();
        DatabaseReference ref = database.getReference("Exam/Writing/" + random.nextInt(20));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                got = 1;
                countDownTimer.cancel();
                // Tắt thông báo khi đã lấy được dữ liệu
                if(progressDialog.isShowing()) progressDialog.dismiss();
                // Gán giá trị cho câu hỏi
                current_question = snapshot.getValue(Question.class);
                // Khởi tạo giá trị các biến toàn cục
                initVariable();
                // Gán giá trị textview
                setUi();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                progressDialog.dismiss();
            }
        });
    }
    // Khởi tạo giá trị các biến
    private void initVariable(){
        click_reset = click_answer = 0;
        list_edt = new ArrayList<>();
        layout_input.removeAllViews();
    }
    // Gán giá trị cho view
    private void setUi(){
        tv_part.setText("Part A4");
        tv_exam_name.setText("Writing");
        tv_question_num.setText(String.valueOf(question) + "/10");
        tv_status.setText("Đoạn văn có " + getQuantitySpace() + " khoảng trống cần điền");
        tv_question.setText(current_question.getContext());
        int size = getQuantitySpace();
        for(int i=0; i<size; i++){
            addInput(i+1);
        }
    }
    private int getQuantitySpace(){
        // Biểu thức chính quy để tìm các chuỗi "(x)....."
        Pattern pattern = Pattern.compile("\\(\\d\\)_____");
        Matcher matcher = pattern.matcher(current_question.getContext());
        int quantity = 0;
        while (matcher.find()) {
            quantity++;
        }
        return quantity;
    }
    // Phương thức thêm input mới vào layout
    private void addInput(int index) {
        Context context = this;
        // Tạo mới TextInputLayout
        TextInputLayout textInputLayout = new TextInputLayout(context);

        TextInputLayout.LayoutParams textInputLayoutParams = new TextInputLayout.LayoutParams(
                TextInputLayout.LayoutParams.MATCH_PARENT,
                TextInputLayout.LayoutParams.WRAP_CONTENT
        );
        textInputLayoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.margin_vertical), 0, getResources().getDimensionPixelSize(R.dimen.margin_vertical)); // Thiết lập margins
        textInputLayout.setLayoutParams(textInputLayoutParams);
        textInputLayout.setHintEnabled(true); // Cho phép hiển thị hint
        textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE); // Thiết lập viền cho TextInputLayout
        textInputLayout.setBoxStrokeColor(getColor(R.color.purple_500));
        textInputLayout.setBoxStrokeWidth(2);

        // Tạo mới TextInputEditText
        TextInputEditText textInputEditText = new TextInputEditText(context);
        textInputEditText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                150
        ));
        // Đặt id cho TextInputLayout
        String inputId = "edt_" + index;
        int id = getResources().getIdentifier(inputId, "id", getPackageName());
        textInputEditText.setId(id);
        textInputEditText.setHint("(" + index + ")");
        textInputEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        textInputEditText.setBackground(null);
        textInputEditText.setPadding(30, 20, 30, 20);
        if(index == 1){
            textInputEditText.requestFocus();
            textInputEditText.setSelection(0);
        }
        list_edt.add(textInputEditText);

        // Thêm TextInputEditText vào TextInputLayout
        textInputLayout.addView(textInputEditText);

        // Thêm TextInputLayout vào LinearLayout chính
        layout_input.addView(textInputLayout);
    }

    // Hàm onClickListener
    private void setOnClickListener() throws IllegalAccessException, InstantiationException {
        onClickButtonAnswer();
        onClickButtonReset();

        // Menu dưới màn hình
        onClickImVMenu(imV_back, ExamMain.class.newInstance());
        onClickImVMenu(imV_home, MainActivity.class.newInstance());
        onClickImVMenu(imV_learn, LearnMain.class.newInstance());
        onClickImVMenu(imV_exercise, ExerciseMain.class.newInstance());
        onClickImVMenu(imV_support, SupportMain.class.newInstance());
        onClickImVMenu(imV_profile, Profile.class.newInstance());
    }

    // Hàm onClick button Đáp án
    private void onClickButtonAnswer(){
        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click_answer == 0){
                    // Gọi hàm lấy dữ liệu từ input
                    getInputText();
                    // Check nhập đầy đủ
                    if(!checkFilled()) return;
                    // Set unable cho input
                    setInputUnable();
                    // Check và set danh sách các input đúng và sai
                    checkCorrect();
                    // Hiển thị thông báo kết quả
                    showDialogResult();
                    List<Integer> list = IntStream.range(0, current_question.getAnswer().size()).boxed().collect(Collectors.toList());
                    showAnswer(current_question.getAnswer(), list, new ArrayList<>());
                    // Set lại text và trạng thái click cho button
                    btn_reset.setText("Xem lại");
                    if(question==10) btn_answer.setText("Kết thúc");
                    else btn_answer.setText("Tiếp theo");
                    click_reset = click_answer = 1;
                }
                else {
                    if(question == 10){
                        sendToFinal();
                    }
                    else{
                        setTextNextQuestion();
                    }
                }
            }
        });
    }
    private void onClickButtonReset(){
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click_reset==0){
                    for(TextInputEditText edt : list_edt) edt.setText("");
                    list_edt.get(0).requestFocus();
                    list_edt.get(0).setSelection(0);
                    tv_question.setText(current_question.getContext());
                }
                else if(click_reset==1){
                    showAnswer(list_user_input, list_index_correct, list_index_wrong);
                    // Set lại text cho status và button
                    tv_status.setText("Câu trả lời của bạn");
                    btn_reset.setText("Đáp án");
                    btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamWriting.this, R.color.green));
                    click_reset = 2;
                }
                else{
                    List<Integer> list = IntStream.range(0, current_question.getAnswer().size()).boxed().collect(Collectors.toList());
                    showAnswer(current_question.getAnswer(), list, new ArrayList<>());
                    // Set lại text cho status và button
                    tv_status.setText("Đáp án");
                    btn_reset.setText("Xem lại");
                    btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamWriting.this, R.color.red));
                    click_reset = 1;
                }
            }
        });
    }
    // Hàm lấy dữ liệu user nhập
    private void getInputText(){
        list_user_input = new ArrayList<>();
        for(int i=0; i<list_edt.size(); i++){
            list_user_input.add(list_edt.get(i).getText().toString().trim());
        }
    }
    // Hàm check điền đầy đủ thông tin
    private boolean checkFilled(){
        for(int i=0; i<list_user_input.size(); i++){
            if(list_user_input.get(i).trim().equals(null) || list_user_input.get(i).trim().equals("")){
                list_edt.get(i).requestFocus();
                list_edt.get(i).setSelection(0);
                show_dialog("Bạn chưa hoàn thành câu. Vui lòng điền đầy đủ các khoảng trống!", 2);
                return false;
            }
        }
        return true;
    }
    // Hàm vô hiệu hoá input
    private void setInputUnable(){
        for(int i=0; i<list_edt.size(); i++){
            list_edt.get(i).setEnabled(false);
        }
    }
    // Hàm hiển thị từ lên đoạn văn
    private void showAnswer(List<String> list_word, List<Integer> list_index_correct, List<Integer> list_index_wrong) {
        Pattern pattern = Pattern.compile("\\((\\d)\\)_____");
        Matcher matcher = pattern.matcher(current_question.getContext());
        SpannableStringBuilder string = new SpannableStringBuilder(current_question.getContext());
        int index = 0;
        while (matcher.find()) {
            String replacement = "(" + (index + 1) + ")" + list_word.get(index);
            int start = matcher.start();
            int end = matcher.end();
            int color = R.color.yellow;
            if (list_index_correct.contains(index)) color = R.color.green;
            if (list_index_wrong.contains(index)) color = R.color.red;
            string.replace(start, end, replacement);
            string.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ExamWriting.this, color)), start, start + replacement.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Cập nhật vị trí start và end cho lần tiếp theo
            matcher = pattern.matcher(string);
            index++;
        }
        tv_question.setText(string);
    }

    // Hàm so sánh hai kết quả
    private void checkCorrect(){
        list_index_correct = new ArrayList<>();
        list_index_wrong = new ArrayList<>();
        for(int i=0; i<list_user_input.size(); i++){
            if(list_user_input.get(i).equals(current_question.getAnswer().get(i))) list_index_correct.add(i);
            else list_index_wrong.add(i);
        }
    }
    private void showDialogResult(){
        if(list_index_correct.size() == list_user_input.size()){
            point+= 1;
            showDialogNextQuestion("Câu trả lời của bạn hoàn toàn chính xác. Bạn được cộng 1 điểm!");
        }
        else showDialogNextQuestion("Bạn đã trả lời đúng " + list_index_correct.size() + "/" + list_user_input.size() + " khoảng trống!");
    }
    // Hàm set giá trị cho câu hỏi tiếp theo
    private void setTextNextQuestion(){
        // Sang câu hỏi tiếp theo
        question++;
        // Gọi hàm chờ loading
        showDialogLoadingNextQuestion();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Get dữ liệu câu từ Database
                getQuestionFromDatabase();
            }
        }, 1000); // Số milliseconds bạn muốn Dialog biến mất sau đó

        btn_answer.setText("Đáp án");
        btn_reset.setText("Reset");
        btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamWriting.this, R.color.red));
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
        DatabaseReference ref = database.getReference("User/" + uid + "/exam/writing");
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
        builder.setMessage("Bài kiểm tra Writing sẽ bao gồm 10 đoạn văn có các từ/cum từ/câu còn thiếu với chủ đề ngẫu nhiên, điền đầy đủ và đúng các chỗ trống được cộng 1 điểm. Các câu sẽ lần lượt hiển thị sau khi click vào Tiếp theo và không được quay lại câu trước đó. Chúc bạn hoàn thành tốt bài kiểm tra!");

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
    // Hàm chờ loading
    private void showDialogLoadingNextQuestion(){
        ProgressDialog progressDialog = new ProgressDialog(ExamWriting.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        // Sử dụng Handler để gửi một tin nhắn hoạt động sau một khoảng thời gian
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ẩn Dialog sau khi đã qua một khoảng thời gian nhất định
                progressDialog.dismiss();
            }
        }, 1000); // Số milliseconds bạn muốn Dialog biến mất sau đó
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
                if(cancel == 1){
                    progressDialog = new ProgressDialog(ExamWriting.this);
                    showDialogLoading();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void countDown(){
        progressDialog = new ProgressDialog(this);
        got = 0; cancel = 0;
        countDownTimer = new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                if(got == 0){
                    // Hiển thị thông báo sau 1 giây nếu chưa get được dữ liệu
                    showDialogLoading();
                }
            }
        }.start();
    }
    public void showDialogLoading(){
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Đường truyền không ổn định. Vui lòng chờ trong giây lát!");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi người dùng nhấn cancel
                if (countDownTimer != null) {
                    countDownTimer.cancel(); // Hủy bộ đếm ngược nếu đang chạy
                }
                try {
                    cancel = 1;
                    showDialogOutExam(ExamMain.class.newInstance());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        progressDialog.show();
    }
    private void show_dialog(String s, int time){
        ProgressDialog progressDialog = new ProgressDialog(ExamWriting.this);
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
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn chưa hoàn thành bài kiểm tra. Bài làm sẽ bị huỷ nếu bạn chuyển sang chức năng khác. Bạn có chắc chắn muốn tiếp tục?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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