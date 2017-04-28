package com.example.bookstorelocaldemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MyFragmentAdapter fragmentadapter;
    private List<Fragment> fragmentlist;
    ViewPager viewpager;

    public static void actionStart(Context context){
        Intent i=new Intent(context,MainActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        //初始化节目头部
        ActionBar ab=getSupportActionBar();
        if(ab!=null){
            ab.hide();
        }

        viewpager=(ViewPager)findViewById(R.id.viewpager);
        RadioButton rb_search=(RadioButton)findViewById(R.id.main_search);
        RadioButton rb_mybook=(RadioButton)findViewById(R.id.main_mybook);
        RadioButton rb_user=(RadioButton)findViewById(R.id.main_user);

        rb_search.setOnClickListener(this);
        rb_mybook.setOnClickListener(this);
        rb_user.setOnClickListener(this);

        Fragment searchfragment=new SearchFragment();
        Fragment mybookfragment=new MybookFragment();
        Fragment userfragment=new UserFragment();
        fragmentlist=new ArrayList<Fragment>();
        fragmentlist.add(searchfragment);
        fragmentlist.add(mybookfragment);
        fragmentlist.add(userfragment);

        FragmentManager fm=getSupportFragmentManager();
        fragmentadapter=new MyFragmentAdapter(fm,fragmentlist);

        viewpager.setAdapter(fragmentadapter);
        viewpager.setCurrentItem(0);
        ChangeButtonColor(0);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //ChangeButtonColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        viewpager=(ViewPager)findViewById(R.id.viewpager);
        switch (v.getId()){
            case R.id.main_search:
                viewpager.setCurrentItem(0);
                ChangeButtonColor(0);
                break;
            case R.id.main_mybook:
                viewpager.setCurrentItem(1);
                ChangeButtonColor(1);
                break;
            case R.id.main_user:
                viewpager.setCurrentItem(2);
                ChangeButtonColor(2);
                break;
            default:
        }
    }

    public void ChangeButtonColor(int position){

        final RadioButton rb1=(RadioButton)findViewById(R.id.main_search);
        final RadioButton rb2=(RadioButton)findViewById(R.id.main_mybook);
        final RadioButton rb3=(RadioButton)findViewById(R.id.main_user);

        switch (position){
            case 0:
                rb1.setTextColor(0xff8DB6CD);
                rb2.setTextColor(0xff000000);
                rb3.setTextColor(0xff000000);
                break;
            case 1:
                rb2.setTextColor(0xff8DB6CD);
                rb1.setTextColor(0xff000000);
                rb3.setTextColor(0xff000000);
                break;
            case 2:
                rb3.setTextColor(0xff8DB6CD);
                rb2.setTextColor(0xff000000);
                rb1.setTextColor(0xff000000);
                break;
            default:
                Toast.makeText(MainActivity.this,"failed",Toast.LENGTH_SHORT).show();
        }
    }
}
