package com.sparky.moivebazaaradmin.RecyclerItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sparky.moivebazaaradmin.R;
import com.sparky.moivebazaaradmin.YoutubeSearch.PlayerActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Belal on 10/18/2017.
 */


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Product> productList;

    //getting the context and product list with constructor
    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.layout_episode, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        Product product = productList.get(position);

        //binding the data with the viewholder views
        holder.video_title.setText(product.getTitle());

        Picasso.get()
                .load(product.getImageUrl())
                .resize(480,270)
                .centerCrop()
                .into(holder.video_thumbnail);


        //String a = product.getVideoUrl();
        holder.movieLayout.setOnClickListener(view -> {


            Intent intent = new Intent(mCtx, PlayerActivity.class);


            intent.putExtra("VIDEO_ID", product.getVideoUrl());
            intent.putExtra("VIDEO_TITLE",product.getTitle());
            intent.putExtra("VIDEO_DESC",product.getImageUrl());

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mCtx.startActivity(intent);
        });



    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView video_title, textViewShortDesc, textViewRating, textViewPrice;
        ImageView video_thumbnail;

        CardView movieLayout;
        public ProductViewHolder(View itemView) {
            super(itemView);

            movieLayout = itemView.findViewById(R.id.movieLayout);
            video_title = itemView.findViewById(R.id.video_title);
            video_thumbnail = itemView.findViewById(R.id.video_thumbnail);
        }
    }
}
