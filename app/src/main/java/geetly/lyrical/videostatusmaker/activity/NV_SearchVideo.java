package geetly.lyrical.videostatusmaker.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import geetly.lyrical.videostatusmaker.MyApplication;
import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.adapter.NvVideoViewAdapter;

import geetly.lyrical.videostatusmaker.model.ModelVideoResponce;
import geetly.lyrical.videostatusmaker.model.VideoviewModel;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;
import geetly.lyrical.videostatusmaker.utils.Utils_Permission;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static geetly.lyrical.videostatusmaker.Retrofit.APIClientData.getInterface;

public class NV_SearchVideo extends AppCompatActivity {

    private Activity context;
    private ArrayList<VideoviewModel> videoviewModel = new ArrayList<>();
    private boolean isBackPressed = false;
    private RecyclerView rv_all_category = null;
    private RecyclerView rv_all_videos = null;
    private LinearLayout ll_no_data_available = null;

    private int page = 1;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean loading = false;
    private EditText et_search_bar;
    private AdView adView;
    ActionBarDrawerToggle toggle;
    private LinearLayout ll_progressbar;
    private LottieAnimationView lottieAnimationView;
    private NvVideoViewAdapter nvVideoViewAdapter;
    public static final String TAG_SEARCH_TERM = "tag_search";
    private SwipeRefreshLayout swiperefreshLayout;
    //    private ImageView saveWp;
    private StaggeredGridLayoutManager layoutManager;

    private LottieAnimationView lottiAnimationNodata;


    LinearLayout ll_back;
    ArrayAdapter<Object> nvCategoryAdapter;
    private DrawerLayout drawer_layout;
    private ImageView home;

    public String search_text;

    Dialog dialog;
    private NativeAd nativeAd;
    private ProgressBar progressBar;
    private ImageView saveWp;
    private NavigationView navigationView;

    private ImageView iv_back_main;
    private FrameLayout ll_fbbanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_video);
        context = this;

        iv_back_main = findViewById(R.id.iv_back_main);
        iv_back_main.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        if (intent != null) {
            if(intent.hasExtra("search_text")) {
                search_text = intent.getStringExtra("search_text");
            }
        }

        TextView ct = findViewById(R.id.search_result);
        ct.setText("Search Result: "+search_text);

        ll_progressbar =  findViewById(R.id.ll_progressbar);
        swiperefreshLayout = findViewById(R.id.swiperefreshLayout);


        lottiAnimationNodata =  findViewById(R.id.lottiAnimationNodata);
        lottieAnimationView =  findViewById(R.id.animationView);
        rv_all_category =  findViewById(R.id.rv_all_category);
        ll_no_data_available =  findViewById(R.id.ll_no_data_available);
        rv_all_videos =  findViewById(R.id.rv_all_videos);
        ll_fbbanner =  findViewById(R.id.ll_fbbanner);


        MyAppUtils.showBannerAds(this,ll_fbbanner);
        lottiAnimationNodata.playAnimation();

        new Utils_Permission(context).checkPermissionsGranted();
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        copyFile("blankimage.jpg");
        swiperefreshLayout.setOnRefreshListener(() -> {

            page = 1;
            videoviewModel.clear();
            nvVideoViewAdapter.notifyDataSetChanged();
            getSearchVideos(search_text);
            swiperefreshLayout.setRefreshing(false);

        });

        /*iv_back_main.setOnClickListener(view -> {
            MyAppUtils.showInterstitialAds(context);
            Intent inMani = new Intent(NV_SearchVideo.this, NV_LibraryActivity.class);
            startActivity(inMani);
        });*/


        RecyclerViewsetup();
        MyApplication.page_from = "SearchPage";
        MyApplication.search_text = search_text;

    }

    private void copyFile(String str) {
        try {
            InputStream open = getAssets().open(str);
            StringBuilder sb = new StringBuilder();
            sb.append(getCacheDir());
            sb.append("/");
            sb.append(str);
            FileOutputStream fos = new FileOutputStream(sb.toString());
            byte[] bArr = new byte[1024];
            while (true) {
                int read = open.read(bArr);
                if (read != -1) {
                    fos.write(bArr, 0, read);
                } else {
                    open.close();
                    fos.flush();
                    fos.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void RecyclerViewsetup() {

        layoutManager = new StaggeredGridLayoutManager(2, 1);
        rv_all_videos.setLayoutManager(layoutManager);
        rv_all_videos.setHasFixedSize(true);


        rv_all_videos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
               // Log.e("hhhhh", "search for video : " + "hhhh2");
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                int[] firstVisibleItems = null;
                firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    pastVisibleItems = firstVisibleItems[0];
                   // Log.e("hhhhh", "onScrolled: " + "hhhh1");
                }

                if (!loading) {
                   // Log.e("hhhhh", "onScrolled: " + "hhhh0");
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = true;
                      //  Log.e("hhhhh", "onScrolled: " + "hhhh");
                        page = page + 1;

                        ll_progressbar.setVisibility(View.VISIBLE);

                        new Handler(getMainLooper()).postDelayed(() -> {

                            loadMoreData(search_text);

                            ll_progressbar.setVisibility(View.GONE);

                        }, 100);

                    }
                }

            }
        });

        if (MyAppUtils.isConnectingToInternet(context)) {
            lottieAnimationView.setVisibility(View.GONE);
            lottieAnimationView.pauseAnimation();

            if(search_text != "")
            {
                getSearchVideos(search_text);
               // Log.e("Search Video Text ","Done : "+search_text);
            }
        } else {
            lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.playAnimation();
        }


    }

    private void loadMoreData(String str) {
        ll_progressbar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("app", getApplicationContext().getPackageName());
        //jsonObject.addProperty("cat", str);
        jsonObject.addProperty("vidtype",100);
        jsonObject.addProperty("search", str);
        jsonObject.addProperty("page", page);
        try {
            getInterface().getSearchVideo(jsonObject).enqueue(new Callback<ModelVideoResponce>() {
                @Override
                public void onFailure(@NotNull Call<ModelVideoResponce> call, @NotNull Throwable th) {


                }

                @Override
                public void onResponse(@NotNull Call<ModelVideoResponce> call, @NotNull Response<ModelVideoResponce> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        ModelVideoResponce modelVideoResponce = response.body();
                        int i = 0;

                        ll_no_data_available.setVisibility(View.GONE);
                        ll_progressbar.setVisibility(View.GONE);
                        rv_all_videos.setVisibility(View.VISIBLE);

                        while (i < modelVideoResponce.getMsg().size()) {
                            if (i % 5 == 2) {
                                videoviewModel.add(null);
                            }
                            videoviewModel.add(modelVideoResponce.getMsg().get(i));
                            i++;
                        }

                        nvVideoViewAdapter.setDataList(videoviewModel);
                        nvVideoViewAdapter.notifyDataSetChanged();
                        loading = false;

                    } else {
                        Toast.makeText(context, "No video found", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void getSearchVideos(String str) {



        String f = "1";
        // Log.e("sssss", "onResponse: " + str + page);
        ll_progressbar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("app", getApplicationContext().getPackageName());
        //jsonObject.addProperty("cat", str);
        jsonObject.addProperty("vidtype",100);
        jsonObject.addProperty("search", str);
        jsonObject.addProperty("page", page);
        try {
            getInterface().getSearchVideo(jsonObject).enqueue(new Callback<ModelVideoResponce>() {
                public void onFailure(Call<ModelVideoResponce> call, Throwable th) {
                    ll_progressbar.setVisibility(View.GONE);
                    ll_no_data_available.setVisibility(View.VISIBLE);
                    rv_all_videos.setVisibility(View.GONE);

                }

                public void onResponse(@NotNull Call<ModelVideoResponce> call, @NotNull Response<ModelVideoResponce> response) {

                    try {
                        ModelVideoResponce modelVideoResponce = response.body();
                        int i = 0;

                        if (modelVideoResponce.getCode() == null || !modelVideoResponce.getCode().equals("200")) {
                            if (modelVideoResponce.getCode().equals("400"))
                            {
                                showNodataMessage();
                            }
                            ll_progressbar.setVisibility(View.GONE);
                            ll_no_data_available.setVisibility(View.VISIBLE);
                            rv_all_videos.setVisibility(View.GONE);

                            return;
                        }
                        ll_no_data_available.setVisibility(View.GONE);
                        ll_progressbar.setVisibility(View.GONE);
                        rv_all_videos.setVisibility(View.VISIBLE);
                        videoviewModel = new ArrayList();
                        while (i < modelVideoResponce.getMsg().size()) {
                            if (i % 5 == 0 && i != 0) {
                                videoviewModel.add(null);
                            }
                            videoviewModel.add(modelVideoResponce.getMsg().get(i));
                            i++;
                        }
                        nvVideoViewAdapter = new NvVideoViewAdapter(context, videoviewModel);
                        layoutManager = new StaggeredGridLayoutManager(2, 1);
                        rv_all_videos.setLayoutManager(layoutManager);
                        rv_all_videos.setAdapter(nvVideoViewAdapter);

                    } catch (Exception e) {
                        ll_progressbar.setVisibility(View.GONE);
                        ll_no_data_available.setVisibility(View.VISIBLE);
                        rv_all_videos.setVisibility(View.GONE);
                    }

                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void showNodataMessage()
    {
        ll_no_data_available.setVisibility(View.VISIBLE);
        rv_all_videos.setVisibility(View.GONE);
        ll_progressbar.setVisibility(View.GONE);

        Toast.makeText(NV_SearchVideo.this,"No any video found for your search result",Toast.LENGTH_LONG).show();
        new  Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(NV_SearchVideo.this, NV_LibraryActivity.class);
                startActivity(i);
                finish();
            }

        }, 5000);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.Click = false;
    }

    @Override
    public void onBackPressed() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent i;
            i = new Intent(this, NV_LibraryActivity.class);
            startActivity(i);
            finishAffinity();
            finish();
            super.onBackPressed();
        }

    }
}