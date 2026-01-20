package geetly.lyrical.videostatusmaker.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;



import android.content.Intent;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy;
import android.util.Log;

import android.view.Gravity;


import android.view.View;
import android.widget.EditText;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.media3.common.Player;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import geetly.lyrical.videostatusmaker.adapter.NvVideoViewAdapter1;
import geetly.lyrical.videostatusmaker.utils.SnappingRecyclerView;
import geetly.lyrical.videostatusmaker.MyApplication;
import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.Retrofit.APIClientData;
import geetly.lyrical.videostatusmaker.model.ModelVideoResponce;
import geetly.lyrical.videostatusmaker.model.VideoviewModel;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;
import geetly.lyrical.videostatusmaker.utils.Utils_Admob;
import geetly.lyrical.videostatusmaker.utils.Utils_Permission;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static geetly.lyrical.videostatusmaker.MyApplication.simpleCache;

public class MainActivity extends AppCompatActivity implements Player.Listener{


    private Activity context;
    private ArrayList<VideoviewModel> videoviewModel = new ArrayList<>();
    private boolean isBackPressed = false;
    private RecyclerView rv_all_category = null;
    private SnappingRecyclerView rv_all_videos = null;
    private LinearLayout ll_no_data_available = null;
    private String selected_Category = "Latest";
    private int page = 1;
    private boolean loading = false;
    private EditText et_search_bar;

    ActionBarDrawerToggle toggle;
    private LinearLayout ll_progressbar;
    private LottieAnimationView lottieAnimationView;
    private NvVideoViewAdapter1 nvVideoViewAdapter;
    int currentPage = -1;
    private ImageView saveWp;
    private StaggeredGridLayoutManager layoutManager;
    private LottieAnimationView lottiAnimationNodata;
    private DrawerLayout drawer_layout;
    private ImageView home;
    //private CacheDataSource.Factory cacheDataSourceFactory;
    private boolean isVisibleToUser = true;
    //SimpleExoPlayer priviousPlayer;
    LinearLayout ll_library;
    private NavigationView navigationView;

    private FrameLayout ll_fbbanner;

    Dialog dialog;

    private ProgressBar progressBar;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        context = this;



        home =  findViewById(R.id.home);
        navigationView =  findViewById(R.id.navigationView);
        et_search_bar =  findViewById(R.id.et_search_bar);
        ll_progressbar =  findViewById(R.id.ll_progressbar);
        drawer_layout =  findViewById(R.id.drawer_layout);
        saveWp =  findViewById(R.id.save_wp);


        lottiAnimationNodata =  findViewById(R.id.lottiAnimationNodata);
        lottieAnimationView =  findViewById(R.id.animationView);
        rv_all_category =  findViewById(R.id.rv_all_category);
        ll_no_data_available =  findViewById(R.id.ll_no_data_available);
        rv_all_videos = findViewById(R.id.rv_all_videos);
        ll_library = findViewById(R.id.ll_library);
        rv_all_videos.enableViewScaling(true);

        ll_fbbanner =  findViewById(R.id.ll_fbbanner);

        lottiAnimationNodata.playAnimation();
        MyAppUtils.showBannerAds(this,ll_fbbanner);

        home.setOnClickListener(view -> drawer_layout.openDrawer(Gravity.START, true));

        toggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close);
        drawer_layout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.album:
                    startActivity(new Intent(context, NV_AlbumdataActivity.class));
                    return true;
                case R.id.privacy:

                    startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(MyAppUtils.PRIVACY_URL), "text/plain"));

                    return true;

                case R.id.rateUs:
                    MyAppUtils.rateApp(context);
                    return true;
                case R.id.share:
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share) + getPackageName());
                    startActivity(Intent.createChooser(intent, "Share App..."));

                    return true;
                default:
                    return false;
            }
        });


        saveWp.setOnClickListener(v -> {
            MyAppUtils.showInterstitialAds(context);
            startActivity(new Intent(getApplicationContext(), NV_WhatsappActivity.class));


        });

        new Utils_Permission(context).checkPermissionsGranted();


        StrictMode.setVmPolicy(new VmPolicy.Builder().build());
        copyFile("blankimage.jpg");

        ll_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, NV_LibraryActivity.class);
                startActivity(i);
            }
        });




        RecyclerViewsetup();

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

        rv_all_videos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                int pageNo = scrollOffset / height;
                Log.e("xxxx", "onScrolled: "+pageNo +"---"+dx+"---"+dy);

//                if (pageNo != currentPage) {

                currentPage = pageNo;
                int pos = SnappingRecyclerView.getSelectedPosition();
                int ccc = pos +currentPage;
                Log.e("lllll", "onScrollStateChanged: "+pos);
                //releasePriviousPlayer();
                //setPlayer(ccc);
//                    setPlayer(currentPage);

//                }

//                int pos = SnappingRecyclerView.getSelectedPosition();
//
//                Log.e("kkkk", "onScrolled: "+pos );

//                setPlayer();
                if (!loading) {
                    Log.e("hhhhh", "onScrolled: " + "hhhh0");
//                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    loading = true;
                    Log.e("hhhhh", "onScrolled: " + "hhhh");
                    page = page + 1;

                    ll_progressbar.setVisibility(View.VISIBLE);

                    new Handler(getMainLooper()).postDelayed(() -> {

                        loadMoreData(selected_Category);

                        ll_progressbar.setVisibility(View.GONE);

                    }, 100);

//                    }
                }

            }
        });

        if (MyAppUtils.isConnectingToInternet(context)) {
            lottieAnimationView.setVisibility(View.GONE);
            lottieAnimationView.pauseAnimation();
//            getCategory();
            getVideos("Latest");
        } else {
            lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.playAnimation();
        }


    }

    private void loadMoreData(String str) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("app", getApplicationContext().getPackageName());
        jsonObject.addProperty("cat", str);
        jsonObject.addProperty("page", page);
        jsonObject.addProperty("vidtype",100);
        try {
            APIClientData.getInterface().getCatVideo(jsonObject).enqueue(new Callback<ModelVideoResponce>() {
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


    public void getVideos(String str) {
        String f = "1";
        Log.e("sssss", "onResponse: " + str + page);
        ll_progressbar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("app", getApplicationContext().getPackageName());
        jsonObject.addProperty("cat", str);
        jsonObject.addProperty("page", page);
        jsonObject.addProperty("vidtype",100);
        try {
            APIClientData.getInterface().getCatVideo(jsonObject).enqueue(new Callback<ModelVideoResponce>() {
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
                            ll_no_data_available.setVisibility(View.VISIBLE);
                            rv_all_videos.setVisibility(View.GONE);
                            ll_progressbar.setVisibility(View.GONE);
                            return;
                        }
                        ll_no_data_available.setVisibility(View.GONE);
                        ll_progressbar.setVisibility(View.GONE);
                        rv_all_videos.setVisibility(View.VISIBLE);
                        videoviewModel = new ArrayList();

                        while (i < modelVideoResponce.getMsg().size()) {
                            videoviewModel.add(modelVideoResponce.getMsg().get(i));
                            i++;
                        }
//                        rv_all_videos.findViewHolderForAdapterPosition(pos);
                        nvVideoViewAdapter = new NvVideoViewAdapter1(context, videoviewModel);

//                        nvVideoViewAdapter.setEventListener(new NvVideoViewAdapter1.EventListener() {
//
//
//                            @Override
//                            public void onItemViewClick(int position) {
//
//
//                                setPlayer(position);
//
//                            }
//                        });
//                        layoutManager = new StaggeredGridLayoutManager(2, 1);
//                        rv_all_videos.setLayoutManager(layoutManager);
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




    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
       /* if (isBackPressed) {
            super.onBackPressed();
            return;
        }
        isBackPressed = true;
        Toast.makeText(context, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler(getMainLooper()).postDelayed(() -> isBackPressed = false, 2000);*/

        super.onBackPressed();
        super.onBackPressed();
        if (!this.dialog.isShowing())
            this.dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //releasePriviousPlayer();

    }


    @Override
    public void onStop() {
        super.onStop();
        /*if (priviousPlayer != null) {
            priviousPlayer.setPlayWhenReady(false);
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*if (priviousPlayer != null) {
            priviousPlayer.setPlayWhenReady(true);
        }*/
    }


    @Override
    public void onPause() {
        super.onPause();

        // if (priviousPlayer != null)
        //    priviousPlayer.setPlayWhenReady(false);
    }
    @Override
    public void onResume() {
        super.onResume();

        //if ((priviousPlayer != null)) {
        //    priviousPlayer.setPlayWhenReady(true);
        //}
        MyApplication.Click = false;
    }



}
