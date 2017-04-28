package com.example.bookstorelocaldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    public static void actionStart(Activity activity){
        Intent i=new Intent(activity,RegistrationActivity.class);
        activity.startActivityForResult(i,1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_reg);

        //界面头部初始化
        ActionBar ab=getSupportActionBar();
        if(ab!=null){
            ab.hide();
        }
        TextView title=(TextView)findViewById(R.id.title_tv);
        title.setText("注册");

        //初始化控件
        Button reg=(Button)findViewById(R.id.reg_bt);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText reg_username=(EditText)findViewById(R.id.reg_username);
                EditText reg_password=(EditText)findViewById(R.id.reg_password);
                EditText reg_passcheck=(EditText)findViewById(R.id.reg_passwordcheck);

                String username=reg_username.getText().toString();
                String password=reg_password.getText().toString();
                String passwordcheck=reg_passcheck.getText().toString();

                if (username.equals("")){
                    Toast.makeText(RegistrationActivity.this,"用户名不为空",Toast.LENGTH_SHORT)
                            .show();
                }else{
                    if (password.equals("")){
                        Toast.makeText(RegistrationActivity.this,"密码不为空",Toast.LENGTH_SHORT)
                                .show();
                    }else {
                        if (passwordcheck.equals("")){
                            Toast.makeText(RegistrationActivity.this,"确认密码不为空",Toast.LENGTH_SHORT)
                                    .show();
                        }else {

                            if (password.equals(passwordcheck)){

                                List<Users> list= DataSupport.where("username=?",username).find(Users.class);

                                if(list.size()>0){
                                    Toast.makeText(RegistrationActivity.this,"该用户名已注册",Toast.LENGTH_SHORT)
                                            .show();
                                    reg_username.setText("");
                                }else {

                                    Users user=new Users();
                                    user.setUsername(username);
                                    user.setPassword(password);
                                    user.save();

                                    Intent intent=new Intent();
                                    intent.putExtra("username",username);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                            }else{
                                Toast.makeText(RegistrationActivity.this,"两次密码不匹配",Toast.LENGTH_SHORT)
                                        .show();
                                reg_passcheck.setText("");
                            }

                        }
                    }
                }

            }
        });
    }
}
