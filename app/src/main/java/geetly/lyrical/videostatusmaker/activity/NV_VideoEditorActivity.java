package geetly.lyrical.videostatusmaker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.app.PictureInPictureParams;
import android.util.Rational;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor;
import androidx.media3.datasource.cache.SimpleCache;
import androidx.media3.exoplayer.DefaultRenderersFactory;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import geetly.lyrical.videostatusmaker.MyApplication;
import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.adapter.NvImageListAdapter;
import geetly.lyrical.videostatusmaker.utils.Utils;
import geetly.lyrical.videostatusmaker.interfaces.onclick;
import geetly.lyrical.videostatusmaker.model.VideoviewModel;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;


import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegKitConfig;
import com.arthenica.ffmpegkit.FFmpegSession;
import com.arthenica.ffmpegkit.ReturnCode;

import com.foysaldev.cropper.CropImage;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnUserEarnedRewardListener;


import com.google.android.gms.ads.nativead.NativeAd;



import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;

import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import life.knowledge4.videotrimmer.utils.FileUtils;
import geetly.lyrical.videostatusmaker.utils.Utils_Admob;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;




public  class  NV_VideoEditorActivity extends AppCompatActivity implements MaxRewardedAdListener {
    public static final int REQUEST_PICK = 9162;
    public static boolean flagVideo = false;
    public static String resizedVideoPathFinal;
    JSONObject jsonObj;
    int i;
    private Dialog dialog,dialog2;
    private RelativeLayout relativeLayout;
    private String url;
    private Handler handler;
    private Runnable runnable;
    private String mathc;
    private long K;
    private Utils_Admob.MyListener myListener;
    private String[] cmd;
    private CardView saveVideo;
    private int totalImage;
    private TextView btnTryAgain;
    private String imageList;
    private String ratio;
    private String downloadFileName;
    private String ffCmd;
    private PlayerView exo_playerview;
    private String ffCmdVideo;
    private String ffUser;
    private String videoResolution;
    private String duration;
    private String colorKayRandom;
    private String opVideo;
    private LinearLayout layoutTryAgain;
    private String picPath;
    private NativeAd nativeAd;
    private int o = 0;
    private applyFiler applyFiler;
    private ProgressBar progressBarExoplayer;
    private RecyclerView rv_numberimg;
    private NvImageListAdapter nvImageListAdapter;
    @SuppressLint("UnsafeOptInUsageError")
    private SimpleExoPlayer simpleExoPlayer;
    private ExoPlayer exoPlayer;
    private ProgressBar progressBar;
    private String[] totalImages;
    private CardView cardView1;
    private CardView cardVideo;
    private TextView cardTextImage;
    private TextView cardTxtVideo;
    private ImageView thumb;
    private RelativeLayout rv_video_list;
    private String waterMarkPath,WaterMarkPathBlank;
    private ArrayList<String> finalCommand;
    private String filesPath;
    private Activity context;
    private Animation animation;
    private String[] listImages;
    private AdView adView;
    private FrameLayout ll_fbbanner;

    public static int is_reward_ads_shown = 0;
    private RewardedAd mRewardedAd;
    private final String TAG = "Nv_VideoEditorActivity";

    private MaxRewardedAd rewardedAd;
    private int retryAttemept;

    boolean isLoading;


    private int totalText;
    public String[] totalTexts,JsonInputLables,JsonInputValues;
    private CardView card_text_view;
    public static String ratio_image;
    public int has_text_field = 0;

    @Override
    @SuppressLint({"ClickableViewAccessibility", "IntentReset"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_editor);
        context = this;

        exo_playerview =  findViewById(R.id.exo_player);
        layoutTryAgain =  findViewById(R.id.layout_try_again);
        progressBarExoplayer =  findViewById(R.id.progressBar_exoplayer);
        btnTryAgain =  findViewById(R.id.btn_try_again);
        rv_video_list =  findViewById(R.id.rv_video_list);
        thumb =  findViewById(R.id.view_thumb);
        cardView1 =  findViewById(R.id.card_images);
        cardVideo =  findViewById(R.id.card_video);
        cardTextImage =  findViewById(R.id.card_text_img);
        cardTxtVideo =  findViewById(R.id.card_txet_video);
        saveVideo =  findViewById(R.id.save_video);

        ll_fbbanner =  findViewById(R.id.ll_fbbanner);
        rv_numberimg =  findViewById(R.id.rv_numberimg);

        setRemoveWaterMarkDialog();



        ImageView rmwatermarkimg = (ImageView) findViewById(R.id.removeWaterMark);

        if(MyApplication.show_reward > 0) {
            rmwatermarkimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog2.show();
                }
            });
            loadRewardADs();
        }



        dialog = new Dialog(this);
        filesPath = getIntent().getStringExtra("filepath");

        // Copy  watermark to external storage
        try {
            copyRAWtoSDCard(R.raw.watermark, getDataDir() + "/.tempUV", "watermark.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
        waterMarkPath = getDataDir() + "/.tempUV/watermark.gif";


        try {
            copyRAWtoSDCard(R.raw.watermarkno, getDataDir() + "/.tempUV", "watermarkno.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
        WaterMarkPathBlank = getDataDir() + "/.tempUV/watermarkno.gif";




        Log.e("WaterMakeFile 1:",waterMarkPath);

        finalCommand = new ArrayList<>();
        File yourFile = new File(filesPath + "/python.json");
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(yourFile);

            String jsonStr = null;
            try {

                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                jsonStr = Charset.defaultCharset().decode(bb).toString();
                jsonObj = new JSONObject(jsonStr);
                totalImage = jsonObj.getJSONArray("images").length();
                totalImages = new String[totalImage];
                for (int count = 0; count < jsonObj.getJSONArray("images").length(); count++) {
//                finalCommand.add
                    totalImages[count] = filesPath + "/" + jsonObj.getJSONArray("images").getJSONObject(count).getString("name");
                }


                has_text_field =0;
                totalText = 0;
                if (jsonObj.has("texts")) {
                    totalText = jsonObj.getJSONArray("texts").length();
                    totalTexts = new String[totalText];
                    JsonInputLables = new String[totalText];
                    JsonInputValues = new String[totalText];

                    for (int count = 0; count < jsonObj.getJSONArray("texts").length(); count++) {
//                finalCommand.add
                        totalTexts[count] = jsonObj.getJSONArray("texts").getJSONObject(count).getString("replace_key");
                        JsonInputLables[count] = jsonObj.getJSONArray("texts").getJSONObject(count).getString("label");
                        JsonInputValues[count] = jsonObj.getJSONArray("texts").getJSONObject(count).getString("value");
                    }
                    has_text_field = 1;
                }

                int width;
                int height;

                height = Integer.parseInt(jsonObj.getJSONObject("video").getString("h"));
                width = Integer.parseInt(jsonObj.getJSONObject("video").getString("w"));
                duration = jsonObj.getJSONObject("video").getString("duration");
                if (height > width) {
                    ratio = "9,16";
                } else {
                    ratio = "16,9";
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cardView1.setBackgroundResource(R.drawable.orangegradient);
        cardVideo.setBackgroundResource(R.drawable.round_white);
        cardTextImage.setTextColor(getColor(R.color.white));
        cardTxtVideo.setTextColor(getColor(R.color.colorPrimary));

        cardView1.setOnClickListener(view -> {
            flagVideo = false;
            cardView1.setBackgroundResource(R.drawable.orangegradient);
            cardVideo.setBackgroundResource(R.drawable.round_white);
            cardTextImage.setTextColor(getColor(R.color.white));
            cardTxtVideo.setTextColor(getColor(R.color.colorPrimary));
            rv_numberimg.setVisibility(View.VISIBLE);
            rv_video_list.setVisibility(View.GONE);
        });


        cardVideo.setOnClickListener(view -> {
            if (resizedVideoPathFinal != null) {
                flagVideo = true;
            }
            cardView1.setBackgroundResource(R.drawable.round_white);
            cardVideo.setBackgroundResource(R.drawable.orangegradient);
            cardTxtVideo.setTextColor(getColor(R.color.white));
            cardTextImage.setTextColor(getColor(R.color.colorPrimary));

            if (dialog == null || !dialog.isShowing()) {
                String str = Intent.ACTION_GET_CONTENT;
                String str2 = "video/*";
                Intent intent;
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 1001);
                    return;
                }
                intent = new Intent();
                intent.setType(str2);
                intent.setAction(str);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_video)), 1001);
                return;
            }
            try {
                if (animation != null) {
                    relativeLayout.startAnimation(animation);
                }
            } catch (Exception e) {
                e.printStackTrace();

                MyAppUtils.setToast(context, "Please Wait while Creating Video.");
            }
        });
        String[] listImages1 = listImages;
        if (listImages1 != null && listImages1.length > 0) {
            i = 0;
            while (true) {
                String[] strArr2 = listImages;
                if (i >= strArr2.length) {
                    break;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(getCacheDir());
                stringBuilder.append("/blankimage.jpg");
                strArr2[i] = stringBuilder.toString();
                i++;
            }
        }


        listImages1 = totalImages;
        if (listImages1 != null && listImages1.length > 0) {

            if(totalText > 0)
            {
                totalImage = (totalImage +1);
            }
            Log.e("DEBUG_CARD", " AFter Text added Image Count "+totalImage);

            nvImageListAdapter = new NvImageListAdapter(context, totalImage, totalImages, new clickAdapter(),totalText,totalTexts,JsonInputLables,JsonInputValues);
            rv_numberimg.setLayoutManager(new GridLayoutManager((Context) context, 1, RecyclerView.HORIZONTAL, false));
            rv_numberimg.setItemAnimator(new DefaultItemAnimator());
            rv_numberimg.setAdapter(nvImageListAdapter);
        }
        ( findViewById(R.id.back)).setOnClickListener(view -> onBackPressed());

        VideoviewModel videoviewModelData = Utils.static_video_model_data;
        if (!(videoviewModelData == null || videoviewModelData.getVideoThumb() == null || isFinishing())) {
            setDownloadDialog();
        }


        saveVideo.setOnClickListener(view -> {
            pausePlayer();
            if (dialog != null && dialog.isShowing()) {
                try {
                    if (animation != null) {
                        relativeLayout.startAnimation(animation);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            {
                if (!(dialog == null || dialog.isShowing())) {
                    dialog.show();
                }

                applyFiler = new applyFiler();
                applyFiler.execute(new Void[0]);
            }
        });
        btnTryAgain.setOnClickListener(view -> {
            layoutTryAgain.setVisibility(View.GONE);
            progressBarExoplayer.setVisibility(View.VISIBLE);
            initializePlayer();
        });

        MyAppUtils.showBannerAds(this, ll_fbbanner);


    }


    public NV_VideoEditorActivity() {
        String str = "";
        downloadFileName = str;
        url = str;
        handler = new Handler();

        runnable = new AdShow();
        mathc = "\\btime=\\b\\d\\d:\\d\\d:\\d\\d.\\d\\d";
        myListener = new Utils_Admob.MyListener() {
            @Override
            @SuppressLint({"WrongConstant"})
            public void callback(String str) {
                dialog.dismiss();
                Intent intent = new Intent(context, NV_SaveVideoFileActivity.class);
                intent.putExtra("videourl", url);
                intent.addFlags(335544320);
                startActivity(intent);
                finish();
            }

            @Override
            @SuppressLint({"WrongConstant"})
            public void callback2(String str) {
                dialog.dismiss();
                Intent intent = new Intent(context, NV_SaveVideoFileActivity.class);
                intent.putExtra("videourl", url);
                intent.addFlags(335544320);
                startActivity(intent);
                finish();
            }
        };
    }




    public final String replaceToOriginal(String str) {

        Log.e("EXPORT_DEBUG", String.valueOf(has_text_field));

        if(has_text_field > 0) {
            totalTexts = nvImageListAdapter.getArrayList("");

            for (int count = 0; totalText > count; count++) {
                str = str.replace("replace_key_" + (count + 1), totalTexts[count]);
                //str.replace("replace_key_"+(count+1), totalTexts[count]);
                Log.e("REPLACE", "Replace String :  replace_key_" + (count + 1));
                Log.e("REPLACE", "================================" + totalTexts[count]);
            }
        }

        return str.replace("{pythoncomplex}", "filter_complex").replace("{pythonmerge}", "alphamerge").replace("{pythono}", "overlay").replace("{pythonz}", "zoom").replace("{pythonf}", "fade").replace("{folder_path}", filesPath+"/" );
    }


    private void beginCrop(Uri uri) {
        if (uri != null) {
            try {
                String[] split = ratio_image.split(",");
                Log.e("CROP_DEBUG",Integer.parseInt(split[0])+" = "+Integer.parseInt(split[1])+" URL:"+uri);


                CropImage.activity(uri).setAspectRatio(Integer.parseInt(split[0]), Integer.parseInt(split[1])).start(this);

                //CropImage.activity(uri).setAspectRatio(250, 100).start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    private void startTrimActivity(@NonNull Uri uri) {
        Intent intent = new Intent(context, NV_VideoTrimActivity.class);
        intent.putExtra("EXTRA_VIDEO_PATH", FileUtils.getPath(context, uri));
        intent.putExtra("duration", duration);
        intent.putExtra("video_resolution", videoResolution);
        startActivity(intent);
    }

    public void callBroadCast() {
        MediaScannerConnection.scanFile(context, new String[]{getDataDir().toString()}, null, new OnScanCompletedListener() {
            public void onScanCompleted(String str, Uri uri) {
                resizedVideoPathFinal = null;
                flagVideo = false;
            }
        });

    }

    public void deleteImage() {
        try {
            if (listImages != null) {
                for (int i = 0; i < listImages.length; i++) {
                    File file = new File("/storage/emulated/0/UV Video Status Maker/.Temp_Frame");
                    if (file.isDirectory()) {
                        String[] list = file.list();
                        int i2 = 0;
                        while (i2 < list.length) {
                            if (new File(file, list[i2]).exists() && new File(file, list[i2]).delete()) {
                                callBroadCast();
                            }
                            i2++;
                        }
                    }
                }
            }
            if (resizedVideoPathFinal != null) {
                File file2 = new File(resizedVideoPathFinal);
                if (file2.exists() && file2.delete()) {
                    callBroadCast();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getPercentages(int curntTime) {

        float progressF;
        int progress;
        int videoDuration = Integer.parseInt(duration);

        progressF = ((float) curntTime / (float) videoDuration) * 100;

        progress = (int) (progressF / 1000);

        if (progress >= 100) {
            return 100;
        } else {
            return progress;
        }


    }

    public void execureCommand(String[] strArr) {

        // Convert duration to long (your original line)
        K = Long.parseLong(duration);


        // Execute FFmpeg with arguments
        FFmpegKit.executeWithArgumentsAsync(strArr, session -> {

            // Get return code properly
            ReturnCode returnCode = session.getReturnCode();

            // Success handling
            if (ReturnCode.isSuccess(returnCode)) {

                new Handler(Looper.getMainLooper()).post(() -> {
                    dialog.dismiss();
                    MediaScannerConnection.scanFile(
                            getApplicationContext(),
                            new String[]{ new File(url).getAbsolutePath() },
                            new String[]{ "mp4" },
                            null
                    );
                    handler.postDelayed(runnable, 500);
                });

            } else {

                // Failure handling
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    MyAppUtils.setToast(context, "Failed..");
                });

            }

        });

        FFmpegKitConfig.enableStatisticsCallback(statistics -> {

            if (progressBar != null) {
                progressBar.setProgress(getPercentages((int) statistics.getTime()));
            }

        });

    }


    @SuppressLint("UnsafeOptInUsageError")
    public void initializePlayer() {
        // Initialize Track Selector

        pausePlayer();


        Log.e("PLAYERDEBUG"," Its Playing...");




        DefaultTrackSelector trackSelector = new DefaultTrackSelector(context);
        trackSelector.setParameters(
                trackSelector.buildUponParameters().setMaxVideoSizeSd()
        );

        // Create ExoPlayer instance using ExoPlayer.Builder
        exoPlayer = new ExoPlayer.Builder(context)
                .setRenderersFactory(new DefaultRenderersFactory(context))
                .setTrackSelector(trackSelector)
                .build();

        // Attach the player to PlayerView
        exo_playerview.setPlayer(exoPlayer);

        // Cache DataSource Factory
        CacheDataSource.Factory cacheDataSourceFactory = new CacheDataSource.Factory()
                .setCache(VideoCache.getInstance(context)) // Use the cache instance
                .setUpstreamDataSourceFactory(
                        new DefaultDataSource.Factory(
                                context,
                                new DefaultHttpDataSource.Factory().setUserAgent("MyVideoMakerApplication")
                        )
                )
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);

        try {
            if (Utils.static_video_model_data != null &&
                    !TextUtils.isEmpty(Utils.static_video_model_data.getVideo_link())) {


                Uri fileUri = Uri.fromFile(new File(filesPath + "/output.mp4"));

                File videoFile = new File(filesPath + "/output.mp4");
                if (videoFile.exists()) {
                    Log.d("Player", "Video file exists: " + videoFile.getAbsolutePath());

                    // Create a Uri from the file
                    fileUri = Uri.fromFile(videoFile);
                    Log.d("Player", "Video URI: " + fileUri);


                    MediaItem mediaItem = new MediaItem.Builder()
                            .setUri(fileUri)
                            .build();

                    // Create MediaSource
                    ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(
                            new DefaultDataSource.Factory(context)
                    ).createMediaSource(mediaItem);

                    // Attach MediaSource to ExoPlayer
                    exoPlayer.setMediaSource(mediaSource);

                    // Set ExoPlayer to Play When Ready
                    exoPlayer.setPlayWhenReady(true);
                    exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);

                    // Prepare and Play
                    exoPlayer.prepare();

                    // Hide the controller when visibility changes
                    exo_playerview.setControllerVisibilityListener(new PlayerView.ControllerVisibilityListener() {
                        @Override
                        public void onVisibilityChanged(int visibility) {
                            exo_playerview.hideController();
                        }
                    });

                    // Set Touch Listener
                    exo_playerview.setOnTouchListener(new View.OnTouchListener() {
                        private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                            @Override
                            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                                float deltaX = e1.getX() - e2.getX();
                                float deltaXAbs = Math.abs(deltaX);

                                if (deltaXAbs > 100 && deltaXAbs < 1000) {
                                    if (deltaX > 0) {
                                        onBackPressed();
                                    }
                                }
                                return true;
                            }

                            @Override
                            public boolean onSingleTapUp(MotionEvent e) {
                                if (!exoPlayer.getPlayWhenReady()) {
                                    exoPlayer.setPlayWhenReady(true);
                                } else {
                                    new Handler(Looper.getMainLooper()).postDelayed(() -> exoPlayer.setPlayWhenReady(false), 200);
                                }
                                return true;
                            }

                            @Override
                            public boolean onDoubleTap(MotionEvent e) {
                                if (!exoPlayer.getPlayWhenReady()) {
                                    exoPlayer.setPlayWhenReady(true);
                                }
                                return super.onDoubleTap(e);
                            }
                        });

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            gestureDetector.onTouchEvent(event);
                            return true;
                        }
                    });


                    // Use the MediaItem as required
                } else {
                    Log.e("Player", "Video file does not exist at path: " + videoFile.getAbsolutePath());
                }



            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add ExoPlayer Listeners
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                progressBarExoplayer.setVisibility(
                        playbackState == Player.STATE_BUFFERING ? View.VISIBLE : View.INVISIBLE
                );
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                if (error != null && error.getMessage() != null && error.getMessage().contains("Unable to connect")) {
                    exo_playerview.hideController();
                    layoutTryAgain.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_PICK && resultCode == -1) {
            beginCrop(intent.getData());
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (intent != null) {
                if (resultCode == -1) {
                    Uri uri = CropImage.getActivityResult(intent).getUri();

                    totalImages[o] = uri.getPath();
                    nvImageListAdapter.notifyDataSetChanged();
                    rv_numberimg.setAdapter(nvImageListAdapter);
                }
            } else {
                return;
            }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            CropImage.getActivityResult(intent).getError();
        }
        if (requestCode == 1001 && resultCode == -1) {
            try {
                Uri data = intent.getData();
                if (data == null || !new File(FileUtils.getPath(context, data)).exists()) {
                    Toast.makeText(context, R.string.toast_cannot_retrieve_selected_video, Toast.LENGTH_SHORT).show();
                } else {
                    startTrimActivity(data);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void copyRAWtoSDCard(int id, String path,String filename) throws IOException {
        InputStream in = getResources().openRawResource(id);
        File file = new File(path);
        file.mkdirs();
        FileOutputStream out = new FileOutputStream(path + "/"+filename);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }

        } finally {
            in.close();
            out.close();
        }
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (progressBar != null && progressBar.isAnimating()) {
                Toast.makeText(context, "You can't go back, please wait.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, NV_LibraryActivity.class);
                startActivity(intent);
                finish(); // optional, to close the current activity
            }
        }
    }

    public void showDialogBack() {
        if (dialog == null || !dialog.isShowing()) {
            deleteImage();
            finish();
            return;
        }
        dialog.setContentView(R.layout.dialog_confirm_back);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        final Dialog finalDialog = dialog;
        ((ImageView) dialog.findViewById(R.id.cancel)).setOnClickListener(view -> {
            finalDialog.dismiss();
            dialog.show();
        });
        ((Button) dialog.findViewById(R.id.yes)).setOnClickListener(view -> {
            deleteImage();
            applyFiler.cancel(true);
            dialog.dismiss();
            finish();
        });
        final Dialog finalDialog1 = dialog;
        ((Button) dialog.findViewById(R.id.no)).setOnClickListener(view -> {
            finalDialog1.dismiss();
            dialog.show();
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }


    @SuppressLint("UnsafeOptInUsageError")
    @Override

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        exoPlayer.release();
    }


    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        imageList = bundle.getString("image_list");
        videoResolution = bundle.getString("video_resolution");
        colorKayRandom = bundle.getString("colorkey_rand");
        duration = bundle.getString("duration");
        ffCmd = bundle.getString("ff_cmd");
        ffCmdVideo = bundle.getString("ff_cmd_video");
        ffUser = bundle.getString("ff_cmd_user");
        picPath = bundle.getString("picturePath");
        opVideo = bundle.getString("opt_video");
    }



    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("total_image", String.valueOf(totalImage));
        bundle.putString("image_list", String.valueOf(imageList));
        bundle.putString("video_resolution", String.valueOf(videoResolution));
        bundle.putString("image_ratio", String.valueOf(ratio));
        bundle.putString("colorkey_rand", String.valueOf(colorKayRandom));
        bundle.putString("duration", String.valueOf(duration));
        bundle.putString("ff_cmd", String.valueOf(ffCmd));
        bundle.putString("ff_cmd_video", String.valueOf(ffCmdVideo));
        bundle.putString("ff_cmd_user", String.valueOf(ffUser));
        bundle.putString("picturePath", String.valueOf(picPath));
        bundle.putString("opt_video", String.valueOf(opVideo));
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
        pausePlayer();
    }

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    protected void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false); // Pause playback
            exoPlayer.getPlaybackState();     // Maintain playback state
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            exoPlayer.release(); // Release resources
            exoPlayer = null;    // Clear reference to avoid memory leaks
        }
    }



    @SuppressLint("UnsafeOptInUsageError")
    public void pausePlayer() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            // You can use the playback state if needed, but this line is optional
            int playbackState = exoPlayer.getPlaybackState();
        }
    }


    public void setDownloadDialog() {
        if (Utils.static_video_model_data != null) {
            dialog = new Dialog(this);

            this.dialog.requestWindowFeature(1);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_download_file);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            progressBar = dialog.findViewById(R.id.progress_download_video);
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if(MyApplication.show_native > 0) {
                //loadNativeAds((FrameLayout) dialog.findViewById(R.id.fl_adplaceholder), (NativeAdLayout) dialog.findViewById(R.id.fb_native_ad_container));
                MyAppUtils.loadAdmobNativeAd((FrameLayout) dialog.findViewById(R.id.fl_adplaceholder),context);
            }
            progressBar.setProgress(0);

            CardView cancel = dialog.findViewById(R.id.ll_cancel_download);
            cancel.setVisibility(View.GONE);

            TextView title = dialog.findViewById(R.id.tv_title);
            title.setText("Crafting");

            TextView downloading = dialog.findViewById(R.id.tvDownloading);
            downloading.setText("Please Wait, While We are crafting your Video ");

            cancel.setOnClickListener(view -> {
                try {
                    if (progressBar.getProgress() < 100.0d) {

                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_confirm_back);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        dialog.setCancelable(false);
                        ((ImageView) dialog.findViewById(R.id.cancel)).setOnClickListener((View.OnClickListener) view12 -> {

                            dialog.dismiss();

                        });
                        ((Button) dialog.findViewById(R.id.yes)).setOnClickListener((View.OnClickListener) view1 -> {

                            applyFiler.cancel(true);
                            dialog.dismiss();
                            finish();


                        });

                        ((Button) dialog.findViewById(R.id.no)).setOnClickListener((View.OnClickListener) view13 -> {
                            dialog.dismiss();

                        });

                        if (!dialog.isShowing())
                            dialog.show();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }


    public void setRemoveWaterMarkDialog() {
        dialog2 = new Dialog(this);

        this.dialog2.requestWindowFeature(1);
        dialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.dialog_remove_watermark);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        progressBar = dialog2.findViewById(R.id.progress_download_video);
        Window window = dialog2.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView btnNo = dialog2.findViewById(R.id.btnNo);
        ImageView btYes = dialog2.findViewById(R.id.btnYes);

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(NV_VideoEditorActivity.this,"Load reward ads",Toast.LENGTH_LONG).show();
                showRewardADs();


            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });


    }

    public  void loadRewardADs() {

        if (MyApplication.ad_network.equals("admob")) {
            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdShowedFullScreenContent() {
                            //Toast.makeText(NV_VideoEditorActivity.this, "Load onAdShowedFullScreenContent", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            mRewardedAd = null;
                            dialog2.dismiss();
                            // Code to be invoked when the ad dismissed full screen content.
                        }
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
                        this, new OnUserEarnedRewardListener() {
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                //REWARD EARN AND NEED TO REMOVE WATER MARK
                                Toast.makeText(NV_VideoEditorActivity.this,"You have successfully removed watermark for this video.",Toast.LENGTH_LONG).show();
                                hideWaterMarkButtonImage();
                                loadRewardADs();
                            }
                        });
            }

        }
        else if (MyApplication.ad_network.equals("applovins")) {
            if(rewardedAd.isReady())
            {
                rewardedAd.showAd();
            }
        }



        /*if (mRewardedAd != null) {
            mRewardedAd.show(
                    this, new OnUserEarnedRewardListener() {
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            //REWARD EARN AND NEED TO REMOVE WATER MARK
                            Toast.makeText(NV_VideoEditorActivity.this,"You have successfully removed watermark for this video.",Toast.LENGTH_LONG).show();
                            hideWaterMarkButtonImage();
                            loadRewardADs();
                        }
                    });
        }*/
    }

    public void hideWaterMarkButtonImage()
    {
        context.findViewById(R.id.removeWaterMark).setVisibility(View.GONE);
        is_reward_ads_shown = 1;

    }
    public void onRewardedVideoCompleted(MaxAd ad) {
        Toast.makeText(NV_VideoEditorActivity.this,"You have successfully removed watermark for this video.",Toast.LENGTH_LONG).show();
        hideWaterMarkButtonImage();
        loadRewardADs();
        rewardedAd = null;
        dialog2.dismiss();
    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {

    }

    @Override
    public void onRewardedVideoStarted(MaxAd maxAd) {

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


    static class VideoCache {
        @SuppressLint("UnsafeOptInUsageError")
        static SimpleCache sDownloadCache;

        @SuppressLint("UnsafeOptInUsageError")
        static Cache getInstance(Context context) {
            if (sDownloadCache == null) {
                sDownloadCache = new SimpleCache(new File(context.getCacheDir(), "exoCache"), new LeastRecentlyUsedCacheEvictor(1073741824));
            }
            return sDownloadCache;
        }
    }

    public class applyFiler extends AsyncTask<Void, Integer, Void> {
        public Void doInBackground(Void... voidArr) {
            return doinback(voidArr);
        }

        @SuppressLint({"SdCardPath", "WrongConstant"})
        public Void doinback(Void... voidArr) {
            try {
                String format = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime());
                StringBuilder stringBuilder = new StringBuilder();

                //stringBuilder.append(getDataDir().getAbsolutePath());
                stringBuilder.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS));

                stringBuilder.append("/");
                stringBuilder.append(getResources().getString(R.string.app_name));
                stringBuilder.append("/video_");
                stringBuilder.append(format);
                stringBuilder.append(".mp4");
                String str = "&";
                //File file = new File(getDataDir() + "/" + getResources().getString(R.string.app_name));
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+ "/" + getResources().getString(R.string.app_name));
                if (!file.exists()) {
                    file.mkdirs();

                }

                finalCommand = new ArrayList<>();
                File yourFile = new File(filesPath + "/python.json");
                FileInputStream stream = null;
                try {
                    stream = new FileInputStream(yourFile);

                    String jsonStr = null;
                    try {

                        for (int count = 0; count < jsonObj.getJSONArray("images").length(); count++) {

                            JSONArray tempArray = jsonObj.getJSONArray("images").getJSONObject(count).getJSONArray("prefix");
                            for (int count1 = 0; count1 < tempArray.length(); count1++) {
                                finalCommand.add(tempArray.getString(count1));
                            }
                            finalCommand.add(totalImages[count]);
                        }

                        for (int count = 0; count < jsonObj.getJSONArray("static_inputs").length(); count++) {

                            JSONArray tempArray = jsonObj.getJSONArray("static_inputs").getJSONObject(count).getJSONArray("prefix");
                            for (int count1 = 0; count1 < tempArray.length(); count1++) {
                                finalCommand.add(tempArray.getString(count1));
                            }
                            finalCommand.add(filesPath + "/" + jsonObj.getJSONArray("static_inputs").getJSONObject(count).getString("name"));
                        }

                        finalCommand.add("-ignore_loop");
                        finalCommand.add("0");
                        finalCommand.add("-i");
                        //finalCommand.add(waterMarkPath);

                        if(is_reward_ads_shown > 0) {

                            Log.e("Reward Ads Seen", String.valueOf(is_reward_ads_shown));
                            finalCommand.add(WaterMarkPathBlank);
                            is_reward_ads_shown = 0;

                        }
                        else
                        {
                            Log.e("Reward Ads Not Seen", String.valueOf(is_reward_ads_shown));
                            finalCommand.add(waterMarkPath);
                        }



                        JSONArray jSONArray = jsonObj.getJSONArray("m");
                        if (jSONArray.length() != 0) {
                            for (int i = 0; i < jSONArray.length(); i++) {
                                finalCommand.add(replaceToOriginal(jSONArray.getString(i)));
                            }
                        }

                        jSONArray = jsonObj.getJSONArray("r");
                        if (jSONArray.length() != 0) {
                            for (int i = 0; i < jSONArray.length(); i++) {
                                finalCommand.add(replaceToOriginal(jSONArray.getString(i)));
                            }
                        }

                        jSONArray = jsonObj.getJSONArray("d");
                        if (jSONArray.length() != 0) {
                            for (int i = 0; i < jSONArray.length(); i++) {
                                finalCommand.add(replaceToOriginal(jSONArray.getString(i)));
                            }
                        }
                        finalCommand.add("-preset");
                        finalCommand.add("slow");              // Better compression & quality
                        finalCommand.add("-crf");
                        finalCommand.add("18");                // Lower = higher quality (18 is visually lossless)
                        finalCommand.add("-b:v");
                        finalCommand.add("4M");                // 4 Mbps bitrate, excellent for portrait HD
                        finalCommand.add("-maxrate");
                        finalCommand.add("5M");                // Peak bitrate cap
                        finalCommand.add("-bufsize");
                        finalCommand.add("10M");               // Smoother bitrate control
                        finalCommand.add("-pix_fmt");
                        finalCommand.add("yuv420p");           // Wide compatibility (for Android/iOS/players)
                        finalCommand.add("-movflags");
                        finalCommand.add("+faststart");        // Makes playback start instantly (stream-friendly)

                        finalCommand.add(stringBuilder.toString());
                        /*
                        finalCommand.add("-preset");
                        finalCommand.add("ultrafast");
                        finalCommand.add(stringBuilder.toString());
                        */

                    } catch (Exception e) {
                        Log.d("FFMPEGException", "" + e);
                    }
                } catch (Exception e) {
                    Log.d("FFMPEGException", "" + e);
                }

                cmd = new String[finalCommand.size()];
                cmd = finalCommand.toArray(cmd);

                url = stringBuilder.toString();
                if (cmd.length != 0) {
                    execureCommand(cmd);
                } else {
                    Toast.makeText(getApplicationContext(), "Command Empty", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onPreExecute() {
        }
    }

    public class AdShow implements Runnable {
        @SuppressLint({"WrongConstant"})
        public void run() {
            try {

                Toast.makeText(context, "Video Saved in Gallery.", Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(runnable);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                deleteImage();
                dialog.dismiss();
                Intent intent = new Intent(context, NV_SaveVideoFileActivity.class);
                intent.putExtra("videourl", url);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                MyAppUtils.showInterstitialAds(context);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class clickAdapter implements onclick {
        public void clickEvent(View view, int i) {
            if (dialog == null || dialog.isShowing()) {
                try {
                    if (animation != null) {
                        relativeLayout.startAnimation(animation);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "Please Wait while Creating Video.", Toast.LENGTH_SHORT).show();
                return;
            }
            flagVideo = false;
            Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
            try {
                o = i;
                startActivityForResult(intent, REQUEST_PICK);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(context, R.string.crop__pick_error, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true); // Resume playback if needed
        }
        try {
            if (resizedVideoPathFinal != null) {
                rv_numberimg.setVisibility(View.GONE);
                rv_video_list.setVisibility(View.VISIBLE);
                Picasso.get().load(resizedVideoPathFinal).into(thumb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && exoPlayer != null && exoPlayer.isPlaying()) {
            PictureInPictureParams.Builder params = new PictureInPictureParams.Builder();
            params.setAspectRatio(new Rational(16, 9));
            enterPictureInPictureMode(params.build());
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        if (isInPictureInPictureMode) {
            // Hide controls or UI elements if needed
        } else {
            // Restore UI when exiting PiP
        }
    }

}
