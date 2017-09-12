package kr.co.citizoomproject.android.citizoom.Setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.R;

/**
 * Created by ccei on 2016-07-26.
 */
public class MyPageActivity extends AppCompatActivity {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mypage_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icn_back);

        ViewPager viewPager = (ViewPager) findViewById(R.id.mypage_viewpager);
        if (viewPager != null) {
            setupMypageViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.mypage_tabs);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(this).inflate(R.layout.tab_layout, tabLayout, false);

            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title);
            tabTextView.setText(tab.getText());
            tab.setCustomView(relativeLayout);
        }
    }

    private void setupMypageViewPager(ViewPager viewPager) {
        NationalMemberPagerAdapter nationalMemberPagerAdapter = new NationalMemberPagerAdapter(getSupportFragmentManager());
        nationalMemberPagerAdapter.appendFragment(MyPageUserActivityFragment.newInstance(1), "활동내역");
        nationalMemberPagerAdapter.appendFragment(MyPageUserActivityNotificationFragment.newInstance(1), "활동알림");
        viewPager.setAdapter(nationalMemberPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        long intervalTime = currentTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = currentTime;
            Toast.makeText(getApplicationContext(), "'뒤로' 버튼 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class NationalMemberPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragment = new ArrayList<>();
        private final ArrayList<String> tabTitles = new ArrayList<String>();

        public NationalMemberPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment, String title) {
            this.fragment.add(fragment);
            tabTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragment.get(position);
        }

        @Override
        public int getCount() {
            return fragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }

    }
}
