package thedezine.android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import thedezine.android.R;
import thedezine.android.utils.GlobalListener;



public class InquiryTypeFragment extends Fragment {

    RadioGroup typeRadioGroup;
    GlobalListener mCallback;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inquiry_fragment_type, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        typeRadioGroup=(RadioGroup)view.findViewById(R.id.typeRadioGroup);

        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton t=(RadioButton)radioGroup.findViewById(i);
                Log.e("radiogrp2", t.getText()+"");

                mCallback.onRadioClick(t.getText().toString().trim(),2);
            }
        });
    }

    @Override
    public void onAttach(Context con) {
        super.onAttach(con);
        try {
            mCallback = (GlobalListener) con;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        mCallback=null;
        super.onDetach();
    }

}
