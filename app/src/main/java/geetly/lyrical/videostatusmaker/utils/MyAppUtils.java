package geetly.lyrical.videostatusmaker.utils;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowInsets;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import geetly.lyrical.videostatusmaker.MyApplication;
import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.activity.NonNull;
import geetly.lyrical.videostatusmaker.activity.Nullable;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.CacheFlag;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static android.content.Context.CONNECTIVITY_SERVICE;
import static geetly.lyrical.videostatusmaker.MyApplication.banner_ad_id;
import static geetly.lyrical.videostatusmaker.MyApplication.interstitial_ad_id;
import static geetly.lyrical.videostatusmaker.MyApplication.show_inter_new;


public class MyAppUtils {

    public static final String FOLDER_NAME = "MVMaker";
    public static final File RootDirectoryWhatsappShow = new File(Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME + "/WhatsappStatus");


    // Add Your mobile test id here
    public static final String ADMOB_TEST_ID = "5186C6A964A218A7D3A51F4E3D7C6251";

    // Want to turn on Unity Ads? Make it false here
    public static final boolean ENABLE_UNITY_TEST_ADS = true;

    // Enter your PrivacyUrl
    public static final String PRIVACY_URL = "https://sites.google.com/view/geetly-lyrical-video-status/";

    // Create your Unity App Id, from Unity Dashboard(google it) and Add here.
    public static final String UNITY_APP_ID = "54542";

    // Turn on this if you want to serve ads.
    public static final Boolean IS_AD_ENABLED = true;
    public static InterstitialAd admobBannerView;

    private static boolean isAdShown = false;
    private static AdView adView;

    public static boolean personalizationAd = false;

    public static void createFileFolder() {

        if (!RootDirectoryWhatsappShow.exists()) {
            RootDirectoryWhatsappShow.mkdirs();
        }
    }

    public static boolean isConnectingToInternet(Context context) {
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean verifyInstallerId(Context context) {
        // A list with valid installers package name
        List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback", "geetly.lyrical.videostatusmaker"));

        // The package name of the app that has installed your app
        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());

        // true if your app has been downloaded from Play Store
        return installer != null && validInstallers.contains(installer);
    }



    private static AdSize getAdSize(Activity activity, FrameLayout adContainerView) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = activity.getResources().getDisplayMetrics().density;
        float widthPixels = activity.getResources().getDisplayMetrics().widthPixels;   // outMetrics.widthPixels;
        //float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        //Toast.makeText(activity, "No application Ads Width" + adWidth + " Dencity "+ density, Toast.LENGTH_LONG).show();

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }



    @SuppressLint("MissingPermission")
    public static void showBannerAds(Context context, FrameLayout mAdViewLayout) {

        if (MyApplication.show_banner <= 0) {
            mAdViewLayout.setVisibility(View.GONE);
            return;
        }

        if (!"admob".equals(MyApplication.ad_network)) {
            return;
        }

        if (!(context instanceof Activity)) {
            return;
        }

        Activity activity = (Activity) context;

        AdView mAdView = new AdView(activity);
        mAdView.setAdUnitId(banner_ad_id);

        AdSize adSize = getAdSize(activity, mAdViewLayout);
        mAdView.setAdSize(adSize);

        // ✅ Build AdRequest (Google handles GDPR internally)
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdViewLayout.removeAllViews();
        mAdViewLayout.addView(mAdView);
        mAdViewLayout.bringToFront();

        mAdView.loadAd(adRequest);
    }



    public static void setStatusBarTransparentFlag(Activity context) {

        View decorView = context.getWindow().getDecorView();
        decorView.setOnApplyWindowInsetsListener((v, insets) -> {

            WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
            return defaultInsets.replaceSystemWindowInsets(
                    defaultInsets.getSystemWindowInsetLeft(),
                    0,
                    defaultInsets.getSystemWindowInsetRight(),
                    defaultInsets.getSystemWindowInsetBottom());

        });
        ViewCompat.requestApplyInsets(decorView);
        context.getWindow().setStatusBarColor(ContextCompat.getColor(context, android.R.color.transparent));

    }



    public static String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString() + " ";

    }

    public static void shareImage(Context context, String filePath) {


        String appLink = "\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName();


        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.share_txt) + appLink);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), filePath, "", null);
            Uri screenshotUri = Uri.parse(path);
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/*");
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share_image_via)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void setUpRecycler(RecyclerView recyclerView) {


        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public static void shareImageVideoOnWhatsapp(Context context, String filePath, boolean isVideo) {
        // Uri imageUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",  new File(filePath));
        // Uri imageUri = FileProvider.getUriForFile(context, "geetly.lyrical.videostatusmaker.provider", new File(filePath));
        //Uri imageUri = Uri.parse(filePath);
        Uri imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(filePath));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share) + context.getPackageName());
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        if (isVideo) {
            shareIntent.setType("video/*");
        } else {
            shareIntent.setType("image/*");
        }
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(shareIntent);
        } catch (Exception e) {
            setToast(context, "Whtasapp not installed.");
        }
    }

    public static void shareVideo(Context context, String filePath) {
        Uri mainUri = Uri.parse(filePath);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("video/mp4");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, mainUri);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No application found to open this file.", Toast.LENGTH_LONG).show();
        }
    }

    public static void setToast(Context context, String str) {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void rateApp(Context context) {
        final String appName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        }
    }


    @UnstableApi
    public static void showInterstitialAds(Activity activity)
    {

        if ((show_inter_new > 0) && (activity != null)) {


            MyApplication.interstitial_call++;

           // Log.d("ADSDEBUG"," Interstitial Count (Callled) "+ MyApplication.interstitial_call+ " Show At"+MyApplication.interstitial_show_count);

            if (MyApplication.interstitial_call > MyApplication.interstitial_show_count)
            {

                MyApplication.interstitial_call=0;

                if (MyApplication.ad_network.equals("admob"))
                {
                    AdRequest adRequest;
                    if (personalizationAd) {
                        adRequest = new AdRequest.Builder()
                                .build();
                    } else {
                        Bundle extras = new Bundle();
                        extras.putString("npa", "1");
                        adRequest = new AdRequest.Builder()
                                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                                .build();
                    }

                    InterstitialAd.load(activity, interstitial_ad_id, adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                           // Log.i("admob_error", "onAdLoaded");
                            //progressDialog.dismiss();
                            interstitialAd.show(activity);
                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Called when fullscreen content is dismissed.
                                   // Log.e("TAG", "The ad was dismissed.");
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                                    // Called when fullscreen content failed to show.
                                   // Log.e("TAG", "The ad failed to show.");
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when fullscreen content is shown.
                                    // Make sure to set your reference to null so you don't
                                    // show it a second time.
                                   // Log.e("TAG", "The ad was shown.");
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                           // Log.i("admob_error", loadAdError.getMessage());
                            //progressDialog.dismiss();
                        }
                    });

                }  else if (MyApplication.ad_network.equals("applovins")) {
                    MaxInterstitialAd maxInterstitialAd = new MaxInterstitialAd(interstitial_ad_id, (Activity) activity);
                    maxInterstitialAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(MaxAd ad) {
                            //progressDialog.dismiss();
                            maxInterstitialAd.showAd();
                        }

                        @Override
                        public void onAdDisplayed(MaxAd ad) {
                        }

                        @Override
                        public void onAdHidden(MaxAd ad) {
                            //progressDialog.dismiss();
                        }

                        @Override
                        public void onAdClicked(MaxAd ad) {
                        }

                        @Override
                        public void onAdLoadFailed(String adUnitId, MaxError error) {
                            //progressDialog.dismiss();

                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                            //progressDialog.dismiss();

                        }
                    });
                    // Load the first ad
                    maxInterstitialAd.loadAd();
                }
            }

        }

    }


    public static void loadAdmobNativeAd(final FrameLayout adHolderView, Activity context) {

        if (MyApplication.ad_network.equals("admob")) {
            @SuppressLint("InflateParams") View view = context.getLayoutInflater().inflate(R.layout.admob_ad, null, true);
            TemplateView templateView = view.findViewById(R.id.my_template);
            if (templateView.getParent() != null) {
                ((ViewGroup) templateView.getParent()).removeView(templateView); // <- fix
            }
            adHolderView.removeAllViews();
            adHolderView.addView(templateView);
            adHolderView.setVisibility(View.VISIBLE);

            AdLoader adLoader = new AdLoader.Builder(context, MyApplication.native_ad_id)
                    .forNativeAd(nativeAd -> {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder()
                                .build();

                        templateView.setStyles(styles);
                        templateView.setNativeAd(nativeAd);

                    })
                    .build();

            AdRequest adRequest;
            if (MyApplication.personalizationAd) {
                adRequest = new AdRequest.Builder()
                        .build();
            } else {
                Bundle extras = new Bundle();
                extras.putString("npa", "1");
                adRequest = new AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                        .build();
            }
            adLoader.loadAd(adRequest);
        } else if (MyApplication.ad_network.equals("applovins")) {
            LayoutInflater inflater = LayoutInflater.from(context);
            FrameLayout nativeAdLayout = (FrameLayout) inflater.inflate(R.layout.activity_native_max_template, adHolderView, false);
            MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(MyApplication.native_ad_id, context);
            nativeAdLoader.loadAd();
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                    super.onNativeAdLoaded(maxNativeAdView, maxAd);
                    // Add ad view to view.
                    nativeAdLayout.removeAllViews();
                    nativeAdLayout.addView(maxNativeAdView);
                    adHolderView.addView(nativeAdLayout);
                }

                @Override
                public void onNativeAdLoadFailed(String s, MaxError maxError) {
                    super.onNativeAdLoadFailed(s, maxError);
                }

                @Override
                public void onNativeAdClicked(MaxAd maxAd) {
                    super.onNativeAdClicked(maxAd);
                }
            });
        }
    }


}