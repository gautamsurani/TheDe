package thedezine.android.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.regex.Pattern;

import thedezine.android.R;
import thedezine.android.utils.Constant;
import thedezine.android.utils.Global;
import thedezine.android.utils.RequestMethod;
import thedezine.android.utils.RestClient;
import thedezine.android.utils.SmoothActionBarDrawerToggle;
import thedezine.android.utils.Tools;


public class Sample extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    NestedScrollView drawerView;
    RelativeLayout mainView;

    LinearLayout lvPotfolieScreen,lvOurClients,lvChatScreen,
                 lvOurTeams,lvSAbout,lvBlogScreen,lvContactUsScreen,lvHelpScreen,
                 lvSOverview,lvRatusScreen;


    Intent mPendingDrawerIntent;
    Global global;
    SharedPreferences mprefs;
    String deviceID = "", deviceEMAIL = "", fcmTOKEN = "";
    String strUserId = "";
    String strName = "";
    String strMobile = "";
    String strUserImage = "";
    String strEmail = "";
    ProgressDialog progressDialog;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    SharedPreferences mPrefs;
    FloatingActionButton overviewFAB;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);

        global = new Global(this);
        overviewFAB = (FloatingActionButton) findViewById(R.id.mainFAB);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        Tools.systemBarLolipop(this);

        fcmTOKEN = FirebaseInstanceId.getInstance().getToken();
        deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (ContextCompat.checkSelfPermission(Sample.this, Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            deviceEMAIL="";

        } else {
            Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
            Account[] accounts = AccountManager.get(this).getAccounts();
            for (Account account : accounts){
                if (gmailPattern.matcher(account.name).matches()){
                    deviceEMAIL=account.name;
                }
            }
        }

        Log.e("fcm",fcmTOKEN);
        Log.e("did",deviceID);
        Log.e("dmail",deviceEMAIL);


        overviewFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sample.this, EnquiryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                drawerView.animate().scaleX(1).scaleY(1);
                supportInvalidateOptionsMenu();

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
              //  drawerView.animate().scaleX(1f-slideOffset*2).scaleY(1f-slideOffset*2);
                //drawerView.setTranslationX(slideOffset * mainView.getWidth());
                mainView.setTranslationX(slideOffset * drawerView.getWidth());
                mDrawerLayout.bringChildToFront(drawerView);
                mDrawerLayout.requestLayout();

                if (slideOffset <= 0 && mPendingDrawerIntent != null) {
                    startActivity(mPendingDrawerIntent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    mPendingDrawerIntent = null;
                }
            }

        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        if (global.isNetworkAvailable()) {
            // new sendDATA().execute();
        } else {
            retryInternet();
        }

        lvPotfolieScreen.setOnClickListener(this);
        lvOurClients.setOnClickListener(this);
        lvChatScreen.setOnClickListener(this);
        lvOurTeams.setOnClickListener(this);
        lvSAbout.setOnClickListener(this);
        lvBlogScreen.setOnClickListener(this);
        lvContactUsScreen.setOnClickListener(this);
        lvHelpScreen.setOnClickListener(this);
        lvSOverview.setOnClickListener(this);
        lvRatusScreen.setOnClickListener(this);
        lvHelpScreen.setOnClickListener(this);
    }

    private void init() {
        lvPotfolieScreen = (LinearLayout) findViewById(R.id.lvPotfolieScreen);
        lvOurClients = (LinearLayout)findViewById(R.id.lvOurClients);
        lvChatScreen = (LinearLayout)findViewById(R.id.lvChatScreen);
        lvOurTeams = (LinearLayout)findViewById(R.id.lvOurTeams);
        lvSAbout = (LinearLayout)findViewById(R.id.lvSAbout);
        lvBlogScreen = (LinearLayout)findViewById(R.id.lvBlogScreen);
        lvContactUsScreen = (LinearLayout)findViewById(R.id.lvContactUsScreen);
        lvHelpScreen = (LinearLayout)findViewById(R.id.lvHelpScreen);
        lvSOverview= (LinearLayout)findViewById(R.id.lvSOverview);
        lvRatusScreen = (LinearLayout)findViewById(R.id.lvRatusScreen);

        drawerView = (NestedScrollView) findViewById(R.id.drawerView);
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

    }


    public void retryInternet() {
        final Dialog dialog = new Dialog(Sample.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.layout_nointernet);
        Button btnRetryinternet = (Button) dialog.findViewById(R.id.btnRetryinternet);
        btnRetryinternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (global.isNetworkAvailable()) {
                    dialog.dismiss();
                    new sendDATA().execute();
                } else {
                    Toast.makeText(Sample.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();

                }
            }
        });
        dialog.show();
    }

    private long exitTime = 0;

    public void doExitApp()
    {
        if ((System.currentTimeMillis() - exitTime) > 2000)
        {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            // finishAffinity();

        } else
        {
            finish();
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    @Override
    public void onBackPressed()
    {
        doExitApp();
    }

    @Override
    public void onClick(View view) {
        mDrawerLayout.closeDrawers();
        switch (view.getId()){
            case R.id.lvPotfolieScreen:

                break;
            case R.id.lvOurClients:

                break;

            case R.id.lvChatScreen:

                mPendingDrawerIntent=new Intent(Sample.this, ChatActivity.class);
                break;
            case R.id.lvOurTeams:

                break;

            case R.id.lvSAbout:

                break;
            case R.id.lvBlogScreen:

                break;

            case R.id.lvContactUsScreen:

                break;
            case R.id.lvHelpScreen:

                break;
            case R.id.lvSOverview:

                break;
            case R.id.lvRatusScreen:

                break;
        }
    }

    private class sendDATA extends AsyncTask<String,Boolean,Boolean> {

        JSONObject jsonObjectList;
        String resMessage = "";
        String resCode = "";
        String resVersion = "";
        String verMSG = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Sample.this);
            progressDialog.show();
            progressDialog.setMessage(getString(R.string.loading));
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            String strHOME = Constant.BASE_URL + Constant.HOME_PAGE + fcmTOKEN + Constant.DEVICE_ID
                    + deviceID + Constant.DEVICE_EMAIL + deviceEMAIL;
            Log.d("strHOME", strHOME);

            try {

                RestClient restClient = new RestClient(strHOME);
                try {
                    restClient.Execute(RequestMethod.GET);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                String AboutUs = restClient.getResponse();
                Log.e("HOMEPAGE", AboutUs);

                if (AboutUs != null && AboutUs.length() != 0) {

                    jsonObjectList = new JSONObject(AboutUs);

                    if (jsonObjectList.length() != 0) {
                        resMessage = jsonObjectList.getString("message");
                        resCode = jsonObjectList.getString("msgcode");
                        resVersion = jsonObjectList.getString("version");
                        verMSG = jsonObjectList.getString("msg");

                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog.isShowing() && progressDialog != null) {
                progressDialog.dismiss();

                if (aBoolean) {

                } else {

                }

            }

        }
    }
}

