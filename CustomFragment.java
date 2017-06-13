package br.com.esolucoes.ecards.util;

import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import br.com.esolucoes.ecards.R;

/**
 * Created by Diogo on 31/01/2017.
 */
public class CustomFragment extends Fragment {

    private LinearLayout linearLayout;

    //UTILS

    public void showProgress(){
        linearLayout = new LinearLayout(getActivity());
        linearLayout.setGravity(Gravity.CENTER);
        ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        linearLayout.addView(progressBar);
        ((FrameLayout)getActivity().findViewById(R.id.main_content)).addView(linearLayout);
    }

    public void dismissProgress(){
        ((FrameLayout)getActivity().findViewById(R.id.main_content)).removeView(linearLayout);
    }


}