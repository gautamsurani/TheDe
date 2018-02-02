package thedezine.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import thedezine.android.R;
import thedezine.android.utils.Global;
import thedezine.android.utils.Tools;


public class OTPActivity extends AppCompatActivity
{

    Global global;
    private Context context;
    public static ProgressDialog progressDialog;
    public static ProgressDialog progressBar3;
    public static EditText etOtp;
    TextView tvResendOtp,tvVerifyOtp,newuser;
    SharedPreferences mprefs;
    boolean IsName=true;

    public String strOtp="";
    private static int OTP_TIME_OUT = 8000;
    boolean BoolCancel=false;
    String resId = "",resName = "",resPhone = "",resImage = "",resEmail = "",resCity = "",strPhone="";

    int wishlistSize =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_screen);
        context = this;
        global = new Global(this);
        initComp();
        Tools.systemBarLolipop(this);
    }
    public void initComp()
    {
        etOtp = (EditText) findViewById(R.id.etOtp);
        tvResendOtp = (TextView) findViewById(R.id.tvResendOtp);
        tvVerifyOtp = (TextView) findViewById(R.id.tvVerifyOtp);

        tvVerifyOtp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (!validateOTP())
                {
                    return;
                }
                global.setPrefBoolean("Verify", true);
                Intent intent = new Intent(context,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                finish();
            }
        });


        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(global.isNetworkAvailable()) {
                    etOtp.setText("");
                }
                else {

                }
            }
        });
    }


    private boolean validateOTP() {
        if (etOtp.getText().toString().trim().isEmpty() ||
                etOtp.getText().toString().trim().length() > 4)
        {
            Toast.makeText(OTPActivity.this,"Please enter OTP", Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }



    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private long exitTime = 0;

    //   @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void doExitApp()
    {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_back, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
        else
        {
            finish();
            Intent intent = new Intent(OTPActivity.this,LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }

    }
    @Override
    public void onBackPressed()
    {
        doExitApp();
    }


}
