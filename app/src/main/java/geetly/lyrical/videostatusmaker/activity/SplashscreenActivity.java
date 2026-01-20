package geetly.lyrical.videostatusmaker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;

import com.google.android.gms.ads.MobileAds;
import com.google.gson.JsonObject;


import java.util.ArrayList;

import geetly.lyrical.videostatusmaker.MyApplication;
import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.Retrofit.APIClientData;
import geetly.lyrical.videostatusmaker.model.MultipleResource;
import geetly.lyrical.videostatusmaker.model.VideoviewModel;
import geetly.lyrical.videostatusmaker.utils.AppOpenManager;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SplashscreenActivity extends AppCompatActivity {

    private Activity context;
    private LottieAnimationView lottieAnimationView;

    private String id = "0", subId = "", type = "";

    String title = "";
    String video_id = "";
    String video_thumb = "";
    String video_link = "";
    String video_zip = "";
    String video_category = "";
    private int position = 0;

    ArrayList<VideoviewModel> videoviewdata = new ArrayList<>();

    private static AppOpenManager appOpenManager;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        context = this;
        lottieAnimationView =  findViewById(R.id.animationView);


        Log.d("ADS: -> Banner ", "Application In SplaceScreen");
        Log.d("ADS: -> Banner ", String.valueOf(MyApplication.show_banner));
        Log.d("ADS: -> Inter ", String.valueOf(MyApplication.show_inter));
        Log.d("ADS: -> Native ", String.valueOf(MyApplication.show_native));
        Log.d("ADS: -> Reward ", String.valueOf(MyApplication.show_reward));


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

                Log.i("OneSignal:", "Params :: video_id => " + video_id + " Title => " + title + " Category => " + video_category + " Video Zip => " + video_zip);

            }

        }

        if (MyAppUtils.isConnectingToInternet(this)) {
            lottieAnimationView.setVisibility(View.GONE);
            lottieAnimationView.pauseAnimation();
            getSettings();

        } else {

            // splashbg.setVisibility(View.GONE);
            lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.playAnimation();
        }


    }

    public void getSettings() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("app", getApplicationContext().getPackageName());
        jsonObject.addProperty("page", 1);

        Log.d("RequestPayload", jsonObject.toString());

        Call<MultipleResource> call = APIClientData.getInterface().getAppSettings(jsonObject);
        call.enqueue(new Callback<MultipleResource>() {
            @Override
            public void onResponse(Call<MultipleResource> call, Response<MultipleResource> response) {


                Log.d("ResponseCode", String.valueOf(response.code()));

                String displayResponse = "";
                MultipleResource resource = response.body();

                Log.d("ResponseBody", response.body().toString());



                MyApplication.ad_network =  resource.ad_network;



                MyApplication.admob_publisher_id = resource.admob_publisher_id;

                MyApplication.show_open_ads_admob = resource.show_open_ads_admob;

                Log.d("Reponse_CALL SC AdShow Setting : ",response.toString() );

                MyApplication.show_banner = resource.show_banner;
                MyApplication.show_inter = resource.show_inter;
                MyApplication.show_native = resource.show_native;
                MyApplication.show_reward = resource.show_reward;


                MyApplication.interstitial_show_count = resource.interstitial_show_count;
                MyApplication.native_show_count = resource.native_show_count;

                MyApplication.banner_ad_id = resource.banner_ad_id;
                MyApplication.native_ad_id = resource.native_ad_id;
                MyApplication.interstitial_ad_id = resource.interstitial_ad_id;
                MyApplication.reward_ad_id = resource.reward_ad_id;


                MyApplication.unity_game_id = resource.unity_game_id;
                MyApplication.startapp_app_id = resource.startapp_app_id;

                MyApplication.app_update_status = resource.app_update_status;
                MyApplication.app_new_version = resource.app_new_version;
                MyApplication.app_update_desc = resource.app_update_desc;
                MyApplication.app_redirect_url = resource.app_redirect_url;
                MyApplication.cancel_update_status = resource.cancel_update_status;

                initializeAds();

                Log.d("Reponse_CALL SC AdShow Setting : ", " Show Banner => " + MyApplication.show_banner + " Show Open Ads => " + MyApplication.show_open_ads_admob +  " Show Interstitial => " + MyApplication.show_inter + " Show Native  => " + MyApplication.show_native + " Show Reward => " + MyApplication.show_reward + " => Int show Count" + MyApplication.interstitial_show_count );

                new  Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        if ( (!TextUtils.isEmpty(video_zip))  &&  (!TextUtils.isEmpty(video_link)) && (!TextUtils.isEmpty(video_thumb))) {

                            //Log.i("OneSignal:", "If condition :");

                            Intent  intent = new Intent(SplashscreenActivity.this, NV_VideoListActivity.class);
                            intent.putExtra("video_id",video_id);
                            intent.putExtra("title",title);
                            intent.putExtra("video_category",video_category);
                            intent.putExtra("video_link",video_link);
                            intent.putExtra("video_zip",video_zip);
                            intent.putExtra("video_thumb",video_thumb);
                            startActivity(intent);
                            finish();
                        }
                        else if (!TextUtils.isEmpty(title))
                        {
                            //Log.i("OneSignal:", "ELSE If condition :");
                            Intent  intent = new Intent(SplashscreenActivity.this, NV_LibraryActivity.class);
                            intent.putExtra("video_id",video_id);
                            intent.putExtra("title",title);
                            intent.putExtra("video_category",video_category);
                            intent.putExtra("video_link",video_link);
                            intent.putExtra("video_zip",video_zip);
                            intent.putExtra("video_thumb",video_thumb);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            // Log.i("OneSignal:", "ELSE:");
                            Intent i = new Intent(SplashscreenActivity.this, NV_LibraryActivity.class);
                            startActivity(i);
                            finish();
                        }


                        //Intent i = new Intent(SplashscreenActivity.this, NV_LibraryActivity.class);

                    }

                }, 4000);

            }

            @SuppressLint("NewApi")
            private void initializeAds() {
                Log.e("Check Execution","First splash");

                //AudienceNetworkAds.initialize(SplashscreenActivity.this);

                switch (MyApplication.ad_network) {

                    case "applovins":
                        AppLovinSdk.getInstance(SplashscreenActivity.this).setMediationProvider(AppLovinMediationProvider.MAX);
                        AppLovinSdk.getInstance(SplashscreenActivity.this).initializeSdk(config -> {

                        });
                        break;

                    case "admob":
                        MobileAds.initialize(SplashscreenActivity.this, initializationStatus -> {
                            //appOpenManager = new AppOpenManager(SplashscreenActivity.this);
                            Log.d("OPEN Ads", "Execute Message From Splac Screen ");
                            //MyApplication.manageOpenAdsAdmob();
                            if(MyApplication.show_open_ads_admob > 0) {
                                ((MyApplication) getApplication()).manageOpenAdsAdmob();
                            }
                        });
                        break;
                }

            }

            @Override
            public void onFailure(Call<MultipleResource> call, Throwable t) {
                call.cancel();
                Log.d("API_CALL : ",t.getMessage());
            }
        });

    }


}

