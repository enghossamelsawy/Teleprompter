package com.example.hossam.teleprompter.helper;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.hossam.teleprompter.R;

import java.util.ArrayList;

/**
 * Created by hossam on 5/3/2017.
 */

public class RecyclerViewNewsAdabter  extends RecyclerView.Adapter<RecyclerViewNewsAdabter.MyviewHolder> {
    ArrayList<String> newsDescription;
    int textSize;

    public  RecyclerViewNewsAdabter(ArrayList<String>newsDescription, int textSize)
    {
        this.newsDescription = newsDescription;
        this.textSize= textSize;
    }
    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View  row = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items,parent,false);
        MyviewHolder holder = new MyviewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {
        String newsdata= newsDescription.get(position);
        holder.tvNewsHolder.setText(newsdata);
        holder.tvNewsHolder.setTextSize(textSize);

    }

    @Override
    public int getItemCount() {
        return newsDescription.size();
    }


  public class MyviewHolder extends RecyclerView.ViewHolder
    {
        TextView tvNewsHolder ;
        ImageView ivNewsHolder;
        public MyviewHolder(View itemView) {
            super(itemView);
            tvNewsHolder= (TextView) itemView.findViewById(R.id.editText);
            ivNewsHolder = (ImageView)itemView.findViewById(R.id.news_sprator);
        }
    }
}
