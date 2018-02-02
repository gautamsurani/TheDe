package thedezine.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import thedezine.android.R;
import thedezine.android.utils.Global;
import thedezine.android.utils.Tools;


public class ForgotActivity extends AppCompatActivity {

    Context context;
    Global global;
    ProgressDialog progressDialog;
    TextView tvSubmitnewpassword;
    EditText etforgotemailphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgorpwd);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_screen);
        context = this;
        global = new Global(context);
        inticomp();
        Tools.systemBarLolipop(this);

        tvSubmitnewpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateEmailPhone()) {
                    return;
                }

                Intent intent = new Intent(context,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);


            }
        });

    }

    private void inticomp()
    {
        tvSubmitnewpassword = (TextView)findViewById(R.id.tvSubmitnewpassword);
        etforgotemailphone = (EditText) findViewById(R.id.etforgotemailphone);
    }


    private boolean validateEmailPhone() {
        if (etforgotemailphone.getText().toString().trim().isEmpty())
        {
            Toast.makeText(context,"Enter email or phone", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        super.onBackPressed();
        Intent intent = new Intent(context,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);


    }





}
