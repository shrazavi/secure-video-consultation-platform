package com.shrazavi.dadmehr.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nkzawa.emitter.Emitter;
import com.shrazavi.dadmehr.Adapter.RecyclerAdapterSearch;
import com.shrazavi.dadmehr.DataClass.ChatsText;
import com.shrazavi.dadmehr.DataClass.Key;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.shrazavi.dadmehr.core.util.SymmetricAlgorithmAES;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.github.nkzawa.socketio.client.Socket;

public class ActivitySearch extends AppCompatActivity {
    private ImageView imgBack;
    private EditText edtSearch;
    private ImageView imgClear;
    public Handler handler;
    ArrayList<ChatsText> chatsTexts;
    String myid,toid,room="";
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerViewChats;
    SecretKeySpec secretKey;
    String content;
    String enk;
    String vu = "";
    SecretKeySpec Key;
    RecyclerAdapterSearch recyclerAdapterSearch;
//    public SharedPreferences preferences;
//    private Socket socket;
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
        setContentView(R.layout.activity_search);
//        socket.connect();
        handler = new Handler();
        chatsTexts = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(G.context);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        imgClear = (ImageView) findViewById(R.id.imgClear);
        recyclerViewChats = findViewById(R.id.search_list);
        recyclerViewChats.setHasFixedSize(true);
        recyclerViewChats.setLayoutManager(linearLayoutManager);
        recyclerAdapterSearch = new RecyclerAdapterSearch(getApplicationContext(), chatsTexts);
        imgClear.setVisibility(View.GONE);
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);

//        toid = (String) getIntent().getExtras().get("username");
        room = (String) getIntent().getExtras().get("room");
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        myid = BasicActivity.userid;
        vu = BasicActivity.vu;
//        enk = BasicActivity.preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
//        content = Base64.encodeToString(SymmetricAlgorithmAES.encryption(Key, vu+"-"+myId+"-"+R.string.developer), Base64.NO_WRAP);
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + myid + "-" + getResources().getString(R.string.developer), enk);
        content=BasicActivity.content;

        edtSearch.setHint(getIntent().getStringExtra("hint"));
        BasicActivity.socket.on("search", handlerSearch);
        Call<Key> callkey = RI.getkey(content, room, myid);
        callkey.enqueue(new Callback<Key>() {
            @Override
            public void onResponse(Call<Key> call, Response<Key> response) {
                secretKey = null;
                Log.e("key", response.body().getKey());
                byte[] data = Base64.decode(response.body().getKey(), Base64.DEFAULT);
                Log.e("keylength", data.length + "");
                secretKey = new SecretKeySpec(data, 0, data.length, "AES");
                JSONObject postDataForGetAllMessage = new JSONObject();
                try {
//                    Log.e("myIdget", myId);
                    postDataForGetAllMessage.put("from", myid);
                    postDataForGetAllMessage.put("room", room);
                    postDataForGetAllMessage.put("content", content);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                BasicActivity.socket.emit("getallchats", postDataForGetAllMessage);
//                BasicActivity.socket.on("getallchats", handlerAllChats);
            }

            @Override
            public void onFailure(Call<Key> call, Throwable t) {
                Log.e("errorkey", t + "");
            }
        });


        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    chatsTexts.clone();
                    recyclerAdapterSearch.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edtSearch.getText().toString().equals(""))
                {imgClear.setVisibility(View.VISIBLE);}
                else
                {imgClear.setVisibility(View.GONE);}

                Log.e("edtsmessage",edtSearch.getText().toString());
                chatsTexts.clear();
                JSONObject postDataForGetAllMessage = new JSONObject();
                try {
//                        edtSearch.getText().toString()
                    postDataForGetAllMessage.put("message", edtSearch.getText().toString());
                    postDataForGetAllMessage.put("room", room);
                    postDataForGetAllMessage.put("to", myid);
                    postDataForGetAllMessage.put("content", content);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BasicActivity.socket.emit("search", postDataForGetAllMessage);

            }
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    View view =
                            ActivitySearch.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    Log.e("edtmessage",edtSearch.getText().toString());

                    chatsTexts.clear();
                    JSONObject postDataForGetAllMessage = new JSONObject();
                    try {
//                        edtSearch.getText().toString()
                        postDataForGetAllMessage.put("message", edtSearch.getText().toString());
                        postDataForGetAllMessage.put("room", room);
                        postDataForGetAllMessage.put("to", myid);
                        postDataForGetAllMessage.put("content", content);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    BasicActivity.socket.emit("search", postDataForGetAllMessage);

//                    postItems.clear();
//                    mRecyclerpostAdapter.notifyDataSetChanged();
//                    progressBar.setVisibility(View.VISIBLE);
//                    page = 1;
//                    searchpostRequest(edtSearch.getText().toString());

                    return true;
                }

                return false;
            }
        });

    }

//

    public Emitter.Listener handlerSearch = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONArray jsonArray = (JSONArray) args[0];
                    String message = "";
                    chatsTexts.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        message = "";
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ChatsText chatsText = new ChatsText();
//                            ChatDatabase chatDatabase = new ChatDatabase(G.context);

                            message = jsonObject.getString("text");
                            if (jsonObject.getString("pdfurl").isEmpty()) {
                                if (message.isEmpty()) {
                                    chatsText.setText("");
                                } else {

                                    byte[] encryptedMessage = Base64.decode(message + "", Base64.DEFAULT);
                                    chatsText.setText(new String(SymmetricAlgorithmAES.decryption(secretKey, encryptedMessage)));
                                }
                            } else {
                                chatsText.setText(message);
                            }


//                            Log.e("messageall",jsonObject.getString("text")+"");

                            chatsText.setId(jsonObject.getString("_id"));
                            chatsText.setImgurl(jsonObject.getString("picurl"));
                            chatsText.setVidurl(jsonObject.getString("vidurl"));
                            chatsText.setPdf(jsonObject.getString("pdfurl"));
                            chatsText.setAudio(jsonObject.getString("audio"));
                            chatsText.setVoise(jsonObject.getString("voise"));
//                            chatsText.setText(new String(SymmetricAlgorithmAES.decryption(secretKey, encryptedMessage)));
                            chatsText.setDate(jsonObject.getLong("date"));
                            chatsText.setDeleted(jsonObject.getBoolean("deleted"));
                            if (jsonObject.getString("from").equals(myid)) {
                                chatsText.setFrom("me");
                            } else {
                                chatsText.setFrom("you");
                            }
                            if (jsonObject.getString("read").equals("unread")) {
                                chatsText.setRead(false);
                            } else {
                                chatsText.setRead(true);
                            }
                            chatsText.setTime(jsonObject.getString("time"));

                            chatsTexts.add(chatsText);
//                            chatDatabase.addChat(jsonObject.getString("from"), jsonObject.getString("to"), jsonObject.getString("room"), jsonObject.getString("time"), jsonObject.getLong("date"), jsonObject.getString("picurl"), jsonObject.getString("vidurl"), chatsText.voise = jsonObject.getString("voise"), 1, 1, jsonObject.getString("text"));
                            recyclerAdapterSearch.notifyDataSetChanged();
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            ChatsText chatsText = new ChatsText();
//                            Log.e("message",jsonObject.getString("text"));
////                            if(jsonObject.getString("room").equals(myid+toid)||jsonObject.getString("room").equals(toid+myid)){
//                            if (jsonObject.getString("from").equals(myid)) {
//
//                                chatsText.setImgurl(jsonObject.getString("picurl"));
//                                chatsText.setVidurl(jsonObject.getString("vidurl"));
//                                chatsText.setVoise(jsonObject.getString("voise"));
//                                chatsText.setText( jsonObject.getString("text"));
//                                chatsText.setDate(jsonObject.getLong("date"));
//                                chatsText.setFrom("me");
////                            chatsText.to = jsonObject.getString("to");
//                                chatsText.setTime(jsonObject.getString("time"));
//                                chatsText.setRead(true);
//                                chatsTexts.add(chatsText);
//                                recyclerAdapterSearch.notifyDataSetChanged();
//                            } else {
//
//
//                                chatsText.setImgurl(jsonObject.getString("picurl"));
//                                chatsText.setVidurl(jsonObject.getString("vidurl"));
//                                chatsText.setVoise(jsonObject.getString("voise"));
//                                chatsText.setText(jsonObject.getString("text"));
//                                chatsText.setDate(jsonObject.getLong("date"));
//                                chatsText.setFrom("you");
////                            chatsText.to = jsonObject.getString("to");
//                                chatsText.setTime(jsonObject.getString("time"));
//                                chatsText.setRead(true);
//                                chatsTexts.add(chatsText);
//                                recyclerAdapterSearch.notifyDataSetChanged();
//                            }

//                            chatDatabase.addChat(jsonObject.getString("from"), jsonObject.getString("to"), jsonObject.getString("room"), jsonObject.getString("time"), jsonObject.getLong("date"), jsonObject.getString("picurl"), jsonObject.getString("vidurl"), chatsText.voise = jsonObject.getString("voise"), 1, 1, jsonObject.getString("text"));
//                            recyclerAdapterChats.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                            recyclerViewChats.setAdapter(new RecyclerAdapterSearch(ActivitySearch.this, chatsTexts));
                            scrollToBottom(recyclerViewChats);

//                    Log.i("GET", jsonArray.toString());

                }
            });
        }
    };
    private void scrollToBottom(final RecyclerView recyclerView) {
        // scroll to last item to get the view of last item
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();
        final int lastItemPosition = adapter.getItemCount() - 1;

        layoutManager.scrollToPositionWithOffset(lastItemPosition, 0);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                // then scroll to specific offset
                View target = layoutManager.findViewByPosition(lastItemPosition);
                if (target != null) {
                    int offset = recyclerView.getMeasuredHeight() - target.getMeasuredHeight();
                    layoutManager.scrollToPositionWithOffset(lastItemPosition, offset);

                }
            }
        });
    }
}
