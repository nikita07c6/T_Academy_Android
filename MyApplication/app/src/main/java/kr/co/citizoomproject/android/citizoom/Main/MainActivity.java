package kr.co.citizoomproject.android.citizoom.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.Interest.InterestLawMemberActivity;
import kr.co.citizoomproject.android.citizoom.Issue.IssueFragment;
import kr.co.citizoomproject.android.citizoom.Issue.IssueWritingActivity;
import kr.co.citizoomproject.android.citizoom.Law.LawFragment;
import kr.co.citizoomproject.android.citizoom.Member.MemberSearchActivity;
import kr.co.citizoomproject.android.citizoom.NationalMemberFragment;
import kr.co.citizoomproject.android.citizoom.R;
import kr.co.citizoomproject.android.citizoom.Setting.MyPageActivity;
import kr.co.citizoomproject.android.citizoom.Setting.SettingActivity;
import kr.co.citizoomproject.android.citizoom.Setting.SettingProfileChangeActivity;

public class MainActivity extends AppCompatActivity {
    public static final int FILTER_SELECTED = 1;
    private final long FINISH_INTERVAL_TIME = 2000;
    Bundle bundle;
    private DrawerLayout mDrawerLayout;
    private long backPressedTime = 0;
    private LawFragment lawFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.icn_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ImageView close_drawer = (ImageView) findViewById(R.id.drawer_close);
        close_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
            }
        });

        ImageView myInterest = (ImageView) findViewById(R.id.interest);
        myInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InterestLawMemberActivity.class);
                startActivity(intent);
            }
        });

       /* ImageView  = (ImageView) findViewById(R.id.);
        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainSearchActivity.class);
                startActivity(intent);
            }
        });*/

        ImageView SearchMemberBtn = (ImageView) findViewById(R.id.search_member);
        SearchMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MemberSearchActivity.class);
                startActivity(intent);
            }
        });

        ImageView myPageBtn = (ImageView) findViewById(R.id.mypage);
        myPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingProfileChangeActivity.class);
                startActivity(intent);
            }
        });

        ImageView mySettingBtn = (ImageView) findViewById(R.id.menu_setting
        );
        mySettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupMainViewPager(viewPager);
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "이슈작성페이지", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), IssueWritingActivity.class);
                startActivity(intent);
            }
        });

        assert viewPager != null;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    fab.hide();
                    // 나중에 여기나 스와이프에서 불러올 것
                    // final AsyncTask<String, Integer, ArrayList<LawEntityObject>> jsonlist;


                } else if (position == 0) {
                    fab.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    } //onCreate

    private void setupMainViewPager(ViewPager viewPager) {
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPagerAdapter.appendFragment(IssueFragment.newInstance(1), "ISSUE");
        lawFragment = LawFragment.newInstance(1);
        mainPagerAdapter.appendFragment(lawFragment, "법안");
        mainPagerAdapter.appendFragment(NationalMemberFragment.newInstance(1), "의원");
        viewPager.setAdapter(mainPagerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        //switch (requestCode){
        // case FILTER_SELECTED : {

        bundle = new Bundle();
        bundle.putStringArrayList("committee", data.getStringArrayListExtra("topic"));
        bundle.putStringArrayList("month", data.getStringArrayListExtra("month"));
        bundle.putString("year", data.getStringExtra("year"));
        bundle.putString("proposer", data.getStringExtra("proposer"));
        lawFragment.refreshLaw(bundle);
        //break;
        //}
        //default:

        // break;
        //}
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));

    }

    private static class MainPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragments = new ArrayList<>();
        private final ArrayList<String> tabTitles = new ArrayList<String>();

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            tabTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }

    }

}
