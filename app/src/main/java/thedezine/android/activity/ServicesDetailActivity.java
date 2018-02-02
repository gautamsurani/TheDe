package thedezine.android.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thedezine.android.R;
import thedezine.android.adapter.AllServiceAdapter;
import thedezine.android.adapter.ServiceDetailAdapter;
import thedezine.android.fragment.ServiceFragment;
import thedezine.android.model.ServiceDetailsModel;
import thedezine.android.model.ServiceModel;
import thedezine.android.model.TechList;
import thedezine.android.utils.Constant;
import thedezine.android.utils.Global;
import thedezine.android.utils.RequestMethod;
import thedezine.android.utils.RestClient;


public class ServicesDetailActivity extends AppCompatActivity {

    RecyclerView serviceDetailRecycle;
    TextView tvServiceDetails;
    ImageView imgServiceDetails;
    Global global;
    GridLayoutManager gridLayoutManager;
    ProgressBar progressbar1;
    String sID = "";
    String title = "";

    String sdImage = "", sdText = "", SDTECH = "";
    LinearLayout lyt_not_found;
    ServiceDetailAdapter adapter;
    List<TechList> techList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servicesdetail_view);
        global = new Global(this);
        progressbar1 = (ProgressBar) findViewById(R.id.progressbar1);
        tvServiceDetails = (TextView) findViewById(R.id.tvServiceDetails);
        imgServiceDetails = (ImageView) findViewById(R.id.imgServiceDetails);
        serviceDetailRecycle = (RecyclerView) findViewById(R.id.serviceDetailRecycle);
        lyt_not_found = (LinearLayout) findViewById(R.id.lyt_not_found);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        techList = new ArrayList<>();

        gridLayoutManager = new GridLayoutManager(this, 3);
        serviceDetailRecycle.setHasFixedSize(true);
        adapter = new ServiceDetailAdapter(this, techList);
        serviceDetailRecycle.setAdapter(adapter);
        serviceDetailRecycle.setLayoutManager(gridLayoutManager);


        Bundle b = getIntent().getExtras();
        if (b != null) {
            sID = b.getString("sid");
            title = b.getString("title");
            toolbar.setTitle(title);

            if (global.isNetworkAvailable()) {
                progressbar1.setVisibility(View.VISIBLE);
                new serviceDetailsFetch().execute();
            } else {
                retryInternet();
            }

        } else {

        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    private class serviceDetailsFetch extends AsyncTask<String, Void, String> {

        JSONObject jsonObjectList;
        String resMessage = "";
        String resCode = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            String strServiceDetails = Constant.BASE_URL + Constant.SERVICES_DETAILS + sID;
            Log.d("strServiceDetails", strServiceDetails);

            try {
                RestClient restClient = new RestClient(strServiceDetails);
                try {
                    restClient.Execute(RequestMethod.GET);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                String SersDet = restClient.getResponse();
                Log.e("SersDet", SersDet);

                if (SersDet != null && SersDet.length() != 0) {
                    jsonObjectList = new JSONObject(SersDet);

                    if (jsonObjectList.length() != 0) {
                        resMessage = jsonObjectList.getString("message");
                        resCode = jsonObjectList.getString("msgcode");

                        if (resCode.equalsIgnoreCase("0")) {
                            try {

                                sdText = jsonObjectList.getString("text");
                                sdImage = jsonObjectList.getString("image");
                                SDTECH = jsonObjectList.getString("tech");

                                if (SDTECH.equalsIgnoreCase("Yes")) {

                                    JSONArray jsonArray = jsonObjectList.getJSONArray("tech_list");
                                    {
                                        if (jsonArray != null && jsonArray.length() != 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                TechList tl = new TechList();
                                                tl.setColor(jsonObject.getString("color"));
                                                tl.setIcon(jsonObject.getString("icon"));

                                                techList.add(tl);
                                            }
                                        }
                                    }

                                } else {

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
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
            progressbar1.setVisibility(View.GONE);

            if (s != null) {

                if (resCode.equalsIgnoreCase("0")){
                    tvServiceDetails.setText(Html.fromHtml(sdText));

                    Picasso.with(ServicesDetailActivity.this)
                            .load(sdImage)
                            .into(imgServiceDetails);

                }
                if (techList.size() > 0) {
                    adapter.notifyDataSetChanged();
                    lyt_not_found.setVisibility(View.GONE);
                    serviceDetailRecycle.setVisibility(View.VISIBLE);

                } else {
                    serviceDetailRecycle.setVisibility(View.GONE);
                    lyt_not_found.setVisibility(View.VISIBLE);
                }

            } else {
                serviceDetailRecycle.setVisibility(View.GONE);

                Toast.makeText(ServicesDetailActivity.this, R.string.noconnection, Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void retryInternet() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.layout_nointernet);
        Button btnRetryinternet = (Button) dialog.findViewById(R.id.btnRetryinternet);
        btnRetryinternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (global.isNetworkAvailable()) {
                    dialog.dismiss();
                    new serviceDetailsFetch().execute();
                } else {
                    Toast.makeText(ServicesDetailActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();

                }
            }
        });
        dialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            // Toast.makeText(MainCategoriesActivity.this, "BackWorking", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}
