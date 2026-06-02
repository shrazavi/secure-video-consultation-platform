package com.shrazavi.dadmehr.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.shrazavi.dadmehr.Adapter.spinnerAdapter;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.DataClass.Ostan;
import com.shrazavi.dadmehr.DataClass.Shahrestan;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactory;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import java.util.ArrayList;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLocation extends AppCompatActivity {
    private static final int CHOOSE_FILE_REQUEST_CODE = 8777;
    private static final int PICKFILE_RESULT_CODE = 8778;
    Button btnok, btnback;
    TextInputEditText txtaddress;
    AutoCompleteTextView spinostan, spinshahr;
    String ostan = "";
    String shahr = "";
    int id_ostan = 0;
    int id_shahrestan = 0;
    Retrofitinformation RI;
    Retrofitinformation RInode;
    String content;
    String enk;
    SecretKeySpec Key;
//    public SharedPreferences preferences;
    String username = "";
    String vu = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        RI = RetrofitFactory.getclient().create(Retrofitinformation.class);
        RInode = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        username = BasicActivity.userid;
        vu = BasicActivity.vu;
//        enk = BasicActivity.preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + username + "-" + getResources().getString(R.string.developer), enk);
        content=BasicActivity.content;

        spinostan = (AutoCompleteTextView) findViewById(R.id.spn_location_ostan);
        spinshahr = (AutoCompleteTextView) findViewById(R.id.spn_location_shahr);
        btnok = (Button) findViewById(R.id.btn_location_ok);
        btnback = (Button) findViewById(R.id.btn_location_back);
        txtaddress = (TextInputEditText) findViewById(R.id.edt_location_address);

//        txtdetail = (TextView) findViewById(R.id.txt_rs_detail);
//        btnchoose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                chooseFile();
//            }
//        });
        Call<Vakil> callvakil = RInode.getvakil(content, username, username);
        callvakil.enqueue(new Callback<Vakil>() {
            @Override
            public void onResponse(Call<Vakil> call, Response<Vakil> response) {
                spinostan.setText(response.body().getOstan());
                spinshahr.setText(response.body().getShahr());
                txtaddress.setText(response.body().getAddress());

            }

            @Override
            public void onFailure(Call<Vakil> call, Throwable t) {
                Log.e("error???", t + "");
            }
        });

        Call<ArrayList<Ostan>> callostan = RI.getostan();
        callostan.enqueue(new Callback<ArrayList<Ostan>>() {
            @Override
            public void onResponse(Call<ArrayList<Ostan>> call, Response<ArrayList<Ostan>> response) {
                spinnerAdapter Adapterostan = new spinnerAdapter(ActivityLocation.this, android.R.layout.simple_list_item_1);
                for (int i = 0; i < response.body().size(); i++) {
                    Adapterostan.add(response.body().get(i).getName());
                }
                Adapterostan.add("");
//                Adapterostan.add("استان");
                spinostan.setAdapter(Adapterostan);
//                spinostan.setSelection(Adapterostan.getCount());


            }

            @Override
            public void onFailure(Call<ArrayList<Ostan>> call, Throwable t) {

            }
        });
        spinostan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinostan.clearListSelection();
            }
        });
        spinshahr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinshahr.clearListSelection();
            }
        });
        spinostan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.e("getSelectionStart", spinostan.getSelectionStart() + "");
//        Log.e("getSelectionEnd", spinostan.getSelectionEnd() + "");
//        Log.e("getListSelection", spinostan.getListSelection() + "");
        Log.e("id ostan", id_ostan + "");
                spinshahr.setText("");
                id_ostan = position + 1;
//                Log.e("id ostan", id_ostan + "");
                Call<ArrayList<Shahrestan>> callshahr = RI.getshahr(id_ostan);
                callshahr.enqueue(new Callback<ArrayList<Shahrestan>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Shahrestan>> call, Response<ArrayList<Shahrestan>> response) {
                        spinnerAdapter Adaptershar = new spinnerAdapter(ActivityLocation.this, android.R.layout.simple_list_item_1);
                        for (int i = 0; i < response.body().size(); i++) {
                            Adaptershar.add(response.body().get(i).getName());
                        }
                        Adaptershar.add("");

//                Adaptershar.add("شهر");
                        spinshahr.setAdapter(Adaptershar);


                    }

                    @Override
                    public void onFailure(Call<ArrayList<Shahrestan>> call, Throwable t) {

                    }
                });
            }
        });

        spinshahr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String shahrestan = spinshahr.getText().toString();
                Log.e("shahrname", shahrestan);
                Call<Shahrestan> calluser = RI.getidshahr(shahrestan);
                calluser.enqueue(new Callback<Shahrestan>() {
                    @Override
                    public void onResponse(Call<Shahrestan> call, Response<Shahrestan> response) {

                        id_shahrestan = Integer.parseInt(response.body().getId());
                        shahr = response.body().getName();
                        Call<Ostan> callostan = RI.getnameostan(Integer.parseInt(response.body().getId_ostan()));
                        callostan.enqueue(new Callback<Ostan>() {
                            @Override
                            public void onResponse(Call<Ostan> call, Response<Ostan> response) {

                                ostan = response.body().getName();
                                Log.e("shahrid", shahr + "/" + ostan);

                            }

                            @Override
                            public void onFailure(Call<Ostan> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Shahrestan> call, Throwable t) {
                        Log.e("shahrid", t + "");
                    }
                });
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("vakil", username + "/n" + edtname.getText().toString() + "/n" + ostan + "/n" + shahr + "/n" + arrtime + "/n" + selectedItemsList + "/n");
                if (spinostan.getText().toString().isEmpty() || spinshahr.getText().toString().isEmpty() || txtaddress.toString().isEmpty()) {
                    Toast.makeText(ActivityLocation.this, "لطفا مشخصاتتان را تکمیل کنید", Toast.LENGTH_SHORT).show();
                } else {
                    ostan = spinostan.getText().toString();
                    shahr = spinshahr.getText().toString();
                    Call<MessageSignup> callvakil = RInode.upgradelocation(content,
                            username,
                            ostan,
                            shahr,
                            txtaddress.getText().toString(),
                            username);

                    callvakil.enqueue(new Callback<MessageSignup>() {
                        @Override
                        public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                            Boolean status = response.body().getStatus();
                            if (status) {
//                            Log.e("mess", response.body().getMessage());
                                Intent intent = new Intent(ActivityLocation.this, ActivityOption.class);
                                ActivityLocation.this.startActivity(intent);
                                ActivityLocation.this.finish();

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
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLocation.this, ActivityOption.class);
                ActivityLocation.this.startActivity(intent);
                ActivityLocation.this.finish();
            }
        });
    }

}


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == PICKFILE_RESULT_CODE) {
//                if (null == data)
//                    return;
//
//                File destination = new File("/data/data/com.shrazavi.darmano/databases/chat_db");
//                Uri selectedImageUri = data.getData();
//                System.out.println(selectedImageUri.toString());
//                // MEDIA GALLERY
//                String selectedImagePath = getPath(
//                        ActivityLocation.this, selectedImageUri);
//                Log.i("Image File Path", "" + selectedImagePath);
//                System.out.println("Image Path =" + selectedImagePath);
//                if (selectedImagePath != null && !selectedImagePath.equals("")) {
//
//                    try {
//                        File source = new File(selectedImagePath);
//                        copy(source, destination);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//
//                }
//
//
//            }
//        }
//    }


//    private void copy(File source, File destination) throws IOException {
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream(source);
//            String outFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Darmano/backup/chat_db";
//
//
//            Log.e("src", outFileName);
//
//            OutputStream output = new FileOutputStream(destination);
//
//
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = fis.read(buffer)) > 0) {
//                output.write(buffer, 0, length);
//            }
//            output.flush();
//            output.close();
//            fis.close();
//            txtdetail.setText("بازگردانی با موفقیت انجام شد");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void chooseFile() {
//
//        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
//        chooseFile.setType("*/*");
//        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
//        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
//
//    }
//
//    public static String getPath(final Context context, final Uri uri) {
//
//        // check here to KITKAT or new version
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//
//        // DocumentProvider
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//
//                // ExternalStorageProvider
//                if (isExternalStorageDocument(uri)) {
//                    final String docId = DocumentsContract.getDocumentId(uri);
//                    final String[] split = docId.split(":");
//                    final String type = split[0];
//
//                    if ("primary".equalsIgnoreCase(type)) {
//                        return Environment.getExternalStorageDirectory() + "/"
//                                + split[1];
//                    }
//                }
//                // DownloadsProvider
//                else if (isDownloadsDocument(uri)) {
//
//                    final String id = DocumentsContract.getDocumentId(uri);
//                    final Uri contentUri = ContentUris.withAppendedId(
//                            Uri.parse("content://downloads/public_downloads"),
//                            Long.valueOf(id));
//
//                    return getDataColumn(context, contentUri, null, null);
//                }
//                // MediaProvider
//                else if (isMediaDocument(uri)) {
//                    final String docId = DocumentsContract.getDocumentId(uri);
//                    final String[] split = docId.split(":");
//                    final String type = split[0];
//
//                    Uri contentUri = null;
//                    if ("image".equals(type)) {
//                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                    } else if ("video".equals(type)) {
//                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                    } else if ("audio".equals(type)) {
//                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                    }
//
//                    final String selection = "_id=?";
//                    final String[] selectionArgs = new String[]{split[1]};
//
//                    return getDataColumn(context, contentUri, selection,
//                            selectionArgs);
//                }
//            }
//            // MediaStore (and general)
//            else if ("content".equalsIgnoreCase(uri.getScheme())) {
//
//                // Return the remote address
//                if (isGooglePhotosUri(uri))
//                    return uri.getLastPathSegment();
//
//                return getDataColumn(context, uri, null, null);
//            }
//            // File
//            else if ("file".equalsIgnoreCase(uri.getScheme())) {
//                return uri.getPath();
//            }
//        }
//
//        return null;
//    }
//
//
//    public static String getDataColumn(Context context, Uri uri,
//                                       String selection, String[] selectionArgs) {
//
//        Cursor cursor = null;
//        final String column = "_data";
//        final String[] projection = {column};
//
//        try {
//            cursor = context.getContentResolver().query(uri, projection,
//                    selection, selectionArgs, null);
//            if (cursor != null && cursor.moveToFirst()) {
//                final int index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(index);
//            }
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return null;
//    }
//
//
//    public static boolean isExternalStorageDocument(Uri uri) {
//        return "com.android.externalstorage.documents".equals(uri
//                .getAuthority());
//    }
//
//
//    public static boolean isDownloadsDocument(Uri uri) {
//        return "com.android.providers.downloads.documents".equals(uri
//                .getAuthority());
//    }
//
//
//    public static boolean isMediaDocument(Uri uri) {
//        return "com.android.providers.media.documents".equals(uri
//                .getAuthority());
//    }
//
//
//    public static boolean isGooglePhotosUri(Uri uri) {
//        return "com.google.android.apps.photos.content".equals(uri
//                .getAuthority());
//    }

