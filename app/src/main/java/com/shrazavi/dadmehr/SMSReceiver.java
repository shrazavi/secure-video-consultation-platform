package com.shrazavi.dadmehr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.shrazavi.dadmehr.Activity.ActivityValidation;


/**
 * Created by General on 6/23/2017.
 */

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            Object[] objects=(Object[])bundle.get("pdus");
            for (int i=0;i<objects.length;i++){
                SmsMessage smsMessage= SmsMessage.createFromPdu((byte[])objects[i]);
                String message=smsMessage.getDisplayMessageBody();
                String number=smsMessage.getOriginatingAddress();

                if(number.equals("50001070")||number.equals("50001117")||number.equals("9850001070")||number.equals("9850001117")||number.equals("9850001730")){
                    String replacemessage=message.replace("به اپلیکیشن درمانو خوش آمدید کد تایید شما ","");

                    ActivityValidation.edtCode.setText(replacemessage);
//                    ActivityValidation.message(context);
//                    G.message();
                }

            }
        }
    }
}
