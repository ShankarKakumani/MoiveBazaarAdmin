package com.sparky.moivebazaaradmin.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.sparky.moivebazaaradmin.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MovieView extends RecyclerView.ViewHolder implements View.OnClickListener {


    public ItemClickListener itemClickListener;
    public LinearLayout movieLayout;
    public TextView movieTypeText;
    public ImageView movieImageView;

    public MovieView(@NonNull View itemView) {
        super(itemView);



        movieLayout = itemView.findViewById(R.id.movieLayout);
        movieTypeText = itemView.findViewById(R.id.movieTypeText);
        movieImageView = itemView.findViewById(R.id.movieImageView);


        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

    public void setDetails(Context applicationContext, String subjectName) {
    }
}
