package com.shrazavi.dadmehr.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageBase64;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddPost extends BasicActivity {
    Button btnadd;
    ImageView imgpost;
    EditText edtpost;
    String content;
    String enk;
    String vu;
    SecretKeySpec secretKey;
    public static final int REQUEST_IMAGE = 100;
    public String filePath = "";
    public String imgposturl = "";
    public String username = "";
//    private Socket socket;
    public Handler handler;
//    public SharedPreferences preferences;
    Retrofitinformation RI;


//    {
//        try {
//            socket = IO.socket(G.nodeurl);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        btnadd = (Button) findViewById(R.id.btn_add_post);
        imgpost = (ImageView) findViewById(R.id.img_add_post);
        edtpost = (EditText) findViewById(R.id.edt_add_post);

//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        username = BasicActivity.userid;

        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);

//        socket.connect();
        handler = new Handler();
//        socket.emit("nickname", username);
//        socket.on("picpost", handleruploadpic);
        vu = BasicActivity.vu;
//        enk = preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        secretKey = new SecretKeySpec(data, 0, data.length, "AES");

//        content = Base64.encodeToString(SymmetricAlgorithmAES.encryption(secretKey, vu+"-"+username+"-"+R.string.developer), Base64.NO_WRAP);
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + username + "-" +getResources().getString(R.string.developer), enk);
        content=BasicActivity.content;


        imgpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileImageClick();
            }
        });


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgposturl.isEmpty() || edtpost.getText().toString().isEmpty()) {
                    Toast.makeText(ActivityAddPost.this, "لطفا اطلاعات را تکمیل کنید", Toast.LENGTH_SHORT).show();

                } else {
                    Call<MessageSignup> calladdpost = RI.addpost(content, username, imgposturl, edtpost.getText().toString());
                    calladdpost.enqueue(new Callback<MessageSignup>() {
                        @Override
                        public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                            Intent intent = new Intent(ActivityAddPost.this, ActivityMain.class);
                            ActivityAddPost.this.startActivity(intent);

                        }

                        @Override
                        public void onFailure(Call<MessageSignup> call, Throwable t) {

                        }
                    });
                }
            }
        });


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
        Intent intent = new Intent(ActivityAddPost.this, ActivityImagePicker.class);
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
        Intent intent = new Intent(ActivityAddPost.this, ActivityImagePicker.class);
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

            JSONObject postDatapicpost = new JSONObject();
            try {
                postDatapicpost.put("username", username);
                postDatapicpost.put("pic", encodedImage);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            socket.emit("picturepost", postDatapicpost);
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
                    try {

                        imgposturl = jsonObject.getString("pic");
                        Picasso.with(G.context).load(G.nodeurl + imgposturl).into(imgpost);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    };


}
