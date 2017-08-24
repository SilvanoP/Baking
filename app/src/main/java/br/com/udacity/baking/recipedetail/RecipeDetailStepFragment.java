package br.com.udacity.baking.recipedetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import br.com.udacity.baking.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailStepFragment extends Fragment {

    @BindView(R.id.step_video_exo_player)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.step_no_video_image)
    ImageView mNoVideoImageView;
    @BindView(R.id.step_instruction_short_text)
    TextView mInstructionShortTextView;
    @BindView(R.id.step_instruction_description_text)
    TextView mInstructionDescriptionTextView;
    @BindView(R.id.previous_button)
    Button mPreviousButton;
    @BindView(R.id.next_button)
    Button mNextButton;

    private RecipeStep mStep;
    private StepsCallback mCallback;
    private int mStepIndex;
    private SimpleExoPlayer mPlayer;
    private boolean mPlayWhenReady;
    private String mVideoUrl;
    private boolean mShowVideo;

    interface StepsCallback {
        void goToVideo(int stepNumber);
    }

    public RecipeDetailStepFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailStepFragment newInstance() {
        Bundle args = new Bundle();
        RecipeDetailStepFragment fragment = new RecipeDetailStepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_detail_step, container, false);
        ButterKnife.bind(this, view);

        if (mStep != null) {
            mPlayWhenReady = true;
            mInstructionShortTextView.setText(mStep.getShortDescription());
            mInstructionDescriptionTextView.setText(mStep.getDescription());

            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.goToVideo(mStepIndex+1);
                }
            });
            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.goToVideo(mStepIndex - 1);
                }
            });

            verifyVideoUrl();
            initializePlayer();
        }

        return view;
    }

    private void verifyVideoUrl() {
        mVideoUrl = mStep.getVideoURL();
        mShowVideo = true;
        if (TextUtils.isEmpty(mVideoUrl)) {
            mVideoUrl = mStep.getThumbnailURL();
            if (TextUtils.isEmpty(mVideoUrl)) {
                mExoPlayerView.setVisibility(View.GONE);
                mNoVideoImageView.setVisibility(View.VISIBLE);
                mShowVideo = false;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    public void initializePlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

        mExoPlayerView.setPlayer(mPlayer);

        mPlayer.setPlayWhenReady(mPlayWhenReady);

        Uri uri = Uri.parse(mVideoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        mPlayer.prepare(mediaSource, true, false);
    }

    public void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void setAttributes(RecipeStep step, int index, StepsCallback callback) {
        mStep = step;
        mStepIndex = index;
        mCallback = callback;

        Log.i(this.getClass().getSimpleName(), "Step index:" + index );
    }
}
