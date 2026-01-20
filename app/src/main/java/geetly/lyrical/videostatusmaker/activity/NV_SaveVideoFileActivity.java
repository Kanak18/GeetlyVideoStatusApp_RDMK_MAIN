package geetly.lyrical.videostatusmaker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PictureInPictureParams;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Rational;
import android.view.View;

import geetly.lyrical.videostatusmaker.BuildConfig;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdView;

import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;

import java.io.File;

public class NV_SaveVideoFileActivity extends AppCompatActivity {
    ImageView whatsapp;
    ImageView facebook;
    ImageView insta;
    ImageView more;
    ImageView playPuase;
    RelativeLayout rlPlaypause;
    ImageView back;
    NV_SessionManager NVSessionManager;
    VideoView videoView;
    private String videoUrl = "";
    private Activity context;
    private AdView adView;
    private FrameLayout ll_fbbanner;

    @SuppressLint({"ClickableViewAccessibility"})
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_saved);

        back =  findViewById(R.id.back);
        videoView =  findViewById(R.id.videoview);
        playPuase =  findViewById(R.id.play_pause);
        rlPlaypause =  findViewById(R.id.playing_status);
        whatsapp =  findViewById(R.id.whatsapp);
        facebook =  findViewById(R.id.facebook);
        insta =  findViewById(R.id.insta);

        ll_fbbanner =  findViewById(R.id.ll_fbbanner);
        context = this;




        NVSessionManager = new NV_SessionManager(this);

        Intent intent1 = getIntent();
        if (intent1.getExtras() != null) {
            videoUrl = intent1.getStringExtra("videourl");
        }
       // Log.e("Video Url List :",videoUrl);

        this.back.setOnClickListener(view -> {
            onBackPressed();

            MyAppUtils.showInterstitialAds(context);


        });

        CardView backHome =  findViewById(R.id.home);


        if (!NVSessionManager.getBooleanData(NV_SessionManager.PREF_APP_RATED)) {
            showRateDialog();
        }
        backHome.setOnClickListener(view -> {
            Intent intent = new Intent(NV_SaveVideoFileActivity.this, NV_LibraryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            MyAppUtils.showInterstitialAds(context);


        });

        initVideo();

        whatsapp.setOnClickListener(view -> {

           // Log.e("Share with WH",videoUrl);

            MyAppUtils.shareImageVideoOnWhatsapp(context, videoUrl, true);

            MyAppUtils.showInterstitialAds(context);
        });


        facebook.setOnClickListener(view -> {
            // Log.e("Share with FB",videoUrl);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("video/*");
            Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(videoUrl));
            //Uri uriForFile = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(videoUrl));
            //Log.e("Share with FB After Path",uriForFile.toString());
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
            String stringBuilder2 = getString(R.string.share) + getPackageName();
            intent.putExtra(Intent.EXTRA_TEXT, stringBuilder2);
            intent.setPackage("com.facebook.katana");
            try {

                startActivity(Intent.createChooser(intent, "Share Video..."));
                MyAppUtils.showInterstitialAds(context);

            } catch (ActivityNotFoundException unused) {
                Toast.makeText(NV_SaveVideoFileActivity.this, R.string.install_fb, Toast.LENGTH_LONG).show();
            }
        });


        this.insta.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("video/*");
            Uri uriForFile = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(videoUrl));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
            String stringBuilder2 = getString(R.string.share) +
                    getPackageName();
            intent.putExtra(Intent.EXTRA_TEXT, stringBuilder2);
            intent.setPackage("com.instagram.android");
            try {
                startActivity(Intent.createChooser(intent, "Share Video..."));


                MyAppUtils.showInterstitialAds(context);

            } catch (ActivityNotFoundException unused) {
                Toast.makeText(NV_SaveVideoFileActivity.this, "Please Install Instagram", Toast.LENGTH_LONG).show();
            }
        });
        this.more =  findViewById(R.id.more);
        this.more.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("video/*");

                Uri uriForFile = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(videoUrl));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share) + getPackageName());
                startActivity(Intent.createChooser(intent, "Share Your Video!"));


                MyAppUtils.showInterstitialAds(context);

            } catch (Exception e) {

                e.printStackTrace();
            }
        });

        MyAppUtils.showBannerAds(this,ll_fbbanner);
    }



    public void showRateDialog() {
        new NV_RatingDialog.Builder(this).title("Like New Wave 2021").positiveButtonTextColor(R.color.grey_500).negativeButtonTextColor(R.color.grey_500).playstoreUrl("https://play.google.com/store/apps/details?id=" + getPackageName()).onRatingBarFormSumbit(str -> {

        }).build().show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initVideo() {

        if (getIntent() != null) {

            try {
                this.videoView.setVideoURI(Uri.parse(videoUrl));
                this.rlPlaypause.setVisibility(View.GONE);
                this.videoView.requestFocus();
                this.videoView.start();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        this.videoView.setOnCompletionListener(mediaPlayer -> videoView.start());
        this.videoView.setOnTouchListener((view, motionEvent) -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                rlPlaypause.setVisibility(View.VISIBLE);
                playPuase.setImageResource(R.drawable.ic_play_new);
                return false;
            }
            playPuase.setImageResource(R.drawable.ic_pause_new);
            videoView.start();
            new Handler(Looper.getMainLooper()).postDelayed(() -> rlPlaypause.setVisibility(View.GONE), 2000);
            return false;
        });
    }



    @Override
    public void onPause() {
        super.onPause();
        this.videoView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.rlPlaypause.setVisibility(View.GONE);
        this.videoView.start();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NV_SaveVideoFileActivity.this, NV_LibraryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        MyAppUtils.showInterstitialAds(context);


    }

    @Override
    public void onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && videoView != null && videoView.isPlaying()) {
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