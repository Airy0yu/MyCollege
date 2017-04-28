package com.example.bookstorelocaldemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class SearchFragment extends Fragment {

    private LayoutInflater inflater;
    private View view;
    private ListView listview;
    private String[] ID;

    private Handler handler=new Handler(){

        public void handleMessage(Message mes){

            //对返回的json数据进行解析
            Bundle b=mes.getData();
            String json=b.getString("json");
            if(!json.equals("")){
                Gson gson=new Gson();
                SearchBookStore bookStore=gson.fromJson(json
                        ,SearchBookStore.class);

                List<SearchBook> booklist=bookStore.getBooks();

                if(booklist.size()>0){

                    //将数据加载入Listview
                    listview=(ListView)view.findViewById(R.id.search_listview);
                    SearchBookAdapter adapter=new SearchBookAdapter(getActivity()
                            ,R.layout.searchbook_item,booklist);
                    listview.setAdapter(adapter);
                    Log.d("SearchFragment","success");

                    ID=new String[booklist.size()];

                    for(int i=0;i<booklist.size();i++){
                        SearchBook book=booklist.get(i);
                        ID[i]=book.getId();
                    }
                }else{
                    Log.d("SearchFragment","fail");
                }
            }else {
                Log.d("SearchFragment","message,fail");
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search,container,false);
        this.inflater=inflater;
        this.view=view;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化SearchView
        SearchView search=(SearchView)view.findViewById(R.id.search);
        search.setIconified(false);
        search.setIconifiedByDefault(false);
        search.setSubmitButtonEnabled(true);
        search.setQuery("龙族",false);

        //对submit点击事件进行监听
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(!query.equals("")){
                    Log.d("Fragment",query);
                    OkhttpSearch(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        listview=(ListView)view.findViewById(R.id.search_listview);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookInfo.actionStart(getActivity(),ID[position],0);
            }
        });
    }

    //调用HTTPUtil进行网络数据加载
    private void OkhttpSearch(String query){
        HttpUtil.sendOkhttpRequest(0,"龙族",new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json=response.body().string();
                Log.d("SearchFragment",json);

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
