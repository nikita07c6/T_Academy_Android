package kr.co.citizoomproject.android.citizoom.Interest;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.R;

/**
 * Created by ccei on 2016-07-27.
 */
public class InterestLawMemberActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_law_member);

        Toolbar toolbar = (Toolbar) findViewById(R.id.interest_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icn_back);

        ViewPager viewPager = (ViewPager) findViewById(R.id.interest_viewpager);
        if (viewPager != null) {
            InterestViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.interest_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void InterestViewPager(ViewPager viewPager) {
        SearchPagerAdapter searchPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager());
        searchPagerAdapter.appendFragment(InterestLawFragment.newInstance(1), "관심법안");
        searchPagerAdapter.appendFragment(InterestMemberFragment.newInstance(1), "관심의원");
        viewPager.setAdapter(searchPagerAdapter);
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

    private static class SearchPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> searchFragment = new ArrayList<>();
        private final ArrayList<String> tabTitles = new ArrayList<String>();

        public SearchPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment, String title) {
            searchFragment.add(fragment);
            tabTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return searchFragment.get(position);
        }

        @Override
        public int getCount() {
            return searchFragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }

    }


    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }
}
