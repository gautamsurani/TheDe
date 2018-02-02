package thedezine.android.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thedezine.android.R;
import thedezine.android.fragment.AboutFragment;
import thedezine.android.fragment.AddressFragment;
import thedezine.android.fragment.ContactFragment;
import thedezine.android.fragment.InquiryBudgetFragment;
import thedezine.android.fragment.InquiryFormFragment;
import thedezine.android.fragment.InquiryServiceFragment;
import thedezine.android.fragment.InquiryTypeFragment;
import thedezine.android.fragment.MainContactFragment;
import thedezine.android.fragment.MapFragment;
import thedezine.android.utils.Constant;
import thedezine.android.utils.FormListener;
import thedezine.android.utils.Global;
import thedezine.android.utils.GlobalListener;
import thedezine.android.utils.RequestMethod;
import thedezine.android.utils.RestClient;


public class InquiryActivity extends AppCompatActivity implements GlobalListener, FormListener {

    ProgressBar progressCount;
    Global global;
    Toolbar toolbar;
    private ViewPager viewPager;
    InquiryServiceFragment inquiryServiceFragment;
    InquiryTypeFragment inquiryTypeFragment;
    InquiryBudgetFragment inquiryBudgetFragment;
    InquiryFormFragment inquiryFormFragment;
    int currentPage = -1;
    TextView backBTN, nextBTN, submit;
    RelativeLayout bottomBTNS;
    ProgressDialog progressDialog;
    String strService = "";
    String strType = "";
    String strbudget = "";
    String strFormName = "";
    String strFormEmail = "";
    String strFormMobile = "";
    String strFormDesc = "";

    RelativeLayout mainrel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inquiry_view);
        global = new Global(this);
        viewPager = (ViewPager) findViewById(R.id.pagerinquiry);
        progressCount = (ProgressBar) findViewById(R.id.progressCount);
        progressCount.getProgressDrawable().setColorFilter(Color.parseColor("#0086b4"), PorterDuff.Mode.SRC_IN);
        bottomBTNS = (RelativeLayout) findViewById(R.id.bottomBTNS);
        mainrel = (RelativeLayout) findViewById(R.id.mainrel);
        backBTN = (TextView) findViewById(R.id.backBTN);
        nextBTN = (TextView) findViewById(R.id.nextBTN);
        submit = (TextView) findViewById(R.id.submit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Inquiry");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupPager(viewPager);

        progressCount.setProgress(0);


        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("currntdjf1111", currentPage + "         t");
                if (currentPage > 0) {
                    viewPager.setCurrentItem(currentPage - 1, false);


                }
            }
        });

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("currntdjf2222", currentPage + "         t");
                if (currentPage < 4) {

                    if (currentPage == 0) {

                        if (!strService.equalsIgnoreCase(""))
                            viewPager.setCurrentItem(currentPage + 1, true);

                    } else if (currentPage == 1) {

                        if (!strType.equalsIgnoreCase(""))
                            viewPager.setCurrentItem(currentPage + 1, true);

                    } else if (currentPage == 2) {

                        if (!strbudget.equalsIgnoreCase(""))
                            viewPager.setCurrentItem(currentPage + 1, true);

                    } else if (currentPage == 3) {

                        if (!strService.equalsIgnoreCase(""))
                            viewPager.setCurrentItem(currentPage + 1, true);

                    }


                }
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position > -1) {
                    Log.e("currntdjf3333", position + "         t");
                    if (position == 0) {
                        nextBTN.setVisibility(View.VISIBLE);
                        bottomBTNS.setVisibility(View.VISIBLE);
                        progressCount.setProgress(25);
                    } else if (position == 1) {
                        nextBTN.setVisibility(View.VISIBLE);
                        bottomBTNS.setVisibility(View.VISIBLE);
                        progressCount.setProgress(25);
                    } else if (position == 2) {
                        nextBTN.setVisibility(View.VISIBLE);
                        bottomBTNS.setVisibility(View.VISIBLE);
                        progressCount.setProgress(50);
                    } else if (position == 3) {

                        nextBTN.setVisibility(View.GONE);
                        //  bottomBTNS.setVisibility(View.GONE);
                        progressCount.setProgress(75);
                    }

                }

                currentPage = position;

                if (position > 0) {
                    backBTN.setVisibility(View.VISIBLE);
                } else {
                    backBTN.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /*mainrel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mainrel.getWindowVisibleDisplayFrame(r);
                int heightDiff = mainrel.getRootView().getHeight() - (r.bottom - r.top);

                if (heightDiff > 200) { // if more than 100 pixels, its probably a keyboard...
                    //ok now we know the keyboard is up...
                    bottomBTNS.setVisibility(View.GONE);

                }else{
                    //ok now we know the keyboard is down...
                    bottomBTNS.setVisibility(View.VISIBLE);

                }
            }
        });*/

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inquiryFormFragment.SendBackData();
            }
        });
    }

    private void setupPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (inquiryServiceFragment == null) {
            inquiryServiceFragment = new InquiryServiceFragment();
        }
        if (inquiryTypeFragment == null) {
            inquiryTypeFragment = new InquiryTypeFragment();
        }
        if (inquiryBudgetFragment == null) {
            inquiryBudgetFragment = new InquiryBudgetFragment();
        }
        if (inquiryFormFragment == null) {
            inquiryFormFragment = new InquiryFormFragment();
        }

        adapter.addFragment(inquiryServiceFragment, "");
        adapter.addFragment(inquiryTypeFragment, "");
        adapter.addFragment(inquiryBudgetFragment, "");
        adapter.addFragment(inquiryFormFragment, "");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }

    @Override
    public void onRadioClick(String value, int pageNumber) {

        Log.e("onew", strService + "        9");
        Log.e("twow", strType + "        9");
        Log.e("threew", strbudget + "        9");


        if (pageNumber == 1) {
            strService = value;
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(1, true);
                }
            }, 300);


        } else if (pageNumber == 2) {
            strType = value;
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(2, true);
                }
            }, 300);

        } else if (pageNumber == 3) {
            strbudget = value;
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(3, true);
                }
            }, 300);

        } else if (pageNumber == 4) {

        }


    }

    @Override
    public void onFormClick(String name, String email, String mobile, String desc, int visible) {

        if (visible == 1) {
            // submit.setVisibility(View.VISIBLE);
            // bottomBTNS.setVisibility(View.GONE);
            progressCount.setProgress(75);
            submit.setVisibility(View.GONE);

        } else if (visible == 0) {
            progressCount.setProgress(100);
            submit.setVisibility(View.VISIBLE);
            return;
            // submit.setVisibility(View.GONE);
            // bottomBTNS.setVisibility(View.VISIBLE);
        } else if (visible == 2) {


            bottomBTNS.setVisibility(View.INVISIBLE);
            if (!name.equalsIgnoreCase("")) {
                strFormName = name;
            }
            if (!email.equalsIgnoreCase("")) {
                strFormEmail = email;
            }
            if (!mobile.equalsIgnoreCase("")) {
                strFormMobile = mobile;
            }
            if (!desc.equalsIgnoreCase("")) {
                strFormDesc = desc;
            }

            Log.e("gottt", strFormEmail + "\n" + strFormMobile + "\n" + strFormDesc);

            if (global.isNetworkAvailable()) {
                if (!strFormEmail.equalsIgnoreCase("")) {
                    new inquiryDATA().execute();
                }
            } else {
                retryInternet();
            }

        }

    }


    private class inquiryDATA extends AsyncTask<String, Void, String> {

        JSONObject jsonObjectList;
        String resMessage = "";
        String resCode = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(InquiryActivity.this);
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.loading));
        }

        @Override
        protected String doInBackground(String... strings) {

            String strInquiry = Constant.BASE_URL + Constant.INQUIRY + strService
                    + "&service_type=" + strType + "&budget=" + strbudget + "&name="
                    + strFormName + "&email=" + strFormEmail + "&mobile=" + strFormMobile
                    + "&description=" + strFormDesc;

            String restUrl = strInquiry.replaceAll(" ", "%20");

            Log.d("strInquiry", restUrl);

            try {
                RestClient restClient = new RestClient(restUrl);
                try {
                    restClient.Execute(RequestMethod.GET);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                String InqDATA = restClient.getResponse();
                Log.e("InqDATA", InqDATA);

                if (InqDATA != null && InqDATA.length() != 0) {
                    jsonObjectList = new JSONObject(InqDATA);

                    if (jsonObjectList.length() != 0) {
                        resMessage = jsonObjectList.getString("message");
                        resCode = jsonObjectList.getString("msgcode");
                    }
                }
                return "yes";
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing() && progressDialog != null) {
                progressDialog.dismiss();

                if (s != null) {
                    Toast.makeText(InquiryActivity.this, resMessage, Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(InquiryActivity.this,SuccessActivity.class);
                    intent.putExtra("formName",strFormName);
                   // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                } else {
                    Toast.makeText(InquiryActivity.this, R.string.noconnection, Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }


    public void retryInternet() {
        final Dialog dialog = new Dialog(InquiryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.layout_nointernet);
        Button btnRetryinternet = (Button) dialog.findViewById(R.id.btnRetryinternet);
        btnRetryinternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (global.isNetworkAvailable()) {
                    dialog.dismiss();
                    new inquiryDATA().execute();
                } else {
                    Toast.makeText(InquiryActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();

                }
            }
        });
        dialog.show();
    }

}
