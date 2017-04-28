package com.example.bookstorelocaldemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.ViewHolder> {

    private List<BookDetail> list;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{

        View bookview;
        ImageView cover;
        TextView title;

        public ViewHolder(View view){
            super(view);
            bookview=view;
            cover=(ImageView)view.findViewById(R.id.bookcover);
            title=(TextView)view.findViewById(R.id.booktitle);
        }
    }

    public MyRecycleAdapter(List<BookDetail> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookshelf_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        holder.bookview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                BookDetail book=list.get(position);
                BookInfo.actionStart(context,book.getBookid(),1);
            }
        });
       return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String booktitle=list.get(position).getTitle();
        String bookcover=list.get(position).getImage();
        holder.title.setText(booktitle);
        Picasso.with(context).load(bookcover)
                .error(R.mipmap.ic_launcher).into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
