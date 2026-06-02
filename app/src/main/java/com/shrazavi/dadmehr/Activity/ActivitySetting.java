package com.shrazavi.dadmehr.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shrazavi.dadmehr.Adapter.spinnerAdapter;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.DataClass.User;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageBase64;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactory;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySetting extends AppCompatActivity {
    String[] arrspecialty = {"حقوقی", "کیفری", "ثبتی", "خانواده", "ثبت شرکت", "مهاجرت"};
    TextInputEditText edtemail, edtname, edtexperience, edtbio, edtdegree, edteducation, edtshaba, edtcart;
    AutoCompleteTextView spinspecialty;
    TextView txtusername;
    Button btnchange;
    LinearLayout layes, layeducation, layhesab;
    String selectItensServer;
    String[] selectInitItens;
    static Handler threadHandler;
    public final int REQUEST_OPEN_GALLERY = 1;
    ImageView imgProfile;
    Thread uploadThread;
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String username = "";
    String vu = "";
    //    private Socket socket;
    public Handler handler;
    public static String imageProfile = "";
    String boundary = "*****";
    int bytesRead, bytesAvailable, bufferSize;
    public static Handler uploadHandler;
    public static Context context;
    byte[] buffer;
    int maxBufferSize = 1 * 1024 * 1024;
    //    public static SharedPreferences preferences;
    Retrofitinformation RI;
    Retrofitinformation RInode;
    FloatingActionButton btnupload;
    String gen;
    String filePath = "";
    public static MaterialProgressBar prload;
    private static final String TAG = ActivitySetting.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;
    String content;
    String enk;
    SecretKeySpec Key;
    TextInputLayout txtspin;

//    {
//        try {
//            socket = IO.socket(G.nodeurl);//http://192.168.1.103:8000
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
//        socket.connect();
        handler = new Handler();
        threadHandler = new Handler();
        context = ActivitySetting.this;
        layes = (LinearLayout) findViewById(R.id.lay_setting_es);
        layhesab = (LinearLayout) findViewById(R.id.lay_setting_hesab);
        layeducation = (LinearLayout) findViewById(R.id.lay_setting_education);
        edtemail = (TextInputEditText) findViewById(R.id.edt_setting_email);
        edtname = (TextInputEditText) findViewById(R.id.edt_setting_name);
        edtshaba = (TextInputEditText) findViewById(R.id.edt_setting_shaba);
        edtcart = (TextInputEditText) findViewById(R.id.edt_setting_cart);
        edtbio = (TextInputEditText) findViewById(R.id.edt_setting_bio);
        edtexperience = (TextInputEditText) findViewById(R.id.edt_setting_experience);
        edteducation = (TextInputEditText) findViewById(R.id.edt_setting_education);
        edtdegree = (TextInputEditText) findViewById(R.id.edt_setting_degree);
        spinspecialty = (AutoCompleteTextView) findViewById(R.id.spn_setting_specialty);
        txtusername = (TextView) findViewById(R.id.txt_setting_usernam);
        btnchange = (Button) findViewById(R.id.btn_setting_change);
        btnupload = (FloatingActionButton) findViewById(R.id.btn_setting_upload);
        imgProfile = (ImageView) findViewById(R.id.img_setting_prof);
        prload = findViewById(R.id.pr_setting_load);

//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        username = BasicActivity.userid;
        vu = BasicActivity.vu;
        BasicActivity.socket.on("pic", handleruploadpic);


        RI = RetrofitFactory.getclient().create(Retrofitinformation.class);
        RInode = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        txtusername.setText(username);
        String imgurl = BasicActivity.preferences.getString("imgprofile", "not");
        prload.setVisibility(View.GONE);
        btnchange.setVisibility(View.VISIBLE);
//        JSONObject nickname = new JSONObject();
//        try {
//            nickname.put("userid", username);
//            nickname.put("vu", vu);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        socket.emit("nickname", nickname);
        selectItensServer = "0b2,0e5,0f6,0h8";
        selectInitItens = selectItensServer.split(",");
//        enk = BasicActivity.preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + username + "-" + getResources().getString(R.string.developer), enk);
        content = BasicActivity.content;


        uploadHandler = new Handler();


        if (vu.equals("vl")) {
            edtdegree.setVisibility(View.VISIBLE);
            edteducation.setVisibility(View.VISIBLE);
            spinspecialty.setVisibility(View.VISIBLE);
            edtexperience.setVisibility(View.VISIBLE);
            layes.setVisibility(View.VISIBLE);
            layeducation.setVisibility(View.VISIBLE);
            layhesab.setVisibility(View.VISIBLE);

            spinspecialty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spinspecialty.clearListSelection();

                }
            });

            Call<Vakil> callvakil = RInode.getvakil(content, username, username);
            callvakil.enqueue(new Callback<Vakil>() {
                @Override
                public void onResponse(Call<Vakil> call, Response<Vakil> response) {

//                    preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
                    SharedPreferences.Editor editor = BasicActivity.preferences.edit();
                    editor.putString("imgprofile", G.nodeurl + response.body().getProfile());
                    editor.commit();
                    String imgurl = BasicActivity.preferences.getString("imgprofile", "not");
                    Log.e("name", response.body().getName());
                    Picasso.with(G.context).load(imgurl).into(imgProfile);
                    edtname.setText(response.body().getName());
                    edtemail.setText(response.body().getEmail());
                    edtdegree.setText(response.body().getDegree());
                    edteducation.setText(response.body().getEducation());
                    spinspecialty.setText(response.body().getSpecialty());
                    edtexperience.setText(response.body().getExperience());
                    edtshaba.setText(response.body().getShaba());
                    edtcart.setText(response.body().getCart());
                    edtbio.setText(response.body().getBio());
                    spinnerAdapter Adapterostan = new spinnerAdapter(ActivitySetting.this, android.R.layout.simple_list_item_1);
                    Adapterostan.addAll(arrspecialty);
                    spinspecialty.setAdapter(Adapterostan);

                }

                @Override
                public void onFailure(Call<Vakil> call, Throwable t) {
                    Log.e("error???", t + "");
                }
            });

        } else {

            edtdegree.setVisibility(View.GONE);
            edteducation.setVisibility(View.GONE);
            spinspecialty.setVisibility(View.GONE);
            edtexperience.setVisibility(View.GONE);
            layes.setVisibility(View.GONE);
            layeducation.setVisibility(View.GONE);
            layhesab.setVisibility(View.GONE);

            Log.e("username", username);
            Log.e("username", vu);

            Call<User> calluser = RInode.getuser(content, username, username);
            calluser.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

//                    preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
                    SharedPreferences.Editor editor = BasicActivity.preferences.edit();
                    editor.putString("imgprofile", G.nodeurl + response.body().getProfile());
                    editor.commit();
                    String imgurl = BasicActivity.preferences.getString("imgprofile", "not");
                    Picasso.with(G.context).load(imgurl).into(imgProfile);
                    Log.e("name", response.body().getName());
                    edtemail.setText(response.body().getEmail());
                    edtname.setText(response.body().getName());

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        }

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySetting.this, ActivityImageView.class);
                intent.putExtra("imgurl", imgurl);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("username", username);
                prload.setVisibility(View.VISIBLE);
                btnchange.setVisibility(View.GONE);

                if (vu.equals("vl")) {
                    if (edtname.toString().isEmpty() ||
                            edtemail.toString().isEmpty() ||
                            edtexperience.toString().isEmpty() ||
                            edtbio.toString().isEmpty() ||
                            spinspecialty.getText().toString().isEmpty() ||
                            edtcart.getText().toString().isEmpty() ||
                            edtshaba.getText().toString().isEmpty()) {
                        Toast.makeText(ActivitySetting.this, "لطفا مشخصاتتان را تکمیل کنید", Toast.LENGTH_SHORT).show();
                    } else {


                        Call<MessageSignup> callvakil = RInode.upgradeprofilevl(content,
                                username,
                                edtname.getText().toString(),
                                edtexperience.getText().toString(),
                                spinspecialty.getText().toString(),
                                edtdegree.getText().toString(),
                                edteducation.getText().toString(),
                                edtshaba.getText().toString(),
                                edtcart.getText().toString(),
                                edtemail.getText().toString(),
                                edtbio.getText().toString(),
                                username);

                        callvakil.enqueue(new Callback<MessageSignup>() {
                            @Override
                            public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                Boolean status = response.body().getStatus();
                                if (status) {
                                    Log.e("mess", response.body().getMessage());
                                    Intent intent = new Intent(ActivitySetting.this, ActivityOption.class);
                                    ActivitySetting.this.startActivity(intent);

                                } else {

                                }
                            }

                            @Override
                            public void onFailure(Call<MessageSignup> call, Throwable t) {
                                Log.e("error???", t + "");
                            }
                        });


                    }
                } else {
                    if (edtname.toString().isEmpty() || edtbio.toString().isEmpty() || edtemail.toString().isEmpty()) {
                        Toast.makeText(ActivitySetting.this, "لطفا مشخصاتتان را تکمیل کنید", Toast.LENGTH_SHORT).show();
                    } else {
//
                        Call<MessageSignup> calluser = RInode.upgradeprofileuser(content,
                                username,
                                edtname.getText().toString(),
                                edtemail.getText().toString(),
                                edtbio.getText().toString(),
                                username);

                        calluser.enqueue(new Callback<MessageSignup>() {
                            @Override
                            public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                Boolean status = response.body().getStatus();
                                if (status) {

                                    Intent intent = new Intent(ActivitySetting.this, ActivityOption.class);
                                    ActivitySetting.this.startActivity(intent);
                                    ActivitySetting.this.finish();
                                } else {

                                }
                            }

                            @Override
                            public void onFailure(Call<MessageSignup> call, Throwable t) {
                                Log.e("error???", t + "");
                            }
                        });


                    }
                }
            }
        });


        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileImageClick();
//                chooseFile();
                Log.e("vu", vu);
                Log.e("username", username);
            }
        });

    }


    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_OPEN_GALLERY);
    }

    void onProfileImageClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        } else {
                            // TODO - handle permission denied case
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ActivityImagePicker.showImagePickerOptions(this, new ActivityImagePicker.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                ActivityImagePicker.clearCache(G.context);
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(ActivitySetting.this, ActivityImagePicker.class);
        intent.putExtra(ActivityImagePicker.INTENT_IMAGE_PICKER_OPTION, ActivityImagePicker.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ActivityImagePicker.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ActivityImagePicker.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ActivityImagePicker.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ActivityImagePicker.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ActivityImagePicker.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ActivityImagePicker.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(ActivitySetting.this, ActivityImagePicker.class);
        intent.putExtra(ActivityImagePicker.INTENT_IMAGE_PICKER_OPTION, ActivityImagePicker.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ActivityImagePicker.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ActivityImagePicker.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ActivityImagePicker.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", requestCode + "");
        Log.e("resultCode", resultCode + "");
        Log.e("data", data + "");

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                filePath = uri.getPath();
            }
//        if (requestCode == REQUEST_OPEN_GALLERY && resultCode == RESULT_OK && data != null) {
////            Uri uri = data.getData();
////            String[] info = {MediaStore.Images.Media.DATA};
////            Cursor cursor = getContentResolver().query(uri, info, null, null, null);
////            cursor.moveToFirst();
////            int columnIndex = cursor.getColumnIndex(info[0]);
////            final String filePath = cursor.getString(columnIndex);
            String encodedImage = "";
////        if (requestCode == REQUEST_OPEN_GALLERY && resultCode == RESULT_OK && data != null) {
//            Uri uri = data.getData();
//            filePath = uri.toString();
//            Log.e("filePath", filePath + "");
            try {


                encodedImage = ImageBase64
                        .with(getApplicationContext())
                        .requestSize(1024, 1024) // default size
                        .encodeFile(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject postDataTest = new JSONObject();
            try {
                postDataTest.put("username", username);
                postDataTest.put("pic", encodedImage);
                postDataTest.put("vu", vu);
                postDataTest.put("content", content);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            BasicActivity.socket.emit("picture", postDataTest);
            // loading profile image from local cache
//                    loadProfile(uri.toString());


        }
    }

    public Emitter.Listener handleruploadpic = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    //                        String userName = jsonObject.getString("read");
                    Log.e("ejra", "ok" + "");

                    if (vu.equals("vl")) {
                        Call<Vakil> callvakil = RInode.getvakil(content, username, username);
                        callvakil.enqueue(new Callback<Vakil>() {
                            @Override
                            public void onResponse(Call<Vakil> call, Response<Vakil> response) {

//                                preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
                                SharedPreferences.Editor editor = BasicActivity.preferences.edit();
                                editor.putString("imgprofile", G.nodeurl + response.body().getProfile());
                                editor.commit();
                                String imgurl = BasicActivity.preferences.getString("imgprofile", "not");
                                Picasso.with(G.context).load(imgurl).into(imgProfile);

                            }

                            @Override
                            public void onFailure(Call<Vakil> call, Throwable t) {

                            }
                        });
                    } else {
                        Call<User> calluser = RInode.getuser(content, username, username);
                        calluser.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {

//                                preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
                                SharedPreferences.Editor editor = BasicActivity.preferences.edit();
                                editor.putString("imgprofile", G.nodeurl + response.body().getProfile());
                                editor.commit();
                                String imgurl = BasicActivity.preferences.getString("imgprofile", "not");
                                Picasso.with(G.context).load(imgurl).into(imgProfile);
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
                    }


                }
            });
        }
    };

    private void uploadFile(String filePath) {
        File file = new File(filePath);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            URL urlup = new URL(G.phpurl + "/upload.php?id=" + username + "&du=" + vu);
            //URL url=new URL("http://172.20.10.4/sms/upload.php");
            HttpURLConnection connection = (HttpURLConnection) urlup.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            connection.setRequestProperty("uploaded_file", filePath);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd + "Content-Disposition: form-data;" +
                    " name=\"uploaded_file\";filename=\"" + filePath + "\"\r\n\r\n");


            // dataOutputStream.writeBytes("--*****\r\nContent-Disposition: form-data;" +
            //        "name=\"uploaded_file\";filename=\""+filePath+"\"\r\n\r\n");


            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {

                dataOutputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }

            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // int serverResponseCode=connection.getResponseCode();
//             Log.e("LOG",serverResponseCode+"");

            if (connection.getResponseCode() == 200) {
                uploadHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        dialog.dismiss();
                        Toast.makeText(ActivitySetting.this, "Upload completed", Toast.LENGTH_SHORT).show();
                        imageProfile = "";
//                        new AsyncTaskGetProfileUrl(G.phpurl + "/getimageprofile.php", id, du).execute();
                        //Toast.makeText(G.context,imageProfile,Toast.LENGTH_SHORT).show();

//                        final Timer timer = new Timer();
//                        timer.scheduleAtFixedRate(new TimerTask() {
//                            @Override
//                            public void run() {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (!ActivitySetting.imageProfile.equals("")) {
//                                            //Toast.makeText(G.context,imageProfile,Toast.LENGTH_SHORT).show();
//
////                                            Glide.with(G.context).load(imgurl).placeholder(R.drawable.ic_profle).error(R.drawable.ic_profle).into(imgProfile);
//
//                                            timer.cancel();
//                                        }
//                                    }
//                                });
//                            }
//                        }, 1, 1000);


                    }
                });
            }

            fileInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> arraytime(int h1, int h2, int m1, int m2) {
        ArrayList<String> arrtime = new ArrayList<>();


        int h = 0, m = 0;
        // console.log("m1:" + m1);
        // console.log("h1:" + h1);
        //
        // console.log("h2:" + h2);
        // console.log("m2:" + m2);

        if (m1 == 0) {
            arrtime.add(h1 + ":" + m1);
            Log.e("h1:", "" + h1);
            Log.e("m1:", "" + m1);
            //arr.add(k:m1);
        }
        if (m2 >= 30) {
            // console.log("m1:"+m1);
            for (int i = h1; i < h2; i++) {
                h = i;
                for (int j = 0; j <= 1; j++) {
                    if (j == 0) {
                        if (m1 <= 30) {
                            m = 30;
                            m1 = 30;
                            arrtime.add(h + ":" + m);
                            //arr.add(h:m);
                            Log.e("h2:", "" + h);
                            Log.e("m2:", "" + m);
                        } else {
                            m = 0;
                            m1 = 0;

                        }

                    } else {
                        if (m1 == 30) {
                            m = 0;
                            m1 = 0;
                            h++;
                            arrtime.add(h + ":" + m);
                            Log.e("h3:", "" + h);
                            Log.e("m3:", "" + m);
                            //arr.add(h:m);
                        } else {
                            m = 0;
                            h++;
                            arrtime.add(h + ":" + m);
                            //arr.add(h:m);
                            Log.e("h4:", "" + h);
                            Log.e("m4:", "" + m);
                        }


                    }

                }

            }

        } else {
            h2--;
            for (int i = h1; i < h2; i++) {
                h = i;
                for (int j = 0; j <= 1; j++) {
                    if (j == 0) {
                        if (m1 <= 30) {
                            m = 30;
                            m1 = 30;
                            arrtime.add(h + ":" + m);
                            Log.e("h5:", "" + h);
                            Log.e("m5:", "" + m);
                            //arr.add(h:m);
                        } else {
                            m = 0;
                            m1 = 0;

                        }

                    } else {
                        if (m1 == 30) {
                            m = 0;
                            m1 = 0;
                            h++;
                            arrtime.add(h + ":" + m);
                            Log.e("h6+:", "" + h);
                            Log.e("m6:", "" + m);
                            //arr.add(h:m);
                        } else {
                            m = 0;
                            h++;
                            arrtime.add(h + ":" + m);
                            Log.e("m7:", "" + m);
                            Log.e("h7:", "" + h);
                            //arr.add(h:m);
                        }


                    }

                }

            }
            arrtime.add(h + ":" + "30");
            Log.e("h8:", "" + h);
            Log.e("m8:", "" + "30");
            //arr.add(h:30);
        }


        return arrtime;
    }

}

//        edtdatevisit.setInputType(InputType.TYPE_NULL);
//
//
//        edtdatevisit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                boolean[] checkedItems = new boolean[]{false, false, false, false, false, false, false};
//
//                week.clear();
//
//                String[] listItems = {"شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه", "جمعه"};
//                for (int i = 0; i < listItems.length; i++) {
//                    Week w1 = new Week();
//                    w1.setWeekname(listItems[i]);
//                    w1.setChecked(false);
//                    week.add(w1);
//
//                }
//                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySetting.this);
//                builder.setTitle("روزهای فعال بودن در اپلیکیشن");
//
//                //this will checked the items when user open the dialog
//                builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
////                        Toast.makeText(ActivitySetting.this, "Position: " + which + " Value: " + listItems[which] + " State: " + (isChecked ? "checked" : "unchecked"), Toast.LENGTH_LONG).show();
//
//
//                        Week w = new Week();
//                        w.setWeekname(listItems[which]);
//                        w.setChecked(isChecked);
//                        week.set(which, w);
////                        Log.e("cheked", isChecked + "");
//                    }
//                });
//
//                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        StringBuilder result = new StringBuilder();
//
//                        for (Week item : week) {
////                            Log.e("week", item.getWeekname() + item.isChecked);
//                            if (item.isChecked) {
////                                selectedItemsList.add(item.getWeek());
////                                Log.e("weekcheked", item.getWeek() + "");
//
//                                result.append(item.getWeekname());
//                                result.append("  ");
////                                days += item.getWeek() + " ";
//                            }
//                        }
//
////                        Log.e("week", week + "");
//
//
//                        edtdatevisit.setText(result);
//                        days = "";
//                    }
//                });
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });


//                    for(int i=0 ; i<mAdapter.mCheckStates.size();i++) {
////                        Log.e("chek",week.get(i)+"");
//                        StringBuilder result = new StringBuilder();
//                        Week day[] = new Week[0];
//                        if(mAdapter.mCheckStates.get(i)==true)
//                        {
//
//                            result.append(day[i].getWeek());
//                            result.append(",");
//                        }
////                        if(day.isChecked) {
//////                            selectedItemsList.add(item);
////                            days += day.getWeek() + ",";
////                        }
//
//                    }
//        edtdatevisit.setText(days + "");
//
//
//        edtvisitstart.setInputType(InputType.TYPE_NULL);
//        edtvisitstart.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(ActivitySetting.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        edtvisitstart.setText(selectedHour + ":" + selectedMinute);
//                    }
//                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//
//            }
//        });
//
//        edtvisitend.setInputType(InputType.TYPE_NULL);
//        edtvisitend.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(ActivitySetting.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        edtvisitend.setText(selectedHour + ":" + selectedMinute);
//                    }
//                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//
//            }
//        });


//                        thread = new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//
//
//                                String start = edtvisitstart.getText().toString();
//                                String end = edtvisitend.getText().toString();
//
//                                String[] arrday = edtdatevisit.getText().toString().split("  ");
//                                selectedItemsList = new ArrayList<String>(Arrays.asList(arrday));
//
//                                String[] partsstart = start.split(":");
//                                int h1 = Integer.parseInt(partsstart[0]);
//                                int m1 = Integer.parseInt(partsstart[1]);
//                                String[] partsend = end.split(":");
//                                int h2 = Integer.parseInt(partsend[0]);
//                                int m2 = Integer.parseInt(partsend[1]);
//
//                                arrtime = arraytime(h1, h2, m1, m2);
//
//                            }
//
//                        });
//
//                        thread.start();

//                        thread2 = new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                //                                    if (arrtime.isEmpty()) {
////                                        Thread.sleep(5000);
////                                    }
////                                    Thread.sleep(5000);
////                                    Log.e("vakil", username + "/n" + edtname.getText().toString() + "/n" + ostan + "/n" + shahr + "/n" + arrtime + "/n" + selectedItemsList + "/n");
//
//
//                            }
//
//                        });
//
//                        thread2.start();

//