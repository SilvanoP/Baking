package br.com.udacity.android.baking.ui.recipe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import br.com.udacity.android.baking.R;
import br.com.udacity.android.baking.data.Step;
import br.com.udacity.android.baking.data.source.RecipesDataSource;
import br.com.udacity.android.baking.data.source.RecipesRepository;
import br.com.udacity.android.baking.util.Inject;

public class StepFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_id";
    private static final String ARG_STEP_ID = "step_id";
    private static final String STATE_EXO_PLAYER_LAST_POSITION = "state_ep_last_position";
    private static final String STATE_EXO_PLAYER_IS_PLAYING = "state_ep_is_playing";

    private Step mStep;
    private TextView mDescription;
    private View mNoVideoMessage;

    // ExoPlayer variables
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private boolean mExoPlayerIsPlaying = false;
    private long mExoPlayerLastPosition = C.POSITION_UNSET;

    Target mThumbnailTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mSimpleExoPlayerView.setDefaultArtwork(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    public StepFragment() {
    }

    public static StepFragment newInstance(int recipeId, int stepId) {
        StepFragment fragment = new StepFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        args.putInt(ARG_STEP_ID, stepId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mExoPlayerIsPlaying = savedInstanceState.getBoolean(STATE_EXO_PLAYER_IS_PLAYING);
            mExoPlayerLastPosition = savedInstanceState.getLong(STATE_EXO_PLAYER_LAST_POSITION);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(STATE_EXO_PLAYER_IS_PLAYING, mExoPlayerIsPlaying);
        outState.putLong(STATE_EXO_PLAYER_LAST_POSITION, mExoPlayerLastPosition);
        super.onSaveInstanceState(outState);
    }

    private void loadStep(int recipeId, int stepId) {
        final RecipesRepository mRepository = Inject.getRecipeRepository(getContext());
        mRepository.getStep(recipeId, stepId, new RecipesDataSource.LoadStepCallback() {
            @Override
            public void onStepLoaded(Step step) {
                mStep = step;
                if (mDescription != null) {
                    mDescription.setText(mStep.getDescription());
                }
                if (!TextUtils.isEmpty(mStep.getThumbnailURL())) {
                    Picasso.with(getContext())
                            .load(mStep.getThumbnailURL())
                            .into(mThumbnailTarget);
                }
                loadVideo();
            }

            @Override
            public void onDataNotAvailable() {
                Snackbar.make(getView(), getString(R.string.internal_error), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        mDescription = rootView.findViewById(R.id.step_frag_description_textview);
        mNoVideoMessage = rootView.findViewById(R.id.step_frag_novideo_textview);
        mSimpleExoPlayerView = rootView.findViewById(R.id.step_frag_simpleexoplayerview);

        int recipeId = getArguments().getInt(ARG_RECIPE_ID, -1);
        int stepId = getArguments().getInt(ARG_STEP_ID);
        loadStep(recipeId, stepId);

        return rootView;
    }

    private void loadVideo() {
        // step not loaded. Nothing to do.
        if (mStep == null) {
            return;
        }
        // There is no video for this step. Just show a message.
        if (TextUtils.isEmpty(mStep.getVideoURL())) {
            mNoVideoMessage.setVisibility(View.VISIBLE);
            return;
        }

        mNoVideoMessage.setVisibility(View.GONE);

        Uri videoUri = Uri.parse(mStep.getVideoURL());
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the mExoPlayer
        mExoPlayer =
                ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        // Bind the mExoPlayer to the view.
        mSimpleExoPlayerView.setPlayer(mExoPlayer);


        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "yourApplicationName"), null);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoUri);
        // Prepare the mExoPlayer with the source.
        mExoPlayer.prepare(videoSource);

        // Retrieve last state
        mExoPlayer.setPlayWhenReady(mExoPlayerIsPlaying);
        mExoPlayer.seekTo(mExoPlayerLastPosition);
        mSimpleExoPlayerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                if (mCallback != null) {
                    mCallback.onPlayerControlesVisibilityChanged(visibility == View.VISIBLE);
                }
            }
        });
    }

    OnPlayerControlsVisibilityChangeListener mCallback;

    // Container Activity must implement this interface
    public interface OnPlayerControlsVisibilityChangeListener {
        void onPlayerControlesVisibilityChanged(boolean isFullscreen);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPlayerControlsVisibilityChangeListener) {
            mCallback = (OnPlayerControlsVisibilityChangeListener) context;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            loadVideo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) {
            loadVideo();
        }
        mSimpleExoPlayerView.showController();
        if (mCallback != null) {
            mCallback.onPlayerControlesVisibilityChanged(true);
        }
    }

    /**
     * This method is only used by viewpager because the viewpager doesn't call onPause after
     * changing the fragment
     */
    public void losingVisibility() {
        String desc = mStep != null ? mStep.getShortDescription() : "mStep == null";
        Log.e("SPRENGEL", String.format("losingVisibility: %s", desc));
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        savePlayerState();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    public void releasePlayer() {
        if (mExoPlayer != null) {
            // Pause the mExoPlayer
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void savePlayerState() {
        if (mExoPlayer == null) {
            return;
        }
        // Save ExoPlayer state
        // Store off if we were playing so we know if we should start when we're foregrounded again.
        mExoPlayerIsPlaying = mExoPlayer.getPlayWhenReady();
        // Store off the last position our mExoPlayer was in before we paused it.
        mExoPlayerLastPosition = mExoPlayer.getCurrentPosition();
    }

}
