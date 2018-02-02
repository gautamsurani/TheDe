package thedezine.android.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONObject;

import thedezine.android.R;
import thedezine.android.utils.Constant;
import thedezine.android.utils.Global;
import thedezine.android.utils.RequestMethod;
import thedezine.android.utils.RestClient;


public class ContactFragment extends Fragment {

    Global global;
    ProgressDialog progressDialog;

    private EditText etCname;
    private EditText etCemail;
    private EditText etCcomplain;
    private EditText etCphone;
    Button btnSubmit;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_fragment_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        global = new Global(getActivity());


        etCname = (EditText) view.findViewById(R.id.etCname);
        etCemail = (EditText) view.findViewById(R.id.etCemail);
        etCcomplain = (EditText) view.findViewById(R.id.etCcomplain);
        etCphone = (EditText) view.findViewById(R.id.etCphone);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmitComplaint);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateName()) {
                    return;
                }
                if (!validatePhone()) {
                    return;
                }
                if (!validateComplaint()) {
                    return;
                }
                if (global.isNetworkAvailable()) {

                    new submitHelpAsync().execute();

                } else {
                    retryInternet();
                }

            }
        });

    }

    private class submitHelpAsync extends AsyncTask<String,Void,String>
    {
        JSONObject jsonObjectList;
        String resMessage = "";
        String resCode =  "";
        String strName = "";
        String strEmail = "";
        String strComplain = "";
        String strPhone = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
            progressDialog.setMessage(getString(R.string.loading));
            strName = etCname.getText().toString();
            strEmail = etCemail.getText().toString();
            if(strEmail.length()==0)
            {
                strEmail = "";
            }
            else
            {
                strEmail = etCemail.getText().toString();
            }
            strPhone = etCphone.getText().toString();
            strComplain = etCcomplain.getText().toString();


        }

        @Override
        protected String doInBackground(String... params) {

            String strHelp = Constant.BASE_URL + Constant.CONTACT_FORM + strName
                    +"&email="+strEmail
                    +"&phone="+strPhone
                    +"&msg="+strComplain;

            String restUrl = strHelp.replaceAll(" ","%20");
             Log.d("restUrlHelp", restUrl);

            try
            {
                RestClient restClient=new RestClient(restUrl);
                try {
                    restClient.Execute(RequestMethod.POST);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                String Help=restClient.getResponse();
                  Log.e("Help", Help);

                if(Help!=null && Help.length()!=0)
                {
                    jsonObjectList=new JSONObject(Help);

                    if(jsonObjectList.length()!=0) {

                        resMessage = jsonObjectList.getString("message");
                        resCode = jsonObjectList.getString("msgcode");
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog.isShowing() && progressDialog!=null)
            {
                progressDialog.dismiss();
                if(resCode.equalsIgnoreCase("0"))
                {
                    hideKeyboard();
                    Toast.makeText(getActivity(),resMessage, Toast.LENGTH_SHORT).show();
                }
                else if(resCode.equalsIgnoreCase("1"))
                {
                    Toast.makeText(getActivity(),resMessage, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private boolean validateName() {
        if (etCname.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getActivity(), getString(R.string.err_msg_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validatePhone() {

        String strPhone = etCphone.getText().toString();
        if (strPhone.length()<10 || strPhone.isEmpty())
        {
            Toast.makeText(getActivity(), getString(R.string.err_msg_phone), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private boolean validateComplaint() {
        if (etCcomplain.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getActivity(), getString(R.string.err_enter_msg), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void retryInternet ()
        {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.layout_nointernet);
            Button btnRetryinternet = (Button) dialog.findViewById(R.id.btnRetryinternet);
            btnRetryinternet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (global.isNetworkAvailable()) {
                        dialog.dismiss();
                        new submitHelpAsync().execute();
                    } else {
                        Toast.makeText(getActivity(), R.string.nonetwork, Toast.LENGTH_SHORT).show();

                    }
                }
            });
            dialog.show();
        }
    }
