package com.example.applayout.core.exam.listening;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.applayout.R;
import com.example.applayout.core.MainActivity;
import com.example.applayout.core.Profile;
import com.example.applayout.core.exam.ExamMain;
import com.example.applayout.core.exam.ExamPartFinal;
import com.example.applayout.core.exam.Result;
import com.example.applayout.core.exam.grammar.ExamGrammar;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.main_class.RandomArray;
import com.example.applayout.core.support.SupportMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExamListening extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseDatabase database;
    int question = 1;
    TextView tv_exam, tv_part, tv_exam_name, tv_question_num;
    MediaPlayer audio;
    private Question current_question;
    List<LinearLayout> list_layout_ans;
    List<TextView> list_tv_ans;
    Button btn_answer;
    int choice, click_answer;
    ImageView imV_back, imV_home, imV_learn, imV_exercise, imV_exam, imV_support, imV_profile, imV_volume, imV_image;
    int point = 0;
    RandomArray randomArray = new RandomArray(20);
    List<Integer> randomPermutation = randomArray.generateRandomCombination(10);
    private ProgressDialog progressDialog;
    private CountDownTimer countDownTimer;
    private Integer got, cancel;
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

        imV_volume = findViewById(R.id.imV_volume);
        imV_image = findViewById(R.id.imV_image);
        list_layout_ans = new ArrayList<>();
        list_layout_ans.add(findViewById(R.id.layout_a1));
        list_layout_ans.add(findViewById(R.id.layout_a2));
        list_layout_ans.add(findViewById(R.id.layout_a3));
        list_layout_ans.add(findViewById(R.id.layout_a4));
        list_tv_ans = new ArrayList<>();
        list_tv_ans.add(findViewById(R.id.tv_a1));
        list_tv_ans.add(findViewById(R.id.tv_a2));
        list_tv_ans.add(findViewById(R.id.tv_a3));
        list_tv_ans.add(findViewById(R.id.tv_a4));
        // Ánh xạ view button
        btn_answer = findViewById(R.id.btn_answer);
    }
    private void getQuestionFromDatabase(){
        // Gọi hàm đếm ngược sau 1 giây nếu không get được dữ liệu thì hiển thị thông báo
        countDown();

        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Exam/Listening/" + randomPermutation.get(question-1));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Gán giá trị cho câu hỏi
                current_question = snapshot.getValue(Question.class);
                // Khởi tạo giá trị các biến toàn cục
                initVariable();
                // Gán giá trị textview
                setUi();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    // Khởi tạo giá trị các biến
    private void initVariable(){
        choice = click_answer = 0;
    }
    // Gán giá trị cho view
    private void setUi(){
        tv_part.setText("Part A3");
        tv_exam_name.setText("Listening");
        tv_question_num.setText(String.valueOf(question) + "/10");
        imV_volume.setImageDrawable(getResources().getDrawable(R.drawable.icon_audio2));
        audio = new MediaPlayer();
        try {
            audio.setDataSource(current_question.getAudio());
            audio.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        // Sử dụng Glide để tải hình ảnh và đặt vào ImageView
        Glide.with(this)
                .load(current_question.getImage())
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressDialog.dismiss();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        got = 1;
                        countDownTimer.cancel();
                        // Tắt thông báo khi đã lấy được dữ liệu
                        if(progressDialog.isShowing()) progressDialog.dismiss();
                        return false;
                    }
                })
                .into(imV_image);

        for(LinearLayout layout : list_layout_ans) layout.setBackgroundColor(getResources().getColor(R.color.white));
        list_tv_ans.get(0).setText("A");
        list_tv_ans.get(1).setText("B");
        list_tv_ans.get(2).setText("C");
        list_tv_ans.get(3).setText("D");
    }

    // Hàm onClickListener
    private void setOnClickListener() throws IllegalAccessException, InstantiationException {
        onClickVolume();
        for(int i=0; i<4; i++) onClickAnswer(list_tv_ans.get(i), i+1);
        onClickButtonAnswer();

        // Menu dưới màn hình
        onClickImVMenu(imV_back, ExamMain.class.newInstance());
        onClickImVMenu(imV_home, MainActivity.class.newInstance());
        onClickImVMenu(imV_learn, LearnMain.class.newInstance());
        onClickImVMenu(imV_exercise, ExerciseMain.class.newInstance());
        onClickImVMenu(imV_support, SupportMain.class.newInstance());
        onClickImVMenu(imV_profile, Profile.class.newInstance());
    }
    // Hàm click nghe câu hỏi
    private void onClickVolume(){
         imV_volume.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(audio.isPlaying()){
                     imV_volume.setImageDrawable(getResources().getDrawable(R.drawable.icon_audio2));
                     audio.pause();
                 } else {
                     imV_volume.setImageDrawable(getResources().getDrawable(R.drawable.icon_audio));
                     audio.start();
                     audio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                         @Override
                         public void onCompletion(MediaPlayer mp) {
                             imV_volume.setImageDrawable(getResources().getDrawable(R.drawable.icon_audio2));
                         }
                     });

                 }
             }
         });
    }
    // Hàm onClick chọn câu trả lời
    private void onClickAnswer(TextView tv, int i){
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click_answer == 0){
                    choice = i;
                    for(LinearLayout layout : list_layout_ans) layout.setBackgroundColor(getResources().getColor(R.color.white));
                    list_layout_ans.get(i-1).setBackgroundColor(getResources().getColor(R.color.exam_yellow_light));
                }
            }
        });
    }

    // Hàm onClick button Đáp án
    private void onClickButtonAnswer(){
        btn_answer.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(click_answer == 0){
                    // Check đã chọn
                    if(choice == 0){
                        show_dialog("Vui lòng chọn đáp án của bạn!", 2);
                        return;
                    }
                    // Tắt âm thanh hiện tại nếu đang bật
                    audio.stop();
                    // Đổi icon loa thành tắt
                    imV_volume.setImageDrawable(getResources().getDrawable(R.drawable.icon_audio2));
                    // Chuẩn bị audio sẵn sàng phát lại
                    try {
                        audio.prepare();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    };
                    // Hiển thị thông báo kết quả
                    showDialogResult();
                    // Hiển thị dữ liệu câu hỏi
                    setTextAnswer();
                    // Set lại text và trạng thái click cho button
                    if(question==10) btn_answer.setText("Kết thúc");
                    else btn_answer.setText("Tiếp theo");
                    click_answer = 1;
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
    // Hàm hiển thị dữ liệu câu hỏi
    private void setTextAnswer(){
        list_tv_ans.get(0).setText("A. " + current_question.getAnswer1());
        list_tv_ans.get(1).setText("B. " + current_question.getAnswer2());
        list_tv_ans.get(2).setText("C. " + current_question.getAnswer3());
        list_tv_ans.get(3).setText("D. " + current_question.getAnswer4());
        if(choice != current_question.getCorrect_ans())
            list_layout_ans.get(choice-1).setBackgroundColor(getResources().getColor(R.color.exam_red_light));
        list_layout_ans.get(current_question.getCorrect_ans()-1).setBackgroundColor(getResources().getColor(R.color.exam_green_light));
    }
    private void showDialogResult(){
        if(choice == current_question.getCorrect_ans()){
            point+= 1;
            showDialogNextQuestion("Câu trả lời của bạn hoàn toàn chính xác. Bạn được cộng 1 điểm!");
        }
        else showDialogNextQuestion("Câu trả lời của bạn chưa chính xác!");
    }
    // Hàm set giá trị cho câu hỏi tiếp theo
    private void setTextNextQuestion(){
        // Tắt âm thanh hiện tại nếu đang bật
        if(audio.isPlaying()){
            audio.stop();
        }
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
        // Set lại text cho button
        btn_answer.setText("Đáp án");
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
        DatabaseReference ref = database.getReference("User/" + uid + "/exam/listening");
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
        builder.setMessage("Bài kiểm tra Listen sẽ bao gồm 10 câu, mỗi câu sẽ có 1 ảnh cùng với âm thanh mô tả và 4 đáp án để lựa chọn 1 đáp án đúng nhất cho nội dung của ảnh. Các câu sẽ lần lượt hiển thị sau khi click vào Tiếp theo và không được quay lại câu trước đó. Chúc bạn hoàn thành tốt bài kiểm tra!");

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
    // Hàm chờ loading
    private void showDialogLoadingNextQuestion(){
        ProgressDialog progressDialog = new ProgressDialog(ExamListening.this);
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
                    progressDialog = new ProgressDialog(ExamListening.this);
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
        ProgressDialog progressDialog = new ProgressDialog(ExamListening.this);
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

                // Tắt âm thanh hiện tại nếu đang bật
                if(audio.isPlaying()){
                    audio.stop();
                }
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