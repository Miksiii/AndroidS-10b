package com.example.milan.dz10_21;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.nikola.dz10_21.R;


public class Fragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_fragment1, container, false);

    }
    ImageView imageView = (ImageView)getView().findViewById(R.id.imageView);

    int[] imageArray = { R.mipmap.ic_launcher, R.mipmap.met1};

    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        int i = 0;

        public void run() {
            imageView.setImageResource(imageArray[i]);
            i++;
            if (i > imageArray.length - 1) {
                i = 0;
            }
            handler.postDelayed(this, 2000);
        }
    };

}
