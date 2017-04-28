package com.example.bookstorelocaldemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class BookInfo extends AppCompatActivity {

    //用HAVE和NOTHAVE判断此书是否已购买
    private final int HAVE=0;
    private final int NOTHAVE=1;

    private BookDetail book;
    private String username;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static void actionStart(Context context,String id,int sign){
        Intent i=new Intent(context,BookInfo.class);
        i.putExtra("id",id);
        i.putExtra("sign",sign);
        context.startActivity(i);
    }

    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            //对返回的json数据进行解析
            Bundle b=msg.getData();
            String json=b.getString("json");
            if(!json.equals("")){
                Gson gson=new Gson();
                book=gson.fromJson(json,BookDetail.class);
                UpdateUi(book,NOTHAVE);
            }else {
                Log.d("BookInfo","message,fail");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bookinfo);

        //初始化节目头部
        ActionBar ab=getSupportActionBar();
        if(ab!=null){
            ab.hide();
        }

        //获取从主程序中传来的图书Id
        Intent i=getIntent();
        final String ID=i.getStringExtra("id");
        final int sign=i.getIntExtra("sign",0);

        switch (sign){
            case 0:
                OkhttpSearch(ID);
                break;
            case 1:
                requestDatabase(ID);
                break;
            default:OkhttpSearch(ID);
        }


        //初始化控件
        ImageButton bt_return=(ImageButton) findViewById(R.id.bookinfo_reture);
        Button price=(Button)findViewById(R.id.bookinfo_price);

        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //提醒客户是否确认购买
                AlertDialog.Builder dialog=new AlertDialog.Builder(BookInfo.this);
                dialog.setTitle("确认购买此书？");
                dialog.setPositiveButton("购买", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //将购买的图书保存进数据库
                        book.setUsername(username);
                        book.setBookid(ID);
                        book.save();

                        Toast.makeText(BookInfo.this,"感谢您的购买,欢迎继续选购",Toast.LENGTH_SHORT)
                                .show();
                        MainActivity.actionStart(BookInfo.this);
                    }
                });

                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });
    }

    private void OkhttpSearch(String id){

        //获取用户名
        preferences=getSharedPreferences("AutoLogin",MODE_PRIVATE);
        username=preferences.getString("username","");
        if (username.equals("")){
            Toast.makeText(BookInfo.this,"can't get user infomation!Please login again"
                    ,Toast.LENGTH_SHORT).show();
            editor=getSharedPreferences("AutoLogin",MODE_PRIVATE).edit();
            editor.putBoolean("auto",false);
            editor.apply();

            LoginActivity.actionStart(BookInfo.this);
            finish();
        }
        //在数据库内查找此书，确认用户是否购买
        List<BookDetail> list= DataSupport.where("bookid=?",id)
                .find(BookDetail.class);

        if (list.size()>0){
            //若此书在数据库中存在则直接加载，判断该用户是否拥有
            boolean have=false;
            for (BookDetail b:list){
                if (b.getUsername().equals(username)){
                    UpdateUi(list.get(0),HAVE);
                    have=true;
                    break;
                }
            }
            if(!have) UpdateUi(list.get(0),NOTHAVE);

        }else{
            //若此书在数据库中不存在，从网络上加载
            HttpUtil.sendOkhttpRequest(1,id,new okhttp3.Callback(){
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json=response.body().string();
                    Log.d("BookInfo",json);

                    //将返回的json数据传回主线程进行解析
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("json",json);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    //有空补上
                }
            });
        }
    }

    private void requestDatabase(String id){

        List<BookDetail> list=DataSupport.where("bookid=?",id)
                .find(BookDetail.class);
        UpdateUi(list.get(0),HAVE);
    }

    //根据字数对textview字体进行调整
    private void TextAdapter(TextView tv,String content,float size){

        if (content.length()>=(100/size)*2.5){
            double s=size*0.85+1;
            tv.setTextSize((float)s);
        }else{
            tv.setTextSize(size);
        }
        tv.setText(content);
    }

    //更新Ui
    private void UpdateUi(BookDetail book0,int sign){
        TextView title=(TextView)findViewById(R.id.bookinfo_title);
        TextView author=(TextView)findViewById(R.id.bookinfo_author);
        TextView pubdate=(TextView)findViewById(R.id.bookinfo_pubdate);
        TextView summary=(TextView)findViewById(R.id.bookinfo_summary);
        ImageView image=(ImageView)findViewById(R.id.bookinfo_image);
        Button price=(Button)findViewById(R.id.bookinfo_price);

        TextAdapter(title,book0.getTitle(),36);
        TextAdapter(author,"作者："+book0.getAuthor().get(0),24);
        pubdate.setText("出版日期："+book0.getPubdate());
        summary.setText("图书简介：\n\n    "+book0.getSummary());
        image.setVisibility(View.VISIBLE);
        Picasso.with(BookInfo.this).load(book0.getImage())
                .error(R.mipmap.ic_launcher).into(image);

        switch (sign){
            case HAVE:
                price.setVisibility(View.GONE);
                break;
            case NOTHAVE:
                price.setText(book.getPrice()+"一键购买");
                break;
        }
    }
}
