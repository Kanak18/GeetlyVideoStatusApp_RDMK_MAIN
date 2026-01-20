package geetly.lyrical.videostatusmaker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.adapter.NvShowImagesAdapter;
import geetly.lyrical.videostatusmaker.fragments.WhatsappSavedFragment;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;


import com.google.android.gms.ads.AdView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NV_FullViewDataActivity extends AppCompatActivity {
    public String s_FilePath = MyAppUtils.RootDirectoryWhatsappShow + "/";
    NvShowImagesAdapter nvShowImagesAdapter;
    String fileName = "";
    Activity context;
    private NV_FullViewDataActivity activity;
    private ArrayList<File> fileArrayList;
    private int position = 0;
    private int Status = 0;
    private ViewPager vpView;
    private ImageView iv_close;
    private ImageView iv_share;
    private ImageView iv_whatsappshare;
    private ImageView iv_delete;
    private AdView adView;
    private FrameLayout ll_fbbanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view);

        vpView =  findViewById(R.id.vp_view);
        iv_close =  findViewById(R.id.iv_close);
        iv_share =  findViewById(R.id.iv_share);
        iv_whatsappshare =  findViewById(R.id.iv_whatsappshare);
        iv_delete =  findViewById(R.id.iv_delete);

        ll_fbbanner =  findViewById(R.id.ll_fbbanner);
        activity = this;





        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fileArrayList = (ArrayList<File>) getIntent().getSerializableExtra("ImageDataFile");
            position = getIntent().getIntExtra("Position", 0);
        }


        Status = getIntent().getExtras().getInt(WhatsappSavedFragment.isStatus);
        fileName = fileArrayList.get(position).getName().substring(12);
        initViews();

        if (new File(s_FilePath + fileName).exists()) {
            if (Status == 0) {

                iv_delete.setImageResource(R.drawable.ic_saved);

            } else {
                iv_delete.setImageResource(R.drawable.ic_delete_black_24dp);
            }
        }

        MyAppUtils.showBannerAds(this,ll_fbbanner);

    }

    public void initViews() {
        nvShowImagesAdapter = new NvShowImagesAdapter(this, fileArrayList, NV_FullViewDataActivity.this);
        vpView.setAdapter(nvShowImagesAdapter);
        vpView.setCurrentItem(position);
        vpView.setPageTransformer(true, new ZoomOutSlideTransformer());

        vpView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int arg, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                NV_FullViewDataActivity.this.position = position;

                fileName = fileArrayList.get(NV_FullViewDataActivity.this.position).getName().substring(12);
                if (new File(s_FilePath + fileArrayList.get(NV_FullViewDataActivity.this.position).getName().substring(12)).exists()) {

                    if (Status == 0) {

                        iv_delete.setImageResource(R.drawable.ic_saved);

                    } else {

                        iv_delete.setImageResource(R.drawable.ic_delete_black_24dp);

                    }

                } else {
                    if (Status == 0) {

                        iv_delete.setImageResource(R.drawable.ic_download);

                    } else {

                        iv_delete.setImageResource(R.drawable.ic_delete_black_24dp);

                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if (Status == 0) {

            iv_delete.setImageResource(R.drawable.ic_download);
            iv_delete.setOnClickListener(view -> {

                if (!new File(s_FilePath + fileName).exists()) {

                    downloadFile();
                } else {
                    Toast.makeText(activity, "Already Downloaded", Toast.LENGTH_SHORT).show();
                }

            });

        } else {
            iv_delete.setImageResource(R.drawable.ic_delete_black_24dp);
            iv_delete.setOnClickListener(view -> {

                AlertDialog.Builder ab = new AlertDialog.Builder(activity);
                ab.setPositiveButton("Yes", (dialog, id) -> {
                    boolean b = fileArrayList.get(position).delete();
                    if (b) {
                        deleteFileAA(position);
                    }
                });

                ab.setNegativeButton("No", (dialog, id) -> dialog.cancel());
                AlertDialog alert = ab.create();
                alert.setTitle("Are you sure to delete it?");
                alert.setIcon(R.drawable.logo);
                alert.show();
            });

        }
        iv_share.setOnClickListener(view -> {
            if (fileArrayList.get(position).getName().contains(".mp4")) {
               // Log.d("SSSSS", "onClick: " + fileArrayList.get(position) + "");
                MyAppUtils.shareVideo(activity, fileArrayList.get(position).getPath());
            } else {
                MyAppUtils.shareImage(activity, fileArrayList.get(position).getPath());
            }


            MyAppUtils.showInterstitialAds(context);

        });
        iv_whatsappshare.setOnClickListener(view -> {
            if (fileArrayList.get(position).getName().contains(".mp4")) {
                MyAppUtils.shareImageVideoOnWhatsapp(activity, fileArrayList.get(position).getPath(), true);
            } else {
                MyAppUtils.shareImageVideoOnWhatsapp(activity, fileArrayList.get(position).getPath(), false);
            }


            MyAppUtils.showInterstitialAds(context);

        });

        iv_close.setOnClickListener(v -> {
            onBackPressed();

            // MyAppUtils.showInterstitialAds(context);

        });
    }


    public void downloadFile() {
        final String path = fileArrayList.get(position).getPath();
        String filename = path.substring(path.lastIndexOf("/") + 1);
        final File file = new File(path);
        File destinationFile = new File(s_FilePath);
        try {
            FileUtils.copyFileToDirectory(file, destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String changefilename = filename.substring(12);
        File newFile = new File(s_FilePath + changefilename);

        String contentType = "image/*";
        if (fileArrayList.get(position).getName().endsWith(".mp4")) {
            contentType = "video/*";
        } else {
            contentType = "image/*";
        }
        MediaScannerConnection.scanFile(activity, new String[]{newFile.getAbsolutePath()}, new String[]{contentType},
                new MediaScannerConnection.MediaScannerConnectionClient() {
                    public void onMediaScannerConnected() {
                    }

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });

        File from = new File(s_FilePath, filename);
        File to = new File(s_FilePath, changefilename);
        from.renameTo(to);

        iv_delete.setImageResource(R.drawable.ic_saved);
        Toast.makeText(activity, "Saved!", Toast.LENGTH_LONG).show();


        MyAppUtils.showInterstitialAds(context);

    }


    public void deleteFileAA(int position) {
        fileArrayList.remove(position);
        nvShowImagesAdapter.notifyDataSetChanged();
        MyAppUtils.setToast(activity, "File Deleted.");
        if (fileArrayList.isEmpty()) {
            onBackPressed();
        }


        MyAppUtils.showInterstitialAds(context);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }



    public class ZoomOutSlideTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;


        @Override
        public void transformPage(@NonNull View view, float position) {
            if (position >= -1 || position <= 1) {
                // Modify the default slide transition to shrink the page as well
                final float height = view.getHeight();
                final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                final float vertMargin = height * (1 - scaleFactor) / 2;
                final float horzMargin = view.getWidth() * (1 - scaleFactor) / 2;

                // Center vertically
                view.setPivotY(0.5f * height);

                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            }


        }
    }



    /*
    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }
    */

    @Override
    public void onBackPressed() {
        //MyAppUtils.showInterstitialAds(context);
        super.onBackPressed();
    }



    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
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

}
