package geetly.lyrical.videostatusmaker;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.media3.common.util.UnstableApi;
import androidx.media3.database.ExoDatabaseProvider;
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor;
import androidx.media3.datasource.cache.SimpleCache;

import geetly.lyrical.videostatusmaker.activity.SplashscreenActivity;
import geetly.lyrical.videostatusmaker.utils.AppOpenManager;
import geetly.lyrical.videostatusmaker.utils.Util_StringShared;
import geetly.lyrical.videostatusmaker.utils.Utils_Shareddata;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import org.json.JSONObject;

import java.io.File;

@UnstableApi
public class MyApplication extends Application {
    public static boolean Click = false;
    public static SimpleCache simpleCache = null;
    public static Long exoPlayerCacheSize = (long) (90 * 1024 * 1024);
    public static LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = null;
    public static ExoDatabaseProvider exoDatabaseProvider = null;
    public Context context;
    public FirebaseRemoteConfig mFirebaseRemoteConfig;
    private static final String ONESIGNAL_APP_ID = "d826ab12-7d5d-4b22-b003-6003e381c878";

    public static int force_update = 0;


    public static int show_inter_new = 1;
    public static int interstitial_call = 0;



    public static String ad_network = "";
    public static String admob_publisher_id = "";


    public static int show_open_ads_admob = 0;
    public static int show_banner = 0;
    public static int show_inter = 0;
    public static int show_native = 0;
    public static int show_reward = 0;

    public static int interstitial_show_count = 3;
    public static int native_show_count = 5;

    public static String banner_ad_id = "";
    public static String native_ad_id = "";
    public static String interstitial_ad_id = "";
    public static String reward_ad_id = "";

    public static String unity_game_id = "";
    public static String startapp_app_id = "";

    public static int  app_update_status = 0;
    public static int  app_new_version = 1;
    public static String app_update_desc = "";
    public static String app_redirect_url = "";
    public static boolean cancel_update_status = false;

    public static boolean personalizationAd = false;

    private static AppOpenManager appOpenManager;

    public static int[] unlocked_premium_template_array = new int[100000]; // array for three ints


    public static String page_from = "";
    public static String search_text = "";

    @UnstableApi
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable verbose OneSignal START
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.setNotificationOpenedHandler(new NotificationHandler());
        //OneSignal.setNotificationWillShowInForegroundHandler(new NotificationWillShowInForegroundHandler());
        // Enable verbose OneSignal END

        Utils_Shareddata.init(this);
        initializeFirebase();
        new Thread(this::FirebaseConfig).start();
        try {
            context = this;
        } catch (Exception e) {
            e.printStackTrace();
        }
        FirebaseApp.initializeApp(this);


    }

    private class NotificationHandler implements OneSignal.OSNotificationOpenedHandler {

        String message, bigpicture, title, video_id,video_thumb,video_link,video_zip,video_category,payment_withdraw, url;

        @Override
        public void notificationOpened(OSNotificationOpenedResult result) {
            try {
                JSONObject data = result.getNotification().getAdditionalData();

                String webViewUrl = (data != null) ? data.optString("url", null) : null;

                String browserUrl = result.getNotification().getLaunchURL();


                Log.v("INFO", "Received 1 notification while app was on foreground or url for browser" + data.toString());
                Log.v("INFO", "Video Zip"+data.getString("video_zip"));

                video_id = data.getString("id");
                video_category = data.getString("video_category");
                title = data.getString("title");
                video_thumb = data.getString("video_thumb");
                video_link = data.getString("video_link");
                video_zip = data.getString("video_zip");

                Log.v("INFO", "Video Zip => " + video_zip + "Video link => " + video_link + " Video thumb => " + video_thumb + " title=> " + title + " video_category=> " + video_category + " video_id=> " + video_id);

                Log.v("INFO", "webViewUrl => " + webViewUrl + "browserUrl => " + browserUrl);

                if ( (!TextUtils.isEmpty(title))) {

                    Log.v("INFO", "Open Activity Based on datas");
                    Intent mainIntent;

                    mainIntent = new Intent(MyApplication.this, SplashscreenActivity.class);
                    mainIntent.putExtra("video_id",video_id);
                    mainIntent.putExtra("title",title);
                    mainIntent.putExtra("video_category",video_category);
                    mainIntent.putExtra("video_link",video_link);
                    mainIntent.putExtra("video_zip",video_zip);
                    mainIntent.putExtra("video_thumb",video_thumb);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mainIntent);

                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }


    public void manageOpenAdsAdmob()
    {
        Log.d("OPEN Ads", "Execute Message From Apppicaton ");
        appOpenManager = new AppOpenManager(MyApplication.this);
    }


    @UnstableApi
    public void FirebaseConfig() {
        try {
            mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this::setupFirebaseConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeFirebase() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600) // Example: set fetch interval
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    }

    public void CacheClear() {

        try {
            File dir = getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


    @UnstableApi
    public void setupFirebaseConfig(Task task) {
        if (task.isSuccessful()) {
            String string = mFirebaseRemoteConfig.getString(getResources().getString(R.string.Kotlins_api));
            String string2 = mFirebaseRemoteConfig.getString(getResources().getString(R.string.Kotlins_key));
            if (TextUtils.isEmpty(Utils_Shareddata.get(Util_StringShared.MYGST_API))) {
                Utils_Shareddata.set(Util_StringShared.MYGST_API, string);
            }
            if (TextUtils.isEmpty(Utils_Shareddata.get(Util_StringShared.MYGST_KEY))) {
                Utils_Shareddata.set(Util_StringShared.MYGST_KEY, string2);
            }
        }


        if (leastRecentlyUsedCacheEvictor == null) {
            leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize);
        }

        if (exoDatabaseProvider != null) {
            exoDatabaseProvider = new ExoDatabaseProvider(this);
        }

        if (simpleCache == null) {
            simpleCache = new SimpleCache(getCacheDir(), leastRecentlyUsedCacheEvictor, exoDatabaseProvider);
            if (simpleCache.getCacheSpace() >= 400207768) {
                CacheClear();
            }
            Log.i("TAG", "onCreate: " + simpleCache.getCacheSpace());
        }


    }
    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }



}


