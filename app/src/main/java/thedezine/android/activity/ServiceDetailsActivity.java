package thedezine.android.activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thedezine.android.R;
import thedezine.android.adapter.ServiceAdapter;
import thedezine.android.model.ServiceModel;
import thedezine.android.utils.Constant;
import thedezine.android.utils.Global;
import thedezine.android.utils.RequestMethod;
import thedezine.android.utils.RestClient;
import thedezine.android.utils.Tools;

public class ServiceDetailsActivity extends AppCompatActivity{

    Global global;
    RecyclerView serviceRecycle;
    GridLayoutManager gridLayoutManager;
    ServiceAdapter adapter;
    List<ServiceModel> serviceModels;
    private String serviceID="";
    Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    TextView recyclerTitle,serviceDetails;
    Context context;
    ProgressDialog progressDialog;
    FloatingActionButton sDetailsFAB;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servicedetails_view);
        context = this;
        Tools.systemBarLolipop(this);
        global = new Global(context);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        sDetailsFAB = (FloatingActionButton) findViewById(R.id.sDetailsFAB);
        serviceRecycle = (RecyclerView) findViewById(R.id.servicedetailRecycle);
        recyclerTitle=(TextView)findViewById(R.id.recyclerTitle);
        gridLayoutManager = new GridLayoutManager(this,3);
        serviceRecycle.setHasFixedSize(true);
        adapter = new ServiceAdapter(this, serviceModels);
        serviceRecycle.setAdapter(adapter);
        serviceRecycle.setLayoutManager(gridLayoutManager);
        initToolbar();

        Bundle b=getIntent().getExtras();
        if (b!=null){
            serviceID=b.getString("serviceID");
            if (global.isNetworkAvailable()) {
                new getServiceDetails().execute();
            } else {
                retryInternet();
            }


        }else {

        }



        adapter.setOnItemClickListener(new ServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int type) {

                Toast.makeText(ServiceDetailsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        sDetailsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServiceDetailsActivity.this, EnquiryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    private class getServiceDetails extends AsyncTask<String,Boolean,Boolean> {

        JSONObject jsonObjectList;
        String resMessage = "";
        String resCode = "";
        String resMainTitle = "";
        String resRecyclerTitle = "";
        String resMainDetails="";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setMessage(getString(R.string.loading));
        }

        @Override
        protected Boolean doInBackground(String... strings) {

           // serviceID

            String strSERDETAIL=""; /*= Constant.BASE_URL + Constant.HOME_PAGE + fcmTOKEN + Constant.DEVICE_ID
                    + deviceID + Constant.DEVICE_EMAIL + deviceEMAIL;*/
            Log.d("strSERDETAIL", strSERDETAIL);

            try {

                RestClient restClient = new RestClient(strSERDETAIL);
                try {
                    restClient.Execute(RequestMethod.GET);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                String ServiceDetails = restClient.getResponse();
                Log.e("SERDETAILS", ServiceDetails);

                if (ServiceDetails != null && ServiceDetails.length() != 0) {

                    jsonObjectList = new JSONObject(ServiceDetails);

                    if (jsonObjectList.length() != 0) {
                        resMessage = jsonObjectList.getString("message");
                        resCode = jsonObjectList.getString("msgcode");

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

                if (aBoolean){


                 //   serviceDetails.setText(Html.fromHtml());
                    if (serviceModels.size() > 0) {
                        recyclerTitle.setVisibility(View.VISIBLE);
                        //recyclerTitle.setText();
                        serviceRecycle.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();

                    } else {
                        recyclerTitle.setVisibility(View.GONE);
                        serviceRecycle.setVisibility(View.GONE);
                    }

                }else {

                }

            }
        }
    }




        private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void retryInternet() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.layout_nointernet);
        Button btnRetryinternet = (Button) dialog.findViewById(R.id.btnRetryinternet);
        btnRetryinternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (global.isNetworkAvailable()) {
                    dialog.dismiss();
                    new getServiceDetails().execute();
                } else {
                    Toast.makeText(context, R.string.nonetwork, Toast.LENGTH_SHORT).show();

                }
            }
        });
        dialog.show();
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

}
