package kr.co.citizoomproject.android.citizoom.Issue;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.citizoomproject.android.citizoom.Main.MainActivity;
import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-21.
 */
public class IssueRecyclerAdapter extends RecyclerView.Adapter<IssueRecyclerAdapter.ViewHolder> {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public ArrayList<IssueGetObject> issueGetObjects;
    public IssueFragment fragment;
    MainActivity owner;
    private String strColor = "#1fc1d6";

    public IssueRecyclerAdapter(Context context, ArrayList<IssueGetObject> resources) {
        this.issueGetObjects = resources;
        owner = (MainActivity) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final IssueGetObject issueGetObject = issueGetObjects.get(position);

        holder.share.setOnClickListener(new View.OnClickListener() {           // 공유하기 버튼 클릭
            @Override
            public void onClick(View view) {
                String fileName = "sns_upload_image_file.jpg";
                File snsShareDir = new File(Environment.getExternalStorageDirectory() +
                        "/sns_share_dir_images/");
                FileOutputStream fos;
                if (Build.VERSION.SDK_INT >= 23) {
                    if (isStoragePermissionGranted()) {
                        holder.totalView.buildDrawingCache();
                        Bitmap captureView = holder.totalView.getDrawingCache();

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
                            owner.startActivity(target);

                        } catch (Exception e) {
                            Log.e("onTouch", e.toString(), e);
                        }
                    }
                } else {
                    holder.totalView.buildDrawingCache();
                    Bitmap captureView = holder.totalView.getDrawingCache();
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
                        owner.startActivity(target);

                    } catch (Exception e) {
                        Log.e("onTouch", e.toString(), e);
                    }
                }

            }
        });

        //**********************************************************************************************************************

        holder.question.setText(issueGetObject.question);
        holder.score.setText(String.valueOf(issueGetObject.totalCount) + " 표");

        if (issueGetObject.profile_image.equals("https://s3.ap-northeast-2.amazonaws.com//citizoom/")) {
            Glide.with(MyApplication.getMyContext()).load(R.drawable.profile_basic).into(holder.user_profile);
        } else {
            Glide.with(MyApplication.getMyContext()).load(issueGetObject.profile_image).into(holder.user_profile);
        }

        holder.user_nick.setText(issueGetObject.nickname);
        holder.date_time.setText(issueGetObject.date);

        Log.i("id", PropertyManager.getInstance().getUserId());

        // 이슈 아이템이 사용자 본인의 것 일때
        if (issueGetObject.register_id.equals(PropertyManager.getInstance().getUserId())) {
            final CharSequence[] items = {"삭제"};

            holder.modifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(owner, R.style.MyDialogTheme);

                    // 여기서 부터는 알림창의 속성 설정
                    builder.setTitle(issueGetObject.nickname)        // 제목 설정
                            .setItems(items, new DialogInterface.OnClickListener() {    // 목록 클릭시 설정
                                public void onClick(DialogInterface dialog, int index) {
                                    if (index == 0) {
                                        new AsyncIssueDeleteJSONList(issueGetObject.issue_id).execute();
                                    }
                                    Toast.makeText(MyApplication.getMyContext(), items[index], Toast.LENGTH_SHORT).show();
                                }
                            });

                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                    dialog.show();    // 알림창 띄우기

                }
            });
            // 보기가 두 개 일때
            if (issueGetObject.choice_arr.size() == 2) {
                issueGetObject.voteFlag = true;

                holder.answer3.setVisibility(View.GONE);
                holder.answer4.setVisibility(View.GONE);
                holder.answer5.setVisibility(View.GONE);

                holder.issue_answer1.setText(issueGetObject.choice_arr.get(0).choice);
                double percent1 = ((double) issueGetObject.choice_arr.get(0).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent1.setText(issueGetObject.choice_arr.get(0).vote_count + " 표");
                setGraphContent(holder.percent_image1, percent1);

                holder.issue_answer2.setText(issueGetObject.choice_arr.get(1).choice);
                double percent2 = ((double) issueGetObject.choice_arr.get(1).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent2.setText(issueGetObject.choice_arr.get(1).vote_count + " 표");
                setGraphContent(holder.percent_image2, percent2);

            }
            if (issueGetObject.choice_arr.size() == 3) {
                issueGetObject.voteFlag = true;

                // 항목 보이게 설정
                holder.answer3.setVisibility(View.VISIBLE);
                holder.answer4.setVisibility(View.GONE);
                holder.answer5.setVisibility(View.GONE);

                holder.issue_answer1.setText(issueGetObject.choice_arr.get(0).choice);
                double percent1 = ((double) issueGetObject.choice_arr.get(0).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent1.setText(issueGetObject.choice_arr.get(0).vote_count + " 표");
                setGraphContent(holder.percent_image1, percent1);

                holder.issue_answer2.setText(issueGetObject.choice_arr.get(1).choice);
                double percent2 = ((double) issueGetObject.choice_arr.get(1).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent2.setText(issueGetObject.choice_arr.get(1).vote_count + " 표");
                setGraphContent(holder.percent_image2, percent2);

                holder.issue_answer3.setText(issueGetObject.choice_arr.get(2).choice);
                double percent3 = ((double) issueGetObject.choice_arr.get(2).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent3.setText(issueGetObject.choice_arr.get(2).vote_count + " 표");
                setGraphContent(holder.percent_image3, percent3);

            }
            if (issueGetObject.choice_arr.size() == 4) {
                issueGetObject.voteFlag = true;

                holder.answer3.setVisibility(View.VISIBLE);
                holder.answer4.setVisibility(View.VISIBLE);
                holder.answer5.setVisibility(View.GONE);

                holder.issue_answer1.setText(issueGetObject.choice_arr.get(0).choice);
                double percent1 = ((double) issueGetObject.choice_arr.get(0).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent1.setText(issueGetObject.choice_arr.get(0).vote_count + " 표");
                setGraphContent(holder.percent_image1, percent1);

                holder.issue_answer2.setText(issueGetObject.choice_arr.get(1).choice);
                double percent2 = ((double) issueGetObject.choice_arr.get(1).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent2.setText(issueGetObject.choice_arr.get(1).vote_count + " 표");
                setGraphContent(holder.percent_image2, percent2);

                holder.issue_answer3.setText(issueGetObject.choice_arr.get(2).choice);
                double percent3 = ((double) issueGetObject.choice_arr.get(2).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent3.setText(issueGetObject.choice_arr.get(2).vote_count + " 표");
                setGraphContent(holder.percent_image3, percent3);

                holder.issue_answer4.setText(issueGetObject.choice_arr.get(3).choice);
                double percent4 = ((double) issueGetObject.choice_arr.get(3).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent4.setText(issueGetObject.choice_arr.get(3).vote_count + " 표");
                setGraphContent(holder.percent_image4, percent4);

            }
            if (issueGetObject.choice_arr.size() == 5) {
                issueGetObject.voteFlag = true;

                holder.answer3.setVisibility(View.VISIBLE);
                holder.answer4.setVisibility(View.VISIBLE);
                holder.answer5.setVisibility(View.VISIBLE);

                holder.issue_answer1.setText(issueGetObject.choice_arr.get(0).choice);
                double percent1 = ((double) issueGetObject.choice_arr.get(0).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent1.setText(issueGetObject.choice_arr.get(0).vote_count + " 표");
                setGraphContent(holder.percent_image1, percent1);

                holder.issue_answer2.setText(issueGetObject.choice_arr.get(1).choice);
                double percent2 = ((double) issueGetObject.choice_arr.get(1).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent2.setText(issueGetObject.choice_arr.get(1).vote_count + " 표");
                setGraphContent(holder.percent_image2, percent2);

                holder.issue_answer3.setText(issueGetObject.choice_arr.get(2).choice);
                double percent3 = ((double) issueGetObject.choice_arr.get(2).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent3.setText(issueGetObject.choice_arr.get(2).vote_count + " 표");
                setGraphContent(holder.percent_image3, percent3);

                holder.issue_answer4.setText(issueGetObject.choice_arr.get(3).choice);
                double percent4 = ((double) issueGetObject.choice_arr.get(3).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent4.setText(issueGetObject.choice_arr.get(3).vote_count + " 표");
                setGraphContent(holder.percent_image4, percent4);

                holder.issue_answer5.setText(issueGetObject.choice_arr.get(4).choice);
                double percent5 = ((double) issueGetObject.choice_arr.get(4).vote_count / (double) issueGetObject.totalCount);
                holder.issue_percent5.setText(issueGetObject.choice_arr.get(4).vote_count + " 표");
                setGraphContent(holder.percent_image5, percent5);


            }
        } // 본인 issue일때

        //********************************************************************************************************************************************************************************************************
        // 사용자 본인의 ISSUE가 아닐때 처리 -> 투표 후 보이게 반영 (새로고침 후 position 유지)


        if (!issueGetObject.register_id.equals(PropertyManager.getInstance().getUserId())) {
            final CharSequence[] items = {"신고"};

            holder.modifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(owner, R.style.MyDialogTheme);

                    // 여기서 부터는 알림창의 속성 설정
                    builder.setTitle(issueGetObject.nickname)        // 제목 설정
                            .setItems(items, new DialogInterface.OnClickListener() {    // 목록 클릭시 설정
                                public void onClick(DialogInterface dialog, int index) {
                                    if (index == 0) {

                                    }
                                    Toast.makeText(MyApplication.getMyContext(), "신고되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });

                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                    dialog.show();    // 알림창 띄우기

                }
            });

            if (!issueGetObject.voteFlag) {
                // 다른 사용자의 issue를 투표를 안 했을 때

                if (issueGetObject.choice_arr.size() == 2) {
                    holder.answer3.setVisibility(View.GONE);
                    holder.answer4.setVisibility(View.GONE);
                    holder.answer5.setVisibility(View.GONE);

                    double percent1 = ((double) issueGetObject.choice_arr.get(0).vote_count / (double) issueGetObject.totalCount);
                    setGraphContent(holder.percent_image1, percent1);
                    holder.percent_image1.setVisibility(View.INVISIBLE);
                    holder.issue_answer1.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer1.setText(issueGetObject.choice_arr.get(0).choice);


                    // 투표하기 (항목 클릭)
                    holder.issue_question1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 0).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });

                    double percent2 = ((double) issueGetObject.choice_arr.get(1).vote_count / (double) issueGetObject.totalCount);
                    setGraphContent(holder.percent_image1, percent2);
                    holder.percent_image2.setVisibility(View.INVISIBLE);
                    holder.issue_answer2.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer2.setText(issueGetObject.choice_arr.get(1).choice);

                    // 투표하기 (항목 클릭)
                    holder.issue_question2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 1).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });


                }
                if (issueGetObject.choice_arr.size() == 3) {
                    holder.answer3.setVisibility(View.VISIBLE);
                    holder.answer4.setVisibility(View.GONE);
                    holder.answer5.setVisibility(View.GONE);

                    double percent1 = ((double) issueGetObject.choice_arr.get(0).vote_count / (double) issueGetObject.totalCount);
                    setGraphContent(holder.percent_image1, percent1);
                    holder.percent_image1.setVisibility(View.INVISIBLE);
                    holder.issue_answer1.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer1.setText(issueGetObject.choice_arr.get(0).choice);

                    // 투표하기 (항목 클릭)
                    holder.issue_question1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            holder.percent_image3.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 0).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });

                    double percent2 = ((double) issueGetObject.choice_arr.get(1).vote_count / (double) issueGetObject.totalCount);
                    setGraphContent(holder.percent_image1, percent2);
                    holder.percent_image2.setVisibility(View.INVISIBLE);
                    holder.issue_answer2.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer2.setText(issueGetObject.choice_arr.get(1).choice);

                    // 투표하기 (항목 클릭)
                    holder.issue_question2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            holder.percent_image3.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 1).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });

                    double percent3 = ((double) issueGetObject.choice_arr.get(2).vote_count / (double) issueGetObject.totalCount);
                    setGraphContent(holder.percent_image1, percent3);
                    holder.percent_image3.setVisibility(View.INVISIBLE);
                    holder.issue_answer3.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer3.setText(issueGetObject.choice_arr.get(2).choice);

                    // 투표하기 (항목 클릭)
                    holder.issue_question3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            holder.percent_image3.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 2).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });

                }
                if (issueGetObject.choice_arr.size() == 4) {
                    holder.answer3.setVisibility(View.VISIBLE);
                    holder.answer4.setVisibility(View.VISIBLE);
                    holder.answer5.setVisibility(View.GONE);

                    holder.percent_image1.setVisibility(View.INVISIBLE);
                    holder.issue_answer1.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer1.setText(issueGetObject.choice_arr.get(0).choice);

                    // 투표하기 (항목 클릭)
                    holder.issue_question1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            holder.percent_image3.setVisibility(View.VISIBLE);
                            holder.percent_image4.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 0).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });

                    holder.percent_image2.setVisibility(View.INVISIBLE);
                    holder.issue_answer2.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer2.setText(issueGetObject.choice_arr.get(1).choice);

                    // 투표하기 (항목 클릭)
                    holder.issue_question2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            holder.percent_image3.setVisibility(View.VISIBLE);
                            holder.percent_image4.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 1).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });

                    holder.percent_image3.setVisibility(View.INVISIBLE);
                    holder.issue_answer3.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer3.setText(issueGetObject.choice_arr.get(2).choice);

                    // 투표하기 (항목 클릭)
                    holder.issue_question3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            holder.percent_image3.setVisibility(View.VISIBLE);
                            holder.percent_image4.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 2).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });

                    holder.percent_image4.setVisibility(View.INVISIBLE);
                    holder.issue_answer4.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer4.setText(issueGetObject.choice_arr.get(3).choice);

                    // 투표하기 (항목 클릭)
                    holder.issue_question4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            holder.percent_image3.setVisibility(View.VISIBLE);
                            holder.percent_image4.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 3).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });

                }
                if (issueGetObject.choice_arr.size() == 5) {
                    holder.answer3.setVisibility(View.VISIBLE);
                    holder.answer4.setVisibility(View.VISIBLE);
                    holder.answer5.setVisibility(View.VISIBLE);

                    holder.percent_image1.setVisibility(View.INVISIBLE);
                    holder.issue_answer1.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer1.setText(issueGetObject.choice_arr.get(0).choice);

                    // 투표하기 (항목 클릭)
                    holder.issue_question1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            holder.percent_image3.setVisibility(View.VISIBLE);
                            holder.percent_image4.setVisibility(View.VISIBLE);
                            holder.percent_image5.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 0).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });

                    holder.percent_image2.setVisibility(View.INVISIBLE);
                    holder.issue_answer2.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer2.setText(issueGetObject.choice_arr.get(1).choice);

                    // 투표하기 (항목 클릭)
                    holder.issue_question2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            holder.percent_image3.setVisibility(View.VISIBLE);
                            holder.percent_image4.setVisibility(View.VISIBLE);
                            holder.percent_image5.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 1).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });

                    holder.percent_image3.setVisibility(View.INVISIBLE);
                    holder.issue_answer3.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer3.setText(issueGetObject.choice_arr.get(2).choice);

                    // 투표하기 (항목 클릭)
                    holder.issue_question3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            holder.percent_image3.setVisibility(View.VISIBLE);
                            holder.percent_image4.setVisibility(View.VISIBLE);
                            holder.percent_image5.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 2).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });

                    holder.percent_image4.setVisibility(View.INVISIBLE);
                    holder.issue_answer4.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer4.setText(issueGetObject.choice_arr.get(3).choice);

                    // 투표하기 (항목 클릭)
                    holder.issue_question4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            holder.percent_image3.setVisibility(View.VISIBLE);
                            holder.percent_image4.setVisibility(View.VISIBLE);
                            holder.percent_image5.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 3).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });

                    holder.percent_image5.setVisibility(View.INVISIBLE);
                    holder.issue_answer5.setTextColor(Color.parseColor(strColor));
                    holder.issue_answer5.setText(issueGetObject.choice_arr.get(4).choice);

                    // 투표하기 (항목 클릭)
                    holder.issue_question5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.percent_image1.setVisibility(View.VISIBLE);
                            holder.percent_image2.setVisibility(View.VISIBLE);
                            holder.percent_image3.setVisibility(View.VISIBLE);
                            holder.percent_image4.setVisibility(View.VISIBLE);
                            holder.percent_image5.setVisibility(View.VISIBLE);
                            issueGetObject.voteFlag = true;
                            new AsyncIssueVoteJSONList(issueGetObject.issue_id, 4).execute();
                            holder.totalView.setClickable(false);
                            issueGetObject.voteFlag = true;
                        }
                    });

                }
            } else if (issueGetObject.voteFlag) {
                    if (issueGetObject.choice_arr.size() == 2) {
                        holder.answer3.setVisibility(View.GONE);
                        holder.answer4.setVisibility(View.GONE);
                        holder.answer5.setVisibility(View.GONE);

                        holder.issue_answer1.setText(issueGetObject.choice_arr.get(0).choice);
                        double percent1 = ((double) issueGetObject.choice_arr.get(0).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent1.setText(String.valueOf((int) (percent1 * 100.0)) + "%");
                        setGraphContent(holder.percent_image1, percent1);

                        holder.issue_answer2.setText(issueGetObject.choice_arr.get(1).choice);
                        double percent2 = ((double) issueGetObject.choice_arr.get(1).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent2.setText(String.valueOf((int) (percent2 * 100.0)) + "%");
                        setGraphContent(holder.percent_image2, percent2);


                    }
                    if (issueGetObject.choice_arr.size() == 3) {
                        holder.answer3.setVisibility(View.VISIBLE);
                        holder.answer4.setVisibility(View.GONE);
                        holder.answer5.setVisibility(View.GONE);

                        int count0 = issueGetObject.choice_arr.get(0).vote_count;
                        Log.i("첫번째 투표수", String.valueOf(count0));
                        issueGetObject.choice_arr.get(0).vote_count = count0;

                        holder.issue_answer1.setText(issueGetObject.choice_arr.get(0).choice);
                        double percent1 = ((double) issueGetObject.choice_arr.get(0).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent1.setText(String.valueOf((int) (percent1 * 100.0)) + "%");
                        setGraphContent(holder.percent_image1, percent1);

                        holder.issue_answer2.setText(issueGetObject.choice_arr.get(1).choice);
                        double percent2 = ((double) issueGetObject.choice_arr.get(1).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent2.setText(String.valueOf((int) (percent2 * 100.0)) + "%");
                        setGraphContent(holder.percent_image2, percent2);

                        holder.issue_answer3.setText(issueGetObject.choice_arr.get(2).choice);
                        double percent3 = ((double) issueGetObject.choice_arr.get(2).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent3.setText(String.valueOf((int) (percent3 * 100.0)) + "%");
                        setGraphContent(holder.percent_image3, percent3);


                    }
                    if (issueGetObject.choice_arr.size() == 4) {
                        holder.answer3.setVisibility(View.VISIBLE);
                        holder.answer4.setVisibility(View.VISIBLE);
                        holder.answer5.setVisibility(View.GONE);


                        holder.issue_answer1.setText(issueGetObject.choice_arr.get(0).choice);
                        double percent1 = ((double) issueGetObject.choice_arr.get(0).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent1.setText(String.valueOf((int) (percent1 * 100.0)) + "%");
                        setGraphContent(holder.percent_image1, percent1);

                        holder.issue_answer2.setText(issueGetObject.choice_arr.get(1).choice);
                        double percent2 = ((double) issueGetObject.choice_arr.get(1).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent2.setText(String.valueOf((int) (percent2 * 100.0)) + "%");
                        setGraphContent(holder.percent_image2, percent2);

                        holder.issue_answer3.setText(issueGetObject.choice_arr.get(2).choice);
                        double percent3 = ((double) issueGetObject.choice_arr.get(2).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent3.setText(String.valueOf((int) (percent3 * 100.0)) + "%");
                        setGraphContent(holder.percent_image3, percent3);

                        holder.issue_answer4.setText(issueGetObject.choice_arr.get(3).choice);
                        double percent4 = ((double) issueGetObject.choice_arr.get(3).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent4.setText(String.valueOf((int) (percent4 * 100.0)) + "%");
                        setGraphContent(holder.percent_image4, percent4);


                    }
                    if (issueGetObject.choice_arr.size() == 5) {
                        holder.answer3.setVisibility(View.VISIBLE);
                        holder.answer4.setVisibility(View.VISIBLE);
                        holder.answer5.setVisibility(View.VISIBLE);


                        holder.issue_answer1.setText(issueGetObject.choice_arr.get(0).choice);
                        double percent1 = ((double) issueGetObject.choice_arr.get(0).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent1.setText(String.valueOf((int) (percent1 * 100.0)) + "%");
                        setGraphContent(holder.percent_image1, percent1);

                        holder.issue_answer2.setText(issueGetObject.choice_arr.get(1).choice);
                        double percent2 = ((double) issueGetObject.choice_arr.get(1).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent2.setText(String.valueOf((int) (percent2 * 100.0)) + "%");
                        setGraphContent(holder.percent_image2, percent2);

                        holder.issue_answer3.setText(issueGetObject.choice_arr.get(2).choice);
                        double percent3 = ((double) issueGetObject.choice_arr.get(2).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent3.setText(String.valueOf((int) (percent3 * 100.0)) + "%");
                        setGraphContent(holder.percent_image3, percent3);

                        holder.issue_answer4.setText(issueGetObject.choice_arr.get(3).choice);
                        double percent4 = ((double) issueGetObject.choice_arr.get(3).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent4.setText(String.valueOf((int) (percent4 * 100.0)) + "%");
                        setGraphContent(holder.percent_image4, percent4);

                        holder.issue_answer5.setText(issueGetObject.choice_arr.get(4).choice);
                        double percent5 = ((double) issueGetObject.choice_arr.get(4).vote_count / (double) issueGetObject.totalCount);
                        holder.issue_percent5.setText(String.valueOf((int) (percent5 * 100.0)) + "%");
                        setGraphContent(holder.percent_image5, percent5);




                }
            }
        }

    } // onBindViewHolder


    @Override
    public int getItemCount() {
        return issueGetObjects.size();
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (owner.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(owner, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                return false;
            }
        } else {
            return true;
        }
    }

    public void setGraphContent(ImageView percentIV, double percent) {
        Log.i("percent", String.valueOf(percent));
        // 핸트폰 크기 재는 코드
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) MyApplication.getMyContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        if (percent == 1) {
            percentIV.setBackgroundResource(R.drawable.full_percent);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) percentIV.getLayoutParams();
            params.width = (int) ((double) (metrics.widthPixels) * 0.84);
            params.height = (int) ((double) (metrics.heightPixels) * 0.052);
            percentIV.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) percentIV.getLayoutParams();
            params.width = (int) ((double) (metrics.widthPixels) * 0.84 * percent);
            params.height = (int) ((double) (metrics.heightPixels) * 0.052);
            percentIV.setLayoutParams(params);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView question;
        public final TextView score;
        public final CircleImageView user_profile;
        public final TextView user_nick;
        public final TextView date_time;
        public final ImageView share;
        public final ImageView modifier;

        public final ImageView issue_question1;
        public final ImageView percent_image1;
        public final TextView issue_answer1;
        public final TextView issue_percent1;

        public final ImageView issue_question5;
        public final ImageView percent_image5;
        public final TextView issue_answer5;
        public final TextView issue_percent5;

        public final ImageView issue_question2;
        public final ImageView percent_image2;
        public final TextView issue_answer2;
        public final TextView issue_percent2;

        public final ImageView issue_question3;
        public final ImageView percent_image3;
        public final TextView issue_answer3;
        public final TextView issue_percent3;

        public final ImageView issue_question4;
        public final ImageView percent_image4;
        public final TextView issue_answer4;
        public final TextView issue_percent4;

        public RelativeLayout answer5, answer3, answer4;

        public RelativeLayout totalView;


        public ViewHolder(View view) {
            super(view);
            totalView = (RelativeLayout) view.findViewById(R.id.sns_share_root);
            mView = view;
            question = (TextView) view.findViewById(R.id.issue_question);
            score = (TextView) view.findViewById(R.id.score);
            user_profile = (CircleImageView) view.findViewById(R.id.user_profile_image);
            user_nick = (TextView) view.findViewById(R.id.user_nick);
            date_time = (TextView) view.findViewById(R.id.date_time);
            share = (ImageView) view.findViewById(R.id.share_issue);
            modifier = (ImageView) view.findViewById(R.id.modifier);

            issue_question1 = (ImageView) view.findViewById(R.id.issue_question1);
            percent_image1 = (ImageView) view.findViewById(R.id.percent_image1);
            issue_answer1 = (TextView) view.findViewById(R.id.issue_answer1);
            issue_percent1 = (TextView) view.findViewById(R.id.issue_agree_percent1);

            issue_question2 = (ImageView) view.findViewById(R.id.issue_question2);
            percent_image2 = (ImageView) view.findViewById(R.id.percent_image2);
            issue_answer2 = (TextView) view.findViewById(R.id.issue_answer2);
            issue_percent2 = (TextView) view.findViewById(R.id.issue_agree_percent2);

            issue_question3 = (ImageView) view.findViewById(R.id.issue_question3);
            percent_image3 = (ImageView) view.findViewById(R.id.percent_image3);
            issue_answer3 = (TextView) view.findViewById(R.id.issue_answer3);
            issue_percent3 = (TextView) view.findViewById(R.id.issue_agree_percent3);

            issue_question4 = (ImageView) view.findViewById(R.id.issue_question4);
            percent_image4 = (ImageView) view.findViewById(R.id.percent_image4);
            issue_answer4 = (TextView) view.findViewById(R.id.issue_answer4);
            issue_percent4 = (TextView) view.findViewById(R.id.issue_agree_percent4);

            issue_question5 = (ImageView) view.findViewById(R.id.issue_question5);
            percent_image5 = (ImageView) view.findViewById(R.id.percent_image5);
            issue_answer5 = (TextView) view.findViewById(R.id.issue_answer5);
            issue_percent5 = (TextView) view.findViewById(R.id.issue_agree_percent5);

            answer3 = (RelativeLayout) view.findViewById(R.id.answer3);
            answer4 = (RelativeLayout) view.findViewById(R.id.answer4);
            answer5 = (RelativeLayout) view.findViewById(R.id.answer5);


        }
    }

    public class AsyncIssueDeleteJSONList extends AsyncTask<String, Integer, IssueGetObject> {
        String issueID;

        public AsyncIssueDeleteJSONList(String issueID) {
            this.issueID = issueID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected IssueGetObject doInBackground(String... params) {
            Response response = null;
            boolean flag = false;

            MultipartBody.Builder MB;
            MB = new MultipartBody.Builder();
            MB.setType(MultipartBody.FORM);

            try {
                MB.addFormDataPart("user_id", PropertyManager.getInstance().getUserId());

                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                RequestBody fileUploadBody = MB.build();

                String url = NetworkDefineConstant.SERVER_URL_ISSUE_DELETE + issueID + "?user_id=" + PropertyManager.getInstance().getUserId();
                Request request = new Request.Builder()
                        .url(url)
                        .delete(fileUploadBody)
                        .build();

                //동기 방식
                response = client.newCall(request).execute();

                flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                Log.e("response==>", String.valueOf(responseCode));

                if (flag) {
                    e("response결과", response.message()); //읃답에 대한 메세지(OK)
                    //e("response응답바디", response.body().string()); //json으로 변신
                }
            } catch (UnknownHostException une) {
                e("이슈지우기1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("이슈지우기2", uee.toString());
            } catch (Exception e) {
                e("이슈지우기3", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return null;

        }


        @Override
        protected void onPostExecute(IssueGetObject result) {

        }
    }

    public class AsyncIssueVoteJSONList extends AsyncTask<String, Integer, String> {
        String issueID;
        int position;

        public AsyncIssueVoteJSONList(String issueID, int position) {
            this.issueID = issueID;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Response response = null;
            boolean flag = false;

            MultipartBody.Builder MB;
            MB = new MultipartBody.Builder();
            MB.setType(MultipartBody.FORM);

            try {
                MB.addFormDataPart("choice_num", String.valueOf(position));
                Log.i("choice_num", String.valueOf(position));
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                RequestBody fileUploadBody = MB.build();

                String url = NetworkDefineConstant.SERVER_URL_ISSUE_DELETE + issueID + "/vote";
                Log.e("url", String.valueOf(url));
                Request request = new Request.Builder()
                        .url(url)
                        .post(fileUploadBody)
                        .build();

                //동기 방식
                response = client.newCall(request).execute();

                flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                Log.e("response==>", String.valueOf(responseCode));

                if (flag) {
                    e("response결과", response.message()); //읃답에 대한 메세지(OK)
                    e("response응답바디", response.body().string()); //json으로 변신

                }
            } catch (UnknownHostException une) {
                e("이슈투표1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("이슈투표2", uee.toString());
            } catch (Exception e) {
                e("이슈투표3", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return "";

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {

                Intent intent = new Intent(owner, MainActivity.class);
                owner.finish();
                owner.startActivity(intent);

            }
        }
    }
}