package com.sparky.moivebazaaradmin.YoutubeSearch;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.sparky.moivebazaaradmin.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;


//The activity which plays has YouTubePlayerView inside layout must extend YouTubeBaseActivity
//implement OnInitializedListener to get the state of the player whether it has succeed or failed
//to load
public class PlayerActivity extends AppCompatActivity {

    com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_player);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //initialising various descriptive data in the UI and player
        TextView video_title = (TextView)findViewById(R.id.player_title);
        TextView video_desc = (TextView)findViewById(R.id.player_description);
        TextView video_id = (TextView)findViewById(R.id.player_id);

        //setting text of each View form UI
        //setText method used to change the text shown in the view
        //getIntent method returns the object of current Intent 
        //of which getStringExtra returns the string which was passed while calling the intent
        //by using the name that was associated during call
        video_title.setText(getIntent().getStringExtra("VIDEO_TITLE"));
        video_id.setText("Video ID : "+(getIntent().getStringExtra("VIDEO_ID")));
        video_desc.setText(getIntent().getStringExtra("VIDEO_DESC"));

        playYoutubeVideo(getIntent().getStringExtra("VIDEO_ID"));

    }


    private void playYoutubeVideo(String video_id) {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.getPlayerUiController().showYouTubeButton(false);
        youTubePlayerView.getPlayerUiController().showFullscreenButton(false);
        youTubePlayerView.getPlayerUiController().showBufferingProgress(true);
        youTubePlayerView.getPlayerUiController().showMenuButton(false);
        youTubePlayerView.getPlayerUiController().showVideoTitle(false);

        //youTubePlayerView.enterFullScreen();


        initPictureInPicture(youTubePlayerView);



        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

            @Override
            public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.loadVideo(video_id,0);
            }
            @Override
            public void onStateChange(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                playerStateToString(state);
            }


            @Override
            public void onCurrentSecond(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                initializeCustomButtons(second ,youTubePlayer);
            }


            @Override
            public void onError(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                super.onError(youTubePlayer, error);

                switch (error)
                {
                    case UNKNOWN:
                    case INVALID_PARAMETER_IN_REQUEST:
                    case VIDEO_NOT_FOUND:
                    case HTML_5_PLAYER:
                        break;
                    case VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER:

                        youTubePlayerView.getPlayerUiController().showYouTubeButton(true);
                        Toast.makeText(PlayerActivity.this, " Click on the Youtube Button to open this Videoin Youtube : ", Toast.LENGTH_LONG).show();


                        break;
                }
            }

            @Override
            public void onPlaybackQualityChange(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackQuality playbackQuality) {
                super.onPlaybackQualityChange(youTubePlayer, playbackQuality);

                QualityManager(youTubePlayerView, playbackQuality);

            }


        });
    }



    private void QualityManager(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView youTubePlayerView, PlayerConstants.PlaybackQuality playbackQuality) {

        TextView qualityText = findViewById(R.id.qualityText);
        switch (playbackQuality) {

            case SMALL:
                qualityText.setText("240p");

                break;
            case MEDIUM:
                qualityText.setText("360p");

                break;
            case LARGE:
                qualityText.setText("480p");

                break;

            case HD720:
                qualityText.setText("HD720");

                break;
            case HD1080:
                qualityText.setText("HD1080");

                break;
            case HIGH_RES:
                qualityText.setText("HIGH_RES");

                break;
            case UNKNOWN:
                qualityText.setText("*");

                break;
            default:
                qualityText.setText("Def");
                break;
        }
        if (qualityText.getParent() != null) {
            ((ViewGroup) qualityText.getParent()).removeView(qualityText); // <- fix
        }
        youTubePlayerView.getPlayerUiController().addView(qualityText);

    }


    private void initPictureInPicture(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView youTubePlayerView) {
        ImageView pictureInPictureView = new ImageView(this);
        pictureInPictureView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_24dp));

        pictureInPictureView.setOnClickListener( view -> {
            if(youTubePlayerView.isFullScreen())
            {
                youTubePlayerView.exitFullScreen();
                pictureInPictureView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_exit_24dp));
            }

            else
            {
                youTubePlayerView.enterFullScreen();
                pictureInPictureView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_24dp));
            }
        });

        youTubePlayerView.getPlayerUiController().addView( pictureInPictureView );

    }



    private void playerStateToString(PlayerConstants.PlayerState state) {
        switch (state) {
            case UNKNOWN:

            case VIDEO_CUED:
                return;

            case UNSTARTED:
            case ENDED:
                removeCustomActionsFromPlayer();
                youTubePlayerView.exitFullScreen();
                setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
                return;

            case PLAYING:
                showCustomActionsToPlayer();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                youTubePlayerView.enterFullScreen();
                return;

            case PAUSED:
                youTubePlayerView.exitFullScreen();
                setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
                return;

            case BUFFERING:
                removeCustomActionsFromPlayer();


                return;

            default:
        }
    }


    private void initializeCustomButtons(float second, com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
        Drawable customAction1Icon = ContextCompat.getDrawable(this, R.drawable.ic_fast_rewind_white_24dp);
        Drawable customAction2Icon = ContextCompat.getDrawable(this, R.drawable.ic_fast_forward_white_24dp);
        assert customAction1Icon != null;
        assert customAction2Icon != null;

        youTubePlayerView.getPlayerUiController().setCustomAction1(customAction1Icon, view ->
                youTubePlayer.seekTo(second - 10));

        youTubePlayerView.getPlayerUiController().setCustomAction2(customAction2Icon, view ->
                youTubePlayer.seekTo(second + 20));


    }



    private void showCustomActionsToPlayer() {
        youTubePlayerView.getPlayerUiController().showCustomAction1(true);
        youTubePlayerView.getPlayerUiController().showCustomAction2(true);
    }


    private void removeCustomActionsFromPlayer() {
        youTubePlayerView.getPlayerUiController().showCustomAction1(false);
        youTubePlayerView.getPlayerUiController().showCustomAction2(false);
    }



    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        Objects.requireNonNull(youTubePlayerView.getPlayerUiController().getMenu()).dismiss();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();

    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Close Video")
                .setMessage("Are you sure you want to exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    youTubePlayerView.release();
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }


}