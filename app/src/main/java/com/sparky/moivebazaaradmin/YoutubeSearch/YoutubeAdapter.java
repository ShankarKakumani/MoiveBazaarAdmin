package com.sparky.moivebazaaradmin.YoutubeSearch;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sparky.moivebazaaradmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Abhishek on 16-Feb-18.
 */


//Adapter class for RecyclerView of videos
public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.MyViewHolder> {

    private Context mContext;
    private List<VideoItem> mVideoList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView thumbnail;
        public TextView video_title, video_id, video_description;
        public RelativeLayout video_view;

        public MyViewHolder(View view) {
            
            super(view);

            //the video_item.xml file is now associated as view object
            //so the view can be called from view's object
            thumbnail = (ImageView) view.findViewById(R.id.video_thumbnail);
            video_title = (TextView) view.findViewById(R.id.video_title);
            video_id = (TextView) view.findViewById(R.id.video_id);
            video_description = (TextView) view.findViewById(R.id.video_description);
            video_view = (RelativeLayout) view.findViewById(R.id.video_view);
        }
    }

    //Parameterised Constructor to save the Activity context and video list
    //helps in initializing a oject for this class
    public YoutubeAdapter(Context mContext, List<VideoItem> mVideoList) {
        this.mContext = mContext;
        this.mVideoList = mVideoList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_episode, parent, false);

        return new MyViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    //filling every item of view with respective text and image
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

         // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final VideoItem singleVideo = mVideoList.get(position);

        //replace the default text with id, title and description with setText method
        holder.video_id.setText("Video ID : "+singleVideo.getId()+" ");
        holder.video_title.setText(singleVideo.getTitle());
        holder.video_description.setText(singleVideo.getDescription());

        //Picasso library allows for hassle-free image loading
        // in your application—often in one line of code!
        //Features :
        //-Handling ImageView recycling and download cancelation in an adapter
        //-Complex image transformations with minimal memory use
        //-Automatic memory and disk caching
        
        //placing the thumbnail with picasso library 
        //by resizing it to the size of thumbnail

        //with method gives access to the global default Picasso instance
        //load method starts an image request using the specified path may be a remote URL, file resource, etc.
        //resize method resizes the image to the specified size in pixels wrt width and height
        //centerCrop crops an image inside of the bounds specified by resize(int, int) rather than distorting the aspect ratio
        //into method asynchronously fulfills the request into the specified Target
        Picasso.get()
                .load(singleVideo.getThumbnailURL())
                .resize(480,270)
                .centerCrop()
                .into(holder.thumbnail);

        String a = singleVideo.getId();
        if(checkWordExistence(a, "yt44hkYDlPs , CESC3l7-lCQ , cIR8N6LJl70 ")) {

            //holder.video_view.setVisibilitvideos_recycler_view(View.VISIBLE);

        } else {

            //holder.video_view.setVisibility(View.GONE);

            //something else
        }



        //setting on click listener for each video_item to launch clicked video in new activity
        //onClick method called when the view is clicked
        holder.video_view.setOnClickListener(view -> {

            ClipboardManager clipman = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            String text = "videosClassList.add(new VideosClass(\"" + singleVideo.getTitle() +"\", \"" + singleVideo.getThumbnailURL()+"\", \"" +singleVideo.getId() +"\"));";
            Objects.requireNonNull(clipman).setText(text);
            Toast.makeText(mContext, "Copied Sucessfull", Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(mContext, PlayerActivity.class);
//
//            intent.putExtra("VIDEO_ID", singleVideo.getId());
//            intent.putExtra("VIDEO_TITLE",singleVideo.getTitle());
//            intent.putExtra("VIDEO_DESC",singleVideo.getThumbnailURL());
//
//
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//            mContext.startActivity(intent);
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    //here the dataset is mVideoList
    @Override
    public int getItemCount() {
        if (mVideoList == null)
            return 0;
        else
        return mVideoList.size();
    }


    public boolean checkWordExistence(String word, String sentence) {
        if (sentence.contains(word)) {
            int start = sentence.indexOf(word);
            int end = start + word.length();

            boolean valid_left = ((start == 0) || (sentence.charAt(start - 1) == ' '));
            boolean valid_right = ((end == sentence.length()) || (sentence.charAt(end) == ' '));

            return valid_left && valid_right;
        }
        return false;
    }
}