package com.example.new_application;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SelectGameRecyclerViewAdapter extends RecyclerView.Adapter<SelectGameRecyclerViewHolder> {
    private List<String> itemList;
    protected Context mcontext;
    private String musername;
    public SelectGameRecyclerViewAdapter(Context context, List<String> itemlist, String username){
        this.mcontext = context;
        this.itemList = itemlist;
        this.musername = username;

    }

    public int getItemViewType(int position) {
        int i;
        i = itemList.get(position) != null ? 1 : 0;
        //i = 0;
        //   if(position == 0){
        //      i = 0;
        // }
        return i;

    }

    @NonNull
    @Override
    public SelectGameRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewtype) {
        // System.out.println("search test1234");

        SelectGameRecyclerViewHolder holder = null;

        // System.out.println("search test1234");
        if(viewtype == 1){
//inflates view if it is valid
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sp_itemlist, viewGroup, false);
            holder = new SelectGameRecyclerViewHolder(layoutView, mcontext,musername);

        }else{
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emptylayout, viewGroup, false);
            holder = new SelectGameRecyclerViewHolder(layoutView, mcontext,musername);


        }
        return holder;
    }

    // setting up viewholder for each list
    @Override
    public void onBindViewHolder(@NonNull SelectGameRecyclerViewHolder viewHolder, int i) {
        System.out.println("search test");
        if(viewHolder instanceof SelectGameRecyclerViewHolder){
            ((SelectGameRecyclerViewHolder)viewHolder).textview.setText(itemList.get(i));
        }
    }


    //important don't forget to count
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

}
