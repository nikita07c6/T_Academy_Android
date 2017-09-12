package kr.co.citizoomproject.android.citizoom.Law;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.ParseDataParseHandler;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-07-28.
 */
public class LawDetailActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static String SERVER_URL_DETAIL_LAW;
    public static int lawID;
    private static ImageView committee, lawStatus;
    private static TextView law_id, title_, date_, proposer_, viewCount_, agree_count_, disagree_count_;
    private static ToggleButton interest_law_mark, agree_Button, disagree_Button;
    String SERVER_URL_LAWS_PROS_CONS, SERVER_URL_LAW_LIKE;
    Bundle bundle;
    private LinearLayout toSNSRoot;

    public static int getCommittee(String string) {
        if (string.equals("농림축산식품해양수산위원회")) {
            return 1;
        } else if (string.equals("국회운영위원회")) {
            return 2;
        } else if (string.equals("국방위원회")) {
            return 3;
        } else if (string.equals("교육문화체육관광위원회")) {
            return 4;
        } else if (string.equals("환경노동위원회")) {
            return 5;
        } else if (string.equals("외교통일위원회")) {
            return 6;
        } else if (string.equals("미래창조과학방송통신위원회")) {
            return 7;
        } else if (string.equals("보건복지위원회")) {
            return 8;
        } else if (string.equals("산업통상자원위원회")) {
            return 9;
        } else if (string.equals("정보위원회")) {
            return 10;
        } else if (string.equals("법제사법위원회")) {
            return 11;
        } else if (string.equals("정무위원회")) {
            return 12;
        } else if (string.equals("안전행정위원회")) {
            return 13;
        } else if (string.equals("기획재정위원회")) {
            return 14;
        } else if (string.equals("국토교통위원회")) {
            return 15;
        } else if (string.equals("여성가족위원회")) {
            return 16;
        }

        return 0;
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        new InterestLawFragment.AsyncLawJSONList().execute("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //new AsyncLawJSONList().execute();
        new AsyncLawDetailJSONList().execute();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.law_detail_activity);
        committee = (ImageView) findViewById(R.id.committee_icon);
        law_id = (TextView) findViewById(R.id.law_id);
        title_ = (TextView) findViewById(R.id.law_title);
        date_ = (TextView) findViewById(R.id.submit_date);
        proposer_ = (TextView) findViewById(R.id.init_member);
        viewCount_ = (TextView) findViewById(R.id.law_view_count);
        agree_count_ = (TextView) findViewById(R.id.law_good_count);
        disagree_count_ = (TextView) findViewById(R.id.law_bad_count);
        interest_law_mark = (ToggleButton) findViewById(R.id.interest_law_Btn);
        agree_Button = (ToggleButton) findViewById(R.id.law_good_Btn);
        disagree_Button = (ToggleButton) findViewById(R.id.law_bad_Btn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.law_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icn_back);

        Intent intent = getIntent();
        lawID = intent.getIntExtra("lawID", lawID);
        SERVER_URL_DETAIL_LAW = "http://" + NetworkDefineConstant.HOST_URL + ":" + NetworkDefineConstant.PORT_NUMBER + "/laws/" + lawID + "/";

        Log.e("SERVER_URL_LAW_COMMENT", SERVER_URL_DETAIL_LAW);

        ImageView status = (ImageView) findViewById(R.id.my_national_member_vote_status);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                LawDetailMemberDialogFragment dialogFragment = new LawDetailMemberDialogFragment();
                dialogFragment.show(fragmentManager, "");
            }
        });

        lawStatus = (ImageView) findViewById(R.id.law_review_status);
        lawStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                LawDetailReviewDialogFragment detailReviewDialogFragment = LawDetailReviewDialogFragment.newInstance(bundle);
                detailReviewDialogFragment.show(fragmentManager, "");
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.law_detail_viewpager);
        if (viewPager != null) {
            setupLawDetailViewPager(viewPager);
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.writing_comment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Snackbar.make(view, "이슈작성페이지", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), LawCommentWritingActivity.class);
                intent.putExtra("LAWID", lawID);
                startActivity(intent);

            }
        });

        fab.hide();

        assert viewPager != null;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 2) {
                    fab.hide();
                    // 나중에 여기나 스와이프에서 불러올 것
                    // final AsyncTask<String, Integer, ArrayList<LawEntityObject>> jsonlist;

                } else if (position == 2) {
                    fab.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        interest_law_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                interest_law_mark.setChecked(false);
                String LAWID = String.valueOf(lawID);
                SERVER_URL_LAW_LIKE = String.format("http://%s:%d/%s/%s/%s", NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER, "laws", LAWID, "like");
                Log.e("SERVER_URL_LAW_LIKE", SERVER_URL_LAW_LIKE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response response = null;
                        boolean flag = false;

                        MultipartBody.Builder MB;
                        MB = new MultipartBody.Builder();
                        MB.setType(MultipartBody.FORM);

                        try {
                            MB.addFormDataPart("user_id", PropertyManager.getInstance().getUserId());

                            OkHttpClient client = MySingleton.sharedInstance().httpClient;

                            //요청 Body 세팅==> 그전 Query Parameter세팅과 같은 개념
                            RequestBody fileUploadBody = MB.build();

                            //요청 세팅
                            Request request = new Request.Builder()
                                    .url(SERVER_URL_LAW_LIKE)
                                    .post(fileUploadBody) //반드시 post로
                                    .build();
                            //동기 방식
                            response = client.newCall(request).execute();

                            flag = response.isSuccessful();
                            int responseCode = response.code();
                            Log.e("response==>", String.valueOf(responseCode));
                            //응답 코드 200등등

                            if (flag) {
                                e("response결과", response.message()); //읃답에 대한 메세지(OK)
                                //e("response응답바디", response.body().string()); //json으로 변신 You can only call string() once.

                                StringBuilder buf = new StringBuilder(response.body().string());

                                JSONObject jsonObject = new JSONObject(buf.toString());
                                final int like = jsonObject.getInt("like");
                                Log.e("like 확인", String.valueOf(like));

                                view.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (like == 1) {
                                            interest_law_mark.setChecked(true);
                                        } else {
                                            interest_law_mark.setChecked(false);
                                        }
                                    }
                                });
                            }

                        } catch (UnknownHostException une) {
                            e("즐겨찾기에러1", une.toString());
                        } catch (UnsupportedEncodingException uee) {
                            e("즐겨찾기에러2", uee.toString());
                        } catch (Exception e) {
                            e("즐겨찾기에러3", e.toString());
                        } finally {
                            if (response != null) {
                                response.close();
                            }
                        }
                    }
                }).start();
                Log.e("즐겨찾기 버튼 눌림", "true");
            }
        });

        agree_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String LAWID = String.valueOf(lawID);
                SERVER_URL_LAWS_PROS_CONS = String.format("http://%s:%d/%s/%s/%s", NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER, "laws", LAWID, "rating");
                Log.e("LAWS_PROS_CONS", SERVER_URL_LAWS_PROS_CONS);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response response = null;
                        boolean flag = false;

                        MultipartBody.Builder MB;
                        MB = new MultipartBody.Builder();
                        MB.setType(MultipartBody.FORM);

                        try {
                            MB.addFormDataPart("user_id", PropertyManager.getInstance().getUserId());
                            MB.addFormDataPart("like", String.valueOf(1));

                            OkHttpClient client = MySingleton.sharedInstance().httpClient;

                            //요청 Body 세팅==> 그전 Query Parameter세팅과 같은 개념
                            RequestBody fileUploadBody = MB.build();

                            //요청 세팅
                            Request request = new Request.Builder()
                                    .url(SERVER_URL_LAWS_PROS_CONS)
                                    .post(fileUploadBody) //반드시 post로
                                    .build();
                            //동기 방식
                            response = client.newCall(request).execute();

                            flag = response.isSuccessful();
                            int responseCode = response.code();
                            Log.e("response==>", String.valueOf(responseCode));
                            //응답 코드 200등등

                            if (flag) {
                                e("response결과", response.message()); //읃답에 대한 메세지(OK)
                                //e("response응답바디", response.body().string()); //json으로 변신 You can only call string() once.

                                StringBuilder buf = new StringBuilder(response.body().string());

                                JSONObject jsonObject = new JSONObject(buf.toString());
                                final int agreeCount = jsonObject.getInt("agree_count");
                                final int disagreeCount = jsonObject.getInt("disagree_count");
                                final boolean agreeFlag = jsonObject.getBoolean("likeFlag");
                                final boolean disagreeFlag = jsonObject.getBoolean("dislikeFlag");

                                Log.e("좋아요 수 확인", String.valueOf(agreeCount));
                                Log.e("싫어요 수 확인", String.valueOf(disagreeCount));

                                view.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        agree_count_.setText(String.valueOf(agreeCount));
                                        agree_Button.setChecked(agreeFlag);
                                        disagree_count_.setText(String.valueOf(disagreeCount));
                                        disagree_Button.setChecked(disagreeFlag);

                                        if (disagree_Button.isChecked()) {
                                            disagree_Button.setChecked(false);
                                        }
                                    }
                                });
                            }

                        } catch (UnknownHostException une) {
                            e("AsyncLawInsertune", une.toString());
                        } catch (UnsupportedEncodingException uee) {
                            e("AsyncLawInsertuee", uee.toString());
                        } catch (Exception e) {
                            e("AsyncLawInserte", e.toString());
                        } finally {
                            if (response != null) {
                                response.close();
                            }
                        }
                    }
                }).start();


                Log.e("좋아요 버튼 눌림", "true");
            }
        });

        disagree_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String LAWID = String.valueOf(lawID);
                SERVER_URL_LAWS_PROS_CONS = String.format("http://%s:%d/%s/%s/%s", NetworkDefineConstant.HOST_URL, NetworkDefineConstant.PORT_NUMBER, "laws", LAWID, "rating");
                Log.e("LAWS_PROS_CONS", SERVER_URL_LAWS_PROS_CONS);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response response = null;
                        boolean flag = false;

                        MultipartBody.Builder MB;
                        MB = new MultipartBody.Builder();
                        MB.setType(MultipartBody.FORM);

                        try {
                            MB.addFormDataPart("user_id", PropertyManager.getInstance().getUserId());
                            MB.addFormDataPart("like", String.valueOf(2));

                            OkHttpClient client = MySingleton.sharedInstance().httpClient;

                            //요청 Body 세팅==> 그전 Query Parameter세팅과 같은 개념
                            RequestBody fileUploadBody = MB.build();

                            //요청 세팅
                            Request request = new Request.Builder()
                                    .url(SERVER_URL_LAWS_PROS_CONS)
                                    .post(fileUploadBody) //반드시 post로
                                    .build();
                            //동기 방식
                            response = client.newCall(request).execute();

                            flag = response.isSuccessful();
                            int responseCode = response.code();
                            Log.e("response==>", String.valueOf(responseCode));
                            //응답 코드 200등등

                            if (flag) {
                                e("response결과", response.message()); //읃답에 대한 메세지(OK)
                                //e("response응답바디", response.body().string()); //json으로 변신 You can only call string() once.

                                StringBuilder buf = new StringBuilder(response.body().string());

                                JSONObject jsonObject = new JSONObject(buf.toString());
                                final int agreeCount = jsonObject.getInt("agree_count");
                                final int disagreeCount = jsonObject.getInt("disagree_count");
                                final boolean agreeFlag = jsonObject.getBoolean("likeFlag");
                                final boolean disagreeFlag = jsonObject.getBoolean("dislikeFlag");
                                Log.e("좋아요 수 확인", String.valueOf(agreeCount));
                                Log.e("싫어요 수 확인", String.valueOf(disagreeCount));

                                view.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        agree_count_.setText(String.valueOf(agreeCount));
                                        agree_Button.setChecked(agreeFlag);
                                        disagree_count_.setText(String.valueOf(disagreeCount));
                                        disagree_Button.setChecked(disagreeFlag);
                                        if (agree_Button.isChecked()) {
                                            agree_Button.setChecked(false);
                                        }
                                    }
                                });
                            }

                        } catch (UnknownHostException une) {
                            e("AsyncLawInsertune", une.toString());
                        } catch (UnsupportedEncodingException uee) {
                            e("AsyncLawInsertuee", uee.toString());
                        } catch (Exception e) {
                            e("AsyncLawInserte", e.toString());
                        } finally {
                            if (response != null) {
                                response.close();
                            }
                        }
                    }
                }).start();
            }
        });

        toSNSRoot = (LinearLayout) findViewById(R.id.sns_share_root); // 공유 할 레이아웃

        ImageView share = (ImageView) findViewById(R.id.share_law);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = "sns_upload_image_file.jpg";
                File snsShareDir = new File(Environment.getExternalStorageDirectory() +
                        "/sns_share_dir_images/");
                FileOutputStream fos;
                if (Build.VERSION.SDK_INT >= 23) {
                    if (isStoragePermissionGranted()) {
                        toSNSRoot.buildDrawingCache();
                        Bitmap captureView = toSNSRoot.getDrawingCache();

                        try {
                            if (!snsShareDir.exists()) {
                                if (!snsShareDir.mkdirs()) {
                                }
                            }
                            File file = new File(snsShareDir, fileName);
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            fos = new FileOutputStream(file);
                            captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            //intent.putExtra(Intent.EXTRA_SUBJECT, "사진제목");
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

                            Intent target = Intent.createChooser(intent, "공유할 곳을 선택하세요");
                            startActivity(target);

                        } catch (Exception e) {
                            Log.e("onTouch", e.toString(), e);
                        }
                    }
                } else {
                    toSNSRoot.buildDrawingCache();
                    Bitmap captureView = toSNSRoot.getDrawingCache();
                    try {
                        if (!snsShareDir.exists()) {
                            if (!snsShareDir.mkdirs()) {
                            }
                        }
                        File file = new File(snsShareDir, fileName);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        fos = new FileOutputStream(file);
                        captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        //intent.putExtra(Intent.EXTRA_SUBJECT, "사진제목");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(snsShareDir));

                        Intent target = Intent.createChooser(intent, "공유할 곳을 선택하세요");
                        startActivity(target);

                    } catch (Exception e) {
                        Log.e("onTouch", e.toString(), e);
                    }
                }


            }
        });
    } //onCreate

    public int getLawStatus(String string) {
        if (string.equals("접수")) {
            return 1;
        } else if (string.equals("위원회심사")) {
            return 2;
        } else if (string.equals("체계자구심사")) {
            return 3;
        } else if (string.equals("본회의심의")) {
            return 4;
        } else if (string.equals("이송")) {
            return 5;
        } else if (string.equals("폐기")) {
            return 6;
        } else if (string.equals("부결")) {
            return 7;
        } else if (string.equals("철회")) {
            return 8;
        }

        return 0;
    }

    private void setupLawDetailViewPager(ViewPager viewPager) {
        LawPagerAdapter lawPagerAdapter = new LawPagerAdapter(getSupportFragmentManager());
        lawPagerAdapter.appendFragment(LawDetailContentFragment.newInstance(bundle));
        lawPagerAdapter.appendFragment(LawDetailGraphFragment.newInstance(1));
        lawPagerAdapter.appendFragment(LawDetailCommentFragment.newInstance(1));
        viewPager.setAdapter(lawPagerAdapter);
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

    private static class LawPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragment = new ArrayList<>();

        public LawPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment) {
            this.fragment.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragment.get(position);
        }

        @Override
        public int getCount() {
            return fragment.size();
        }

    }

    public class AsyncLawDetailJSONList extends AsyncTask<String, Integer, LawEntityObject> {
        ProgressDialog dialog;

        @Override
        protected LawEntityObject doInBackground(String... params) {
            Response response = null;
            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .header("user_id", PropertyManager.getInstance().getUserId())
                        .url(SERVER_URL_DETAIL_LAW)
                        .build();
                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                //int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONLawDetailRequestList(
                            new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("fileUpLoad", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("fileUpLoad", uee.toString());
            } catch (Exception e) {
                e("fileUpLoad", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(LawDetailActivity.this, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(LawEntityObject result) {
            dialog.dismiss();

            bundle = new Bundle();
            bundle.putString("status", result.processStatus);
            bundle.putString("contents", result.contents);
            bundle.putString("pdfurl", result.billPDFUrl);

            ViewPager viewPager = (ViewPager) findViewById(R.id.law_detail_viewpager);
            if (viewPager != null) {
                setupLawDetailViewPager(viewPager);
            }

            TabLayout tabLayout = (TabLayout) findViewById(R.id.law_tabs);
            tabLayout.setupWithViewPager(viewPager);

            if (TextUtils.isEmpty(result.committee)) {
                LawDetailActivity.committee.setImageResource(R.drawable.view_undecided);
            }

            if (result != null && !TextUtils.isEmpty(result.committee)) {
                String whatcommittee = result.committee.toString();
                int thiscommittee = getCommittee(whatcommittee);
                switch (thiscommittee) {
                    case 1:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_agricultureforestry);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.agricultureforestry);
                        break;
                    case 2:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_assemblyoperation);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.assemblyoperation);
                        break;
                    case 3:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_defense);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.defense);
                        break;
                    case 4:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_educationculture);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.educationculture);
                        break;
                    case 5:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_environmentlabor);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.environmentlabor);
                        break;
                    case 6:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_foreign);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.foreign);
                        break;
                    case 7:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_futurecreation);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.futurecreation);
                        break;
                    case 8:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_healthwelfare);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.healthwelfare);
                        break;
                    case 9:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_industrycommercial);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.industrycommercial);
                        break;
                    case 10:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_information);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.information);
                        break;
                    case 11:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_law);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.law);
                        break;
                    case 12:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_politicalaffairs);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.politicalaffairs);
                        break;
                    case 13:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_safety);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.safety);
                        break;
                    case 14:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_strategyfinance);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.strategyfinance);
                        break;
                    case 15:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_traffic);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.traffic);
                        break;
                    case 16:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_womenfamily);
                        //LawDetailActivity.date_.setBackgroundResource(R.color.womenfamily);
                        break;
                    default:
                        LawDetailActivity.committee.setImageResource(R.drawable.view_undecided);
                        break;

                }
            }

            Log.e("법안 폐기냐", result.processStatus);
            if (!TextUtils.isEmpty(result.processStatus)) {
                String whatstatus = result.processStatus;
                int thisstatus = getLawStatus(whatstatus);
                switch (thisstatus) {
                    case 1:
                        lawStatus.setImageResource(R.drawable.screeningprocess);
                        break;
                    case 2:
                        lawStatus.setImageResource(R.drawable.screeningprocess);
                        break;
                    case 3:
                        lawStatus.setImageResource(R.drawable.screeningprocess);
                        break;
                    case 4:
                        lawStatus.setImageResource(R.drawable.screeningprocess);
                        break;
                    case 5:
                        lawStatus.setImageResource(R.drawable.screeningprocess);
                        break;
                    case 6:
                        lawStatus.setImageResource(R.drawable.textbox_disuse);
                        lawStatus.setClickable(false);
                        break;
                    case 7:
                        lawStatus.setImageResource(R.drawable.textbox_votedown);
                        lawStatus.setClickable(false);
                        break;
                    case 8:
                        lawStatus.setImageResource(R.drawable.textbox_withdrawal);
                        lawStatus.setClickable(false);
                        break;
                }
            }

            LawDetailActivity.title_.setText(result.title.toString());
            LawDetailActivity.law_id.setText(String.valueOf(result.lawID));
            //LawDetailActivity.date_.setText(result.date.toString());
            LawDetailActivity.proposer_.setText(result.proposer.toString());
            LawDetailActivity.viewCount_.setText(String.valueOf(result.view));
            LawDetailActivity.agree_count_.setText(String.valueOf(result.agree_count));
            LawDetailActivity.disagree_count_.setText(String.valueOf(result.disagree_count));
            if (result.likeFlag) {
                LawDetailActivity.agree_Button.setChecked(true);
            } else if (!result.likeFlag) {
                LawDetailActivity.agree_Button.setChecked(false);
            }
            if (result.dislikeFlag) {
                LawDetailActivity.disagree_Button.setChecked(true);
            } else if (!result.dislikeFlag) {
                LawDetailActivity.disagree_Button.setChecked(false);
            }
            if (result.interFlag) {
                LawDetailActivity.interest_law_mark.setChecked(true);
            } else if (!result.interFlag) {
                LawDetailActivity.interest_law_mark.setChecked(false);
            }


        }

    }
}

