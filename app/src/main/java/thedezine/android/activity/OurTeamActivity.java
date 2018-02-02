package thedezine.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import thedezine.android.R;
import thedezine.android.utils.Global;
import thedezine.android.utils.Tools;


public class OurTeamActivity extends AppCompatActivity {


    Context context;
    Global global;
   LinearLayout liOne,liTwo;
    FloatingActionButton ourteamFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ourteam);
        context = this;
        global = new Global(context);
        initToolbar();
        ourteamFAB=(FloatingActionButton)findViewById(R.id.ourteamFAB);
        initComp();

        Tools.systemBarLolipop(this);

        ourteamFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OurTeamActivity.this, EnquiryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        liOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OurTeamActivity.this, OurTeamDetailsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        liTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OurTeamActivity.this, OurTeamDetailsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
     //   toolbar.setNavigationIcon(R.drawable.ic_back_white);
      //  TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
      //  textView.setTextColor(getResources().getColor(R.color.cattextcolour));
        toolbar.setTitle("Our Team");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
      }

    private void initComp() {
        liOne = (LinearLayout) findViewById(R.id.liOne);
        liTwo = (LinearLayout) findViewById(R.id.liTwo);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            // Toast.makeText(MainCategoriesActivity.this, "BackWorking", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);


    }




}
