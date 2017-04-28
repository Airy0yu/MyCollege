package com.example.bookstorelocaldemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

public class UserFragment extends Fragment {

    private int controlInit=0;
    private double consumption;
    private int count;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String username;
    private LayoutInflater inflater;
    private View view;
    private List<BookDetail> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user,container,false);

        if(controlInit++==0) {
            initData();
        }

        this.inflater=inflater;
        this.view=view;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView user=(TextView)view.findViewById(R.id.usermes_username);
        TextView bookcount=(TextView)view.findViewById(R.id.usermes_bookcount);
        TextView userconsum=(TextView)view.findViewById(R.id.usermes_consumption);
        Button bt=(Button)view.findViewById(R.id.loginout);

        user.setText("用户名："+username);
        bookcount.setText("拥有图书："+count+"本");
        userconsum.setText("已消费："+consumption+"元");
        HideKeyboard(view);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor=getContext().getSharedPreferences("AutoLogin"
                        ,getContext().MODE_PRIVATE).edit();
                editor.putBoolean("auto",false);
                editor.apply();

                LoginActivity.actionStart(getActivity());
                getActivity().finish();
            }
        });
    }

    private void initData(){
        preferences=getActivity().getSharedPreferences("AutoLogin"
                ,getActivity().MODE_PRIVATE);
        username=preferences.getString("username","");
        if (username.equals("")){
            Toast.makeText(getActivity(),"can't get user infomation!Please login again"
                    ,Toast.LENGTH_SHORT).show();
            editor=getActivity().getSharedPreferences("AutoLogin",getActivity().MODE_PRIVATE).edit();
            editor.putBoolean("auto",false);
            editor.apply();

            LoginActivity.actionStart(getActivity());
            getActivity().finish();
        }

        list= DataSupport.where("username=?",username)
                .find(BookDetail.class);
        consumption=0;
        if ((count = list.size())>0){
            for (BookDetail b:list){

                String price=b.getPrice();
                String p="";

                for (int i=0;i<price.length();i++){
                    char c=price.charAt(i);

                    if ((c>47&&c<58)||c=='.'){
                        p+=c;
                    }
                }
                consumption+=Double.parseDouble(p);
            }
        }
    }

    public static void HideKeyboard(View v)
    {
        InputMethodManager imm = (InputMethodManager )v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);

        }
    }
}
