package geetly.lyrical.videostatusmaker.activity;

import android.os.Bundle;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import geetly.lyrical.videostatusmaker.R;
import geetly.lyrical.videostatusmaker.fragments.WhatsappSavedFragment;
import geetly.lyrical.videostatusmaker.fragments.WhatsappStatusDataFragment;
import geetly.lyrical.videostatusmaker.utils.MyAppUtils;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public class NV_WhatsappActivity extends AppCompatActivity {

    private ImageView iv_back;
    private TabLayout tablayout;
    private ViewPager viewpager;

    private FrameLayout ll_fbbanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp);


        iv_back = findViewById(R.id.iv_back);
        tablayout = findViewById(R.id.tablayout);
        viewpager = findViewById(R.id.viewpager);

        ll_fbbanner =  findViewById(R.id.ll_fbbanner);
        MyAppUtils.showBannerAds(this,ll_fbbanner);

        MyAppUtils.createFileFolder();

        setupViewPager(viewpager);
        tablayout.setupWithViewPager(viewpager);
        tablayout.getTabAt(0).setIcon(R.drawable.ic_sd_status);
        tablayout.getTabAt(1).setIcon(R.drawable.ic_sd_saved);
        iv_back.setOnClickListener(view -> onBackPressed());

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter padapter = new ViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        padapter.addFragment(new WhatsappStatusDataFragment(), "Status");
        padapter.addFragment(new WhatsappSavedFragment(), "Saved Status");
        viewPager.setAdapter(padapter);
        viewPager.setOffscreenPageLimit(1);

    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> nvFragmentList = new ArrayList<>();
        private final List<String> nvFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }


        @Override
        public Fragment getItem(int position) {
            return nvFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return nvFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            nvFragmentList.add(fragment);
            nvFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return nvFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}