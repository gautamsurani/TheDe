package thedezine.android.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import thedezine.android.R;
import thedezine.android.utils.Global;
import thedezine.android.utils.Tools;


public class LoginActivity extends AppCompatActivity {


    TextView signup,signin1,forgotpass,showhide;
    EditText etemailphone,etPassword;
    Global global;
    ProgressDialog progressDialog;
    Context context;
    SharedPreferences mprefs;
    RelativeLayout loginroot;

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_screen);
        context = this;
        global = new Global(context);
        inticomp();
        Tools.systemBarLolipop(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(it);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(it);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        showhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showhide.getText().equals("Hide"))
                {
                    showhide.setText("Show");
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else if(showhide.getText().equals("Show"))
                {
                    showhide.setText("Hide");
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        signin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateEmailPhone()) {
                    return;
                }
                hideKeyboard();
                Intent intent = new Intent(context,OTPActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void inticomp()
    {
        signup = (TextView)findViewById(R.id.signup);
        signin1 = (TextView)findViewById(R.id.tvSignup);
        etemailphone = (EditText) findViewById(R.id.etemailphone);
        etPassword = (EditText) findViewById(R.id.etPassword);
        forgotpass= (TextView)findViewById(R.id.forgotpass);
        showhide= (TextView)findViewById(R.id.showhide);

        loginroot = (RelativeLayout) findViewById(R.id.loginroot);
    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private boolean validateEmailPhone() {
        if (etemailphone.getText().toString().trim().isEmpty())
        {
            //inputLayoutName.setError(getString(R.string.err_msg_name));
            Toast.makeText(LoginActivity.this,"Enter mobile number", Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }

    private boolean validatePassword() {

        if (etPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(LoginActivity.this,"Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private long exitTime = 0;

    //   @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void doExitApp()
    {
        if ((System.currentTimeMillis() - exitTime) > 2000)
        {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            // finishAffinity();

        }
        else
        {
            finish();
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
        }

    }
    @Override
    public void onBackPressed()
    {
        doExitApp();
    }



}
