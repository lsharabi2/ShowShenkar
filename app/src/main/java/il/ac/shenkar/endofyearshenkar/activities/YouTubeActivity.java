package il.ac.shenkar.endofyearshenkar.activities;

import android.os.Bundle;
import android.view.View;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import il.ac.shenkar.endofyearshenkar.R;


public class YouTubeActivity extends YouTubeBaseActivity implements

        YouTubePlayer.OnInitializedListener {

    public static final String API_KEY = "AIzaSyCBu6Kh-XTr-XW3D1w8ZzlpFfEBmCotjuk";
    private YouTubePlayerView playtube;
    private YouTubePlayer youTubeP;
    private String urlVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_you_tube);
        playtube=(YouTubePlayerView) findViewById(R.id.viewVideo);
        playtube.setVisibility(View.GONE);
        playtube.initialize(API_KEY, this);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            urlVideo = extras.getString("url");
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubeP=youTubePlayer;
        youTubePlayer.cueVideo(urlVideo);
        youTubeP.setFullscreen(true);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
