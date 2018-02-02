package thedezine.android.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import thedezine.android.R;



public class SuccessActivity extends AppCompatActivity {

    TextView dashBtn,tvMain,explorer;
    String formName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_view);
        dashBtn=(TextView)findViewById(R.id.dashBtn);
        tvMain=(TextView)findViewById(R.id.tvMain);
        explorer=(TextView)findViewById(R.id.explorer);

        Bundle b=getIntent().getExtras();

        if( b !=null){
            formName=b.getString("formName");
            tvMain.setText("Thank You "+formName+",");
        }else {
            tvMain.setText("Thank You");
        }

        dashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(SuccessActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(SuccessActivity.this,new String[]{Manifest.permission.CALL_PHONE},
                            127);
                }else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: "+"+919924523136"));
                    startActivity(intent);
                }


            }
        });


        explorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i2 = new Intent(SuccessActivity.this, MainActivity.class);
                i2.putExtra("explorTYPE","yes");
                startActivity(i2);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 127: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + "+919924523136"));
                        startActivity(intent);

                        Log.d("Permissions", "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                        Toast.makeText(SuccessActivity.this,"Call Permission Denied!", Toast.LENGTH_SHORT).show();

                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
                break;
            }

        }
    }

    @Override
    public void onBackPressed() {
    }

}
