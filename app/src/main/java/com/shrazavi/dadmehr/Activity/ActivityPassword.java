package com.shrazavi.dadmehr.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import javax.crypto.spec.SecretKeySpec;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPassword extends AppCompatActivity {
    String content;
    String enk;
    SecretKeySpec Key;
//    public SharedPreferences preferences;
    String username = "";
    String vu = "";
    public static MaterialProgressBar progresslogin;
    TextInputLayout layold, laynew, layrepeat;
    public static TextInputEditText edtold, edtnew, edtnewrepeat;
    public static Boolean oldpass = false;
    Button btnsetpassword, btnback;
    TextView detailpass;
    Retrofitinformation RInode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        RInode = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        username = BasicActivity.userid;
        vu = BasicActivity.vu;
//        enk = BasicActivity.preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + username + "-" + getResources().getString(R.string.developer), enk);
        content=BasicActivity.content;

        layold = (TextInputLayout) findViewById(R.id.lay_edt_pass_old);
        laynew = (TextInputLayout) findViewById(R.id.lay_edt_pass_new);
        layrepeat = (TextInputLayout) findViewById(R.id.lay_edt_pass_new_repeat);
        edtold = (TextInputEditText) findViewById(R.id.edt_pass_old);
        edtnew = (TextInputEditText) findViewById(R.id.edt_pass_new);
        edtnewrepeat = (TextInputEditText) findViewById(R.id.edt_pass_new_repeat);
        btnsetpassword = (Button) findViewById(R.id.btn_pass_set);
        btnback = (Button) findViewById(R.id.btn_pass_back);
        detailpass = (TextView) findViewById(R.id.txt_pass);


        layold.setBoxBackgroundColor(getResources().getColor(R.color.defaultpass));
        layold.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.passhint)));
        layold.setBoxStrokeColor(getResources().getColor(R.color.passhint));
        laynew.setBoxBackgroundColor(getResources().getColor(R.color.defaultpass));
        laynew.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.passhint)));
        laynew.setBoxStrokeColor(getResources().getColor(R.color.passhint));
        layrepeat.setBoxBackgroundColor(getResources().getColor(R.color.defaultpass));
        layrepeat.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.passhint)));
        layrepeat.setBoxStrokeColor(getResources().getColor(R.color.passhint));


        btnsetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtnew.getText().toString().equals(edtnewrepeat.getText().toString())) {

                    Call<MessageSignup> callvakil = RInode.changepassword(content,
                            username,
                            edtold.getText().toString(),
                            edtnew.getText().toString(),
                            vu,
                            username);

                    callvakil.enqueue(new Callback<MessageSignup>() {
                        @Override
                        public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                            Boolean status = response.body().getStatus();
                            if (status) {
//                            Log.e("mess", response.body().getMessage());
                                if (response.body().getMessage().equals("invalid password")) {
                                    Toast.makeText(G.context, "گذر واژه قبلی وارد شده صحیح نمی باشد.", Toast.LENGTH_SHORT).show();
                                    layold.setBoxBackgroundColor(getResources().getColor(R.color.errorpass));
                                    layold.setBoxStrokeColor(getResources().getColor(R.color.red));
                                    layold.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));

                                } else if (response.body().getMessage().equals("upgrade password ok")) {
                                    Intent intent = new Intent(ActivityPassword.this, ActivityOption.class);
                                    ActivityPassword.this.startActivity(intent);
                                    ActivityPassword.this.finish();
                                    layold.setBoxBackgroundColor(getResources().getColor(R.color.defaultpass));
                                    layold.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.passhint)));
                                    layold.setBoxStrokeColor(getResources().getColor(R.color.passhint));
                                } else {
                                    Toast.makeText(G.context, "خطایی غیر منتظره رخ داد لطفا مجددا تلاش کنید", Toast.LENGTH_SHORT).show();

                                }


                            } else {
                                Toast.makeText(G.context, "خطایی غیر منتظره رخ داد لطفا مجددا تلاش کنید", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageSignup> call, Throwable t) {
                            Log.e("error???", t + "");
                        }
                    });
                } else {
                    Toast.makeText(G.context, "گذر واژه جدید همخوانی ندارد.", Toast.LENGTH_SHORT).show();
                    layold.setBoxBackgroundColor(getResources().getColor(R.color.defaultpass));
                    layold.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.passhint)));
                    layold.setBoxStrokeColor(getResources().getColor(R.color.passhint));
                    laynew.setBoxBackgroundColor(getResources().getColor(R.color.errorpass));
                    laynew.setBoxStrokeColor(getResources().getColor(R.color.red));
                    laynew.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    laynew.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    layrepeat.setBoxBackgroundColor(getResources().getColor(R.color.errorpass));
                    layrepeat.setBoxStrokeColor(getResources().getColor(R.color.red));
                    layrepeat.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                }


            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPassword.this, ActivityOption.class);
                ActivityPassword.this.startActivity(intent);
                ActivityPassword.this.finish();
            }
        });

    }


}
