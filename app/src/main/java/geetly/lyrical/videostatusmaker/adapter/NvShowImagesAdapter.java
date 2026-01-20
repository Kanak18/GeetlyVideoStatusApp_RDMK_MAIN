package geetly.lyrical.videostatusmaker.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.activity.NV_FullViewDataActivity;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;



public class NvShowImagesAdapter extends PagerAdapter {
    NV_FullViewDataActivity fullViewDataActivity;
    private Context context;
    private ArrayList<File> imageList;
    private LayoutInflater inflater;

    public NvShowImagesAdapter(Context context, ArrayList<File> imageList, NV_FullViewDataActivity fullViewDataActivity) {
        this.context = context;
        this.imageList = imageList;
        this.fullViewDataActivity = fullViewDataActivity;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView((View) object);
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_image_status_view, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.im_fullViewImage);
        final ImageView imVpPlay = imageLayout.findViewById(R.id.im_vpPlay);
        final ImageView iv_share = imageLayout.findViewById(R.id.im_share);
        final ImageView iv_delete = imageLayout.findViewById(R.id.im_delete);


        Glide.with(context).load(imageList.get(position).getPath()).into(imageView);
        view.addView(imageLayout, 0);
        String extension = imageList.get(position).getName().substring(imageList.get(position).getName().lastIndexOf("."));
        if (extension.equals(".mp4")) {
            imVpPlay.setVisibility(View.VISIBLE);
        } else {
            imVpPlay.setVisibility(View.GONE);
        }

        imVpPlay.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(imageList.get(position).getPath()), "video/*");
            context.startActivity(intent);
        });

        iv_delete.setOnClickListener(v -> {
            boolean b = imageList.get(position).delete();
            if (b) {
                fullViewDataActivity.deleteFileAA(position);
            }
        });

        iv_share.setOnClickListener(v -> {
            String extension1 = imageList.get(position).getName().substring(imageList.get(position).getName().lastIndexOf("."));
            if (extension1.equals(".mp4")) {
                MyAppUtils.shareVideo(context, imageList.get(position).getPath());
            } else {
                MyAppUtils.shareImage(context, imageList.get(position).getPath());
            }
        });


        return imageLayout;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, @NotNull Object object) {
        return view.equals(object);
    }


}