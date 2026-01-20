package geetly.lyrical.videostatusmaker.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.RecyclerView;


import geetly.lyrical.videostatusmaker.MyApplication;
import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.activity.NV_VideoListActivity;
import geetly.lyrical.videostatusmaker.utils.Utils;
import geetly.lyrical.videostatusmaker.model.VideoviewModel;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;


import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;

import com.google.android.gms.ads.AdLoader;

import com.google.android.gms.ads.AdRequest;



import com.google.android.gms.ads.nativead.NativeAdView;

import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NvVideoViewAdapter extends RecyclerView.Adapter {


    ArrayList<VideoviewModel> videoArr;
    private Activity context;



    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_Ad = 2;

    public NvVideoViewAdapter(Activity context, ArrayList<VideoviewModel> arrayList) {
        this.context = context;
        this.videoArr = arrayList;

    }


    public static String removeLastChars(String str, int chars) {

        String string = str.substring(0, str.length() - chars);
        String lastWord = str.substring(str.lastIndexOf(" ") + 1);
        if (lastWord.trim().toLowerCase().equals("v")) {

            return string;

        } else {
            return str;
        }
    }


    public void setDataList(ArrayList<VideoviewModel> dataListMe) {
        this.videoArr = dataListMe;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(context).inflate(R.layout.layout_loading_item, parent, false);
            return new ProgressViewHolder(v);
        } else if (viewType == VIEW_TYPE_Ad) {
            View view = LayoutInflater.from(context).inflate(R.layout.adview_adapter, parent, false);
            return new AdOption(view);
        }
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        //Log.d("Recycle Position 125 VIEW TYPE ", String.valueOf(holder.getItemViewType()));
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {

            final ViewHolder viewHolder = (ViewHolder) holder;

            String videoTitle = (this.videoArr.get(i)).getTitle().trim().replaceAll("[0-9]", "").replace("_", " ").replace("boo", "");

            String titleCapital = removeLastChars(MyAppUtils.capitalize(videoTitle).trim(), 1);

            viewHolder.videoName.setText(titleCapital);

            if ((this.videoArr.get(i)).getIsPremium() > 0)
            {
                viewHolder.premiumThumb.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.premiumThumb.setVisibility(View.GONE);
            }

            Picasso.get().load((this.videoArr.get(i)).getVideoThumb() != null ? (this.videoArr.get(i)).getVideoThumb() : "").placeholder(R.drawable.bg_card).into(viewHolder.videoThumb);

            viewHolder.videoThumb.setOnClickListener(view -> {
                if (!MyAppUtils.isConnectingToInternet(context)) {
                    Toast.makeText(context, "Please Connect to Internet.", Toast.LENGTH_SHORT).show();
                } else if (NvVideoViewAdapter.this.videoArr.get(i) != null) {

                    // ✅ Show the loading overlay
                    FrameLayout loader = ((Activity) context).findViewById(R.id.loading_overlay);
                    if (loader != null) loader.setVisibility(View.VISIBLE);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Utils.static_video_model_data = videoArr.get(i);

                        VideoviewModel videoData = Utils.static_video_model_data;

                        if (videoData == null || videoData.getVideo_link() == null) {
                            Toast.makeText(context, "Slow Network Connection. Try Again.", Toast.LENGTH_SHORT).show();
                            if (loader != null) loader.setVisibility(View.GONE);
                            return;
                        }

                        MyAppUtils.showInterstitialAds(context);

                        context.startActivity(new Intent(context, NV_VideoListActivity.class)
                                .putExtra("mdata", videoArr)
                                .putExtra("position", i));

                        // ✅ Loader will be hidden in NV_VideoListActivity after video loads

                    }, 200); // Optional delay

                } else {
                    Toast.makeText(context, "Something went wrong! please check internet connection.", Toast.LENGTH_SHORT).show();
                }
            });


        } else if (holder.getItemViewType() == VIEW_TYPE_Ad) {
            AdOption adOption = (AdOption) holder;
            if (adOption.conAdView.getChildCount() == 0) {
                if (MyApplication.ad_network.equals("admob")) {

                    @SuppressLint("InflateParams") View view = context.getLayoutInflater().inflate(R.layout.admob_ad, null, true);

                    TemplateView templateView = view.findViewById(R.id.my_template);
                    if (templateView.getParent() != null) {
                        ((ViewGroup) templateView.getParent()).removeView(templateView); // <- fix
                    }
                    adOption.conAdView.addView(templateView);

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
                }   else if (MyApplication.ad_network.equals("applovins")) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    FrameLayout nativeAdLayout = (FrameLayout) inflater.inflate(R.layout.activity_native_max_template, adOption.conAdView, false);
                    MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(MyApplication.native_ad_id, context);
                    nativeAdLoader.loadAd();
                    nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                        @Override
                        public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                            super.onNativeAdLoaded(maxNativeAdView, maxAd);
                            // Add ad view to view.
                            nativeAdLayout.removeAllViews();
                            nativeAdLayout.addView(maxNativeAdView);
                            adOption.conAdView.addView(nativeAdLayout);
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
        else if (holder.getItemViewType() == VIEW_TYPE_LOADING)
        {
            this.hideHeader();
        }


    }

    @Override
    public int getItemCount() {
        return this.videoArr.size() + 1;
    }

    public void hideHeader() {
        ProgressViewHolder.progressBar.setVisibility(View.GONE);
    }


    @Override
    public int getItemViewType(int position) {
        if (position >= videoArr.size()) return VIEW_TYPE_LOADING;
        VideoviewModel model = videoArr.get(position);
        return model == null ? VIEW_TYPE_Ad : VIEW_TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView videoThumb;
        private ImageView premiumThumb;
        private TextView videoName;
        private FrameLayout fbPlaceHolder;
        private NativeAdView fbNativeAsContainer;


        public ViewHolder(View view) {
            super(view);
            videoThumb = (ImageView) view.findViewById(R.id.video_thumb);
            premiumThumb = (ImageView) view.findViewById(R.id.premium_icon);
            videoName = (TextView) view.findViewById(R.id.video_name);
            fbPlaceHolder = (FrameLayout) view.findViewById(R.id.fl_adplaceholder);
            fbNativeAsContainer = (NativeAdView) view.findViewById(R.id.unified);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public static ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar_loading);
        }
    }

    public class AdOption extends RecyclerView.ViewHolder {

        private ConstraintLayout conAdView;

        public AdOption(View itemView) {
            super(itemView);
            conAdView = itemView.findViewById(R.id.con_adView);
        }
    }

}



