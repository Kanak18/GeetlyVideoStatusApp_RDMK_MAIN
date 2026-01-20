package geetly.lyrical.videostatusmaker.adapter;

import static geetly.lyrical.videostatusmaker.MyApplication.native_ad_id;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.facebook.ads.NativeAdLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader.Builder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

import geetly.lyrical.videostatusmaker.MyApplication;
import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.activity.NV_SaveVideoFileActivity;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;

public class NvAlbumAdapterBackup extends Adapter<NvAlbumAdapterBackup.MyViewHolder> {
    private Context mContext;
    private ArrayList<String> arrayList;
    private NativeAd nativeAd;

    private Dialog dialog;

    public NvAlbumAdapterBackup(Context context, ArrayList<String> arrayList) {
        this.mContext = context;
        this.arrayList = arrayList;
    }


    private void nativeAd(final MyViewHolder myViewHolder, final int i) {
        Context context = this.mContext;
        Builder builder = new Builder(context, native_ad_id);
        builder.forNativeAd(unifiedNativeAd -> {
            if (NvAlbumAdapterBackup.this.nativeAd != null) {
                NvAlbumAdapterBackup.this.nativeAd.destroy();
            }
            NvAlbumAdapterBackup.this.nativeAd = unifiedNativeAd;

            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(NvAlbumAdapterBackup.this.mContext).inflate(R.layout.ad_creation, null);

            NvAlbumAdapterBackup.this.populateUnifiedNativeAdView(unifiedNativeAd, (NativeAdView) relativeLayout.findViewById(R.id.unified));

            try {
                myViewHolder.adplace.removeAllViews();
                myViewHolder.adplace.addView(relativeLayout);
            } catch (Exception unused) {
                unused.getStackTrace();
            }
        });
        builder.withNativeAdOptions(new NativeAdOptions.Builder().setVideoOptions(new VideoOptions.Builder().setStartMuted(true).build()).build());
        builder.withAdListener(new AdListener() {
            public void onAdFailedToLoad(int i) {
                // NvAlbumAdapter.this.loadFBAds(myViewHolder, i);
            }
        }).build().loadAd(new AdRequest.Builder().build());
    }

    private void populateUnifiedNativeAdView(NativeAd u_nativead, NativeAdView u_nativeAadview){
        u_nativeAadview.setMediaView((MediaView) u_nativeAadview.findViewById(R.id.ad_media));
        u_nativeAadview.setHeadlineView(u_nativeAadview.findViewById(R.id.ad_headline));
        u_nativeAadview.setBodyView(u_nativeAadview.findViewById(R.id.ad_body));
        u_nativeAadview.setCallToActionView(u_nativeAadview.findViewById(R.id.ad_call_to_action));
        u_nativeAadview.setIconView(u_nativeAadview.findViewById(R.id.ad_app_icon));
        u_nativeAadview.setPriceView(u_nativeAadview.findViewById(R.id.ad_price));
        u_nativeAadview.setStarRatingView(u_nativeAadview.findViewById(R.id.ad_stars));
        u_nativeAadview.setStoreView(u_nativeAadview.findViewById(R.id.ad_store));
        u_nativeAadview.setAdvertiserView(u_nativeAadview.findViewById(R.id.ad_advertiser));
        ((TextView) u_nativeAadview.getHeadlineView()).setText(u_nativead.getHeadline());
        if (u_nativead.getBody() == null) {
            u_nativeAadview.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            u_nativeAadview.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) u_nativeAadview.getBodyView()).setText(u_nativead.getBody());
        }
        if (u_nativead.getCallToAction() == null) {
            u_nativeAadview.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            u_nativeAadview.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) u_nativeAadview.getCallToActionView()).setText(u_nativead.getCallToAction());
        }
        if (u_nativead.getIcon() == null) {
            u_nativeAadview.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) u_nativeAadview.getIconView()).setImageDrawable(u_nativead.getIcon().getDrawable());
            u_nativeAadview.getIconView().setVisibility(View.VISIBLE);
        }
        if (u_nativead.getPrice() == null) {
            u_nativeAadview.getPriceView().setVisibility(View.GONE);
        } else {
            u_nativeAadview.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) u_nativeAadview.getPriceView()).setText(u_nativead.getPrice());
        }
        if (u_nativead.getStore() == null) {
            u_nativeAadview.getStoreView().setVisibility(View.GONE);
        } else {
            u_nativeAadview.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) u_nativeAadview.getStoreView()).setText(u_nativead.getStore());
        }
        if (u_nativead.getStarRating() == null) {
            u_nativeAadview.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) u_nativeAadview.getStarRatingView()).setRating(u_nativead.getStarRating().floatValue());
            u_nativeAadview.getStarRatingView().setVisibility(View.VISIBLE);
        }
        if (u_nativead.getAdvertiser() == null) {
            u_nativeAadview.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) u_nativeAadview.getAdvertiserView()).setText(u_nativead.getAdvertiser());
            u_nativeAadview.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        u_nativeAadview.setNativeAd(u_nativead);
        VideoController videoController = u_nativead.getMediaContent().getVideoController();
        if (videoController.hasVideoContent()) {
            videoController.setVideoLifecycleCallbacks(new VideoLifecycleCallbacks() {
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    public int getItemViewType(int i) {
        return this.arrayList.get(i) != null ? 1 : 0;
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        int itemViewType = myViewHolder.getItemViewType();
        if (itemViewType == 0) {

            if(MyApplication.show_native > 0) {
                nativeAd(myViewHolder, i);
            }

        } else if (itemViewType == 1) {


            Glide.with(this.mContext).load(this.arrayList.get(i)).placeholder(R.drawable.bg_card).into(myViewHolder.myimage);


            myViewHolder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(NvAlbumAdapterBackup.this.mContext, NV_SaveVideoFileActivity.class);
                intent.putExtra("videourl", NvAlbumAdapterBackup.this.arrayList.get(i));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NvAlbumAdapterBackup.this.mContext.startActivity(intent);
            });


            myViewHolder.delete.setOnClickListener(view -> {
                setupDialog(myViewHolder);
            });
        }
    }


    public void setupDialog(ViewHolder myViewHolder) {
        dialog = new Dialog(mContext);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(1);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete_video);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCanceledOnTouchOutside(false);

        if(MyApplication.show_native > 0) {
            dialog.findViewById(R.id.frame_ad_container).setVisibility(View.VISIBLE);
            //loadNativeAds((FrameLayout) dialog.findViewById(R.id.fl_adplaceholder), (NativeAdView) dialog.findViewById(R.id.unified));
            MyAppUtils.loadAdmobNativeAd((FrameLayout) dialog.findViewById(R.id.fl_adplaceholder), (Activity) this.mContext);
        }
        else
        {
            dialog.findViewById(R.id.frame_ad_container).setVisibility(View.GONE);
        }


        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(view -> {
            //Toast.makeText(context, "No Pressed found", Toast.LENGTH_SHORT).show();
            if (dialog.isShowing()) dialog.dismiss();
        });

        Button btnYes = dialog.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(view -> {

            int item = myViewHolder.getAbsoluteAdapterPosition();

            if (NvAlbumAdapterBackup.this.arrayList.size() > item) {
               // Log.e("Removed","Item tobe removed"+item);
                removeAt(item);
                if (dialog.isShowing()) dialog.dismiss();
            }
            return;
        });
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    public void removeAt(int position) {
        //Toast.makeText(mContext," Removing Element"+position, Toast.LENGTH_SHORT).show();

        String sdCardUri = NvAlbumAdapterBackup.this.arrayList.get(position);
        File pathFile = new File(sdCardUri);

        if (pathFile.exists()) {
            if (pathFile.delete()) {
               // Log.e("Removed DELETE FILE:",pathFile.getPath());
            } else {
               // Log.e("Removed NOT DELETE FILE:",pathFile.getPath());
            }
        }

        NvAlbumAdapterBackup.this.arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, NvAlbumAdapterBackup.this.arrayList.size());
    }

    @NotNull
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        View inflate;
        if (i == 0) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ad_view, viewGroup, false);
        } else if (i != 1) {
            inflate = null;
        } else {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album, viewGroup, false);
        }
        return new MyViewHolder(inflate);
    }

    public static class MyViewHolder extends ViewHolder {
        private ImageView myimage;
        private ImageView delete;
        private FrameLayout adplace;
        private NativeAdLayout nativeAdLayout;

        public MyViewHolder(View view) {
            super(view);
            this.myimage = view.findViewById(R.id.c_myImage);
            this.delete = view.findViewById(R.id.delete);
            this.adplace = view.findViewById(R.id.fl_adplaceholder);
            this.nativeAdLayout = (NativeAdLayout) view.findViewById(R.id.unified);
        }
    }
}
