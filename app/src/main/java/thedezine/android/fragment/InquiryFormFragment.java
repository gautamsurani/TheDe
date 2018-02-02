package thedezine.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import thedezine.android.R;
import thedezine.android.utils.FormListener;
import thedezine.android.utils.GlobalListener;


public class InquiryFormFragment extends Fragment {

    TextInputEditText edtName, edtemail, edtmobile, edtdesc;
    FormListener mCallback;
    TextView submit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inquiry_fragment_form, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtName = (TextInputEditText) view.findViewById(R.id.edtName);
        edtemail = (TextInputEditText) view.findViewById(R.id.edtemail);
        edtmobile = (TextInputEditText) view.findViewById(R.id.edtmobile);
        edtdesc = (TextInputEditText) view.findViewById(R.id.edtdesc);
        submit=(TextView)view.findViewById(R.id.submit);


        edtemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length()>5){
                   /* mCallback.onFormClick(edtName.getText().toString().trim(),
                            edtemail.getText().toString().trim(),edtmobile.getText().toString().trim(),
                            edtdesc.getText().toString().trim(),true
                            );*/
                    if (edtName.getText().length()>3){
                      //  submit.setVisibility(View.VISIBLE);
                          mCallback.onFormClick(edtName.getText().toString().trim(),
                            edtemail.getText().toString().trim(),edtmobile.getText().toString().trim(),
                            edtdesc.getText().toString().trim(),0);
                    }
                }else {
                    mCallback.onFormClick(edtName.getText().toString().trim(),
                            edtemail.getText().toString().trim(),edtmobile.getText().toString().trim(),
                            edtdesc.getText().toString().trim(),1);

                 //   submit.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void SendBackData(){

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edtemail.getText().toString().trim()).matches()) {
            Toast.makeText(getActivity(), "Enter valid email!", Toast.LENGTH_SHORT).show();
        }else if (edtmobile.getText().toString().trim().length() != 10) {
            Toast.makeText(getActivity(), "Enter valid mobile number", Toast.LENGTH_SHORT).show();
        }else {

            mCallback.onFormClick(edtName.getText().toString().trim(),
                    edtemail.getText().toString().trim(),edtmobile.getText().toString().trim(),
                    edtdesc.getText().toString().trim(),2
            );
        }


    }
    @Override
    public void onAttach(Context con) {
        super.onAttach(con);
        try {
            mCallback = (FormListener) con;
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
