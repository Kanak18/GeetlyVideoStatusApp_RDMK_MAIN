package geetly.lyrical.videostatusmaker.utils;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import geetly.lyrical.videostatusmaker.MyApplication;

import java.util.Date;



public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks{
    private static final String LOG_TAG = "AppOpenManager";

    //private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294";
    private static final String AD_UNIT_ID = "ca-app-pub-4239310076591383/8873011874";

    private AppOpenAd appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private Activity currentActivity;
    private final MyApplication myApplication;
    private static boolean isShowingAd = false;
    private long loadTime = 0;
    /** Constructor
     * @param myApplication*/


    public AppOpenManager(MyApplication myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    /** LifecycleObserver methods */
    @OnLifecycleEvent(ON_START)
    public void onStart() {
        showAdIfAvailable();
        //Log.d(LOG_TAG, "onStart Line 51");
    }
    /** Request an ad */
    public void fetchAd() {
        // We will implement this below.
        // Have unused ad, no need to fetch another.
        // Log.d(LOG_TAG, "Fetchin Ad Called Line 57");
        if (isAdAvailable()) {
            return;
        }
        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     *
                     * @param ad the loaded app open ad.
                     */
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        AppOpenManager.this.appOpenAd = ad;
                        AppOpenManager.this.loadTime = (new Date()).getTime();
                        // Log.d(LOG_TAG, "Ads loaded and Line 73 @ Time "+ (new Date()).getTime());
                    }
                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        // Handle the error.
                    }
                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(myApplication, AD_UNIT_ID, request,loadCallback);

    }
    public void showAdIfAvailable() {
// and an ad is available.
        //Log.d(LOG_TAG, "showAdIfAvailable Line 88 "+ this.loadTime);
        if (!isShowingAd && isAdAvailable()) {
            // Log.d(LOG_TAG, "Will show ad.");
            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            AppOpenManager.this.appOpenAd = null;
                            isShowingAd = false;
                            fetchAd();
                        }
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {}
                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };
            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(currentActivity);
        } else {
            //Log.d(LOG_TAG, "Can not show ad.  Line 111");
            if(appOpenAd == null)
            {
                //Log.d(LOG_TAG, "Can not show ad. appOpenAd nulled so fetchAd Called Line 114");
                fetchAd();
            }
        }
    }
    /** Creates and returns ad request. */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 30000;
        // Log.d(LOG_TAG, "wasLoadTimeLessThanNHoursAgo  dateDifference "+dateDifference+" Line 130");
        return (dateDifference > (numMilliSecondsPerHour));
    }

    /** Check if ad exists and can be shown. */
    public boolean isAdAvailable() {
        if(appOpenAd != null)
        {
            if(wasLoadTimeLessThanNHoursAgo(4))
            {
                // Log.d(LOG_TAG, "isAdAvailable return value true  Line 137");
                return true;
            }
            else
            {
                //Log.d(LOG_TAG, "isAdAvailable return value true  Line 142");
                return false;
            }
        }
        else
        {
            // Log.d(LOG_TAG, "isAdAvailable return value false  Line 148");
            return false;
        }
        //return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    /** ActivityLifecycleCallback methods */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }
    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }
    @Override
    public void onActivityStopped(Activity activity) {}
    @Override
    public void onActivityPaused(Activity activity) {}
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}
    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }
}
