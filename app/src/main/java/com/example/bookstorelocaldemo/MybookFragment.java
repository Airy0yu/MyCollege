package com.example.bookstorelocaldemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MybookFragment extends Fragment {

    private List<BookDetail> list;
    private View view;
    private String username;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mybook,container,false);
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
        this.view=view;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView tv=(TextView) view.findViewById(R.id.focus);
        list= DataSupport.where("username=?",username).find(BookDetail.class);
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recyclerview);

        if (list.size()>0) {
            StaggeredGridLayoutManager layoutManager = new
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recycler.setLayoutManager(layoutManager);
            MyRecycleAdapter adapter = new MyRecycleAdapter(list, getContext());
            recycler.setAdapter(adapter);
            HideKeyboard(view);
        }else{
            Toast.makeText(getActivity(),"书架暂时没有任何书",Toast.LENGTH_SHORT)
                    .show();
        }

        tv.setFocusable(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        HideKeyboard(view);
        Log.d("MainActivity","wwwwwwwwwwwww");
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
