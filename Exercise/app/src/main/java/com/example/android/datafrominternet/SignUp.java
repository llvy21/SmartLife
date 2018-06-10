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
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by ucla on 2018/2/10.
 */

public class SignUp extends AppCompatActivity {

    private EditText username,password,phonenumber,code;

    private Button getcode,signup;
    ImageView title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"c12ad56b69a6e30cb8cc89b566379d19");
        setContentView(R.layout.activity_signup);

        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        phonenumber = (EditText) findViewById(R.id.et_phonenumber);
        code = (EditText) findViewById(R.id.et_code);

        getcode = (Button) findViewById(R.id.btn_get_code);
        signup = (Button) findViewById(R.id.btn_signup);
        title= (ImageView) findViewById(R.id.title_signup);

        Bmob.initialize(this, "c12ad56b69a6e30cb8cc89b566379d19");

        getcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                BmobSMS.requestSMSCode(phonenumber.getText().toString(),"default", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, cn.bmob.v3.exception.BmobException e) {
                        if (e==null){
                            Toast.makeText(SignUp.this,"验证码已发送，请稍等片刻",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SignUp.this,"老板穷到交不起短信费了，爱心支付宝账号：13149522166",Toast.LENGTH_LONG).show();
                            Log.d("test",e.toString()+" ");
                        }
                    }
                });
            }


        });




        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BmobSMS.verifySmsCode(phonenumber.getText().toString(),code.getText().toString(), new UpdateListener() {
                    @Override
                    public void done(cn.bmob.v3.exception.BmobException e) {
                        if (e==null){
                            BmobUser user = new BmobUser();
                            user.setUsername(username.getText().toString());
                            user.setPassword(password.getText().toString());
                            user.setMobilePhoneNumber(phonenumber.getText().toString());
                            user.setMobilePhoneNumberVerified(true);
                            user.signUp(new SaveListener<BmobUser>() {
                                @Override
                                public void done(BmobUser bmobUser, cn.bmob.v3.exception.BmobException e) {
                                    if (e==null) {
                                        Toast.makeText(SignUp.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor = getSharedPreferences("userinfo",MODE_PRIVATE).edit();
                                        editor.putString("username",username.getText().toString());
                                        editor.putString("password",password.getText().toString());
                                        editor.putString("phonenumber",phonenumber.getText().toString());
                                        editor.apply();
                                        Intent intent = new Intent(SignUp.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if (e.getErrorCode()==202){
                                        Toast.makeText(SignUp.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (e.getErrorCode()==209){
                                        Toast.makeText(SignUp.this, "手机号已注册", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Log.d("test",e.toString());
                                        Toast.makeText(SignUp.this, "注册失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        else {
                            Log.d("test",e.toString()+" ,code:"+code.getText().toString()+"PHONe:"+phonenumber.getText().toString());
                            Toast.makeText(SignUp.this,"验证码有误，请重试",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }



}
