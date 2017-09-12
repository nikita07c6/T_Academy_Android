package kr.co.citizoomproject.android.citizoom.Setting;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.MySingleton;
import kr.co.citizoomproject.android.citizoom.NetworkDefineConstant;
import kr.co.citizoomproject.android.citizoom.ParseDataParseHandler;
import kr.co.citizoomproject.android.citizoom.PropertyManager;
import kr.co.citizoomproject.android.citizoom.R;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-07-29.
 */
public class SettingProfileChangeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    EditText nickname;
    /**
     * 카메라에서 이미지 가져오기
     */
    Uri currentSelectedUri; //업로드할 현재 이미지에 대한 Uri
    File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리
    String currentFileName;  //파일이름
    RecyclerView rv;
    boolean flag;
    private ImageView mPhotoImageView;
    private ImageView picturepluse1;
    private ImageView fileUploadBtn;
    private UpLoadValueObject upLoadfiles;
    private InterestCommitteeListAdapter adapter;
    private TextView homeAddr;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_change_profile);

        picturepluse1 = (ImageView) findViewById(R.id.user_profile_image);
        picturepluse1.setOnClickListener(this);

        fileUploadBtn = (ImageView) findViewById(R.id.profile_picture_change);

        fileUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fileUploadBtn.setEnabled(false);
                new FileUpLoadAsyncTask().execute(upLoadfiles);

                //Toast.makeText(SettingProfileChangeActivity.this, "업로드할 파일이 없습니다", Toast.LENGTH_SHORT).show();
                //fileUpload();

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_change_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icn_back);

        nickname = (EditText) findViewById(R.id.editText);
        nickname.setText(PropertyManager.NICKNAME);
        nickname.setSelection(nickname.length());

        ImageView changeNick = (ImageView) findViewById(R.id.setting_nick_change_btn);
        changeNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(nickname.getText())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingProfileChangeActivity.this);     // 여기서 this는 Activity의 this

                    // 여기서 부터는 알림창의 속성 설정
                    builder.setMessage("닉네임을 입력해주세요.")// 메세지 설정
                            .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정

                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                // 확인 버튼 클릭시 설정
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                    dialog.show();    // 알림창 띄우기

                } else {
                    String changednickname = String.valueOf(nickname.getText());
                    new NickChangeAsync(changednickname).execute();
                }
            }
        });

        homeAddr = (TextView) findViewById(R.id.editText1);

        ImageView changeHome = (ImageView) findViewById(R.id.setting_home_change_btn);
        changeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingProfileChangeActivity.this, ChangeHomeActivity.class);
                startActivity(intent);
            }
        });

        ImageView changeInterest = (ImageView) findViewById(R.id.setting_topic_change_btn);
        changeInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingProfileChangeActivity.this, ChangeInterestActivity.class);
                startActivity(intent);
            }
        });

        rv = (RecyclerView) findViewById(R.id.interest_icn);
        rv.setLayoutManager(new GridLayoutManager(SettingProfileChangeActivity.this, 3));

        adapter = new InterestCommitteeListAdapter(SettingProfileChangeActivity.this);
        rv.setAdapter(adapter);

        new AsyncProfileJSONList().execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isSDCardAvailable()) {
            Toast.makeText(this, "SD 카드가 없어 종료 합니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String currentAppPackage = getPackageName();

        myImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), currentAppPackage);

        checkPermission();

        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
                Toast.makeText(getApplication(), " 저장할 디렉토리가 생성 됨", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_STORAGE);

            } else {
                //사용자가 언제나 허락
            }
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    //사용자가 퍼미션을 OK했을 경우

                } else {

                    Log.d(TAG, "Permission always deny");

                    //사용자가 퍼미션을 거절했을 경우
                }
                break;
        }
    }

    public boolean isSDCardAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private void doTakePhotoAction() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //업로드할 파일의 이름
        currentFileName = "upload_" + String.valueOf(System.currentTimeMillis() / 1000) + ".jpg";
        currentSelectedUri = Uri.fromFile(new File(myImageDir, currentFileName));
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, currentSelectedUri);
        startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CROP_FROM_CAMERA: {

                // 크롭된 이미지를 세팅
                final Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    mPhotoImageView.setImageBitmap(photo);
                }
                break;
            }
            case PICK_FROM_ALBUM: {

                currentSelectedUri = data.getData();
                if (currentSelectedUri != null) {
                    //실제 Image의 full path name을 얻어온다.
                    if (findImageFileNameFromUri(currentSelectedUri)) {
                        //ArrayList에 업로드할  객체를 추가한다.
                        upLoadfiles=new UpLoadValueObject(new File(currentFileName), false);
                    }
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap returedBitmap = (Bitmap) extras.get("data");
                    if (tempSavedBitmapFile(returedBitmap)) {
                        Log.e("임시이미지파일저장", "저장됨");
                    } else {
                        Log.e("임시이미지파일저장", "실패");
                    }
                }
                cropIntent(currentSelectedUri);
                break;

            }

            case PICK_FROM_CAMERA: {

                //카메라캡쳐를 이용해 가져온 이미지
                upLoadfiles=new UpLoadValueObject(new File(myImageDir, currentFileName), false);
                cropIntent(currentSelectedUri);
                break;
            }
        }
    }

    private void cropIntent(Uri cropUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(cropUri, "image/*");

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    private boolean tempSavedBitmapFile(Bitmap tempBitmap) {
        boolean flag = false;
        try {
            currentFileName = "upload_" + (System.currentTimeMillis() / 1000);
            String fileSuffix = ".jpg";
            //임시파일을 실행한다.
            File tempFile = File.createTempFile(
                    currentFileName,            // prefix
                    fileSuffix,                   // suffix
                    myImageDir                   // directory
            );
            final FileOutputStream bitmapStream = new FileOutputStream(tempFile);
            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 0, bitmapStream);
            upLoadfiles=new UpLoadValueObject(tempFile, true);
            if (bitmapStream != null) {
                bitmapStream.close();
            }
            currentSelectedUri = Uri.fromFile(tempFile);
            flag = true;
        } catch (IOException i) {
            Log.e("저장중 문제발생", i.toString(), i);
        }
        return flag;
    }

    private boolean findImageFileNameFromUri(Uri tempUri) {
        boolean flag = false;

        //실제 Image Uri의 절대이름
        String[] IMAGE_DB_COLUMN = {MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = null;
        try {
            //Primary Key값을 추출
            String imagePK = String.valueOf(ContentUris.parseId(tempUri));
            //Image DB에 쿼리를 날린다.
            cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_DB_COLUMN,
                    MediaStore.Images.Media._ID + "=?",
                    new String[]{imagePK}, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                currentFileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                Log.e("fileName", String.valueOf(currentFileName));
                flag = true;
            }
        } catch (SQLiteException sqle) {
            Log.e("findImage....", sqle.toString(), sqle);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return flag;
    }

    @Override
    public void onClick(View v) {
        AlertDialog dialog = null;
        mPhotoImageView = (ImageView) v;
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                doTakePhotoAction();
            }
        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                doTakeAlbumAction();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        dialog = new AlertDialog.Builder(this)
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("앨범선택", albumListener)
                .setNegativeButton("사진촬영", cameraListener)
                .setNeutralButton("취소", cancelListener).create();

        dialog.show();
    }

    public class AsyncProfileJSONList extends AsyncTask<String, Integer, ProfileObject> {

        @Override
        protected ProfileObject doInBackground(String... params) {
            Response response = null;

            try {
                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_PROFILE)
                        .build();
                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                //int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONProfileRequest(
                            new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("aaa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bbb", uee.toString());
            } catch (Exception e) {
                e("ccc", e.toString());
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
        }

        @Override
        protected void onPostExecute(ProfileObject result) {

            if (result != null) {
                nickname.setText(result.nickname);
                homeAddr.setText(result.location);
                if (TextUtils.isEmpty(result.profile_image)) {
                    Glide.with(SettingProfileChangeActivity.this).load(R.drawable.profile_basic318).into(picturepluse1);
                } else {
                    Glide.with(SettingProfileChangeActivity.this).load(result.profile_image).into(picturepluse1);
                    Log.i("profileimage", result.profile_image);
                }

                adapter.setResources(result.interesting_topics);
                adapter.notifyDataSetChanged();
            }
        }
    }

    class UpLoadValueObject {
        File file; //업로드할 파일
        boolean tempFiles; //임시파일 유무

        public UpLoadValueObject(File file, boolean tempFiles) {
            this.file = file;
            this.tempFiles = tempFiles;
        }
    }

    public class NickChangeAsync extends AsyncTask<Void, Void, String> {
        boolean flag;
        String changeNick;

        public NickChangeAsync(String changeNick) {
            this.changeNick = changeNick;
        }

        @Override
        protected String doInBackground(Void... params) {
            Response response = null;
            FormBody fd = null;

            try {

                fd = new FormBody.Builder()
                        //.add("nick", changeNick)
                        .build();

                OkHttpClient client = MySingleton.sharedInstance().httpClient;

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_CHANGE_NICK + changeNick)
                        .post(fd)
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

                    StringBuilder buf = new StringBuilder(response.body().string());

                    JSONObject jsonObject = new JSONObject(buf.toString());
                    String str_temp = jsonObject + "";
                    String[] tempArr = str_temp.split(":");
                    String tpm2 = tempArr[1];
                    String nickCheck = tpm2.split("\"")[1];

                    Log.e("nickCheck", nickCheck);
                    if (nickCheck.equals("Fail")) {
                        return null;
                    } else {
                        PropertyManager.getInstance().setNickname(nickCheck);
                        return nickCheck;
                    }
                }
            } catch (UnknownHostException une) {
                e("닉네임변경1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("닉네임변경2", uee.toString());
            } catch (Exception e) {
                e("닉네임변경3", e.toString());
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
        }

        @Override
        protected void onPostExecute(final String s) {
            if (TextUtils.isEmpty(s) || s.equals(null)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingProfileChangeActivity.this);     // 여기서 this는 Activity의 this

                // 여기서 부터는 알림창의 속성 설정
                builder.setMessage("닉네임이 중복됩니다. 다시 입력해주세요.")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기            } else {
                nickname.setText(s);
            }
            if (!TextUtils.isEmpty(s)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingProfileChangeActivity.this);     // 여기서 this는 Activity의 this

                // 여기서 부터는 알림창의 속성 설정
                builder.setMessage("변경되었습니다.")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기            } else {
            }
        }
    }

    private class FileUpLoadAsyncTask extends AsyncTask<UpLoadValueObject, Void, String> {
        //업로드할 Mime Type 설정
        private final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/jpg");


        public FileUpLoadAsyncTask() {
        }
        @Override
        protected String doInBackground(UpLoadValueObject... upLoadValueObjects) {
            Response response = null;
            try {
                OkHttpClient client = MySingleton.getSimpleInstance();

                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                File file = upLoadValueObjects[0].file;
                Log.e("filePath", String.valueOf(file.getAbsoluteFile()));
                builder.addFormDataPart("image", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file));

                RequestBody fileUploadBody = builder.build();

                //요청 세팅
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_CHANGE_PROFILE_PIC)
                        .post(fileUploadBody) //반드시 post로
                        .build();
                //동기 방식
                response = client.newCall(request).execute();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                Log.e("responseCode", String.valueOf(responseCode));
                if (flag) {
                    String json = response.body().string();
                    JSONObject obj = new JSONObject(json);
                    String imageURL = obj.getString("imageUrl");
                    return imageURL;
                }

            } catch (UnknownHostException une) {
                e("aa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bb", uee.toString());
            } catch (Exception e) {
                e("cc", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return "fail";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String returedImageUrl) {
            super.onPostExecute(returedImageUrl);

            if (returedImageUrl.startsWith("https://")) {
                    UpLoadValueObject fileValue = upLoadfiles;
                    if (fileValue.tempFiles) {
                        fileValue.file.deleteOnExit(); //임시파일을 삭제한다
                    }
                Glide.with(SettingProfileChangeActivity.this).load(returedImageUrl).into(picturepluse1);
                Log.i("프로필이미지", returedImageUrl);
            } else {
                Toast.makeText(SettingProfileChangeActivity.this, "파일업로드에 실패했습니다", Toast.LENGTH_LONG).show();
            }
            fileUploadBtn.setEnabled(true);
        }
    }

}
