package thedezine.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import thedezine.android.R;
import thedezine.android.utils.Constant;
import thedezine.android.utils.Global;
import thedezine.android.utils.RequestMethod;
import thedezine.android.utils.RestClient;
import thedezine.android.utils.Tools;


public class HelpActivity extends AppCompatActivity
{


    Global global;
    ProgressDialog progressDialog;
    Context context;
    private EditText etCname;
    private EditText etCemail;
    private EditText etCcomplain;
    private EditText etCphone;
    Button btnSubmit;
    Toolbar toolbar;
    FloatingActionButton helpFAB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_help);
        context = this;
        global = new Global(context);
        initToolbar();
        inotComp();
        helpFAB=(FloatingActionButton)findViewById(R.id.helpFAB);
        Tools.systemBarLolipop(this);

        helpFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, EnquiryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

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
                Intent intent = new Intent(HelpActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    public void initToolbar()
    {
         toolbar = (Toolbar) findViewById(R.id.toolbar);
       //  toolbar.setNavigationIcon(R.drawable.ic_back_white);

       /* TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("Help");
        textView.setTextColor(getResources().getColor(R.color.cattextcolour));*/
        toolbar.setTitle("Help");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }
    public void inotComp()
    {
        etCname = (EditText) findViewById(R.id.etCname);
        etCemail = (EditText) findViewById(R.id.etCemail);
        etCcomplain = (EditText) findViewById(R.id.etCcomplain);
        etCphone = (EditText) findViewById(R.id.etCphone);
        btnSubmit = (Button) findViewById(R.id.btnSubmitComplaint);
    }

   /* public void retryInternet()
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.layout_nointernet);
        Button btnRetryinternet = (Button) dialog.findViewById(R.id.btnRetryinternet);
        btnRetryinternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(global.isNetworkAvailable())
                {
                    dialog.dismiss();
                    new submitHelpAsync().execute();
                }
                else {
                    Toast.makeText(context,R.string.nonetwork,Toast.LENGTH_SHORT).show();

                }
            }
        });
        dialog.show();
    }*/

    private boolean validateName() {
        if (etCname.getText().toString().trim().isEmpty())
        {
            Toast.makeText(HelpActivity.this, getString(R.string.err_msg_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validatePhone() {

        String strPhone = etCphone.getText().toString();
        if (strPhone.length()<10 || strPhone.isEmpty())
        {
            Toast.makeText(HelpActivity.this, getString(R.string.err_msg_phone), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private boolean validateComplaint() {
        if (etCcomplain.getText().toString().trim().isEmpty())
        {
            Toast.makeText(HelpActivity.this, getString(R.string.err_enter_msg), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
            progressDialog = new ProgressDialog(context);
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

            String strHelp = Constant.BASE_URL + Constant.HELP_URL
                    +"&name="+ strName
                    +"&email="+strEmail
                    +"&contact_no="+strPhone
                    +"&message="+strComplain;

            String restUrl = strHelp.replaceAll(" ","%20");
           // Log.d("restUrlHelp", restUrl);

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
              //  Log.e("Help", Help);

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
                    finish();
                    hideKeyboard();
                    Toast.makeText(context,resMessage, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                }
                else if(resCode.equalsIgnoreCase("1"))
                {
                    Toast.makeText(context,resMessage, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
