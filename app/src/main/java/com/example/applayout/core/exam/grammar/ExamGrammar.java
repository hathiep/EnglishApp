package com.example.applayout.core.exam.grammar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ExamGrammar extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database;
    int question = 1;
    TextView tv_exam, tv_part, tv_exam_name, tv_question_num, tv_status;
    List<String> sentence;
    LinearLayout parentLinearLayoutQuestion, parentLinearLayoutAnswer;
    private String question_sentence;
    int[] numbers, words_random;
    private List<String> answers;
    int[] arr;
    Button btn_reset, btn_answer;
    int click_answer, click_reset;
    ImageView imV_back, imV_home, imV_learn, imV_exercise, imV_exam, imV_support, imV_profile;
    int point = 0;
    private ProgressDialog progressDialog;
    private CountDownTimer countDownTimer;
    private Integer got, cancel;

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
        // Get dữ liệu câu từ Database
        getSentenceFromDataBase();
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
        tv_part.setText("Part A2");
        tv_exam_name.setText("Grammar");
        tv_question_num = findViewById(R.id.tv_question_num);
        tv_status = findViewById(R.id.tv_status_gramar);
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

        // Ánh xạ view button
        btn_answer = findViewById(R.id.btn_answer);
        btn_reset = findViewById(R.id.btn_reset);
        // Ánh xạ view khung câu hỏi
        parentLinearLayoutQuestion = findViewById(R.id.parentLinearLayout);
        parentLinearLayoutAnswer = findViewById(R.id.parentLinearLayout1);
    }
    // Hàm lấy dữ liệu word từ RealtimeDatabase
    private void getSentenceFromDataBase(){
        // Gọi hàm đếm ngược sau 1 giây nếu không get được dữ liệu thì hiển thị thông báo
        countDown();

        database = FirebaseDatabase.getInstance();
        Random random = new Random();
        DatabaseReference ref_vocabulary = database.getReference("Exam/Grammar/" + random.nextInt(100));
        ref_vocabulary.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                got = 1;
                countDownTimer.cancel();
                // Tắt thông báo khi đã lấy được dữ liệu
                if(progressDialog.isShowing()) progressDialog.dismiss();
                // Gán giá trị cho câu hỏi
                question_sentence = snapshot.getValue(String.class);
                // Khởi tạo giá trị các biến toàn cục
                initVariable();
                // Hiển thị danh sách các từ
                setText();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    private void initVariable(){
        answers = Arrays.asList(question_sentence.split(" "));
        arr = new int[answers.size()];
        numbers = new int[answers.size()];
        for(int i = 0; i < answers.size(); i++) numbers[i] = i;
        words_random = random(numbers);
        click_answer = click_reset = 0;
    }
    private void setText() {
        tv_question_num.setText(String.valueOf(question) + "/10");
        sentence = new ArrayList<>();
        int quantity_word = answers.size();;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        int marginHorizontal = (int) getResources().getDimensionPixelSize(R.dimen.margin_horizontal);
        int marginVertical = (int) getResources().getDimensionPixelSize(R.dimen.margin_vertical);
        layoutParams.setMargins(0, marginVertical, 0, marginVertical);
        int totalMargin = marginHorizontal * (quantity_word - 1); // Tổng margin giữa các TextView

        LinearLayout currentLinearLayout = new LinearLayout(this);
        currentLinearLayout.setLayoutParams(layoutParams);
        currentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        int parentWidth = parentLinearLayoutQuestion.getWidth(); // Lấy chiều rộng ước lượng của LinearLayout cha

        int currentLineWidth = 0; // Biến này để lưu tổng ước lượng chiều rộng của dòng hiện tại

        for (int i = 0; i < quantity_word; i++) {
            TextView textView = new TextView(this);
            String idName = "tv_q" + (i + 1);
            int id = getResources().getIdentifier(idName, "id", getPackageName());
            textView.setId(id);
            textView.setText(answers.get(words_random[i]));
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textViewParams.setMargins(marginHorizontal, 0, marginHorizontal, 0);
            textView.setLayoutParams(textViewParams);
            textView.setPadding(
                    (int) getResources().getDimensionPixelSize(R.dimen.padding_horizontal),
                    (int) getResources().getDimensionPixelSize(R.dimen.padding_vertical),
                    (int) getResources().getDimensionPixelSize(R.dimen.padding_horizontal),
                    (int) getResources().getDimensionPixelSize(R.dimen.padding_vertical)
            );
            textView.setGravity(Gravity.CENTER);
            textView.setBackground(ContextCompat.getDrawable(this, R.drawable.border_white));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            textView.setTextColor(Color.BLACK);

            onClickWord(textView, i);

            // Thêm TextView vào LinearLayout hiện tại
            currentLinearLayout.addView(textView);

            // Cập nhật tổng ước lượng chiều rộng của dòng hiện tại
            currentLinearLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            currentLineWidth = currentLinearLayout.getMeasuredWidth();

            if (currentLineWidth + totalMargin - 10 > parentWidth) {
                // Nếu tổng ước lượng chiều rộng của dòng hiện tại vượt quá chiều rộng của LinearLayout cha, tạo một dòng mới
                parentLinearLayoutQuestion.addView(currentLinearLayout);

                // Tạo LinearLayout mới cho dòng tiếp theo
                currentLinearLayout = new LinearLayout(this);
                currentLinearLayout.setLayoutParams(layoutParams);
                currentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                if (textView.getParent() != null) {
                    ((ViewGroup) textView.getParent()).removeView(textView);
                }
                currentLinearLayout.addView(textView);

            }
        }

        // Cuối cùng, thêm LinearLayout cuối cùng vào LinearLayout cha
        parentLinearLayoutQuestion.addView(currentLinearLayout);
    }
    private void setTextAnswer(List<String> list_word, List<Integer> list_index_correct,  List<Integer> list_index_wrong, int status){
        parentLinearLayoutAnswer.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        int marginHorizontal = (int) getResources().getDimensionPixelSize(R.dimen.margin_horizontal);
        int marginVertical = (int) getResources().getDimensionPixelSize(R.dimen.margin_vertical);
        layoutParams.setMargins(0, marginVertical, 0, marginVertical);
        int totalMargin = marginHorizontal * (list_word.size() - 1); // Tổng margin giữa các TextView

        LinearLayout currentLinearLayout = new LinearLayout(this);
        currentLinearLayout.setLayoutParams(layoutParams);
        currentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        int parentWidth = parentLinearLayoutAnswer.getWidth(); // Lấy chiều rộng ước lượng của LinearLayout cha

        int currentLineWidth = 0; // Biến này để lưu tổng ước lượng chiều rộng của dòng hiện tại

        for (int i = 0; i < list_word.size(); i++) {
            TextView textView = new TextView(this);
            String idName = "tv_a" + (i + 1);
            int id = getResources().getIdentifier(idName, "id", getPackageName());
            textView.setId(id);
            textView.setText(list_word.get(i));

            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textViewParams.setMargins(marginHorizontal, 0, marginHorizontal, 0);
            textView.setLayoutParams(textViewParams);
            textView.setPadding(
                    (int) getResources().getDimensionPixelSize(R.dimen.padding_horizontal),
                    (int) getResources().getDimensionPixelSize(R.dimen.padding_vertical),
                    (int) getResources().getDimensionPixelSize(R.dimen.padding_horizontal),
                    (int) getResources().getDimensionPixelSize(R.dimen.padding_vertical)
            );
            textView.setGravity(Gravity.CENTER);
            int border = R.drawable.border_yellow;
            if(status == 1) border = R.drawable.border_green;
            if(list_index_correct.contains(i)) border = R.drawable.border_green;
            if(list_index_wrong.contains(i)) border = R.drawable.border_red;
            textView.setBackground(ContextCompat.getDrawable(this, border));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            textView.setTextColor(Color.BLACK);

            // Thêm TextView vào LinearLayout hiện tại
            currentLinearLayout.addView(textView);

            // Cập nhật tổng ước lượng chiều rộng của dòng hiện tại
            currentLinearLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            currentLineWidth = currentLinearLayout.getMeasuredWidth();

            if (currentLineWidth + totalMargin > parentWidth) {
                // Nếu tổng ước lượng chiều rộng của dòng hiện tại vượt quá chiều rộng của LinearLayout cha, tạo một dòng mới
                parentLinearLayoutAnswer.addView(currentLinearLayout);

                // Tạo LinearLayout mới cho dòng tiếp theo
                currentLinearLayout = new LinearLayout(this);
                currentLinearLayout.setLayoutParams(layoutParams);
                currentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                if (textView.getParent() != null) {
                    ((ViewGroup) textView.getParent()).removeView(textView);
                }
                currentLinearLayout.addView(textView);

            }
        }

        // Cuối cùng, thêm LinearLayout cuối cùng vào LinearLayout cha
        parentLinearLayoutAnswer.addView(currentLinearLayout);
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
    private void onClickWord(TextView tv_x, int i){
        int k = i;
        tv_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = tv_x.getText().toString();
                if(arr[k] == 0){
                    sentence.add(word);
                    tv_x.setBackgroundResource(R.drawable.border_yellow);
                    setTextAnswer(sentence, new ArrayList<>(), new ArrayList<>(), 0);
                    arr[k] = 1;
                }
                else if(arr[k] == 1){
                    tv_x.setBackgroundResource(R.drawable.border_white);
                    for(int j=sentence.size()-1; j>=0; j--){
                        if(sentence.get(j).equals(word)){
                            sentence.remove(j);
                            break;
                        }
                    }
                    setTextAnswer(sentence, new ArrayList<>(), new ArrayList<>(), 0);
                    arr[k] = 0;
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
                    for(int i=0; i<arr.length; i++){
                        if(arr[i] == 0){
                            show_dialog("Bạn chưa hoàn thành câu. Vui lòng chọn tất cả các từ!", 2);
                            return;
                        }
                    }
                    setTextAnswer(answers, new ArrayList<>(), new ArrayList<>(), 1);
                    List<Integer> list_index_correct = new ArrayList<>();
                    List<Integer> list_index_wrong = new ArrayList<>();
                    for(int i=0; i<sentence.size(); i++){
                        if(sentence.get(i).equals(answers.get(i))) list_index_correct.add(i);
                        else list_index_wrong.add(i);
                    }
                    if(list_index_wrong.size() == 0){
                        point+= 1;
                        showDialogNextQuestion("Câu trả lời của bạn hoàn toàn chính xác. Bạn được cộng 1 điểm!");
                    }
                    else showDialogNextQuestion("Bạn đã sắp xếp " + list_index_correct.size() + "/" + answers.size() + " từ đúng vị trí!");
                    // Set lại text và trạng thái click cho button
                    btn_reset.setText("Xem lại");
                    if(question==10) btn_answer.setText("Kết thúc");
                    else btn_answer.setText("Tiếp theo");
                    click_answer = click_reset = 1;
                }
                else {
                    if (question == 10) {
                        sendToFinal();
                    } else {
                        setTextNextQuestion();
                    }
                }
            }
        });
    }
    // Hàm onClick button Reset
    private void onClickButtonReset(){
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click_reset==0){
                    // Set lại danh sách câu trả lời trống
                    setTextAnswer(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0);
                    // Set lại danh sách từ
                    setText();
                }
                else if(click_reset==1){
                    // Đánh dấu các từ đúng và sai vị trí
                    List<Integer> list_index_correct = new ArrayList<>();
                    List<Integer> list_index_wrong = new ArrayList<>();
                    for(int i=0; i<sentence.size(); i++){
                        if(sentence.get(i).equals(answers.get(i))) list_index_correct.add(i);
                        else list_index_wrong.add(i);
                    }
                    // Set lại màu cho các từ đúng và sai
                    setTextAnswer(sentence, list_index_correct, list_index_wrong, 0);
                    // Set lại text cho status và button
                    tv_status.setText("Câu trả lời của bạn");
                    btn_reset.setText("Đáp án");
                    btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamGrammar.this, R.color.green));
                    click_reset = 2;
                }
                else{
                    // Set lại màu danh sách câu trả lời đúng
                    setTextAnswer(answers, new ArrayList<>(), new ArrayList<>(), 1);
                    // Set lại text cho status và button
                    tv_status.setText("Đáp án");
                    btn_reset.setText("Xem lại");
                    btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamGrammar.this, R.color.red));
                    click_reset = 1;
                }
            }
        });
    }
    // Hàm set giá trị cho câu hỏi tiếp theo
    private void setTextNextQuestion(){
        // Sang câu hỏi tiếp theo
        question++;
        // Hiển thị số thứ tự câu hỏi
        tv_question_num.setText(String.valueOf(question) + "/10");
        // Set lại text status
        tv_status.setText("Click vào từ ở danh sách bên dưới để sắp xếp");
        // Set ô câu trả lời rỗng
        setTextAnswer(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0);
        // Xoá dữ liệu câu hỏi cũ
        parentLinearLayoutQuestion.removeAllViews();
        parentLinearLayoutAnswer.removeAllViews();
        // Gọi hàm chờ loading
        showDialogLoadingNextQuestion();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Get dữ liệu câu từ Database
                getSentenceFromDataBase();
            }
        }, 1000); // Số milliseconds bạn muốn Dialog biến mất sau đó
        // Set lại text và trạng thái click cho button
        btn_reset.setText("Reset");
        btn_reset.setBackgroundTintList(ContextCompat.getColorStateList(ExamGrammar.this, R.color.red));
        btn_answer.setText("Đáp án");
        // Xoá câu trả lời của câu hỏi trước
        sentence.clear();
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
        DatabaseReference ref = database.getReference("User/" + uid + "/exam/grammar");
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
        builder.setMessage("Bài kiểm tra Grammar sẽ bao gồm 10 câu, mỗi câu có thể xem đáp án và xem lại câu trả lời của mình. Các câu sẽ lần lượt hiển thị sau khi click vào Tiếp theo và không được quay lại câu trước đó. Chúc bạn hoàn thành tốt bài kiểm tra!");

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
        ProgressDialog progressDialog = new ProgressDialog(ExamGrammar.this);
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
                    progressDialog = new ProgressDialog(ExamGrammar.this);
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
        ProgressDialog progressDialog = new ProgressDialog(ExamGrammar.this);
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
