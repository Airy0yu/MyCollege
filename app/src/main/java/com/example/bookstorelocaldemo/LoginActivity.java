package com.example.bookstorelocaldemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static void actionStart(Context context){
        Intent i=new Intent(context,LoginActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        //初始化节目头部
        ActionBar ab=getSupportActionBar();
        if(ab!=null){
            ab.hide();
        }
        TextView title=(TextView)findViewById(R.id.title_tv);
        title.setText("登陆");

        //初始化数据库
        InitData();

        //初始化控件
        TextView login_reg=(TextView)findViewById(R.id.login_reg);
        Button login_bt=(Button)findViewById(R.id.login_button);

        //登陆按钮点击事件
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText login_username=(EditText)findViewById(R.id.login_username);
                EditText login_password=(EditText)findViewById(R.id.login_password);
                CheckBox login_aotologin=(CheckBox)findViewById(R.id.login_aotologin);

                String username=login_username.getText().toString();
                String password=login_password.getText().toString();

                //对可能出现的情况进行判断
                if (username.equals("")){
                    Toast.makeText(LoginActivity.this,"用户名不为空",Toast.LENGTH_SHORT)
                            .show();
                }else{
                    if (password.equals("")){
                        Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT)
                                .show();
                    }else{

                        //从数据库获得用户的密码信息
                        List<Users> list= DataSupport.select("password").where("username=?",username).find(Users.class);

                        if (list.size()<=0){
                            Toast.makeText(LoginActivity.this,"用户名不存在",Toast.LENGTH_SHORT)
                                    .show();
                        }else{
                            Users user=list.get(0);
                            final String sqlpassword=user.getPassword();

                            if (password.equals(sqlpassword)){

                                //判断是否自动登陆
                                editor=getSharedPreferences("AutoLogin",MODE_PRIVATE).edit();
                                if(login_aotologin.isChecked()){
                                    editor.putBoolean("auto",true);
                                }else {
                                    editor.putBoolean("auto",false);
                                }
                                editor.putString("username",username);
                                editor.apply();

                                //跳转到MainActivity
                                MainActivity.actionStart(LoginActivity.this);
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT)
                                        .show();
                                login_password.setText("");
                            }
                        }
                    }
                }
            }
        });

        //注册字点击事件
        login_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationActivity.actionStart(LoginActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("LoginAcitivity","comehere");
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    EditText et1=(EditText)findViewById(R.id.login_username);
                    EditText et2=(EditText)findViewById(R.id.login_password);

                    et1.setText(data.getStringExtra("username"));
                }
                break;
            default:
        }
    }

    //初始化程序
    private void InitData(){

        //判断是否为第一次登陆，是则初始化数据库，不是则进行界面更新
        preferences=getSharedPreferences("LoginFirst",MODE_PRIVATE);
        if(!preferences.getBoolean("first",false)){
            LitePal.getDatabase();
            editor=getSharedPreferences("LoginFirst",MODE_PRIVATE).edit();
            editor.putBoolean("first",true);
            editor.apply();
        }else{
            preferences=getSharedPreferences("AutoLogin",MODE_PRIVATE);
            if(preferences.getBoolean("auto",false)){
                MainActivity.actionStart(LoginActivity.this);
                finish();
            }else {
                String name=preferences.getString("username","");

                final EditText et=(EditText)findViewById(R.id.login_username);
                et.setText(name);
            }
        }
    }
}
