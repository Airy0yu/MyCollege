package com.example.bookstorelocaldemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchBookAdapter extends ArrayAdapter<SearchBook>{

    private int resourceId;
    private ViewHolder holder;

    public SearchBookAdapter(Context context, int ResourceId, List<SearchBook> list){

        super(context,ResourceId,list);
        this.resourceId=ResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        holder = new ViewHolder();
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            holder.icon=(ImageView)view.findViewById(R.id.item_image);
            holder.title=(TextView)view.findViewById(R.id.item_title);
            holder.author=(TextView)view.findViewById(R.id.item_author);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        SearchBook book=getItem(position);

        List<String> authors=book.getAuthor();

        TextAdapter(holder.author," "+authors.get(0),24);
        TextAdapter(holder.title,book.getTitle(),36);

        Picasso.with(getContext()).load(book.getImage())
                .error(R.mipmap.ic_launcher).into(holder.icon);

        return view;
    }

    class ViewHolder {
        ImageView icon;
        TextView title;
        TextView author;
    }

    private void TextAdapter(TextView tv,String content,float size){

        if (content.length()>=(100/size)*2.5){
            double s=size*0.85+1;
            tv.setTextSize((float)s);
        }else{
            tv.setTextSize(size);
        }

        tv.setText(content);
    }
}
