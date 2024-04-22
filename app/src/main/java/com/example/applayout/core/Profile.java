package com.example.applayout.core;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;
import com.example.applayout.core.exam.ExamMain;
import com.example.applayout.core.exercise.ExerciseMain;
import com.example.applayout.core.learn.LearnMain;
import com.example.applayout.core.main_class.User;
import com.example.applayout.core.main_class.Validate;
import com.example.applayout.core.support.SupportMain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    TextView tv_profile;
    EditText edt_name, edt_email, edt_phone;
    Button btn_update, btn_logout, btn_change_password;
    ImageView imV_home, imV_learn, imV_exercise, imV_exam, imV_support, imV_profile;
    Integer btn_status;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Khai báo auth và user hiện tại
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        // Ánh xạ view
        initUi();
        // Set text
        setTextUi();
        // Gọi các hàm onClick
        try {
            onClickListener();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }

    }

    private void initUi(){
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_phone = findViewById(R.id.edt_phone);
        btn_update = findViewById((R.id.btn_update));
        btn_logout = findViewById(R.id.btn_logout);
        btn_change_password = findViewById(R.id.btn_change_password);
        tv_profile = findViewById(R.id.tv_profile);
        imV_home = findViewById(R.id.imV_home);
        imV_learn = findViewById(R.id.imV_learn);
        imV_exercise = findViewById(R.id.imV_exercise);
        imV_exam = findViewById(R.id.imV_exam);
        imV_support = findViewById(R.id.imV_support);
        imV_profile = findViewById(R.id.imV_profile);
    }
    private void setTextUi(){
        // Hiển thị thông tin Profile
        setInputText("name", edt_name);
        setInputText("phone", edt_phone);
        edt_email.setText(user.getEmail());
        // Đánh dấu activity hiện tại trên thanh menu
        imV_profile.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_profile.setImageResource(R.drawable.icon_profile2);
        tv_profile.setTextAppearance(R.style.menu_text);
        btn_status = 0;
    }
    private void setInputText(String attribute, EditText edt){
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("User/" + user.getUid() + "/" + attribute);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                edt.setText(snapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private User getUpdateInfo(){
        return new User(getInput(edt_name), getInput(edt_email), getInput(edt_phone));
    }
    private String getInput(EditText edt){
        return edt.getText().toString().trim();
    }
    private void onClickListener() throws IllegalAccessException, InstantiationException {
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_status == 0){
                    edt_name.setEnabled(true);
                    //edt_email.setEnabled(true);
                    edt_phone.setEnabled(true);
                    btn_update.setText("Lưu Profile");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            edt_name.requestFocus(); // Yêu cầu EditText edt_name nhận focus
                            edt_name.setSelection(edt_name.getText().length()); // Di chuyển con trỏ nháy đến cuối của edt_name
                        }
                    }, 100);
                    btn_status = 1;
                }
                else{
                    edt_name.setEnabled(false);
                    //edt_email.setEnabled(false);
                    edt_phone.setEnabled(false);
                    btn_update.setText("Sửa Profile");
                    btn_status = 0;
                    onClickUpdateEmail();
                }
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showLogoutDialog(); }
        });
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ChangePassword.class);
                startActivity(intent);
            }
        });
        onClickMenu(imV_home, MainActivity.class.newInstance());
        onClickMenu(imV_learn, LearnMain.class.newInstance());
        onClickMenu(imV_exercise, ExerciseMain.class.newInstance());
        onClickMenu(imV_exam, ExamMain.class.newInstance());
        onClickMenu(imV_support, SupportMain.class.newInstance());
    }
    private void onClickMenu(ImageView imV, Context context){
        imV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), context.getClass());
                startActivity(intent);
            }
        });
    }
    private void onClickUpdateEmail(){
        User user_info = getUpdateInfo();
        Validate validate = new Validate(Profile.this);
        if(!validate.validateProfile(user_info.getName(), user_info.getPhone())) return;
        updateUserInRealtimeDatabase(user_info);
        show_dialog("Sửa thông tin cá nhân thành công!", 2);
//        user.updateEmail(user_info.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    updateUserInRealtimeDatabase(user_info);
//                    show_dialog("Sửa thông tin cá nhân thành công!", 2);
//                    onRestart();
//                } else {
//                    showPasswordInputDialog();
//                }
//            }
//        });
    }
    private void reAuthenticateUser(String password){
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            onClickUpdateEmail();
                        }
                        else{
                            show_dialog("Mật khẩu không đúng. Vui lòng nhập lại!", 2);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showPasswordInputDialog();
                                }
                            }, 2000);
                        }
                    }
                });
    }
    private void updateUserInRealtimeDatabase(User user1){
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref_name = database.getReference("User/" + user.getUid() + "/name");
        DatabaseReference ref_phone = database.getReference("User/" + user.getUid() + "/phone");
        ref_name.setValue(user1.getName());
        ref_phone.setValue(user1.getPhone());
    }
    private void showPasswordInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.profile_dialog_password, null);
        final EditText passwordInput = view.findViewById(R.id.password_input);
        builder.setView(view);
        builder.setTitle("Nhập mật khẩu để xác minh");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Xử lý khi người dùng nhấn Yes
                String password = passwordInput.getText().toString();
                reAuthenticateUser(password);
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


    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bạn có muốn đăng xuất?");
        builder.setMessage("Xác nhận đăng xuất khỏi ứng dụng?");

        // Nếu người dùng chọn Yes
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Thực hiện hành động khi người dùng chọn Yes
                auth.signOut();
                dialog.dismiss();
                show_dialog("Đăng xuất thành công!", 2);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
            }
        });

        // Nếu người dùng chọn Cancel hoặc nhấn back
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
    private void overlay(Toast toast){
        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        View overlay = new View(Profile.this);
        overlay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        overlay.setBackgroundColor(Color.parseColor("#80000000")); // Màu mờ: #80 (50% alpha) + màu đen
        rootView.addView(overlay);
        toast.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                // No-op
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                // Loại bỏ layer mờ khi Toast biến mất
                rootView.removeView(overlay);
            }
        });
        toast.show();
    }
    private void custom_toast(String string){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.profile_custom_toast_layout,
                (ViewGroup) findViewById(R.id.custom_toast));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(string);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        overlay(toast);
        toast.show();
    }
    private void show_dialog(String s, int time){
        ProgressDialog progressDialog = new ProgressDialog(Profile.this);
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
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}