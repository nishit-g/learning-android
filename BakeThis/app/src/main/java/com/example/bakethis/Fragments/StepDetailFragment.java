package com.example.bakethis.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bakethis.Helper.Constants;
import com.example.bakethis.Object.StepsObject;
import com.example.bakethis.R;
import com.example.bakethis.databinding.FragmentStepDetailBinding;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.bakethis.Helper.Constants.PLAYER_PLAYBACK_SPEED;

//TODO : Check for internet connection before playing the video


public class StepDetailFragment extends Fragment implements Player.EventListener {
    private FragmentStepDetailBinding fragLayout;
    private static final String TAG = "StepDetailFragment";

    private PlaybackStateCompat.Builder stateBuilder;
    private static MediaSessionCompat mediaSession;

    private TextView shortDesc;
    private TextView longDesc;
    private String videoURL;
    private String thumbnailUrl;
    private boolean hasURL;
    private boolean hasThumbnailUrl;
    private FrameLayout flPlayerView;
    private TextView noVideo;
    private boolean playwhenready;

    private Button nextButton;
    private Button prevButton;

    private SimpleExoPlayer player;
    private PlayerView playerView;

    private int currentWindow;
    private long currentPosition;

    private ArrayList<StepsObject> stepsList;
    private int stepIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragLayout = FragmentStepDetailBinding.inflate(inflater,container,false);

        shortDesc = fragLayout.tvShortDesc;
        longDesc = fragLayout.tvLongDesc;
        playerView = fragLayout.playerView;
        flPlayerView = fragLayout.flPlayerView;
        noVideo = fragLayout.tvNoVideo;
        nextButton = fragLayout.btNext;
        prevButton = fragLayout.btPrev;

        Intent intent = getActivity().getIntent();
        if(getArguments()!=null){
            Log.d(TAG, "onCreateView: From GetArguments");
            playwhenready = true;
            stepsList = getArguments().getParcelableArrayList(Constants.STEPS_LIST);
            stepIndex = getArguments().getInt(Constants.STEP_INDEX);

            checkAndInitializePlayer(stepIndex,stepsList);
        }
        else if(savedInstanceState!=null){
            Log.d(TAG, "onCreateView: From Saved Instance");
            stepsList = savedInstanceState.getParcelableArrayList(Constants.STEPS_LIST);
            stepIndex = savedInstanceState.getInt(Constants.STEP_INDEX);
            currentWindow = savedInstanceState.getInt(Constants.CURRENT_WINDOW);
            currentPosition = savedInstanceState.getLong(Constants.CURRENT_POSITION);
            hasURL = savedInstanceState.getBoolean(Constants.HAS_URL);
            hasThumbnailUrl = savedInstanceState.getBoolean(Constants.HAS_THUMBNAIL_URL);
            videoURL = savedInstanceState.getString(Constants.VIDEO_URL);
            thumbnailUrl = savedInstanceState.getString(Constants.THUMBNAIL_URL);
            playwhenready = savedInstanceState.getBoolean(Constants.PLAY_WHEN_READY);

            initializePlayer();
        }
        else if(intent!=null){
            Log.d(TAG, "onCreateView: Right here with Intent");
            playwhenready = true;
            stepIndex = intent.getIntExtra(Constants.STEP_INDEX,0);
            stepsList = intent.getParcelableArrayListExtra(Constants.STEPS_LIST);

            checkAndInitializePlayer(stepIndex,stepsList);

        }

        inflateViewExceptPlayer();

        initializeMediaSession();

        giveButtonsFunctionality();

        return fragLayout.getRoot();
    }

    private void checkAndInitializePlayer(int stepIndex, ArrayList<StepsObject> stepsList) {
        hasURL = (!stepsList.get(stepIndex).getVideoUrl().isEmpty());
        hasThumbnailUrl = (!stepsList.get(stepIndex).getThumbnailUrl().isEmpty());

        if(hasURL){
            videoURL = stepsList.get(stepIndex).getVideoUrl();
            initializePlayer();
        }
        else if(hasThumbnailUrl){
            thumbnailUrl = stepsList.get(stepIndex).getThumbnailUrl();
            initializePlayer();
        }
        else {
            flPlayerView.setVisibility(View.GONE);
            noVideo.setVisibility(View.VISIBLE);
        }
    }

    private void giveButtonsFunctionality() {
            nextButton.setOnClickListener(v ->
            {
                if(stepIndex<stepsList.size()){
                    replaceFragment(++stepIndex);
                }
            });

            prevButton.setOnClickListener(v -> {
                if(stepIndex>-1){
                    replaceFragment(--stepIndex);
                }
            });
    }

    private void replaceFragment(int index) {
        try{
            Log.d(TAG, "replaceFragment: Replacing Index --> " + index);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Constants.STEPS_LIST,stepsList);
            bundle.putInt(Constants.STEP_INDEX, index);

            StepDetailFragment replacingFragment = new StepDetailFragment();
            replacingFragment.setArguments(bundle);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.f_container,replacingFragment)
                    .commit();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(Objects.requireNonNull(getContext()),TAG);

        // Enable callbacks from MediaButtons and TransportControls
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible
        mediaSession.setMediaButtonReceiver(null);

        stateBuilder =  new PlaybackStateCompat.Builder()
                            .setActions( PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_PAUSE |
                                    PlaybackStateCompat.ACTION_REWIND |
                                    PlaybackStateCompat.ACTION_FAST_FORWARD |
                                    PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());

        mediaSession.setCallback(new MySessionCallback());

        mediaSession.setActive(true);

    }

    private void inflateViewExceptPlayer() {
        StepsObject currentStep = stepsList.get(stepIndex);
        shortDesc.setText(currentStep.getShortDesc());
        longDesc.setText(currentStep.getDesc());
        if(stepIndex==stepsList.size()-1){
            nextButton.setVisibility(View.GONE);
        }
        if(stepIndex==0){
            prevButton.setVisibility(View.GONE);
        }
    }

    private void initializePlayer(){
        if(player==null && (hasURL|| hasThumbnailUrl)){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(getContext());
            player = ExoPlayerFactory.newSimpleInstance(getContext(),defaultRenderersFactory,trackSelector,loadControl);

            playerView.setPlayer(player);
            player.setPlayWhenReady(playwhenready);

            Uri mediaUri = null;

            if(hasURL) {
                mediaUri = Uri.parse(videoURL);
            }
            else if (hasThumbnailUrl){
                mediaUri = Uri.parse(thumbnailUrl);
            }
            boolean haveStartPosition = currentWindow != C.INDEX_UNSET;
            if (haveStartPosition) {
                player.seekTo(currentWindow, currentPosition);
            }
            String UserAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(UserAgent))
                    .createMediaSource(mediaUri);

            player.prepare(mediaSource, !haveStartPosition,false);
            player.addListener(this);
        }
        else{
            flPlayerView.setVisibility(View.GONE);
            noVideo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    player.getCurrentPosition(), PLAYER_PLAYBACK_SPEED);
        } else if (playbackState == Player.STATE_READY) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    player.getCurrentPosition(), PLAYER_PLAYBACK_SPEED);
        } else if (playbackState == Player.STATE_ENDED) {
        }
        playwhenready = playWhenReady;

        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: Saving the state");
        outState.putParcelableArrayList(Constants.STEPS_LIST,stepsList);
        outState.putInt(Constants.STEP_INDEX,stepIndex);

        updateCurrentPosition();

        outState.putInt(Constants.CURRENT_WINDOW, currentWindow);
        outState.putLong(Constants.CURRENT_POSITION, currentPosition);

            outState.putBoolean(Constants.HAS_THUMBNAIL_URL, hasThumbnailUrl);
            outState.putString(Constants.THUMBNAIL_URL, thumbnailUrl);
            outState.putBoolean(Constants.HAS_URL, hasURL);
            outState.putString(Constants.VIDEO_URL, videoURL);
        outState.putBoolean(Constants.PLAY_WHEN_READY, playwhenready);

        super.onSaveInstanceState(outState);
    }

    private void updateCurrentPosition() {
        if(player!=null){
            currentPosition = player.getCurrentPosition();
            Log.d(TAG, "updateCurrentPosition: " + currentPosition);
            currentWindow = player.getCurrentWindowIndex();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void releasePlayer(){
        if(player!=null){
            player.stop();
            player.release();
            player = null;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        updateCurrentPosition();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaSession.setActive(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        fullscreenPlayer();
    }

    private void fullscreenPlayer() {
        if(isSinglePaneLandscape() && (hasURL || hasThumbnailUrl)){
            Log.d(TAG, "fullscreenPlayer: Fulling the screen");
            int flagFullScreen = View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

            playerView.setSystemUiVisibility(flagFullScreen);
//            nextButton.setVisibility(View.GONE);
//            prevButton.setVisibility(View.GONE);
            shortDesc.setVisibility(View.GONE);
            longDesc.setVisibility(View.GONE);

        }
    }

    private boolean isSinglePaneLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !isTabletMode();
    }

    private boolean isTabletMode() {
        // TODO : Update this method
        return false;
    }

    private class MySessionCallback extends MediaSessionCompat.Callback{
        @Override
        public void onPlay() {
            player.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            player.setPlayWhenReady(false);
        }

        @Override
        public void onRewind() {
            player.seekTo(Math.max(player.getCurrentPosition() - Constants.REWIND_INCREMENT, Constants.START_POSITION));
        }

        @Override
        public void onFastForward() {
            long duration = player.getDuration();
            player.seekTo(Math.min(player.getCurrentPosition() + Constants.FAST_FORWARD_INCREMENT, duration));
        }
    }
}
