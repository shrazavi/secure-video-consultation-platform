package com.shrazavi.dadmehr.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.core.base.BasicActivity;

public class RatingFragment extends Fragment {
    TextView txtrating;
//    public SharedPreferences preferences;


    public RatingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_rating, container, false);

        txtrating = (TextView) myFragmentView.findViewById(R.id.txt_rate_name);


//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        String rating = BasicActivity.preferences.getString("rating", "not");
        txtrating.setText(rating);
        SharedPreferences.Editor editor =BasicActivity.preferences.edit();
        editor.putString("rating", "not");
        editor.commit();

        return myFragmentView;
    }


}
