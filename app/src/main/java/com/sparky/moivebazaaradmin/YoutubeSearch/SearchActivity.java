package com.sparky.moivebazaaradmin.YoutubeSearch;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.sparky.moivebazaaradmin.R;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SearchActivity extends AppCompatActivity {

    private EditText searchInput;
    

    private YoutubeAdapter youtubeAdapter;

    private RecyclerView mRecyclerView;
    

    private ProgressDialog mProgressDialog;
    

    private Handler handler;


    private List<VideoItem> searchResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        mProgressDialog = new ProgressDialog(this);
        searchInput = (EditText)findViewById(R.id.search_input);
        mRecyclerView = (RecyclerView) findViewById(R.id.videos_recycler_view);


        mProgressDialog.setTitle("Searching...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        handler = new Handler();
        //searchOnYoutube("Telugu Movies");


        searchInput.setOnEditorActionListener((v, actionId, event) -> {


            if(actionId == EditorInfo.IME_ACTION_SEARCH){

                mProgressDialog.setMessage("Finding videos for "+v.getText().toString());


                mProgressDialog.show();


                searchOnYoutube(v.getText().toString());

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                return false;
            }
            return true;
        });

    }

    private void searchOnYoutube(final String keywords){
        

        new Thread(){

            //implementing run method
            public void run(){

                YoutubeConnector yc = new YoutubeConnector(SearchActivity.this);

                searchResults = yc.search(keywords);

                //implementing run method of Runnable
                handler.post(() -> {

                    fillYoutubeVideos();

                    mProgressDialog.dismiss();
                });
            }
        //starting the thread
        }.start();
    }

    private void fillYoutubeVideos(){

        youtubeAdapter = new YoutubeAdapter(getApplicationContext(),searchResults);

        mRecyclerView.setAdapter(youtubeAdapter);

        youtubeAdapter.notifyDataSetChanged();
    }
}