package com.example.android.datafrominternet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by ucla on 2018/2/11.
 */

public class LogIn extends AppCompatActivity {

    Button logIn,signUp;
    EditText phonenumber,password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar) findViewById(R.id.pgb_login);

        Bmob.initialize(this, "c12ad56b69a6e30cb8cc89b566379d19");

        SharedPreferences preferences = getSharedPreferences("userinfo",MODE_PRIVATE);
        String number = preferences.getString("phonenumber",null);
        String psw = preferences.getString("password",null);
        if (number!=null){
            progressBar.setVisibility(View.VISIBLE);
            BmobUser.loginByAccount(number, psw, new LogInListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    if (e==null){
                        Toast.makeText(LogIn.this, "自动登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogIn.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(LogIn.this, "自动登录失败", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        logIn = (Button) findViewById(R.id.btn_l_log_in);
        signUp = (Button) findViewById(R.id.btn_l_sign_up);
        phonenumber = (EditText) findViewById(R.id.et_l_phonenumber);
        password = (EditText) findViewById(R.id.et_l_password);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                BmobUser.loginByAccount(phonenumber.getText().toString(), password.getText().toString(), new LogInListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if(e==null){
                            Toast.makeText(LogIn.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogIn.this,MainActivity.class);
                            startActivity(intent);
                            SharedPreferences.Editor editor = getSharedPreferences("userinfo",MODE_PRIVATE).edit();
                            editor.putString("password",password.getText().toString());
                            editor.putString("phonenumber",phonenumber.getText().toString());
                            editor.apply();
                            finish();
                        }else{
                            Toast.makeText(LogIn.this, e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("test",e.toString());
                        }
                    }
                });
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this,SignUp.class);
                startActivity(intent);
            }
        });

    }
}
