package com.example.new_application;

import android.content.Context;
        import android.support.annotation.NonNull;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchViewRecyclerViewAdapter extends RecyclerView.Adapter<SearchPageRecyclerViewHolder> {
    private List<String> itemList;
    protected Context mcontext;

    public SearchViewRecyclerViewAdapter(Context context, List<String> itemlist){
        this.mcontext = context;
        this.itemList = itemlist;

    }
    /*


    @Override
    public SearchPageRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

       SearchPageRecyclerViewHolder viewHolder = null;

            //System.out.println("create");
            //  Random rand = new Random();
            // int n = rand.nextInt(2) + 1;
            //   if(n == 1){
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sp_itemlist, parent, false);
            viewHolder = new SearchPageRecyclerViewHolder(layoutView);
/*
            }else{
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.random_text, parent, false);
                viewHolder = new RecyclerViewHolders(layoutView);


            }
*/

    // System.out.println("create1");
/*
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull SearchPageRecyclerViewHolder holder, int position) {
        if(holder instanceof SearchPageRecyclerViewHolder){

            final String pos;
            final int positionfinal = position;
            pos = Integer.toString(positionfinal);


             ((SearchPageRecyclerViewHolder)holder).textview.setText(itemList.get(position));

        }else{
            //  ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
    */




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
    public SearchPageRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewtype) {
        // System.out.println("search test1234");

        SearchPageRecyclerViewHolder holder = null;

        // System.out.println("search test1234");
        if(viewtype == 1){
//inflates view if it is valid
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sp_itemlist, viewGroup, false);
            holder = new SearchPageRecyclerViewHolder(layoutView, mcontext);
            //   holder = new ViewHolder(layoutView);
        }else{
            View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emptylayout, viewGroup, false);
            holder = new SearchPageRecyclerViewHolder(layoutView, mcontext);


        }


        return holder;

/*
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

           return new ViewHolder(view);
           */
    }

    // setting up viewholder for each list
    @Override
    public void onBindViewHolder(@NonNull SearchPageRecyclerViewHolder viewHolder, int i) {
        System.out.println("search test");
        if(viewHolder instanceof SearchPageRecyclerViewHolder){
//set up text and image for each game in search result
            ((SearchPageRecyclerViewHolder)viewHolder).textview.setText(itemList.get(i));
           final String url =  "http://192.168.0.49:81/game_images/" + itemList.get(i) + ".jpg";
            Picasso.get().load(url).into(((SearchPageRecyclerViewHolder)viewHolder).imageview);

        }
    }


    //important don't forget to count
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

}
