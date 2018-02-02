package thedezine.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import thedezine.android.R;
import thedezine.android.utils.Constant;
import thedezine.android.utils.Global;
import thedezine.android.utils.Tools;


public class RegisterActivity extends AppCompatActivity {

    Global global;
    TextView signin,tvSignup;
    EditText etnewUsername,etnewemail,etnewpassword,etnewPhone;
    ProgressDialog progressDialog;
    Context context;
    SharedPreferences mprefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_screen);
        context = this;
        global = new Global(this);
        mprefs = getSharedPreferences(Constant.PREFS_NAME,MODE_PRIVATE);

        inticomp();
        Tools.systemBarLolipop(this);

    }

    private void inticomp()
    {
        signin = (TextView) findViewById(R.id.signin);
        etnewUsername = (EditText) findViewById(R.id.etnewUsername);
        etnewemail = (EditText) findViewById(R.id.etnewemail);
        etnewpassword = (EditText) findViewById(R.id.etnewpassword);
        etnewPhone = (EditText) findViewById(R.id.etnewPhone);
        tvSignup = (TextView) findViewById(R.id.tvSignup);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(RegisterActivity.this, LoginActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(it);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);

            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateUsername()) {
                    return;
                }

                if (!validateEmailPhone()) {
                    return;
                }
                if (!validatePhone()) {
                    return;
                }

                if (!validatePassword()) {
                    return;
                }
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);

            }
        });
    }



    private boolean validateUsername() {
        if (etnewUsername.getText().toString().trim().isEmpty())
        {
            //inputLayoutName.setError(getString(R.string.err_msg_name));
            Toast.makeText(RegisterActivity.this,"Enter your name", Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }



    private boolean validateEmailPhone() {
        if (etnewemail.getText().toString().trim().isEmpty())
        {
            //inputLayoutName.setError(getString(R.string.err_msg_name));
            Toast.makeText(RegisterActivity.this,"Enter email", Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }

    private boolean validatePhone() {
        String strPhone = etnewPhone.getText().toString();
        if (strPhone.length()>10 || strPhone.length()<10 || strPhone.isEmpty())
        {
            //inputLayoutName.setError(getString(R.string.err_msg_name));
            Toast.makeText(RegisterActivity.this,"Enter mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private boolean validatePassword() {

        if (etnewpassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(RegisterActivity.this,"Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);


    }
}
