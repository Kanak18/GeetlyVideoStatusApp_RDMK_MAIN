package geetly.lyrical.videostatusmaker.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.gms.ads.nativead.NativeAdView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.activity.NV_VideoListActivity;
import geetly.lyrical.videostatusmaker.model.VideoviewModel;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;
import geetly.lyrical.videostatusmaker.utils.Utils;

public class NvVideoViewAdapter1 extends Adapter<NvVideoViewAdapter1.MyViewHolder> {
    ArrayList<VideoviewModel> videoArr;
    private Activity context;
    EventListener mEventListener;

    public NvVideoViewAdapter1(Activity context, ArrayList<VideoviewModel> arrayList) {
        this.context = context;
        this.videoArr = arrayList;
    }

    public interface EventListener {

        void onItemViewClick(int position);

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


    @Override
    public int getItemCount() {
        return this.videoArr.size();
    }


//    @Override
//    public int getItemViewType(int i) {
//        return this.videoArr.get(i) != null ? 1 : 0;
//    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {

        int itemViewType = myViewHolder.getItemViewType();

//        mEventListener.onItemViewClick(position);

        String videoTitle = (this.videoArr.get(i)).getTitle().trim().replaceAll("[0-9]", "").replace("_", " ").replace("boo", "");

        String titleCapital = removeLastChars(MyAppUtils.capitalize(videoTitle).trim(), 1);

        if (i == 0) {
            myViewHolder.rv_back.setBackgroundResource(R.drawable.gradient1);
        }else if(i == 1){
            myViewHolder.rv_back.setBackgroundResource(R.drawable.gradient2);
        }else if(i == 2){
            myViewHolder.rv_back.setBackgroundResource(R.drawable.gradient3);
        }else if(i == 3){
            myViewHolder.rv_back.setBackgroundResource(R.drawable.gradient4);
        }else if(i == 4){
            myViewHolder.rv_back.setBackgroundResource(R.drawable.gradient5);
        }else if(i == 5){
            myViewHolder.rv_back.setBackgroundResource(R.drawable.gradient6);
        }else if(i == 6){
            myViewHolder.rv_back.setBackgroundResource(R.drawable.gradient1);
        }else if(i == 7){
            myViewHolder.rv_back.setBackgroundResource(R.drawable.gradient2);
        }else if(i == 8){
            myViewHolder.rv_back.setBackgroundResource(R.drawable.gradient3);
        }else if(i == 9){
            myViewHolder.rv_back.setBackgroundResource(R.drawable.gradient4);
        }else if(i == 10){
            myViewHolder.rv_back.setBackgroundResource(R.drawable.gradient5);
        }
        Picasso.get().load((this.videoArr.get(i)).getVideoThumb() != null ? (this.videoArr.get(i)).getVideoThumb() : "").into(myViewHolder.videoThumb);

        myViewHolder.videoThumb.setOnClickListener(view -> {
            if (!MyAppUtils.isConnectingToInternet(context)) {

                Toast.makeText(NvVideoViewAdapter1.this.context, "Please Connect to Internet.", Toast.LENGTH_SHORT).show();


            } else if (NvVideoViewAdapter1.this.videoArr.get(i) != null) {

                Utils.static_video_model_data = NvVideoViewAdapter1.this.videoArr.get(i);

                VideoviewModel videoviewModelData = Utils.static_video_model_data;

                if (videoviewModelData == null || videoviewModelData.getVideo_link() == null) {
                    Toast.makeText(NvVideoViewAdapter1.this.context, "Slow Network Connection. Try Again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // MyAppUtils.showInterstitialAds(this.context);

                NvVideoViewAdapter1.this.context.startActivity(new Intent(NvVideoViewAdapter1.this.context, NV_VideoListActivity.class)
                        .putExtra("mdata", videoArr)
                        .putExtra("position", i));

            } else {
                Toast.makeText(NvVideoViewAdapter1.this.context, "Something went wrong! please check internet connection.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        View inflate;

        inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video1, viewGroup, false);

        return new MyViewHolder(inflate);
    }

    public static class MyViewHolder extends ViewHolder {
        private ImageView videoThumb;
        private FrameLayout fbPlaceHolder;
        private NativeAdView fbNativeAsContainer;
        RelativeLayout rv_back;

        public MyViewHolder(View view) {
            super(view);

            videoThumb = (ImageView) view.findViewById(R.id.video_thumb);
            fbPlaceHolder = (FrameLayout) view.findViewById(R.id.fl_adplaceholder);
            fbNativeAsContainer = (NativeAdView) view.findViewById(R.id.unified);
            rv_back = view.findViewById(R.id.rv_back);
        }
    }

    public void setEventListener(EventListener mEventListener) {
        this.mEventListener = mEventListener;
    }

}
