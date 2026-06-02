package com.shrazavi.dadmehr.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.shrazavi.dadmehr.Adapter.spinnerAdapter;
import com.shrazavi.dadmehr.AsyncaTaskSendNumber;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.DataClass.Ostan;
import com.shrazavi.dadmehr.DataClass.Shahrestan;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactory;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.util.MasterKeys;
import com.shrazavi.dadmehr.core.util.SymmetricAlgorithmAES;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySignup extends AppCompatActivity {
    long mellicode;
    String[] arrbase = {"وکیل پایه یکم", "وکیل پایه دوم", "کارآموز", ""};
    String[] arrtype = {"کانون وکلا", "مرکز مشاوران", ""};
    public static String code = "98";
    AutoCompleteTextView spinostan, spinshahr, spinbase, spintype;
    TextInputEditText edtparvane, edtnumber, edtname, edtpass, edtexperience;
    //    EditText edtparvane, edtnumber, edtname, edtpass, edtexperience;
    public EditText edtuser;
    public EditText edtmelli;
    RadioGroup rg;
    Button btnSend, btnhelp;
    String name = "";
    String number = "";
    String melli = "";
    String user = "";
    String pass = "";
    String parvane = "";
    String ostan = "";
    String shahr = "";
    String experience = "";
    String content="";
    int type = 0, base = 0;
    CountryCodePicker ccp;
    public static final Pattern RTL_CHARACTERS =
            Pattern.compile("[\u0600-\u06FF\u0750-\u077F\u0590-\u05FF\uFE70-\uFEFF]");
    int id_ostan = 0;
    int id_shahrestan = 0;
    Retrofitinformation RI;
    Retrofitinformation RInode;
    LinearLayout lay_vl, lay_place, lay_specialty;
    TextInputLayout laymelli, layusername, laynumber;
    //public static SharedPreferences preferences;
    public static Context dialogContext;
    int id;
    public int radioid;
    long x, n, countermelli = 0, countermelli2 = 0, temp;
    public SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            preferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    G.context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        content = SymmetricAlgorithmAES.encrypt(preferences.getString("IM", "not"), preferences.getString("k5", "not"));
        laymelli = (TextInputLayout) findViewById(R.id.lay_number);
        laymelli = (TextInputLayout) findViewById(R.id.lay_melli);
        layusername = (TextInputLayout) findViewById(R.id.lay_username);
        edtparvane = (TextInputEditText) findViewById(R.id.edt_parvane);
        edtmelli = (TextInputEditText) findViewById(R.id.edt_melli);
        edtnumber = (TextInputEditText) findViewById(R.id.edt_number);
        edtname = (TextInputEditText) findViewById(R.id.edt_name);
        edtuser = (TextInputEditText) findViewById(R.id.edt_user);
        edtpass = (TextInputEditText) findViewById(R.id.edt_pass);
        edtexperience = (TextInputEditText) findViewById(R.id.edt_experience);
        lay_vl = (LinearLayout) findViewById(R.id.lay_vl);
        lay_place = (LinearLayout) findViewById(R.id.lay_place);
        lay_specialty = (LinearLayout) findViewById(R.id.lay_specialty);
        spinostan = (AutoCompleteTextView) findViewById(R.id.spn_ostan);
        spinshahr = (AutoCompleteTextView) findViewById(R.id.spn_shahr);
        spinbase = (AutoCompleteTextView) findViewById(R.id.spn_base);
        spintype = (AutoCompleteTextView) findViewById(R.id.spn_organ);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnhelp = (Button) findViewById(R.id.btn_help);

        rg = (RadioGroup) findViewById(R.id.radiogroupsearch);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        laymelli.setBoxBackgroundColor(getResources().getColor(R.color.defaultpass));
        laymelli.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.passhint)));
        laymelli.setBoxStrokeColor(getResources().getColor(R.color.passhint));
        layusername.setBoxBackgroundColor(getResources().getColor(R.color.defaultpass));
        layusername.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.passhint)));
        layusername.setBoxStrokeColor(getResources().getColor(R.color.passhint));

        btnSend.setTypeface(G.face);
        spinnerAdapter Adapterbase = new spinnerAdapter(ActivitySignup.this, android.R.layout.simple_list_item_1);
        spinnerAdapter Adaptertype = new spinnerAdapter(ActivitySignup.this, android.R.layout.simple_list_item_1);
        Adapterbase.addAll(arrbase);
        Adaptertype.addAll(arrtype);
        spinbase.setAdapter(Adapterbase);
        spintype.setAdapter(Adaptertype);


        dialogContext = ActivitySignup.this;


        RI = RetrofitFactory.getclient().create(Retrofitinformation.class);
        RInode = RetrofitFactorynode.getclient().create(Retrofitinformation.class);

        Call<ArrayList<Ostan>> callostan = RI.getostan();
        callostan.enqueue(new Callback<ArrayList<Ostan>>() {
            @Override
            public void onResponse(Call<ArrayList<Ostan>> call, Response<ArrayList<Ostan>> response) {
                spinnerAdapter Adapterostan = new spinnerAdapter(ActivitySignup.this, android.R.layout.simple_list_item_1);
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

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
//                Toast.makeText(ActivitySignup.this, "Updated " + selectedCountry.getName(), Toast.LENGTH_SHORT).show();
                code = selectedCountry.getPhoneCode();

            }
        });
        ccp.setDefaultCountryUsingNameCode("ir");
        ccp.setDefaultCountryUsingPhoneCodeAndApply(98);
        ccp.setDefaultCountryUsingPhoneCode(98);
        ccp.setDefaultCountryUsingNameCodeAndApply("ir");
        spintype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    type = 1;
                } else {
                    type = 2;
                }

            }
        });
        spintype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    base = 1;
                } else if (position == 1) {
                    base = 2;
                } else {
                    base = 3;
                }

            }
        });
        spinostan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.e("getSelectionStart", spinostan.getSelectionStart() + "");
//        Log.e("getSelectionEnd", spinostan.getSelectionEnd() + "");
//        Log.e("getListSelection", spinostan.getListSelection() + "");
//        Log.e("position", position + "");
                spinshahr.setText("");
                id_ostan = position + 1;
                Call<ArrayList<Shahrestan>> callshahr = RI.getshahr(id_ostan);
                callshahr.enqueue(new Callback<ArrayList<Shahrestan>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Shahrestan>> call, Response<ArrayList<Shahrestan>> response) {
                        spinnerAdapter Adaptershar = new spinnerAdapter(ActivitySignup.this, android.R.layout.simple_list_item_1);
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
        //
        id = rg.getCheckedRadioButtonId();

        if (id == -1) {
            //Toast.makeText(getApplicationContext(), "لطفا نوع جستجو را انتخاب کنید", Toast.LENGTH_LONG).show();
            rg.check(R.id.radiomn);

//            lay_aw.setVisibility(View.VISIBLE);
            lay_place.setVisibility(View.GONE);
            lay_specialty.setVisibility(View.GONE);
            lay_vl.setVisibility(View.GONE);

        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioid = checkedId;
                switch (checkedId) {

                    case R.id.radiovl:


                        lay_place.setVisibility(View.VISIBLE);
                        lay_specialty.setVisibility(View.VISIBLE);
                        lay_vl.setVisibility(View.VISIBLE);
                        //   Log.e("iddr",rg.getCheckedRadioButtonId()+"");
                        break;

                    case R.id.radiomn:
                        lay_specialty.setVisibility(View.GONE);
                        lay_place.setVisibility(View.GONE);
                        lay_vl.setVisibility(View.GONE);
                        // Log.e("idmn",rg.getCheckedRadioButtonId()+"");
                        break;
                }
            }
        });
        btnhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ActivitySignup.this, ActivityHelp.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
//                ActivitySignup.this.finish();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                laymelli.setBoxBackgroundColor(getResources().getColor(R.color.defaultpass));
                laymelli.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.passhint)));
                laymelli.setBoxStrokeColor(getResources().getColor(R.color.passhint));
                layusername.setBoxBackgroundColor(getResources().getColor(R.color.defaultpass));
                layusername.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.passhint)));
                layusername.setBoxStrokeColor(getResources().getColor(R.color.passhint));

//                Log.e("conect", isConnected() + "");
                int rd = rg.getCheckedRadioButtonId();
                Log.e("rd", radioid + "");

                if (radioid == R.id.radiovl) {
                    Log.e("vl", "ok");
                    Vlsignup();
                } else {
                    Log.e("user", "ok");
                    Usersignup();

                }


//                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    public void onCheckedChanged(RadioGroup group, int checkedId) {
//                        int rd=rg.getCheckedRadioButtonId();
//
//
//                    }
//                });
//                name = edtname.getText().toString();
//                number = edtnumber.getText().toString();
//                melli = edtmelli.getText().toString();
//                user = edtuser.getText().toString();
//                pass = edtpass.getText().toString();
//                nezam = edtnezam.getText().toString();
//                //Log.e("user",edtuser.getText()+"");
//                //  Toast.makeText(ActivitySignup.this, user, Toast.LENGTH_SHORT).show();
//
//                if (user.isEmpty() || name.isEmpty() || number.isEmpty() || melli.isEmpty() || pass.isEmpty()) {
//                    Toast.makeText(ActivitySignup.this, "فیلد های مورد نظر را پر کنید", Toast.LENGTH_SHORT).show();
//                } else {
//                    new AsyncTaskSendId(G.phpurl + "/insertid.php", user, "empty", name, number, melli, pass).execute();
//                    // new AsyncaTaskSendNumber(G.phpurl + "/index.php", edtnumber.getText().toString()).execute();
//
//                    preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("id", user);
//                    //editor.putString("pass", pass);
//                    editor.commit();
////                Intent intent=new Intent(G.context,ActivityChatList.class);
////                startActivity(intent);
//                    //   Log.e("edtnum",edtnumber.getText().toString());
//
//                    Intent intent = new Intent(G.context, ActivityValidation.class);
//                    intent.putExtra("edtNumber", number);
//                    startActivity(intent);
////                }else{
////                    Log.e("ednum",edtNumber.getText().toString());
////                    new AsyncaTaskSendCode("http://"+getResources().getString(R.string.ip)+"/sms/checkcode.php",edtNumber.getText().toString(),
////                            edtCode.getText().toString()).execute();
////
////                }
//
//
//                }
            }
        });
    }

    private void Vlsignup() {
        if (isConnected()) {

            experience = edtexperience.getText().toString();
            name = edtname.getText().toString();
            number = edtnumber.getText().toString();
            melli = edtmelli.getText().toString();
            user = edtuser.getText().toString();
            pass = edtpass.getText().toString();
            parvane = edtparvane.getText().toString();

            if (user.isEmpty() || name.isEmpty() || number.isEmpty() || melli.isEmpty() || pass.isEmpty() || parvane.isEmpty() || experience.isEmpty() || ostan.isEmpty() || shahr.isEmpty() || type == 0 || base == 0) {
                Toast.makeText(ActivitySignup.this, "لطفا مشخصاتتان را تکمیل کنید", Toast.LENGTH_SHORT).show();
            } else {
                Matcher matcher = RTL_CHARACTERS.matcher(user);
                if (matcher.find()) {
                    Toast.makeText(ActivitySignup.this, "نام کاربری باید به انگلیسی باشد", Toast.LENGTH_SHORT).show();
//                    edtuser.setBackgroundResource(R.drawable.edt_error);
                    layusername.setBoxBackgroundColor(getResources().getColor(R.color.errorpass));
                    layusername.setBoxStrokeColor(getResources().getColor(R.color.red));
                    layusername.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    edtuser.setFocusableInTouchMode(true);
                    edtuser.requestFocus();
//            return true;  // it's RTL
                } else {

                    number = "+" + code + edtnumber.getText().toString();

                    Call<MessageSignup> callvl = RInode.cheackuser(content, melli, "vl", user);
                    callvl.enqueue(new Callback<MessageSignup>() {
                        @Override
                        public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                            Log.e("signupvl", response.body().getMessage());
                            Boolean status = response.body().getStatus();
                            if (status) {
                                Call<MessageSignup> calldata = RI.senddata(content,
                                        SymmetricAlgorithmAES.encrypt(user, preferences.getString("k5", "not")),
                                                preferences.getString("k1", "not"),
                                                preferences.getString("k2", "not"),
                                                preferences.getString("k3", "not"),
                                                preferences.getString("k4", "not"),
                                                preferences.getString("k5", "not"));

                                calldata.enqueue(new Callback<MessageSignup>() {
                                    @Override
                                    public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {


                                    }

                                    @Override
                                    public void onFailure(Call<MessageSignup> call, Throwable t) {

                                    }
                                });
                                new AsyncaTaskSendNumber(G.phpurl + "/index.php", number).execute();
                                Intent intent = new Intent(ActivitySignup.this, ActivityValidation.class);
                                intent.putExtra("name", name);
                                intent.putExtra("vu", "vl");
                                intent.putExtra("melli", melli);
                                intent.putExtra("ostan", ostan);
                                intent.putExtra("shahr", shahr);
                                intent.putExtra("parvane", parvane);
                                intent.putExtra("experience", experience);
                                intent.putExtra("type", type);
                                intent.putExtra("base", base);
                                intent.putExtra("pass", pass);
                                intent.putExtra("number", number);
                                intent.putExtra("username", user);
                                ActivitySignup.this.startActivity(intent);
                                Log.e("validation", response.body().getMessage());
                                SharedPreferences.Editor editor = G.preferences.edit();
//                                    editor.putString("username", user);
                                editor.putString("type", "vl");
                                editor.commit();


                            } else {

                                if (response.body().getMessage().equals("username")) {
                                    layusername.setBoxBackgroundColor(getResources().getColor(R.color.errorpass));
                                    layusername.setBoxStrokeColor(getResources().getColor(R.color.red));
                                    layusername.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                                    laymelli.setBoxBackgroundColor(getResources().getColor(R.color.defaultpass));
                                    laymelli.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.passhint)));
                                    laymelli.setBoxStrokeColor(getResources().getColor(R.color.passhint));
                                    Toast.makeText(G.context, "این نام کاربری قبلا انتخاب شده است.", Toast.LENGTH_SHORT).show();
                                    edtuser.setFocusableInTouchMode(true);
                                    edtuser.requestFocus();
                                } else if (response.body().getMessage().equals("melli")) {
                                    layusername.setBoxBackgroundColor(getResources().getColor(R.color.defaultpass));
                                    layusername.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.passhint)));
                                    layusername.setBoxStrokeColor(getResources().getColor(R.color.passhint));
                                    laymelli.setBoxBackgroundColor(getResources().getColor(R.color.errorpass));
                                    laymelli.setBoxStrokeColor(getResources().getColor(R.color.red));
                                    laymelli.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                                    Toast.makeText(G.context, "این کد ملی قبلا ثبت نام کرده است.", Toast.LENGTH_SHORT).show();
                                    edtmelli.setFocusableInTouchMode(true);
                                    edtmelli.requestFocus();
                                }

                            }


                        }

                        @Override
                        public void onFailure(Call<MessageSignup> call, Throwable t) {

                        }
                    });


//                    new AsyncTaskSendIdDr(G.phpurl + "/insertdoctors.php", user, "empty", name, number, melli, pass, parvane,experience,shahr,ostan).execute();
//                    new AsyncaTaskSendNumber(G.phpurl + "/index.php", number).execute();
//                    Log.e("ednezam", nezam);
                    //  preferences = PreferenceManager.getDefaultSharedPreferences(G.context);


                }
            }
        } else {
            Toast.makeText(ActivitySignup.this, "لطفا اتصال دستگاه خود را به اینترنت بررسی کنید", Toast.LENGTH_SHORT).show();


        }
    }


    private void Usersignup() {
        if (isConnected()) {


            name = edtname.getText().toString();
            number = edtnumber.getText().toString();
            melli = edtmelli.getText().toString();
            user = edtuser.getText().toString();
            pass = edtpass.getText().toString();


            if (user.isEmpty() || name.isEmpty() || number.isEmpty() || melli.isEmpty() || pass.isEmpty()) {
                Toast.makeText(ActivitySignup.this, "لطفا مشخصاتتان را تکمیل کنید", Toast.LENGTH_SHORT).show();
            } else {
                Matcher matcher = RTL_CHARACTERS.matcher(user);
                if (matcher.find()) {
                    Toast.makeText(ActivitySignup.this, "نام کاربری باید به انگلیسی باشد", Toast.LENGTH_SHORT).show();
                    layusername.setBoxBackgroundColor(getResources().getColor(R.color.errorpass));
                    layusername.setBoxStrokeColor(getResources().getColor(R.color.red));
                    layusername.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    edtuser.setFocusableInTouchMode(true);
                    edtuser.requestFocus();
//            return true;  // it's RTL
                } else {
//                mellicode=getDigitsCount(Integer.parseInt(melli));
//                mellicode = 10;
                    if (melli.length() == 10) {

                        number = "+" + code + edtnumber.getText().toString();
                        Call<MessageSignup> calluser = RInode.cheackuser(content, melli, "user", name);
                        calluser.enqueue(new Callback<MessageSignup>() {
                            @Override
                            public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                Log.e("signupuser", response.body().getMessage());
                                Boolean status = response.body().getStatus();
                                if (status) {
                                    Call<MessageSignup> calldata = RI.senddata(content,
                                            SymmetricAlgorithmAES.encrypt(user, preferences.getString("k5", "not")),
                                            preferences.getString("k1", "not"),
                                            preferences.getString("k2", "not"),
                                            preferences.getString("k3", "not"),
                                            preferences.getString("k4", "not"),
                                            preferences.getString("k5", "not"));

                                    calldata.enqueue(new Callback<MessageSignup>() {
                                        @Override
                                        public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {


                                        }

                                        @Override
                                        public void onFailure(Call<MessageSignup> call, Throwable t) {

                                        }
                                    });
                                    new AsyncaTaskSendNumber(G.phpurl + "/index.php", number).execute();
                                    Intent intent = new Intent(ActivitySignup.this, ActivityValidation.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("vu", "user");
                                    intent.putExtra("melli", melli);
                                    intent.putExtra("pass", pass);
                                    intent.putExtra("number", number);
                                    intent.putExtra("username", user);
                                    ActivitySignup.this.startActivity(intent);
//                                    SharedPreferences.Editor editor = G.preferences.edit();
//                                    editor.putString("username", user);
//                                    editor.putString("type", "user");
//                                    editor.commit();
//                                Call<MessageSignup> callvalidation = RI.validation(number);
//                                callvalidation.enqueue(new Callback<MessageSignup>() {
//                                    @Override
//                                    public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
//                                        Intent intent = new Intent(ActivitySignup.this, ActivityValidation.class);
//                                        intent.putExtra("number", number);
//                                        ActivitySignup.this.startActivity(intent);
////                                        Log.e("validation", response.body().getMessage());
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<MessageSignup> call, Throwable t) {
//                                        Log.e("validation", "error"+t);
//                                    }
//                                });
                                } else {

                                    if (response.body().getMessage().equals("username")) {
                                        layusername.setBoxBackgroundColor(getResources().getColor(R.color.errorpass));
                                        layusername.setBoxStrokeColor(getResources().getColor(R.color.red));
                                        layusername.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                                        laymelli.setBoxBackgroundColor(getResources().getColor(R.color.defaultpass));
                                        laymelli.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.passhint)));
                                        laymelli.setBoxStrokeColor(getResources().getColor(R.color.passhint));
                                        Toast.makeText(G.context, "این نام کاربری قبلا انتخاب شده است.", Toast.LENGTH_SHORT).show();
                                        edtuser.setFocusableInTouchMode(true);
                                        edtuser.requestFocus();
                                    } else if (response.body().getMessage().equals("melli")) {
                                        layusername.setBoxBackgroundColor(getResources().getColor(R.color.defaultpass));
                                        layusername.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.passhint)));
                                        layusername.setBoxStrokeColor(getResources().getColor(R.color.passhint));
                                        laymelli.setBoxBackgroundColor(getResources().getColor(R.color.errorpass));
                                        laymelli.setBoxStrokeColor(getResources().getColor(R.color.red));
                                        laymelli.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                                        Toast.makeText(G.context, "این کد ملی قبلا ثبت نام کرده است.", Toast.LENGTH_SHORT).show();
                                        edtmelli.setFocusableInTouchMode(true);
                                        edtmelli.requestFocus();

                                    }

                                }

                            }

                            @Override
                            public void onFailure(Call<MessageSignup> call, Throwable t) {

                            }
                        });

//                    Log.e("ednum", number);
//                    new AsyncTaskSendId(G.phpurl + "/insertid.php", user, "empty", name, number, melli, pass,age,weight,height,gender).execute();
//                    new AsyncaTaskSendNumber(G.phpurl + "/index.php", number).execute();

                        // preferences = PreferenceManager.getDefaultSharedPreferences(G.context);


//                Intent intent = new Intent(G.context, ActivityValidation.class);
//                intent.putExtra("edtNumber", number);
//                startActivity(intent);


                    } else {
                        laymelli.setBoxBackgroundColor(getResources().getColor(R.color.errorpass));
                        laymelli.setBoxStrokeColor(getResources().getColor(R.color.red));
                        laymelli.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        edtmelli.setFocusableInTouchMode(true);
                        edtmelli.requestFocus();
//mellicode
                    }
                }
            }
        } else {
            Toast.makeText(ActivitySignup.this, "لطفا اتصال دستگاه خود را به اینترنت بررسی کنید", Toast.LENGTH_SHORT).show();
        }

    }

    public static int getDigitsCount(int number) {
        int count = 0;
        do {
            number = number / 10;
            count = count + 1;
        } while (number > 0);
        return count;
    }

    public boolean isConnected() {
        ConnectivityManager connect = (ConnectivityManager) G.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connect != null) {
            NetworkInfo[] information = connect.getAllNetworkInfo();
            if (information != null) {
                for (int x = 0; x < information.length; x++) {
                    if (information[x].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
