package com.sparky.moivebazaaradmin.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sparky.moivebazaaradmin.Model.MovieClass;
import com.sparky.moivebazaaradmin.R;
import com.sparky.moivebazaaradmin.ViewHolder.MovieView;
import com.sparky.moivebazaaradmin.YoutubeSearch.SearchActivity;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView movieRecycler;
    DatabaseReference mEnglishMovies,mMovies;
    ProgressBar progressBar;
    Spinner languageSpinner;
    private FirebaseRecyclerAdapter<MovieClass, MovieView> MovieAdapter;

    ProgressDialog progressDialog;

        //PlayerWebView playerWebView;
    LinearLayoutManager movieLinearLayout, hindiLinearLayout, teluguLinearLayout;
    public TextInputEditText movieName, videoUrl, trailerUrl, imageUrlP, imageUrlL, movieYear;
    Button finish, cancelBtn;
    LinearLayout buttonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(this, RecyclerItemActivity.class));

        findViewById(R.id.AddLayout).setOnClickListener(v -> showAddMovieDialog());
        findViewById(R.id.editLayout).setOnClickListener(v -> showEditMovieDialog());

        findViewById(R.id.SearchLayout).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SearchActivity.class)));


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        //playerWebView = findViewById(R.id.playerWebview);
        //  playerWebView.load("x26hv6c");

        //StartAppSDK.setTestAdsEnabled(BuildConfig.DEBUG);
    }

    private void showEditMovieDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Material_Light_NoActionBar);
        dialog.setContentView(R.layout.dialog_edit_movie);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;


        languageSpinner = (Spinner) dialog.findViewById(R.id.languageSpinner);

        final ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.language, android.R.layout.simple_spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);


        movieName = (TextInputEditText) dialog.findViewById(R.id.movieName);
        videoUrl = (TextInputEditText) dialog.findViewById(R.id.videoUrl);
        trailerUrl = (TextInputEditText) dialog.findViewById(R.id.trailerUrl);
        imageUrlP = (TextInputEditText) dialog.findViewById(R.id.imageUrlP);
        imageUrlL = (TextInputEditText) dialog.findViewById(R.id.imageUrlL);
        movieYear = (TextInputEditText) dialog.findViewById(R.id.movieYear);

        finish = (Button) dialog.findViewById(R.id.finish);
        cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);

        finish.setVisibility(View.GONE);

        dialog.findViewById(R.id.searchButton).setOnClickListener(v -> {
            final String sMovieName = Objects.requireNonNull(movieName.getText()).toString().trim();
            final String sLanguage = languageSpinner.getSelectedItem().toString();
            progressDialog.show();
            progressDialog.setMessage("Searching ....");
            movieYear.setText("");
            imageUrlL.setText("");
            imageUrlP.setText("");
            trailerUrl.setText("");
            videoUrl.setText("");

            if (sMovieName.isEmpty()) {
                movieName.setError("Empty");
                movieName.requestFocus();
                progressDialog.dismiss();

            }
            else {
                mMovies = FirebaseDatabase.getInstance().getReference("Movies").child(sLanguage);

                mMovies.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.child(sMovieName).exists())
                        {

                            if(snapshot.child(sMovieName).child("videoUrl").exists())
                            {
                                String mVideoUrl = Objects.requireNonNull(snapshot.child(sMovieName).child("videoUrl").getValue()).toString();
                                videoUrl.setText(mVideoUrl);

                            }
                            if(snapshot.child(sMovieName).child("trailerUrl").exists())
                            {
                                String mTrailerUrl = Objects.requireNonNull(snapshot.child(sMovieName).child("trailerUrl").getValue()).toString();
                                trailerUrl.setText(mTrailerUrl);

                            }
                            if (snapshot.child(sMovieName).child("movieYear").exists())
                            {
                                String mMovieYear = Objects.requireNonNull(snapshot.child(sMovieName).child("movieYear").getValue()).toString();
                                movieYear.setText(mMovieYear);
                            }
                            if (snapshot.child(sMovieName).child("imageUrlP").exists())
                            {
                                String mImageUrlP = Objects.requireNonNull(snapshot.child(sMovieName).child("imageUrlP").getValue()).toString();
                                imageUrlP.setText(mImageUrlP);
                            }
                            if (snapshot.child(sMovieName).child("imageUrlL").exists())
                            {
                                String mImageUrlL = Objects.requireNonNull(snapshot.child(sMovieName).child("imageUrlL").getValue()).toString();
                                imageUrlL.setText(mImageUrlL);
                            }


                            finish.setVisibility(View.VISIBLE);

                        }
                        else {
                            movieName.setError("Not Exists");
                            movieName.requestFocus();

                            movieYear.setText("");
                            imageUrlL.setText("");
                            imageUrlP.setText("");
                            trailerUrl.setText("");
                            videoUrl.setText("");

                            finish.setVisibility(View.GONE);
                        }
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });



        finish.setOnClickListener(v -> {

            cancelBtn.setVisibility(View.GONE);

            progressDialog.show();
            progressDialog.setMessage("Updating Movie");

            final String sMovieName = Objects.requireNonNull(movieName.getText()).toString().trim();
            final String sVideoUrl = Objects.requireNonNull(videoUrl.getText()).toString().trim();
            final String sTrailerUrl = Objects.requireNonNull(trailerUrl.getText()).toString().trim();
            final String sImageUrlP = Objects.requireNonNull(imageUrlP.getText()).toString().trim();
            final String sImageUrlL = Objects.requireNonNull(imageUrlL.getText()).toString().trim();
            final String sMovieYear = Objects.requireNonNull(movieYear.getText()).toString().trim();

            final String sLanguage = languageSpinner.getSelectedItem().toString();


            if (sMovieName.isEmpty()) {
                movieName.setError("Empty");
                movieName.requestFocus();
                finish.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


            }
            else if (sVideoUrl.isEmpty()) {
                videoUrl.setError("Empty");
                videoUrl.requestFocus();
                finish.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


            }else if (sTrailerUrl.isEmpty()) {
                trailerUrl.setError("Empty");
                trailerUrl.requestFocus();
                finish.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


            }else if (sImageUrlP.isEmpty()) {
                imageUrlP.setError("Empty");
                imageUrlP.requestFocus();
                finish.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


            }else if (sImageUrlL.isEmpty()) {
                imageUrlL.setError("Empty");
                imageUrlL.requestFocus();
                finish.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


            }
            else if (sMovieYear.isEmpty()) {
                movieYear.setError("Empty");
                movieYear.requestFocus();
                finish.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


            }
            else
            {

                MovieClass movie = new MovieClass(
                        sVideoUrl,
                        sTrailerUrl,
                        sImageUrlP,
                        sImageUrlL,
                        sMovieYear
                );

                mMovies = FirebaseDatabase.getInstance().getReference("Movies");
                mMovies.child(sLanguage).child(sMovieName).setValue(movie).addOnSuccessListener(aVoid ->
                {
                    Toast.makeText(this, "Movie "+ sMovieName+ " updated to "+ sLanguage , Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                });

                cancelBtn.setVisibility(View.VISIBLE);
            }

        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setAttributes(lp);
    }


    private void showAddMovieDialog() {
        final Dialog mDialogView = new Dialog(this, android.R.style.Theme_Material_Light_NoActionBar);
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialog = inflater.inflate(R.layout.dialog_add_movie, null);
        mDialogView.setContentView(dialog);
        mDialogView.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;


        languageSpinner = (Spinner) dialog.findViewById(R.id.languageSpinner);

        final ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.language, android.R.layout.simple_spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);


        movieName = (TextInputEditText) dialog.findViewById(R.id.movieName);
        videoUrl = (TextInputEditText) dialog.findViewById(R.id.videoUrl);
        trailerUrl = (TextInputEditText) dialog.findViewById(R.id.trailerUrl);
        imageUrlP = (TextInputEditText) dialog.findViewById(R.id.imageUrlP);
        imageUrlL = (TextInputEditText) dialog.findViewById(R.id.imageUrlL);
        movieYear = (TextInputEditText) dialog.findViewById(R.id.movieYear);


        finish = (Button) dialog.findViewById(R.id.finish);
        cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);


        finish.setOnClickListener(v -> {

            progressDialog.show();
            progressDialog.setMessage("Checking MovieName");

            final String sMovieName = Objects.requireNonNull(movieName.getText()).toString().trim();
            final String sVideoUrl = Objects.requireNonNull(videoUrl.getText()).toString().trim();
            final String sTrailerUrl = Objects.requireNonNull(trailerUrl.getText()).toString().trim();
            final String sImageUrlP = Objects.requireNonNull(imageUrlP.getText()).toString().trim();
            final String sImageUrlL = Objects.requireNonNull(imageUrlL.getText()).toString().trim();
            final String sMovieYear = Objects.requireNonNull(movieYear.getText()).toString().trim();

            final String sLanguage = languageSpinner.getSelectedItem().toString();


            if (sMovieName.isEmpty()) {
                Snackbar("Movie Name is empty !" ,dialog);
                movieName.requestFocus();
                finish.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


            }
            else if (sVideoUrl.isEmpty()) {
                Snackbar("Video Url is empty !", dialog);
                videoUrl.requestFocus();
                finish.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


            }else if (sTrailerUrl.isEmpty()) {
                Snackbar("Trailer Url is empty !", dialog);
                trailerUrl.requestFocus();
                finish.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


            }else if (sImageUrlP.isEmpty()) {
                Snackbar("Image Url P is empty !", dialog);
                imageUrlP.requestFocus();
                finish.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


            }else if (sImageUrlL.isEmpty()) {
                Snackbar("Image Url L is empty !", dialog);
                imageUrlL.requestFocus();
                finish.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


            }
            else if (sMovieYear.isEmpty()) {
                Snackbar("Movie Year is empty !", dialog);
                movieYear.requestFocus();
                finish.setVisibility(View.VISIBLE);
                progressDialog.dismiss();


            }
            else
            {

                mMovies = FirebaseDatabase.getInstance().getReference("Movies");

                mMovies.child(sLanguage).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.child(sMovieName).exists())
                        {
                            Snackbar("Movie Name Exists !", dialog);
                            movieName.requestFocus();
                            finish.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }
                        else {
                            progressDialog.setMessage("Uploading Movie");

                            MovieClass movie = new MovieClass(
                                    sVideoUrl,
                                    sTrailerUrl,
                                    sImageUrlP,
                                    sImageUrlL,
                                    sMovieYear

                            );

                            mMovies.child(sLanguage).child(sMovieName).setValue(movie).addOnSuccessListener(aVoid -> {
                                progressDialog.dismiss();
                                Snackbar("Movie "+ sMovieName+ " Uploaded to "+ sLanguage, dialog);
                                mDialogView.dismiss();
                            });




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(MainActivity.this,"error :"+ error, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }


                });

            }

        });

        cancelBtn.setOnClickListener(v -> mDialogView.dismiss());
        mDialogView.show();
        Objects.requireNonNull(mDialogView.getWindow()).setAttributes(lp);
    }

    private void Snackbar(String text, View dialog) {

        Snackbar snackbar = Snackbar.make(dialog, Objects.requireNonNull(text), Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.blue_700))
                .setAction("Okay", view -> {
                });
        snackbar.show();

    }
//    private void initializeRecyclerView() {
//        movieRecycler = findViewById(R.id.movieRecycler);
//
//
//        movieRecycler.hasFixedSize();
//
//
//
//
//        LinearLayoutManager movieLinearLayout = new GridLayoutManager(getApplicationContext(),3);
////        movieLinearLayout.setReverseLayout(true);
////        movieLinearLayout.setStackFromEnd(true);
//
////        movieLinearLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
//
//
//        movieRecycler.setLayoutManager(movieLinearLayout);
//
//
//
//        loadAllMoviesList();
//
//    }
//
//
//
//
//
//    private void loadAllMoviesList() {
//        mEnglishMovies = FirebaseDatabase.getInstance().getReference("Movies");
//        mEnglishMovies.keepSynced(true);
//
//        Query moviesList = mEnglishMovies;
//
//        FirebaseRecyclerOptions<MovieClass> EnglishMovieOption = new FirebaseRecyclerOptions.Builder<MovieClass>()
//                .setQuery(moviesList, MovieClass.class)
//                .build();
//        MovieAdapter = new FirebaseRecyclerAdapter<MovieClass, MovieView>(EnglishMovieOption) {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            protected void onBindViewHolder(@NonNull MovieView holder, final int position, @NonNull final MovieClass model) {
//
//                progressBar.setVisibility(View.GONE);
//                movieRecycler.setVisibility(View.VISIBLE);
//
//                String HINDI_URL ="https://images-na.ssl-images-amazon.com/images/I/51Q-Pd4NmlL._SX258_BO1,204,203,200_.jpg";
//                String ENGLISH_URL = "https://upload.wikimedia.org/wikipedia/en/thumb/a/a6/Once_Upon_a_Time_in_Hollywood_poster.png/220px-Once_Upon_a_Time_in_Hollywood_poster.png";
//                String Language = MovieAdapter.getRef(position).getKey();
//                holder.movieTypeText.setText(Language);
//
//                assert Language != null;
//                if(Language.equals("Hindi"))
//                {
//                    Picasso.get().load(HINDI_URL).placeholder(R.drawable.placeholder_image).into(holder.movieImageView);
//
//                }
//                else if(Language.equals("English"))
//                {
//                    Picasso.get().load(ENGLISH_URL).placeholder(R.drawable.placeholder_image).into(holder.movieImageView);
//                }
//
//
////                holder.movieLayout.setOnClickListener(v -> {
////                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
////
////                    Bundle bundle = new Bundle();
////                    i.putExtra("IMAGE_URl", IMAGE_URl);
////                    i.putExtra("IMAGE_URlB", model.getImageUrlB());
////                    i.putExtra("movieName", MovieAdapter.getRef(position).getKey());
////                    i.putExtra("movieYear", model.getMovieYear());
////
////
////                    i.putExtras(bundle);
////                    startActivity(i);
////
////                });
//            }
//
//            @NonNull
//            @Override
//            public MovieView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                View itemView = LayoutInflater.from(viewGroup.getContext())
//                        .inflate(R.layout.layout_movie_type, viewGroup, false);
//                return new MovieView(itemView);
//            }
//        };
//        movieRecycler.setAdapter(MovieAdapter);
//        MovieAdapter.startListening();
//
//    }
}