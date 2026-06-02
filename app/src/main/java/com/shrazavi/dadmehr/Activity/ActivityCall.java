package com.shrazavi.dadmehr.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shrazavi.dadmehr.Fragment.IncomingCallFragment;
import com.shrazavi.dadmehr.Fragment.OutcomingCallFragment;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.core.base.BasicActivity;

public class ActivityCall extends BasicActivity {
    ImageView imgProfile;
    String room, type, from,call,to;
    Button btnChoose, btncrop;
//    private Socket socket;
    ImageView imgPic;
int timer=0;
    public static Handler handlercall;
    String id = "";
    String callid = "";
    FragmentManager fragmentManager = getSupportFragmentManager();
//    public static SharedPreferences sharedPreferences;

//    {
//        try {
//            socket = IO.socket(G.nodeurl);//http://192.168.1.103:8000
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
//      Intent intent = new Intent(ActivityCall.this, ActivityMain.class);
//        ActivityCall.this.startActivity(intent);
//        ActivityCall.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        callid = (String) getIntent().getExtras().get("id");
        room = (String) getIntent().getExtras().get("room");
        type = (String) getIntent().getExtras().get("type");
        timer = (Integer) getIntent().getExtras().get("timer");
        from = (String) getIntent().getExtras().get("from");
        to = (String) getIntent().getExtras().get("to");
        call=(String) getIntent().getExtras().get("call");
//        socket.connect();
//        socket.emit("join","test");
//
//        socket.on("pic",handlerPic);
        if(call.equals("in")){
            IncomingCallFragment incomfragment = new IncomingCallFragment();
            Bundle arguments = new Bundle();
            arguments.putString( "id" , callid);
            arguments.putString( "room" , room);
            arguments.putInt( "timer" , timer);
            arguments.putString( "from" , from);
            arguments.putString( "to" , to);
            arguments.putString( "type" , type);
            incomfragment.setArguments(arguments);

            FragmentTransaction frm = fragmentManager.beginTransaction().replace(R.id.frm_call, incomfragment);
            frm.commit();
//            ActivityCall.this.finish();
        }else{
            OutcomingCallFragment outcomfragment = new OutcomingCallFragment();
            Bundle arguments = new Bundle();
            arguments.putString( "id" , callid);
            arguments.putString( "room" , room);
            arguments.putInt( "timer" , timer);
            arguments.putString( "from" , from);
            arguments.putString( "to" , to);
            arguments.putString( "type" , type);
            outcomfragment.setArguments(arguments);
            FragmentTransaction frm = fragmentManager.beginTransaction().replace(R.id.frm_call, outcomfragment);
            frm.commit();
//            ActivityCall.this.finish();
        }

//        fragment.setArguments(arguments);
//        final FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(R.id.content, fragment , FRAGMENT_TAG);
//        ft.commit();

        handlercall = new Handler();
//        btnChoose = (Button) findViewById(R.id.btnChoose);
//        btncrop = (Button) findViewById(R.id.btncrop);
        imgPic = (ImageView) findViewById(R.id.imaPic);

      //  BasicActivity.preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        id = BasicActivity.userid;

//        btnChoose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        btncrop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ActivityCall.this, ActivityImagePicker.class);
//
//
//                ActivityCall.this.startActivity(intent);
//            }
//        });
    }


}