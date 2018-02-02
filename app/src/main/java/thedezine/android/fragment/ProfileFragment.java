package thedezine.android.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thedezine.android.R;

import thedezine.android.adapter.ProfileAdapter;

import thedezine.android.model.ProfileData;

import thedezine.android.utils.Constant;
import thedezine.android.utils.Global;
import thedezine.android.utils.RequestMethod;
import thedezine.android.utils.RestClient;

public class ProfileFragment extends Fragment {

    Global global;
  //  ProgressDialog progressDialog;

    RecyclerView profileRecycler;
    ProfileAdapter adapter;
    List<ProfileData> profileDatas;
    private LinearLayoutManager mLinearLayoutManager;

    boolean IsLAstLoading = true;
    Boolean isRef = false;
    int pageCode = 0;
    int curSIZE = 0;
    private ProgressBar progressBar1;
    FloatingActionButton teamFAB;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileRecycler = (RecyclerView) view.findViewById(R.id.profileRecycler);
        global = new Global(getActivity());
        progressBar1 = (ProgressBar) view.findViewById(R.id.progressbar1);
        teamFAB=(FloatingActionButton)view.findViewById(R.id.teamFAB);

        profileDatas = new ArrayList<>();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        profileRecycler.setLayoutManager(mLinearLayoutManager);
        profileRecycler.setHasFixedSize(true);
        adapter = new ProfileAdapter(getActivity(), profileDatas);
        profileRecycler.setAdapter(adapter);
        //adapter.notifyDataSetChanged();


        if (global.isNetworkAvailable()) {

            new profileFetch().execute();

        } else {
            retryInternet();
        }
        teamFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ChatFragment.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        profileRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = mLinearLayoutManager.getChildCount();
                    int totalItemCount = mLinearLayoutManager.getItemCount();
                    int pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();

                    if (IsLAstLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount &&
                                recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom() <= recyclerView.getHeight()) {

                            if (global.isNetworkAvailable()) {

                                IsLAstLoading = false;
                                progressBar1.setVisibility(View.VISIBLE);
                                pageCode++;
                                curSIZE = adapter.getItemCount();
                                new profileFetch().execute();

                            } else {
                                Toast.makeText(getActivity(), R.string.noconnection, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
            }
        });

        adapter.setOnItemClickListener(new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (view.getId()==R.id.callBTN1){
                    Log.e("lcfkllk",profileDatas.get(position).getEmail());
                }

            }
        });

    }


    private class profileFetch extends AsyncTask<String, Void, String> {

        JSONObject jsonObjectList;
        String resMessage = "";
        String resCode = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (IsLAstLoading && !isRef) {
               /* progressDialog = new ProgressDialog(getActivity());
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getString(R.string.loading));*/
                progressBar1.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            String strProfiles = Constant.BASE_URL + Constant.TEAM + pageCode;
            Log.d("strProfiles", strProfiles);

            try {
                RestClient restClient = new RestClient(strProfiles);
                try {
                    restClient.Execute(RequestMethod.GET);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                String ProfDATA = restClient.getResponse();
                Log.e("ProfDATA", ProfDATA);
                return ProfDATA;

            } catch (Exception r) {
                r.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {
                    if (s.length() != 0) {
                        jsonObjectList = new JSONObject(s);
                        if (jsonObjectList.length() != 0) {
                            resMessage = jsonObjectList.getString("message");
                            resCode = jsonObjectList.getString("msgcode");

                            if (resCode.equalsIgnoreCase("0")) {
                                try {
                                    JSONArray jsonArray = jsonObjectList.getJSONArray("list");
                                    if (jsonArray != null && jsonArray.length() != 0) {

                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(j);

                                            ProfileData pd = new ProfileData(jsonObject.getString("ID"),
                                                    jsonObject.getString("image"), jsonObject.getString("name"),
                                                    jsonObject.getString("desi"), jsonObject.getString("fb"),
                                                    jsonObject.getString("email"));

                                            profileDatas.add(pd);
                                        }

                                    }
                                    if (profileDatas.size() == 0) {

                                        progressBar1.setVisibility(View.GONE);
                                        IsLAstLoading = false;

                                    } else {

                                        if (pageCode == 0) {
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            adapter.notifyItemRangeInserted(curSIZE, profileDatas.size() - 1);
                                            profileRecycler.scrollToPosition(curSIZE);
                                        }
                                        progressBar1.setVisibility(View.GONE);
                                        IsLAstLoading = true;

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (resCode.equalsIgnoreCase("1")) {

                                if (pageCode > 0) {
                                    pageCode = pageCode - 1;
                                }
                                IsLAstLoading = true;
                                adapter.notifyDataSetChanged();
                                progressBar1.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), resMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.noconnection, Toast.LENGTH_SHORT).show();
                }


            }else {
                Toast.makeText(getActivity(), R.string.noconnection, Toast.LENGTH_SHORT).show();
            }

            if (pageCode == 0) {
                /*if (progressDialog.isShowing() && progressDialog != null) {
                    progressDialog.dismiss();
                }*/

                if (progressBar1!=null)
                progressBar1.setVisibility(View.GONE);
            }
        }
    }


    public void retryInternet() {
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
                    new profileFetch().execute();
                } else {
                    Toast.makeText(getActivity(), R.string.nonetwork, Toast.LENGTH_SHORT).show();

                }
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Our Team");
        }
    }
}
