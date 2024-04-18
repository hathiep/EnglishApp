package com.example.applayout.core;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Validate {
    private Context context;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{10}$");

    public Validate() {
    }

    public Validate(Context context) {
        this.context = context;
    }
    // Hàm kiểm tra xâu rỗng
    public boolean validateEmpty(String x){
        return TextUtils.isEmpty(x);
    }
    // Hàm thông báo validate xâu rỗng 2 tham số
    public boolean checkValidateEmpty(String a, String b){
        if(validateEmpty(a) || validateEmpty(b)){
            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // Hàm thông báo validate xâu rỗng 3 tham số
    public boolean checkValidateEmpty(String a, String b, String c){
        if(validateEmpty(a) || validateEmpty(b)  || validateEmpty(c)){
            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // Hàm thông báo validate xâu rỗng 4 tham số
    public boolean checkValidateEmpty(String a, String b, String c, String d){
        if(validateEmpty(a) || validateEmpty(b)  || validateEmpty(c)  || validateEmpty(d)){
            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // Hàm thông báo validate xâu rỗng 5 tham số
    public boolean checkValidateEmpty(String a, String b, String c, String d, String e){
        if(validateEmpty(a) || validateEmpty(b)  || validateEmpty(c)  || validateEmpty(d)  || validateEmpty(e) ){
            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // Hàm validate email đúng định dạng
    public boolean validateEmail(String email) {
        // Kiểm tra định dạng chính xác của email
        return EMAIL_PATTERN.matcher(email).matches();
    }
    // Hàm thông báo validate email
    public boolean checkValidateEmail(String email){
        if(!validateEmail(email)){
            Toast.makeText(context, "Email không đúng. Vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // Hàm validate số điện thoại đúng định dạng
    public boolean validatePhone(String phone){
        String sanitizedPhoneNumber = phone.replaceAll("\\s|-", "");

        // Kiểm tra độ dài phù hợp của số điện thoại
        if (sanitizedPhoneNumber.length() != 10) {
            return false;
        }

        // Kiểm tra định dạng chính xác của số điện thoại
        return PHONE_NUMBER_PATTERN.matcher(sanitizedPhoneNumber).matches();
    }
    // Hàm thông báo validate phone
    public boolean checkValidatePhone(String phone){
        if(!validatePhone(phone)){
            Toast.makeText(context, "Số điện thoại không đúng. Vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // Hàm validate mật khẩu đúng định dạng độ dài từ 8 đến 20, bao gồm cả chữ cái viết hoa, chữ cái viết thường và số
    public boolean validatePassword(String password) {
        // Kiểm tra độ dài phù hợp của mật khẩu
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }

        // Kiểm tra sự tồn tại của ít nhất một chữ cái viết thường, một chữ cái viết hoa và số
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;

        for (char ch : password.toCharArray()) {
            if (Character.isLowerCase(ch)) {
                hasLowerCase = true;
            } else if (Character.isUpperCase(ch)) {
                hasUpperCase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            }
        }

        return hasLowerCase && hasUpperCase && hasDigit;
    }
    // Hàm thông báo validate phone
    public boolean checkValidatePassword(String password){
        if(!validatePassword(password)){
            Toast.makeText(context, "Mật khẩu dài từ 8 đến 20 kí tự, bao gồm cả chữ cái viết hoa, chữ cái viết thường và số. Vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    // Hàm validate mật khẩu trùng khớp
    public boolean validatePasswordEqual(String password, String passwordequal){
        return password.equals(passwordequal);
    }
    // Hàm thông báo validate phone
    public boolean checkValidatePasswordEqual(String password, String passwordequal){
        if(!validatePasswordEqual(password, passwordequal)){
            Toast.makeText(context, "Mật khẩu không trùng khớp. Vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public boolean validateLogin(String email, String password){
        if(!checkValidateEmpty(email, password) || !checkValidateEmail(email) || !checkValidatePassword(password)) return false;
        return true;
    }
    public boolean validateRegister(String name, String email, String phone, String password, String passwordagain){
        if(!checkValidateEmpty(name, email, phone, password, passwordagain)
                || !checkValidateEmail(email) || !checkValidatePhone(phone)
                || !checkValidatePassword(password) || !checkValidatePasswordEqual(password, passwordagain)) return false;
        return true;
    }
}
