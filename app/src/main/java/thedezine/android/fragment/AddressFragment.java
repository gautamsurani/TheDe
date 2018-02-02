package thedezine.android.fragment;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import thedezine.android.R;

import thedezine.android.utils.Constant;
import thedezine.android.utils.Global;
import thedezine.android.utils.RequestMethod;
import thedezine.android.utils.RestClient;


public class AddressFragment extends Fragment {

    Global global;
    ProgressDialog progressDialog;
    LinearLayout mainRel;
    String address1 = "", address2 = "", address3 = "", address4 = "", website = "", mobile = "", company = "", text = "", callus = "";
    ImageView viewfb, viewgplus, viewin, viewinstagram, viewtwitter;
    TextView tvAddone, tvAddtwo, tvAddthree, tvAddfour, tvAddwebsite, tvAddphone,
            tvCompanytext, tvCompany;

    String resfacebook_link = "";
    String resgoogle_link = "";
    String reslinkdin_link = "";
    String restwitter_link = "";
    String resinsta_link = "";

    public  static String latitude = "";
    public  static String logitude = "";

    FloatingActionButton CallFAB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.address_fragment_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainRel = (LinearLayout) view.findViewById(R.id.mainRel);
        global = new Global(getContext());
        viewfb = (ImageView) view.findViewById(R.id.viewfb);
        viewgplus = (ImageView) view.findViewById(R.id.viewgplus);
        viewin = (ImageView) view.findViewById(R.id.viewin);
        viewinstagram = (ImageView) view.findViewById(R.id.viewinstagram);
        viewtwitter = (ImageView) view.findViewById(R.id.viewtwitter);

        CallFAB= (FloatingActionButton) view.findViewById(R.id.CallFAB);

        tvAddone = (TextView) view.findViewById(R.id.tvAddone);
        tvAddtwo = (TextView) view.findViewById(R.id.tvAddtwo);
        tvAddthree = (TextView) view.findViewById(R.id.tvAddthree);
        tvAddfour = (TextView) view.findViewById(R.id.tvAddfour);
        tvAddwebsite = (TextView) view.findViewById(R.id.tvAddwebsite);
        tvAddphone = (TextView) view.findViewById(R.id.tvAddphone);
        tvCompanytext = (TextView) view.findViewById(R.id.tvCompanyText);
        tvCompany = (TextView) view.findViewById(R.id.tvCompany);


        CallFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callus.length()>0){

                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},
                                540);
                    }else {

                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: "+callus));
                        startActivity(intent);
                    }

                }
            }
        });


        if (global.isNetworkAvailable()) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new addressCompany().execute();

                }
            },100);

        } else {
            retryInternet();
            mainRel.setVisibility(View.GONE);
            CallFAB.setVisibility(View.GONE);
            // Utils.showToastShort("Please Check Your Internet Connection", getActivity());
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 540: {
                for( int i = 0; i < permissions.length; i++ ) {
                    if( grantResults[i] == PackageManager.PERMISSION_GRANTED ) {

                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: "+callus));
                        startActivity(intent);

                        Log.d("Permissions", "Permission Granted: " + permissions[i]);
                    } else if( grantResults[i] == PackageManager.PERMISSION_DENIED ) {

                        Toast.makeText(getActivity(),"Call Permission Denied.",Toast.LENGTH_SHORT).show();

                        Log.d( "Permissions", "Permission Denied: " + permissions[i] );
                    }
                }
                break;
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            break;
        }
    }

    private void retryInternet() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.layout_nointernet);
        Button btnRetryinternet = (Button) dialog.findViewById(R.id.btnRetryinternet);
        btnRetryinternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (global.isNetworkAvailable()) {
                    dialog.dismiss();
                    new addressCompany().execute();
                } else {
                    Toast.makeText(getActivity(), R.string.nonetwork, Toast.LENGTH_SHORT).show();

                }
            }
        });
        dialog.show();
    }

    private class addressCompany extends AsyncTask<String, Void, String> {

        JSONObject jsonObjectList;
        String resMessage = "";
        String resCode = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.loading));
        }

        @Override
        protected String doInBackground(String... strings) {

            String strAddress = Constant.BASE_URL + Constant.CONTACT;
            Log.d("strAboutUs", strAddress);

            try {
                RestClient restClient = new RestClient(strAddress);
                try {
                    restClient.Execute(RequestMethod.GET);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                String AddUS = restClient.getResponse();
                Log.e("AddUS", AddUS);

                if (AddUS != null && AddUS.length() != 0) {
                    jsonObjectList = new JSONObject(AddUS);

                    if (jsonObjectList.length() != 0) {

                        resMessage = jsonObjectList.getString("message");
                        resCode = jsonObjectList.getString("msgcode");

                        if (resCode.equalsIgnoreCase("0")) {
                            try {
                                JSONArray jsonArray = jsonObjectList.getJSONArray("contact");
                                {
                                    if (jsonArray != null && jsonArray.length() != 0) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                                        company = jsonObject.getString("company");
                                        text = jsonObject.getString("text");
                                        address1 = jsonObject.getString("address1");
                                        address2 = jsonObject.getString("address2");
                                        address3 = jsonObject.getString("address3");
                                        address4 = jsonObject.getString("address4");
                                        website = jsonObject.getString("website");
                                        mobile = jsonObject.getString("contact");
                                        callus = jsonObject.getString("call_us");

                                        resfacebook_link = jsonObject.getString("facebook_link");
                                        resgoogle_link = jsonObject.getString("google_link");
                                        reslinkdin_link = jsonObject.getString("linkdin_link");
                                        restwitter_link = jsonObject.getString("twitter_link");
                                        resinsta_link = jsonObject.getString("insta_link");

                                        latitude= jsonObject.getString("latitude");
                                        logitude= jsonObject.getString("logitude");
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }

                        }else {
                            return null;
                        }
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

                    mainRel.setVisibility(View.VISIBLE);
                    CallFAB.setVisibility(View.VISIBLE);

                    tvAddone.setText(address1);
                    tvAddtwo.setText(address2);
                    tvAddthree.setText(address3);
                    tvAddfour.setText(address4);
                    tvAddwebsite.setText(website);
                    tvAddphone.setText(mobile);
                    tvCompanytext.setText(text);
                    tvCompany.setText(company);




                    if (resfacebook_link.equalsIgnoreCase("")) {
                        viewfb.setVisibility(View.GONE);
                    } else {
                        viewfb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Uri uri = Uri.parse(resfacebook_link);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    if (resgoogle_link.equalsIgnoreCase("")) {
                        viewgplus.setVisibility(View.GONE);
                    } else {
                        viewgplus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Uri uri = Uri.parse(resgoogle_link);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }


                    if (reslinkdin_link.equalsIgnoreCase("")) {
                        viewin.setVisibility(View.GONE);
                    } else {
                        viewin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Uri uri = Uri.parse(reslinkdin_link);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    if (restwitter_link.equalsIgnoreCase("")) {
                        viewtwitter.setVisibility(View.GONE);
                    } else {
                        viewtwitter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Uri uri = Uri.parse(restwitter_link);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    if (resinsta_link.equalsIgnoreCase("")) {
                        viewinstagram.setVisibility(View.GONE);
                    } else {
                        viewinstagram.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Uri uri = Uri.parse(resinsta_link);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                }else {
                    mainRel.setVisibility(View.GONE);
                    CallFAB.setVisibility(View.GONE);
                }
            }
        }


    }
}
