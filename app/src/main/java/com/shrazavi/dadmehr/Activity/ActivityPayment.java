package com.shrazavi.dadmehr.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shrazavi.dadmehr.Adapter.spinnerAdapter;
import com.shrazavi.dadmehr.DataClass.Cash;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.shrazavi.dadmehr.core.util.SymmetricAlgorithmAES;
import com.zarinpal.ZarinPalBillingClient;
import com.zarinpal.billing.purchase.Purchase;
import com.zarinpal.client.BillingClientStateListener;
import com.zarinpal.client.ClientState;
import com.zarinpal.provider.core.future.FutureCompletionListener;
import com.zarinpal.provider.core.future.TaskResult;
import com.zarinpal.provider.model.response.Receipt;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityPayment extends AppCompatActivity {
    Spinner spinpay;
    String userid;
    String content;
    String[] arrpay = {"50000 تومان", "100000 تومان", "150000 تومان", "200000 تومان"};
    String yourSKU;
    String pay = "";
    String account = "";
    String detail = "",detailcash = "";

    //    public static HamrahPay hp = null;
    Button btnpay, btn50, btn100, btn150, btn200, btn500;
    Retrofitinformation RI;
    String character;
    String enk;
    String code;
    final int min = 100000;
    final int max = 999999;
    int random;
    String transid = "";
    //    public static SharedPreferences preferences;
    TextInputEditText edtprice;
    TextInputLayout layprice;
    String k = "";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        spinpay = (Spinner) findViewById(R.id.spin_pay);
        btnpay = (Button) findViewById(R.id.btn_pay);
        btn50 = (Button) findViewById(R.id.btn_50);
        btn100 = (Button) findViewById(R.id.btn_100);
        btn150 = (Button) findViewById(R.id.btn_150);
        btn200 = (Button) findViewById(R.id.btn_200);
        btn500 = (Button) findViewById(R.id.btn_500);
        btnpay.setTypeface(G.face);
        btn50.setTypeface(G.face);
        btn100.setTypeface(G.face);
        btn150.setTypeface(G.face);
        btn200.setTypeface(G.face);
        btn500.setTypeface(G.face);


        edtprice = (TextInputEditText) findViewById(R.id.edt_payment_price);
        layprice = (TextInputLayout) findViewById(R.id.lay_payment_price);

//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        spinnerAdapter adapterpay = new spinnerAdapter(ActivityPayment.this, android.R.layout.simple_list_item_1);
        adapterpay.addAll(arrpay);
        adapterpay.add("مبلغ مورد نظر را انتخاب کنید");
        spinpay.setAdapter(adapterpay);
        spinpay.setSelection(adapterpay.getCount());
        userid = BasicActivity.userid;
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
//        enk = BasicActivity.preferences.getString("k5", "not");
        content = BasicActivity.content;

//        Purchase purchase2 = Purchase.newBuilder()
//                .asAuthority("AUTHORITY_RESOLVED")
//                .build();
//        Purchase purchase3 = Purchase.newBuilder()
//                .asSku("SKU_ID") // SKU_ID is an Id that you've generated on ZarinPal panel.
//                .build();


//        List<String> skus = Arrays.asList("SKU_ID_000", "SKU_ID_001");
//        SkuQueryParams skuQuery = SkuQueryParams.newBuilder("MERCHANT_ID")
//                .setSkuList(skus)
//                .orderByMobile("0935******")
//                .build();


//        client.querySkuPurchased(skuQuery, new FutureCompletionListener<List<SkuPurchased>>() {
//            @Override
//            public void onComplete(TaskResult<List<SkuPurchased>> task) {
//                if (task.isSuccess()) {
//                    List<SkuPurchased> skuPurchaseList = task.getSuccess();
//                    for (SkuPurchased it : skuPurchaseList) {
//                        Log.v("ZP_SKU_PURCHASED", "${it.authority} ${it.productId}");
//                    }
//                } else {
//                    task.getFailure().printStackTrace();
//                }
//            }
//        });
//        client.setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        client.enableShowInvoice();
        random = new Random().nextInt((max - min) + 1) + min;

        spinpay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                if (spinpay.getSelectedItem() == "This is Hint Text") {
                    pay = "";
                    //Do nothing.
                } else {
                    pay = spinpay.getSelectedItem().toString();
//                    Toast.makeText(ActivitySignup.this, spinspecialty.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                    switch (pay) {

                        case "50000 تومان":
                            yourSKU = "hp_5f6f27e5a4ff3773289233";
                            character = "L";
                            account = "50000";
                            code = "ahZ2vtDczIvaYieiSV7P9m/FNpjSlF27duL+mghxh64=";
                            break;
                        case "100000 تومان":
                            yourSKU = "hp_5f6f27f84f899210103710";
                            character = "M";
                            account = "100000";
                            code = "hCVbY8QOtmMUN2QI6JfAmn2BrXHUQWaAxK9Cs7ocSuw=";
                            break;
                        case "150000 تومان":
                            yourSKU = "hp_5f7043bee7f80128162886";
                            account = "150000";
                            character = "H";
                            code = "2w/9ilG6Z36vWUoxoB0dcD/hPzRSrWKN8WLbSV4JxXw=";
                            break;
                        case "200000 تومان":
                            account = "200000";
                            yourSKU = "hp_5f6f27d3f1996946512573";
                            character = "V";
                            code = "jYjIdw23LAFMi0GYeER+b9kvq2BXiylbrTgkuwjImWQ=";
                            break;

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        btn50.setOnClickListener(v -> {
            edtprice.setText("");
            edtprice.setText("50000");

        });
        btn100.setOnClickListener(v -> {
            edtprice.setText("");
            edtprice.setText("100000");
        });
        btn150.setOnClickListener(v -> {
            edtprice.setText("");
            edtprice.setText("150000");
        });
        btn200.setOnClickListener(v -> {
            edtprice.setText("");
            edtprice.setText("200000");
        });
        btn500.setOnClickListener(v -> {
            edtprice.setText("");
            edtprice.setText("500000");
        });
        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                ChatDatabase chatDatabase = new ChatDatabase(G.context);
//                chatDatabase.addTransaction(account + "", currentTime(),currentdate(), "unsuccessful");
//                Log.e("datepay",currentdate()+"");
//                Log.e("content vl", content + "");
                detail = SymmetricAlgorithmAES.encrypt(userid + "/" + random + "/" + edtprice.getText() + "/" + "un", BasicActivity.preferences.getString("k5", "not"));

                Call<MessageSignup> calladdtrans = RI.AddTransaction(content, userid, random, edtprice.getText().toString(), "unsuccessful", detail);
                calladdtrans.enqueue(new Callback<MessageSignup>() {
                    @Override
                    public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
//                    G.income = Integer.parseInt(response.body().getIncome());
                        transid = response.body().getMessage();
                        k="k"+response.body().getContent();


                        detailcash = SymmetricAlgorithmAES.encrypt(userid + "/"  +  edtprice.getText().toString() + "/" + transid, BasicActivity.preferences.getString(k, "not"));
//                        if (edtprice.getText().toString().equals("50000")) {
//                            Call<Cash> callcash = RI.increaseaccount(content, userid, Long.valueOf(edtprice.getText().toString()), transid, detailcash);
//                            callcash.enqueue(new Callback<Cash>() {
//                                @Override
//                                public void onResponse(Call<Cash> call, Response<Cash> response) {
//                                    Intent intent18 = new Intent(ActivityPayment.this, ActivityFinalPayment.class);
//                                    intent18.putExtra("cash", response.body().getCash());
//                                    intent18.putExtra("date", response.body().getDate());
//                                    intent18.putExtra("code", response.body().getCode());
//                                    intent18.putExtra("time", response.body().getTime());
//                                    intent18.putExtra("price", edtprice.getText().toString());
//                                    ActivityPayment.this.startActivity(intent18);
//                                    ActivityPayment.this.finish();
//                                }
//
//                                @Override
//                                public void onFailure(Call<Cash> call, Throwable t) {
//                                    Log.e("peyment error=",t+"");
//
//                                }
//                            });


//                        } else {
                            BillingClientStateListener listener = new BillingClientStateListener() {
                                @Override
                                public void onClientSetupFinished(@NotNull ClientState state) {
                                    //Observing client states
                                }

                                @Override
                                public void onClientServiceDisconnected() {
                                    Log.v("TAG_INAPP", "Billing client Disconnected");
                                    //When Service disconnect
                                }
                            };
                            ZarinPalBillingClient client = ZarinPalBillingClient.newBuilder(ActivityPayment.this)
                                    .enableShowInvoice()
                                    .setListener(listener)
                                    .setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                    .build();

                            Purchase purchase = Purchase.newBuilder().asPaymentRequest(
                                    "14f86589-4f47-433f-a318-7eaaa6d7a8d7",
                                    Long.valueOf(edtprice.getText().toString()),
                                    G.nodeurl+"/zarinpayment",
                                    edtprice.getText().toString()+" RR Purchase"
                            ).build();
                            client.launchBillingFlow(purchase, new FutureCompletionListener<Receipt>() {
                                @Override
                                public void onComplete(TaskResult<Receipt> task) {
                                    if (task.isSuccess()) {
                                        Receipt receipt = task.getSuccess();
                                        Log.v("ZP_RECEIPT", receipt.getTransactionID());
                                        //here you can send receipt data to your server
                                        //sentToServer(receipt)
                                        Call<Cash> callcash = RI.increaseaccount(content, userid, Long.valueOf(edtprice.getText().toString()), transid, detailcash);
                                        callcash.enqueue(new Callback<Cash>() {
                                            @Override
                                            public void onResponse(Call<Cash> call, Response<Cash> response) {
                                                Intent intent18 = new Intent(ActivityPayment.this, ActivityFinalPayment.class);
                                                intent18.putExtra("cash", response.body().getCash());
                                                intent18.putExtra("date", response.body().getDate());
                                                intent18.putExtra("code", response.body().getCode());
                                                intent18.putExtra("time", response.body().getTime());
                                                intent18.putExtra("price", edtprice.getText().toString());
                                                ActivityPayment.this.startActivity(intent18);
                                                ActivityPayment.this.finish();
                                            }

                                            @Override
                                            public void onFailure(Call<Cash> call, Throwable t) {
                                                Log.e("peyment error=",t+"");

                                            }
                                        });


                                    } else {
                                        task.getFailure().printStackTrace();
                                    }
                                }
                            });

//                        }
                    }

                    @Override
                    public void onFailure(Call<MessageSignup> call, Throwable t) {
                        Log.e("error vl", t + "");
                    }
                });


            }
        });


    }

    public String currentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c.getTime());
        return formattedDate;


    }


    public long currentdate() {
        Calendar c = Calendar.getInstance();
//        SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
        long formattedDate = c.getTimeInMillis();
        return formattedDate;


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

//        if (intent.getScheme().equals("hamrahpay")) {
//            hp.verifyPayment();
//        }
    }
}