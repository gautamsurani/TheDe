package thedezine.android.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.regex.Pattern;

import thedezine.android.R;
import thedezine.android.fragment.AboutFragment;
import thedezine.android.fragment.BlogFragment;
import thedezine.android.fragment.ChatFragment;
import thedezine.android.fragment.ClientFragment;
import thedezine.android.fragment.HomeFragment;
import thedezine.android.fragment.MainContactFragment;
import thedezine.android.fragment.PortfolioFragment;
import thedezine.android.fragment.ProfileFragment;
import thedezine.android.fragment.ServiceFragment;
import thedezine.android.utils.Constant;
import thedezine.android.utils.Global;
import thedezine.android.utils.RequestMethod;
import thedezine.android.utils.RestClient;
import thedezine.android.utils.Tools;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    RelativeLayout drawerView;
    RelativeLayout mainView;
    LinearLayout lvPotfolieScreen, lvOurClients, lvChatScreen,
            lvOurTeams, lvSAbout, lvBlogScreen, lvContactUsScreen, lvHelpScreen,
            lvSOverview, lvRatusScreen;
    TextView aboutus, services, portfolio, clients, blog, contactus, ourteam, quote;
    Boolean isBold = false;
    View previousView;

    RelativeLayout navActionPhone, navActionchat, navActionweb;

    Global global;
    String deviceID = "", deviceEMAIL = "", fcmTOKEN = "";
    ProgressDialog progressDialog;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        global = new Global(this);
        toolbar = (Toolbar) findViewById(R.id.app);
        setSupportActionBar(toolbar);
        init();
        Tools.systemBarLolipop(this);

        Bundle b = getIntent().getExtras();

        fcmTOKEN = FirebaseInstanceId.getInstance().getToken();
        deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            deviceEMAIL = "";
        } else {
            Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
            Account[] accounts = AccountManager.get(this).getAccounts();
            for (Account account : accounts) {
                if (gmailPattern.matcher(account.name).matches()) {
                    deviceEMAIL = account.name;
                }
            }
        }
        Log.e("fcm", fcmTOKEN + "   p");
        Log.e("did", deviceID + "    p");
        Log.e("dmail", deviceEMAIL + "   p");

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                drawerView.animate().scaleX(1).scaleY(1);
                supportInvalidateOptionsMenu();

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
                //  drawerView.animate().scaleX(1f-slideOffset*2).scaleY(1f-slideOffset*2);
                //drawerView.setTranslationX(slideOffset * mainView.getWidth());
                mainView.setTranslationX(slideOffset * drawerView.getWidth());
                mDrawerLayout.bringChildToFront(drawerView);
                mDrawerLayout.requestLayout();
            }

        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    assert getSupportActionBar() != null;
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });
                } else {
                    assert getSupportActionBar() != null;
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setTitle(getString(R.string.app_name));
                    aboutus.setTypeface(Typeface.SERIF, Typeface.NORMAL);
                    services.setTypeface(Typeface.SERIF, Typeface.NORMAL);
                    portfolio.setTypeface(Typeface.SERIF, Typeface.NORMAL);
                    clients.setTypeface(Typeface.SERIF, Typeface.NORMAL);
                    blog.setTypeface(Typeface.SERIF, Typeface.NORMAL);
                    contactus.setTypeface(Typeface.SERIF, Typeface.NORMAL);
                    ourteam.setTypeface(Typeface.SERIF, Typeface.NORMAL);
                    quote.setTypeface(Typeface.SERIF, Typeface.NORMAL);

                    lvPotfolieScreen.setSelected(false);
                    lvOurClients.setSelected(false);
                    lvOurTeams.setSelected(false);
                    lvSAbout.setSelected(false);
                    lvBlogScreen.setSelected(false);
                    lvSOverview.setSelected(false);

                    mDrawerToggle.syncState();
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDrawerLayout.openDrawer(GravityCompat.START);
                        }
                    });
                }
            }
        });


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

        navActionPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawerLayout.closeDrawers();
                    }
                }, 50);

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                            120);
                } else {

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + "+919924523136"));
                    startActivity(intent);
                }
            }
        });

        navActionchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    try {
                        Intent browserIntent = new Intent(MainActivity.this, ChatFragment.class);
                        browserIntent.putExtra("EMAIL", deviceEMAIL);
                        startActivity(browserIntent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mDrawerLayout.closeDrawers();
                        }
                    }, 50);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        navActionweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawerLayout.closeDrawers();
                    }
                }, 50);
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.thedezine.in/"));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "No Browser Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        String title = getString(R.string.app_name);
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(title);
        if (b != null) {
            Fragment fragment2 = getSupportFragmentManager().findFragmentByTag("PORT");
            if (fragment2 instanceof PortfolioFragment) {
                isBold = true;
            } else {
                isBold = false;
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.frame_content, new PortfolioFragment(), "PORT")
                        .commit();
            }
        } else {
            if (global.isNetworkAvailable()) {
                new sendDATA().execute();
            } else {
                retryInternet();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 120: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + "+919924523136"));
                        startActivity(intent);

                        Log.d("Permissions", "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                        Toast.makeText(MainActivity.this, "Call Permission Denied!", Toast.LENGTH_SHORT).show();

                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
                break;
            }

        }
    }

    private void init() {
        lvPotfolieScreen = (LinearLayout) findViewById(R.id.lvPotfolieScreen);
        lvOurClients = (LinearLayout) findViewById(R.id.lvOurClients);
        lvChatScreen = (LinearLayout) findViewById(R.id.lvChatScreen);
        lvOurTeams = (LinearLayout) findViewById(R.id.lvOurTeams);
        lvSAbout = (LinearLayout) findViewById(R.id.lvSAbout);
        lvBlogScreen = (LinearLayout) findViewById(R.id.lvBlogScreen);
        lvContactUsScreen = (LinearLayout) findViewById(R.id.lvContactUsScreen);
        lvHelpScreen = (LinearLayout) findViewById(R.id.lvHelpScreen);
        lvSOverview = (LinearLayout) findViewById(R.id.lvSOverview);
        lvRatusScreen = (LinearLayout) findViewById(R.id.lvRatusScreen);

        navActionPhone = (RelativeLayout) findViewById(R.id.navActionPhone);
        navActionchat = (RelativeLayout) findViewById(R.id.navActionchat);
        navActionweb = (RelativeLayout) findViewById(R.id.navActionweb);

        aboutus = (TextView) findViewById(R.id.aboutus);
        services = (TextView) findViewById(R.id.services);
        portfolio = (TextView) findViewById(R.id.portfolio);
        clients = (TextView) findViewById(R.id.clients);
        blog = (TextView) findViewById(R.id.blog);
        contactus = (TextView) findViewById(R.id.contactus);
        ourteam = (TextView) findViewById(R.id.ourteam);
        quote = (TextView) findViewById(R.id.quote);

        drawerView = (RelativeLayout) findViewById(R.id.drawerView);
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

    }


    public void retryInternet() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_nointernet);
        Button btnRetryinternet = (Button) dialog.findViewById(R.id.btnRetryinternet);
        btnRetryinternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (global.isNetworkAvailable()) {
                    dialog.dismiss();
                    new sendDATA().execute();
                } else {
                    Toast.makeText(MainActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();

                }
            }
        });
        dialog.show();
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            Intent intent = new Intent(MainActivity.this, SplashActivity.class);
            intent.putExtra("Exitme", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {

        Fragment webview = getSupportFragmentManager().findFragmentByTag("BLOG");
        if (webview instanceof BlogFragment) {
            boolean goback = ((BlogFragment) webview).canGoBack();
            if (!goback) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return;
                }
                doExitApp();

            } else {
                ((BlogFragment) webview).goBack();
            }

        } else {

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
                return;
            }
            doExitApp();
        }

    }

    @Override
    public void onClick(View view) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawers();
            }
        }, 50);


        switch (view.getId()) {
            case R.id.lvPotfolieScreen:

                Fragment fragment = getSupportFragmentManager().findFragmentByTag("PORT");

                if (fragment instanceof PortfolioFragment) {

                    isBold = true;
                } else {
                    isBold = false;
                    // changeTextSTyle(mView);

                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }

                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.frame_content, new PortfolioFragment(), "PORT")
                            .commit();
                }


                break;
            case R.id.lvOurClients:

                Fragment fragment2 = getSupportFragmentManager().findFragmentByTag("CLIENT");
                if (fragment2 instanceof ClientFragment) {

                    isBold = true;
                } else {
                    isBold = false;
                    //  changeTextSTyle(mView);
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }

                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_content, new ClientFragment(), "CLIENT")
                            .commit();

                }


                break;
            case R.id.lvOurTeams:

                Fragment fragment3 = getSupportFragmentManager().findFragmentByTag("TEAM");
                if (fragment3 instanceof ProfileFragment) {

                    isBold = true;
                } else {
                    isBold = false;
                    //  changeTextSTyle(mView);
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }

                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_content, new ProfileFragment(), "TEAM")
                            .commit();
                }

                break;

            case R.id.lvSAbout:
                Fragment fragment4 = getSupportFragmentManager().findFragmentByTag("ABOUT");
                if (fragment4 instanceof AboutFragment) {

                    isBold = true;
                } else {
                    isBold = false;
                    //   changeTextSTyle(mView);
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }

                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_content, new AboutFragment(), "ABOUT")
                            .commit();


                }

                break;
            case R.id.lvBlogScreen:

                Fragment fragment5 = getSupportFragmentManager().findFragmentByTag("BLOG");
                if (fragment5 instanceof BlogFragment) {

                    isBold = true;
                } else {
                    isBold = false;
                    //  changeTextSTyle(mView);
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }


                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_content, new BlogFragment(), "BLOG")
                            .commit();

                }


                break;

            case R.id.lvContactUsScreen:

                Fragment fragment6 = getSupportFragmentManager().findFragmentByTag("CONTACT");
                if (fragment6 instanceof MainContactFragment) {

                    isBold = true;
                } else {
                    isBold = false;
                    //     changeTextSTyle(mView);
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }


                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_content, new MainContactFragment(), "CONTACT")
                            .commit();
                }


                break;
            case R.id.lvSOverview:
                Fragment fragment7 = getSupportFragmentManager().findFragmentByTag("SERVICE");
                if (fragment7 instanceof ServiceFragment) {

                    isBold = true;
                } else {
                    isBold = false;
                    //   changeTextSTyle(mView);
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }


                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_content, new ServiceFragment(), "SERVICE")
                            .commit();


                }

                break;
            case R.id.lvRatusScreen:
                isBold = true;
                /*try {
                    Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());

                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(viewIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Unable to Connect Try Again...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }*/
                Intent intent = new Intent(MainActivity.this, InquiryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                break;


        }

        if (!isBold) {
            Log.e("texts", "start");
            LinearLayout previousText = (LinearLayout) previousView;
            LinearLayout curText = (LinearLayout) view;
            if (curText.isSelected()) {
                Log.e("texts", "true");
                curText.setSelected(false);
                TextView tb = (TextView) curText.getChildAt(0);
                tb.setTypeface(Typeface.SERIF, Typeface.NORMAL);
            } else {
                Log.e("texts", "false");
                if (previousText != null && previousText.isSelected()) {
                    Log.e("texts", "booobl");
                    previousText.setSelected(false);
                    TextView tb = (TextView) previousText.getChildAt(0);
                    tb.setTypeface(Typeface.SERIF, Typeface.NORMAL);
                }
                curText.setSelected(true);
                TextView tb = (TextView) curText.getChildAt(0);
                tb.setTypeface(Typeface.SERIF, Typeface.BOLD);
                previousView = view;
            }

            Log.e("texts", "done");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blogmenu, menu);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private class sendDATA extends AsyncTask<String, Boolean, Boolean> {
        JSONObject jsonObjectList;
        String resMessage = "";
        String resCode = "";
        String resVersion = "";
        String verMSG = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
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
            }
        }
    }
}