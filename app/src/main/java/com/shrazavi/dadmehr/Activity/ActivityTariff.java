package com.shrazavi.dadmehr.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactory;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityTariff extends AppCompatActivity {

    Button btnok, btnback;
    Switch swchat, swvoise, swvideo;
    TextView txtchat, txtvoise, txtvideo;
    TextInputEditText edtchat, edtvoise, edtvideo;
    TextInputLayout laychat, layvoise, layvideo;
    int chatcheck = 0, voisecheck = 0, videocheck = 0;
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
        setContentView(R.layout.activity_tariff);

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


        btnok = (Button) findViewById(R.id.btn_tariff_ok);
        btnback = (Button) findViewById(R.id.btn_tariff_back);
        txtchat = (TextView) findViewById(R.id.txt_tariff_chat);
        txtvoise = (TextView) findViewById(R.id.txt_tariff_voise);
        txtvideo = (TextView) findViewById(R.id.txt_tariff_video);
        laychat = (TextInputLayout) findViewById(R.id.lay_tariff_chat);
        layvoise = (TextInputLayout) findViewById(R.id.lay_tariff_voise);
        layvideo = (TextInputLayout) findViewById(R.id.lay_tariff_video);
        edtchat = (TextInputEditText) findViewById(R.id.edt_tariff_chat);
        edtvoise = (TextInputEditText) findViewById(R.id.edt_tariff_voise);
        edtvideo = (TextInputEditText) findViewById(R.id.edt_tariff_video);
        swchat = (Switch) findViewById(R.id.sw_chat);
        swvoise = (Switch) findViewById(R.id.sw_voise);
        swvideo = (Switch) findViewById(R.id.sw_video);


        Call<Vakil> callvakil = RInode.getvakil(content, username, username);
        callvakil.enqueue(new Callback<Vakil>() {
            @Override
            public void onResponse(Call<Vakil> call, Response<Vakil> response) {
                int chattariff = 0, voisetariff = 0, videotariff = 0;
                chattariff = response.body().getChattariff();
                voisetariff = response.body().getVoisetariff();
                videotariff = response.body().getVideotariff();
                if (chattariff == 0) {
                    edtchat.setVisibility(View.GONE);
                    laychat.setVisibility(View.GONE);
                    swchat.setChecked(false);
                    txtchat.setText("تماس متنی غیر فعال است");
                } else {
                    edtchat.setVisibility(View.VISIBLE);
                    laychat.setVisibility(View.VISIBLE);
                    swchat.setChecked(true);
                    edtchat.setText(chattariff + "");
                }
                if (voisetariff == 0) {
                    edtvoise.setVisibility(View.GONE);
                    layvoise.setVisibility(View.GONE);
                    swvoise.setChecked(false);
                    txtvoise.setText("تماس صوتی غیر فعال است");
                } else {
                    edtvoise.setVisibility(View.VISIBLE);
                    layvoise.setVisibility(View.VISIBLE);
                    swvoise.setChecked(true);
                    edtvoise.setText(voisetariff + "");

                }
                if (videotariff == 0) {
                    edtvideo.setVisibility(View.GONE);
                    layvideo.setVisibility(View.GONE);
                    swvideo.setChecked(false);
                    txtvideo.setText("تماس تصویری غیر فعال است");
                } else {
                    edtvideo.setVisibility(View.VISIBLE);
                    layvideo.setVisibility(View.VISIBLE);
                    swvideo.setChecked(true);
                    edtvideo.setText(videotariff + "");
                }


            }

            @Override
            public void onFailure(Call<Vakil> call, Throwable t) {
                Log.e("error???", t + "");
            }
        });


        swchat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    txtchat.setText("");
                    edtchat.setVisibility(View.VISIBLE);
                    laychat.setVisibility(View.VISIBLE);
                    chatcheck = 1;
                } else {
                    chatcheck = 0;
                    edtchat.setVisibility(View.GONE);
                    laychat.setVisibility(View.GONE);
                    txtchat.setText("تماس متنی غیر فعال است");
                }
            }
        });
        swvoise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    voisecheck = 1;
                    txtvoise.setText("");
                    edtvoise.setVisibility(View.VISIBLE);
                    layvoise.setVisibility(View.VISIBLE);
                } else {
                    voisecheck = 0;
                    txtvoise.setText("تماس صوتی غیر فعال است");
                    edtvoise.setVisibility(View.GONE);
                    layvoise.setVisibility(View.GONE);
                }
            }
        });
        swvideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    videocheck = 1;
                    txtvideo.setText("");
                    edtvideo.setVisibility(View.VISIBLE);
                    layvideo.setVisibility(View.VISIBLE);
                } else {
                    videocheck = 0;
                    txtvideo.setText("تماس تصویری غیر فعال است");
                    edtvideo.setVisibility(View.GONE);
                    layvideo.setVisibility(View.GONE);
                }
            }
        });

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatcheck == 0 && voisecheck == 0 && videocheck == 0) {
                    Toast.makeText(ActivityTariff.this, "باید حداقل یک گزینه تعیین شود", Toast.LENGTH_SHORT).show();
                } else {
                    if (edtchat.getText().toString().isEmpty() && edtvoise.getText().toString().isEmpty() && edtvideo.getText().toString().isEmpty()) {
                        Toast.makeText(ActivityTariff.this, "باید حداقل یک گزینه تعیین شود", Toast.LENGTH_SHORT).show();
                    } else {

                        int chat = 0;
                        int voise = 0;
                        int video = 0;

                        if (chatcheck == 0 || edtchat.getText().toString().isEmpty()) {
                            chat = 0;
                        } else {
                            chat = Integer.parseInt(edtchat.getText().toString());

                        }

                        if (voisecheck == 0 || edtvoise.getText().toString().isEmpty()) {
                            voise = 0;
                        } else {
                            voise = Integer.parseInt(edtvoise.getText().toString());
                        }

                        if (videocheck == 0 || edtvideo.getText().toString().isEmpty()) {
                            video = 0;
                        } else {
                            video = Integer.parseInt(edtvideo.getText().toString());
                        }
                        if (chat != 0 && chat < 50000 || chat != 0 && chat > 500000) {
                            Toast.makeText(ActivityTariff.this, "مبلغ تعرفه باید بین پنجاه هزار تومان و پانصد هزار تومان باشد", Toast.LENGTH_LONG).show();
                        } else {
                            if (voise != 0 && voise < 50000 || voise != 0 && voise > 500000) {
                                Toast.makeText(ActivityTariff.this, "مبلغ تعرفه باید بین پنجاه هزار تومان و پانصد هزار تومان باشد", Toast.LENGTH_LONG).show();
                            } else {
                                if (video != 0 && video < 50000 || video != 0 && video > 500000) {
                                    Toast.makeText(ActivityTariff.this, "مبلغ تعرفه باید بین پنجاه هزار تومان و پانصد هزار تومان باشد", Toast.LENGTH_LONG).show();
                                } else {
                            Call<MessageSignup> callvakil = RInode.upgradetariff(content,
                                    username,
                                    chat,
                                    voise,
                                    video,
                                    username);

                            callvakil.enqueue(new Callback<MessageSignup>() {
                                @Override
                                public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                    Boolean status = response.body().getStatus();
                                    if (status) {

                                        Intent intent = new Intent(ActivityTariff.this, ActivityOption.class);
                                        ActivityTariff.this.startActivity(intent);
                                        ActivityTariff.this.finish();

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
                    }

                }
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityTariff.this, ActivityOption.class);
                ActivityTariff.this.startActivity(intent);
                ActivityTariff.this.finish();
            }
        });
    }

}
