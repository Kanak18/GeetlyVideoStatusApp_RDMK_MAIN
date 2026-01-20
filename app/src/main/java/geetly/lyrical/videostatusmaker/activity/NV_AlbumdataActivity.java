package geetly.lyrical.videostatusmaker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import geetly.lyrical.videostatusmaker.MyApplication;
import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.adapter.NvAlbumAdapter;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

public class NV_AlbumdataActivity extends AppCompatActivity {

    private RecyclerView rv_video_list;
    private ImageView iv_back;
    ArrayList<String> arrayList = new ArrayList();


    StaggeredGridLayoutManager staggeredGridLayoutManager;
    Activity context;
    ArrayList<String> newList = new ArrayList();

    private FrameLayout ll_fbbanner;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_album);

        iv_back = findViewById(R.id.iv_back);
        rv_video_list = findViewById(R.id.rv_video_list);

        context = this;

        ll_fbbanner =  findViewById(R.id.ll_fbbanner);
        MyAppUtils.showBannerAds(this,ll_fbbanner);

        iv_back.setOnClickListener(view -> {
            MyAppUtils.showInterstitialAds(context);
            NV_AlbumdataActivity.this.onBackPressed();
        });
        initViews();
    }


    public ArrayList<String> getAlbum() {
        this.arrayList.clear();
       // Log.e("My Path :: ", "" + getDataDir().toString());
        //File file = new File(getDataDir().toString(), getResources().getString(R.string.app_name));
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), getResources().getString(R.string.app_name));


        //File file = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdirs();
        }
        String path = file.getPath();
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        File[] listFiles = new File(path).listFiles();
        if (listFiles != null) {
            int i = 0;
            while (i < listFiles.length) {
                PrintStream ps;
                StringBuilder sb;
                if (listFiles[i].isFile()) {
                    ps = System.out;
                    sb = new StringBuilder();
                    sb.append("File ");
                    sb.append(listFiles[i].getName());
                    ps.println(sb.toString());
                    if (listFiles[i].getName().contains(".mp4") && new File(listFiles[i].getPath().toString()).length() > 1024) {
                        this.arrayList.add(listFiles[i].getPath());
                    }
                } else if (listFiles[i].isDirectory()) {
                    ps = System.out;
                    sb = new StringBuilder();
                    sb.append("Directory ");
                    sb.append(listFiles[i].getName());
                    ps.println(sb.toString());
                }
                i++;
            }
        }
        return arrayList;
    }

    public void initViews() {
        arrayList = getAlbum();
        int i = 0;

        if (arrayList.size() > 0) {
            while (i < arrayList.size()) {
                if (i % MyApplication.native_show_count == 0 && i != 0 && MyApplication.show_native > 0) {
                    this.newList.add(null);
                }
                this.newList.add(this.arrayList.get(i));
                i++;
            }
            ArrayList arrayList = newList;
            if (arrayList != null && arrayList.size() > 0) {
                NvAlbumAdapter nvAlbumAdapter = new NvAlbumAdapter(this, newList);
                staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
                rv_video_list.setLayoutManager(this.staggeredGridLayoutManager);
                rv_video_list.setAdapter(nvAlbumAdapter);
                return;
            }
            return;
        }
        Toast.makeText(this, "No Any Creation Found.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
