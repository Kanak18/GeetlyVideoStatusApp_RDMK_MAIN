package geetly.lyrical.videostatusmaker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import android.widget.RelativeLayout;

import android.widget.Toast;


import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DefaultDataSourceFactory;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.datasource.cache.SimpleCache;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import geetly.lyrical.videostatusmaker.MyApplication;
import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.adapter.NvHomeAdapter;
import geetly.lyrical.videostatusmaker.model.VideoviewModel;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.OkHttpDownloader;
import com.coolerfall.download.Priority;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


import geetly.lyrical.videostatusmaker.utils.Utils_VideoDownload;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;
import okhttp3.OkHttpClient;

import static geetly.lyrical.videostatusmaker.MyApplication.unlocked_premium_template_array;


public class NV_VideoListActivity extends AppCompatActivity implements Player.Listener, MaxRewardedAdListener {



    int page = 1;
    ArrayList<VideoviewModel> videoviewdata = new ArrayList<>();
    int currentPage = -1;
    LinearLayoutManager layoutManager;
    RelativeLayout pBar;
    ExoPlayer exoPlayer;
    NvHomeAdapter adapter;
    Dialog dialog,dialog_premium;
    private Activity context;
    private boolean isVisibleToUser = true;
    private SimpleCache simpleCache;
    //private CacheDataSourceFactory cacheDataSourceFactory;
    private CacheDataSource.Factory cacheDataSourceFactory;

    private RecyclerView recyclerView;
    private ImageView llBack;
    private DownloadManager downloadManager;
    private int position = 0;
    private int fileDownloadingId = 0;
    private NativeAd nativeAd;
    private ProgressBar progressBar;

    String title;
    String video_id;
    String video_thumb;
    String video_link;
    String video_zip;
    String video_category;
    int video_is_premium;
    private AdView adView;


    private FrameLayout ll_fbbanner;


    private RewardedAd mRewardedAd;
    private final String TAG = "Nv_VideoListActivity";
    private MaxRewardedAd rewardedAd;
    public static int unlocked_premium_template = 0;


    private FrameLayout loaderOverlay;



    //int[] unlocked_premium_template_array; // array for three ints

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyAppUtils.setStatusBarTransparentFlag(this);
        setContentView(R.layout.activity_video);


        loaderOverlay = findViewById(R.id.loading_overlay); // 👈 add this
        if (loaderOverlay != null) {
            loaderOverlay.setVisibility(View.VISIBLE); // Show overlay initially
        }

        context = this;
        initView();
        llBack.setOnClickListener(v -> onBackPressed());

        ll_fbbanner =  findViewById(R.id.ll_fbbanner);



        setupDialog();
        unlockPremiumDialog();

        if(MyApplication.show_reward > 0) {
            loadRewardADs();
        }


        Intent intent = getIntent();

        if (intent != null) {


            if(intent.hasExtra("title")) {

                position = intent.getIntExtra("position", 0);
                video_id = intent.getStringExtra("video_id");
                title = intent.getStringExtra("title");

                video_category = intent.getStringExtra("video_category");

                video_link = intent.getStringExtra("video_link");
                video_zip = intent.getStringExtra("video_zip");
                video_thumb = intent.getStringExtra("video_thumb");
                video_is_premium = intent.getIntExtra("video_is_premium", 0);

                Log.i("OneSignal:", "Params :: video_id => " + video_id + " Title => " + title + " Category => " + video_category + " Video Zip => " + video_zip);

                VideoviewModel headingModel = new VideoviewModel();

                headingModel.setId(video_id);
                headingModel.setTitle(title);
                headingModel.setCategory(video_category);
                headingModel.setVideo_link(video_link);
                headingModel.setVideo_zip(video_zip);
                headingModel.setVideoThumb(video_thumb);
                headingModel.setIsPremium(video_is_premium);
                videoviewdata.add(0, headingModel);
                Log.i("OneSignal:", "Intent called => " + videoviewdata.get(position).getCategory());

            }
            else {
                videoviewdata = (ArrayList<VideoviewModel>) intent.getSerializableExtra("mdata");
                position = intent.getIntExtra("position", 0);

                video_category = videoviewdata.get(position).getCategory();

                VideoviewModel headingModel = new VideoviewModel();

                headingModel.setId(videoviewdata.get(position).getId());
                headingModel.setTitle(videoviewdata.get(position).getTitle());
                headingModel.setCategory(videoviewdata.get(position).getCategory());
                headingModel.setVideo_link(videoviewdata.get(position).getVideo_link());
                headingModel.setVideo_zip(videoviewdata.get(position).getVideo_zip());
                headingModel.setVideoThumb(videoviewdata.get(position).getVideoThumb());
                headingModel.setIsPremium(videoviewdata.get(position).getIsPremium());
                videoviewdata.clear();
                videoviewdata.add(0, headingModel);
                position = 0;
                Log.i("OneSignal:","Intent called Is Premium => " +video_category);
            }
        }

        layoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);


        recyclerView.scrollToPosition(position);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //here we find the current item number
                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                int pageNo = scrollOffset / height;

                if (pageNo != currentPage) {
                    currentPage = pageNo;
                    releasePriviousPlayer();
                    setPlayer(currentPage);

                }
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                boolean endHasBeenReached = lastVisible >= totalItemCount;
                if (totalItemCount > 0 && endHasBeenReached) {
                }


            }
        });



        setAdapter();

        MyAppUtils.showBannerAds(this,ll_fbbanner);

    }

    private void initView() {
        llBack = findViewById(R.id.iv_close);
        recyclerView = findViewById(R.id.recyclerview);

        pBar = findViewById(R.id.progressbar_rl);

        recyclerView = findViewById(R.id.recyclerview);

    }

    public void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }

    public void unlockPremiumDialog() {
        dialog_premium = new Dialog(this);

        this.dialog_premium.requestWindowFeature(1);
        dialog_premium.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_premium.setContentView(R.layout.dialog_unlock_premium_template);
        dialog_premium.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog_premium.setCanceledOnTouchOutside(false);
        dialog_premium.setCancelable(false);
        //progressBar = dialog2.findViewById(R.id.progress_download_video);
        Window window = dialog_premium.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView btnNo = dialog_premium.findViewById(R.id.btnNo);
        ImageView btYes = dialog_premium.findViewById(R.id.btnYes);

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(NV_VideoListActivity.this,"Load reward ads",Toast.LENGTH_LONG).show();
                showRewardADs();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_premium.dismiss();
            }
        });
    }

    public  void loadRewardADs() {

        if (MyApplication.ad_network.equals("admob")) {
            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {

                    };

            RewardedAd.load(
                    this, MyApplication.reward_ad_id,
                    new AdRequest.Builder().build(),
                    new RewardedAdLoadCallback() {
                        public void onAdLoaded(RewardedAd ad) {
                            mRewardedAd = ad;
                            mRewardedAd.setFullScreenContentCallback(fullScreenContentCallback);
                        }
                    });
        }
        else if (MyApplication.ad_network.equals("applovins")) {
            rewardedAd = MaxRewardedAd.getInstance(MyApplication.reward_ad_id,this);
            rewardedAd.setListener(this);
            rewardedAd.loadAd();
        }

    }
    public  void showRewardADs() {

        if (MyApplication.ad_network.equals("admob")) {
            if (mRewardedAd != null) {
                mRewardedAd.show(
                        this, rewardItem -> {
                            //REWARD EARN AND NEED TO REMOVE WATER MARK
                            Toast.makeText(NV_VideoListActivity.this,"You have successfully unlock premium template.",Toast.LENGTH_LONG).show();
                            unlocked_premium_template_array[unlocked_premium_template] = 1;
                            dialog_premium.dismiss();
                            loadRewardADs();
                        });
            }

        }
        else if (MyApplication.ad_network.equals("applovins")) {
            if(rewardedAd.isReady())
            {
                rewardedAd.showAd();
            }
        }

    }


    public void setupDialog() {
        dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(1);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_download_file);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(false);
        progressBar = dialog.findViewById(R.id.progress_download_video);

        if(MyApplication.show_native > 0) {
            //loadAdmobNativeAd((FrameLayout) dialog.findViewById(R.id.fl_adplaceholder), (NativeAdLayout) dialog.findViewById(unified));
            MyAppUtils.loadAdmobNativeAd((FrameLayout) dialog.findViewById(R.id.fl_adplaceholder),context);
        }

        this.progressBar.setProgress(0);



        CardView cardViewDownload = dialog.findViewById(R.id.ll_cancel_download);

        cardViewDownload.setOnClickListener(view -> {


            if (dialog.isShowing()) dialog.dismiss();


            if (downloadManager != null) {
                downloadManager.cancel(fileDownloadingId);

            }

            File filePath = new File(getDataDir(), videoviewdata.get(position).getTitle());
            String path = filePath.getAbsolutePath();
            File file = new File(path);
            deleteRecursive(file);


        });
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }



    public void setAdapter() {

        adapter = new NvHomeAdapter(context, videoviewdata, (postion, item, view) -> {

            switch (view.getId()) {
                case R.id.useNow:
                    if (MyAppUtils.isConnectingToInternet(context)) {
                        File filePath = new File(getDataDir(), videoviewdata.get(postion).getTitle());


                        //unlocked_premium_template_array[postion] = 0;
                        unlocked_premium_template = Integer.parseInt(videoviewdata.get(postion).getId());
                        //Toast.makeText(context, "FILE PATH => "+filePath, Toast.LENGTH_SHORT).show();

                        if(videoviewdata.get(postion).getIsPremium() > 0 )
                        {
                            //Log.e("DEBUG PREMIUM RESULT IF: POSISTION " + unlocked_premium_template , " Value Stored => "+  String.valueOf(unlocked_premium_template_array[unlocked_premium_template]));

                            String videoDownload = videoviewdata.get(postion).getVideo_zip();
                            if (filePath.exists()) {
                                //Toast.makeText(context, "FILE EXISTED => "+filePath, Toast.LENGTH_SHORT).show();

                                File checkJsonFile = new File(filePath + "/python.json");
                                if(checkJsonFile.exists()) {
                                    Intent intent = new Intent(context, NV_VideoEditorActivity.class);
                                    intent.putExtra("filepath", filePath.getAbsolutePath());
                                    startActivity(intent);
                                }
                                else
                                {
                                    if(unlocked_premium_template_array[unlocked_premium_template]==0) {
                                        dialog_premium.show();
                                    }
                                    else
                                    {
                                        //Log.e("DEBUG PREMIUM RESULT IF 426: ", String.valueOf(unlocked_premium_template_array[unlocked_premium_template]));
                                        downloadFile(videoDownload, filePath.getAbsolutePath() + "/" + videoviewdata.get(postion).getTitle() + ".zip");
                                    }

                                }
                            } else {
                                if(unlocked_premium_template_array[unlocked_premium_template]==0) {
                                    dialog_premium.show();
                                }
                                else
                                {
                                    // Log.e("DEBUG PREMIUM RESULT IF 437: ", String.valueOf(unlocked_premium_template_array[unlocked_premium_template]));
                                    filePath.mkdirs();
                                    downloadFile(videoDownload, filePath.getAbsolutePath() + "/" + videoviewdata.get(postion).getTitle() + ".zip");
                                }
                            }
                        }
                        else
                        {
                            // Log.e("DEBUG PREMIUM RESULT ELSE 445: ", String.valueOf(unlocked_premium_template_array[unlocked_premium_template]));


                            String videoDownload = videoviewdata.get(postion).getVideo_zip();
                            if (filePath.exists()) {
                                //Toast.makeText(context, "FILE EXISTED => "+filePath, Toast.LENGTH_SHORT).show();

                                File checkJsonFile = new File(filePath + "/python.json");
                                if(checkJsonFile.exists()) {
                                    Intent intent = new Intent(context, NV_VideoEditorActivity.class);
                                    intent.putExtra("filepath", filePath.getAbsolutePath());
                                    startActivity(intent);
                                }
                                else
                                {
                                    // Log.d("30092022","JSON FILE MISSING");
                                    downloadFile(videoDownload, filePath.getAbsolutePath() + "/" + videoviewdata.get(postion).getTitle() + ".zip");
                                }
                            } else {
                                filePath.mkdirs();
                                downloadFile(videoDownload, filePath.getAbsolutePath() + "/" + videoviewdata.get(postion).getTitle() + ".zip");
                            }
                        }


                    } else {
                        MyAppUtils.setToast(context, "Please Connect to Internet.");
                    }
                    break;
                case R.id.download:
                    String fileName = URLUtil.guessFileName(item.getVideo_link(), "", "video/*");
                    String location = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + MyAppUtils.FOLDER_NAME + "/" + fileName;
                    if (new File(location).exists()) {
                        Toast.makeText(context, "Already Downloaded", Toast.LENGTH_SHORT).show();
                    } else {
                        new Utils_VideoDownload(context, item.getVideo_link(), 0, 0, postion);
                    }
                    break;
                case R.id.share:
                    new Utils_VideoDownload(context, item.getVideo_link(), 1, 1, postion);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + view.getId());
            }

        });

        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

    }



    public void downloadFile(String downloadUrl, String stringPath) {
        try {
            if (!this.dialog.isShowing())
                this.dialog.show();
            fileDownloadingId = downloadManager.add(new DownloadRequest.Builder().url(downloadUrl).retryTime(5).retryInterval(2, TimeUnit.SECONDS).progressInterval(1, TimeUnit.SECONDS).priority(Priority.HIGH).allowedNetworkTypes(1).destinationFilePath(stringPath).downloadCallback(new DownloadVideoCall()).build());


        } catch (Exception e) {
            e.getMessage();

        }
    }

    public void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory.getParentFile(), ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();


                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }

            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            zipFile.delete();
            Intent intent = new Intent(context, NV_VideoEditorActivity.class);
            intent.putExtra("filepath", targetDirectory.getAbsolutePath());
            startActivity(intent);

        } finally {
            zis.close();
        }
    }

    @UnstableApi
    public void setPlayer(final int currentPage) {
        if (context != null && videoviewdata.get(currentPage) != null) {
            final VideoviewModel item = videoviewdata.get(currentPage);

            // Initialize ExoPlayer
            final ExoPlayer player = new ExoPlayer.Builder(context).build();

            // Get the layout for the current page
            View layout = layoutManager.findViewByPosition(currentPage);
            if (layout == null) return;

            final PlayerView playerView = layout.findViewById(R.id.player_view);
            playerView.setPlayer(player);

            // SimpleCache for caching videos
            //SimpleCache simpleCache = MyApplication.simpleCache;

            // CacheDataSource.Factory for caching video data
            CacheDataSource.Factory cacheDataSourceFactory = new CacheDataSource.Factory()
                    .setCache(simpleCache)
                    .setUpstreamDataSourceFactory(new DefaultDataSourceFactory(context, "MyVideoMakerApplication"))
                    .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);

            // Create MediaItem from video link
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(item.getVideo_link())
                    .build();

            // Set the MediaItem to the player
            player.setMediaItem(mediaItem);
            player.prepare(); // Prepare the player
            player.setPlayWhenReady(true); // Start playback when ready
            player.setRepeatMode(Player.REPEAT_MODE_ALL);

            player.addListener(new Player.Listener() {
                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    if (playbackState == Player.STATE_READY) {
                        if (loaderOverlay != null) {
                            loaderOverlay.setVisibility(View.GONE); // ✅ Hide loader
                        }
                        player.setPlayWhenReady(true); // ✅ Auto-play now that it's ready
                    }
                }
            });


            try {
                MediaPlayer mp = new MediaPlayer();
                mp.setDataSource(context, Uri.parse(item.getVideo_link()));
                mp.prepare();
                int height = mp.getVideoHeight();

                if (height >= 600) {
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                } else {
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
                }
                mp.release();
            } catch (Exception e) {
                e.printStackTrace();
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            }

            // Set touch listener for gestures
            playerView.setOnTouchListener(new View.OnTouchListener() {
                private final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        float deltaY = e1.getX() - e2.getX();
                        float deltaYAbs = Math.abs(deltaY);
                        if ((deltaYAbs > 100) && (deltaYAbs < 1000)) {
                            if (deltaY > 0) {
                                onBackPressed();
                            }
                        }
                        return true;
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        if (!player.getPlayWhenReady()) {
                            player.setPlayWhenReady(true);
                        } else {
                            new Handler(getMainLooper()).postDelayed(() -> player.setPlayWhenReady(false), 200);
                        }
                        return true;
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        if (!player.getPlayWhenReady()) {
                            player.setPlayWhenReady(true);
                        }
                        return true;
                    }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });

            exoPlayer = player; // Assign the player instance to the global variable, if needed.
        }
    }

    public void releasePriviousPlayer() {
        if (exoPlayer != null) {
            exoPlayer.removeListener(this);
            exoPlayer.release();
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        // Log.e("ExpoDebug","Video is being changed");
        if (isLoading) {
            pBar.setVisibility(View.VISIBLE);

        } else {
            pBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == Player.STATE_BUFFERING) {
            pBar.setVisibility(View.VISIBLE);
        } else if (playbackState == Player.STATE_READY) {
            pBar.setVisibility(View.GONE);
        }


    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onRewardedVideoStarted(MaxAd ad) {

    }

    @Override
    public void onRewardedVideoCompleted(MaxAd ad) {
        unlocked_premium_template_array[unlocked_premium_template] = 1;
        Toast.makeText(NV_VideoListActivity.this,"You have successfully unlock premium template.",Toast.LENGTH_LONG).show();
        loadRewardADs();
        rewardedAd = null;
        dialog_premium.dismiss();
    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {

    }

    @Override
    public void onAdLoaded(MaxAd ad) {

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {

    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {

    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

    }

    class DownloadVideoCall implements DownloadCallback {
        @Override
        public void onFailure(int i, int i2, String str) {

            Toast.makeText(context, " Failed Download " + str, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProgress(int i, long j, long j2) {
            j = (j * 100) / j2;
            if (j != 100) {
                progressBar.setProgress((int) j);

                Log.d("progress", "" + (int) j);
            }
        }

        @Override
        public void onRetry(int i) {
        }

        @Override
        public void onStart(int i, long j) {
        }

        @Override
        @SuppressLint({"WrongConstant"})
        public void onSuccess(int i, String str) {
            try {
                unzip(new File(str), new File(str).getParentFile());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (progressBar != null && progressBar.isAnimating()) {

                Toast.makeText(context, "You can't go Back, Please wait to get back.", Toast.LENGTH_SHORT).show();

            } else {
                // Toast.makeText(context, MyApplication.page_from + " Category "+ video_category, Toast.LENGTH_SHORT).show();

                Intent i;
                switch (MyApplication.page_from) {
                    case "MainPage":
                        i = new Intent(this, NV_LibraryActivity.class);
                        startActivity(i);
                        finishAffinity();
                        finish();
                        break;
                    case "SearchPage":
                        i = new Intent(this, NV_SearchVideo.class);
                        i.putExtra("search_text", MyApplication.search_text);
                        startActivity(i);
                        finishAffinity();
                        finish();
                        break;
                    default:
                        i = new Intent(this, NV_CategoryListActivity.class);
                        i.putExtra("video_category", video_category);
                        startActivity(i);
                        finishAffinity();
                        finish();
                        break;
                }

                super.onBackPressed();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        if ((exoPlayer != null)) {
            exoPlayer.setPlayWhenReady(true);
        }

        if (adView != null) {
            adView.resume();
        }

        try {
            if (this.dialog != null && !this.dialog.isShowing()) {
                downloadManager = new DownloadManager.Builder().context(this).downloader(OkHttpDownloader.create(new OkHttpClient.Builder().build())).threadPoolSize(3).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            exoPlayer.release(); // Release resources
            exoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (exoPlayer != null)
            exoPlayer.setPlayWhenReady(false);
        if (adView != null) {
            adView.pause();
        }
    }


    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePriviousPlayer();

        if (adView != null) {
            adView.destroy();
        }


    }

}
