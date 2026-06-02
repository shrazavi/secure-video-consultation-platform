package com.shrazavi.dadmehr.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nkzawa.emitter.Emitter;
import com.shrazavi.dadmehr.Activity.ActivityMain;
import com.shrazavi.dadmehr.Adapter.RecyclerAdapterRoom;
import com.shrazavi.dadmehr.DataClass.ChatInfo;
import com.shrazavi.dadmehr.DataClass.Room;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.crypto.spec.SecretKeySpec;


public class ChatListFragment extends Fragment {
    String content;
    String enk;
    SecretKeySpec Key;
    DrawerLayout drawerLayout;
    ImageView hambergurMenu;
//    private Socket socket;
    TextView txtContact;
    TextView txtChannel;
    TextView txtStartChannel;
    TextView txtSetting;
    RecyclerView recyclerView;
    StringBuffer stringBuffer;
    LinearLayout linearmessage;
    Retrofitinformation RI;
    ArrayList<Room> rooms;
    ArrayList<ChatInfo> infos;
    public String vu = "";
    public String type = "";
    public static Context context;
    Handler threadHandler;
    public String id = "";
//    public static SharedPreferences sharedPreferences;

    public RecyclerAdapterRoom recyclerAdapterroom;


//    {
//        try {
//            socket = IO.socket(G.nodeurl);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    LinearLayoutManager linearLayoutManager;

    public ChatListFragment() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {
            String result = data.getExtras().getString("result");

        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.fragment_chat, container, false);

        rooms = new ArrayList<>();

//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        vu = BasicActivity.vu;
        id = BasicActivity.userid;
//        enk = sharedPreferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key=new SecretKeySpec(data, 0, data.length, "AES");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + id + "-" + getResources().getString(R.string.developer), enk);
        content=BasicActivity.content;
//        content = Base64.encodeToString(SymmetricAlgorithmAES.encryption(Key, vu+"-"+id), Base64.NO_WRAP);
        ActivityMain.handlerchat = new Handler();

//        socket.connect();

        JSONObject postDataForRoom = new JSONObject();
        try {
            postDataForRoom.put("user", id);
            postDataForRoom.put("vu", vu);
            postDataForRoom.put("content", content);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        BasicActivity.socket.emit("getroom", postDataForRoom);

        BasicActivity.socket.on("room", handlerReceiveRoom);
        context = G.context;

        stringBuffer = new StringBuffer();

        isConnected();
        if (isConnected()) {
            ActivityMain.txtconnection.setText("Connected");

        } else {

            ActivityMain.txtconnection.setText("Waiting For Connection");
        }

        drawerLayout = (DrawerLayout) myFragmentView.findViewById(R.id.drawer);
        hambergurMenu = (ImageView) myFragmentView.findViewById(R.id.hambergurMenu);
        txtContact = (TextView) myFragmentView.findViewById(R.id.contacts);
        txtChannel = (TextView) myFragmentView.findViewById(R.id.txtChannel);
        txtStartChannel = (TextView) myFragmentView.findViewById(R.id.txtStartChannel);
        txtSetting = (TextView) myFragmentView.findViewById(R.id.txtSetting);
        recyclerView = (RecyclerView) myFragmentView.findViewById(R.id.recycler_chat_list);
        linearLayoutManager = new LinearLayoutManager(G.context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearmessage = (LinearLayout) myFragmentView.findViewById(R.id.linearMessage1);


        return myFragmentView;
    }


    Emitter.Listener handlerReceiveRoom = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            ActivityMain.handlerchat.post(new Runnable() {
                @Override
                public void run() {

                    JSONArray jsonArray = (JSONArray) args[0];
                    rooms.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Room room = new Room();

                            room.setId(jsonObject.getString("_id"));
                            room.setUserA(jsonObject.getString("userA"));
                            room.setUserB(jsonObject.getString("userB"));
                            room.setReadA(jsonObject.getInt("readA"));
                            room.setReadB(jsonObject.getInt("readB"));
                            room.setDate(jsonObject.getString("date"));
                            room.setName(jsonObject.getString("name"));
                            rooms.add(room);


                            // Log.e("readsocket", read);


                            recyclerView.setAdapter(recyclerAdapterroom = new RecyclerAdapterRoom(rooms, G.context,content));
                            recyclerAdapterroom.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            });
        }
    };

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
