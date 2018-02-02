package thedezine.android.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thedezine.android.R;
import thedezine.android.activity.PortifolioDetailsActivity;
import thedezine.android.adapter.ClientAdapter;
import thedezine.android.adapter.PortfolioAdapter;
import thedezine.android.model.ClientModel;
import thedezine.android.model.ClientTag;
import thedezine.android.model.PortfolioModel;
import thedezine.android.model.Project;
import thedezine.android.model.Tag;
import thedezine.android.utils.Constant;
import thedezine.android.utils.Global;
import thedezine.android.utils.RequestMethod;
import thedezine.android.utils.RestClient;
import thedezine.android.utils.TagGroup;

public class ClientFragment extends Fragment {

    Global global;
    RecyclerView clientRecycle;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayoutManager mLinearLayoutManager;
  //  ProgressDialog progressDialog;
    ClientAdapter adapter;
    LinearLayout lyt_not_found;
    FloatingActionMenu fab;
    boolean IsLAstLoading = true;
    Boolean isRef = false;
    int pageCode = 0;
    int curSIZE = 0;
    ProgressBar progressbar1;

    private List<ClientModel> clientModels = new ArrayList<>();
    List<Tag> tags = new ArrayList<>();
    FloatingActionButton clentFAB;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clients, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        global = new Global(getActivity());
        lyt_not_found=(LinearLayout)view.findViewById(R.id.lyt_not_found);
        progressbar1 = (ProgressBar) view.findViewById(R.id.progressbar1);
        clientRecycle = (RecyclerView) view.findViewById(R.id.clientRecycle);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        clentFAB= (FloatingActionButton) view.findViewById(R.id.clentFAB);

        clientRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0)
                {
                    int visibleItemCount = mLinearLayoutManager.getChildCount();
                    int totalItemCount = mLinearLayoutManager.getItemCount();
                    int pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();

                    if (IsLAstLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount &&
                                recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom() <= recyclerView.getHeight()) {

                            if (global.isNetworkAvailable()) {

                                IsLAstLoading = false;
                                pageCode++;
                                curSIZE = adapter.getItemCount();

                                clientModels.add(null);
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyItemInserted(curSIZE);
                                        clientRecycle.scrollToPosition(curSIZE);
                                    }
                                });

                                new clientFetchData().execute();

                            } else {

                            }

                        }
                    }
                }
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeContainer.setRefreshing(true);
                (new Handler()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (global.isNetworkAvailable()) {
                            isRef = true;
                            pageCode = 0;
                            new clientFetchData().execute();
                        } else {

                        }
                    }
                });
            }
        });


clentFAB.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), ChatFragment.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
});
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        clientRecycle.setLayoutManager(mLinearLayoutManager);
        clientRecycle.setHasFixedSize(true);
        adapter = new ClientAdapter(getActivity(), clientModels);
        //clientRecycle.setItemAnimator(new DefaultItemAnimator());
        clientRecycle.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (global.isNetworkAvailable()) {
            new clientFetchData().execute();
        } else {
            retryInternet();
        }

    }

    private class clientFetchData extends AsyncTask<String, Void, String> {

        JSONObject jsonObjectList;
        String resMessage = "";
        String resCode = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (IsLAstLoading && !isRef) {
                /*progressDialog = new ProgressDialog(getActivity());
                progressDialog.show();
                progressDialog.setMessage(getString(R.string.loading));*/
                progressbar1.setVisibility(View.VISIBLE);
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String strClientList = Constant.BASE_URL + Constant.CLIENT + pageCode;

            Log.d("strClientList", strClientList);

            try {
                RestClient restClient = new RestClient(strClientList);
                try {
                    restClient.Execute(RequestMethod.GET);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                String Clietn = restClient.getResponse();
                Log.e("Clietn", Clietn);

                if (Clietn != null && Clietn.length() != 0) {
                    jsonObjectList = new JSONObject(Clietn);

                    if (jsonObjectList.length() != 0) {
                        resMessage = jsonObjectList.getString("message");
                        resCode = jsonObjectList.getString("msgcode");

                        if (resCode.equalsIgnoreCase("0")) {
                            try {
                                JSONArray jsonArray = jsonObjectList.getJSONArray("client_list");
                                {
                                    if (jsonArray != null && jsonArray.length() != 0) {

                                        if (isRef) {
                                            clientModels.clear();
                                        }
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(j);

                                            ClientModel cm = new ClientModel();

                                            cm.setID(jsonObject.getString("ID"));
                                            cm.setImage(jsonObject.getString("image"));
                                            cm.setName(jsonObject.getString("name"));
                                            cm.setDesc(jsonObject.getString("desc"));

                                            JSONArray jsonArray2 = jsonObject.getJSONArray("tag");

                                            if (jsonArray2 != null && jsonArray2.length() != 0) {
                                                tags = new ArrayList<>();
                                                for (int k = 0; k < jsonArray2.length(); k++) {
                                                    JSONObject jsonObject2 = jsonArray2.getJSONObject(k);

                                                    Tag tg = new Tag();
                                                    tg.setPlatform(jsonObject2.getString("title"));
                                                    tg.setUrl(jsonObject2.getString("link"));
                                                    tags.add(tg);
                                                }

                                                cm.setTag(tags);
                                            }
                                            clientModels.add(cm);
                                        }


                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            swipeContainer.setRefreshing(false);


            if (resCode.equalsIgnoreCase("0")) {


                if (clientModels.size() == 0) {

                    lyt_not_found.setVisibility(View.VISIBLE);
                    //  progressBar1.setVisibility(View.GONE);
                    IsLAstLoading = false;
                } else {
                    lyt_not_found.setVisibility(View.GONE);
                    if (isRef) {
                        clientRecycle.removeAllViews();
                        clientRecycle.removeAllViewsInLayout();
                        clientRecycle.clearAnimation();
                    }
                    if (pageCode == 0) {
                        adapter.notifyDataSetChanged();
                    } else {

                        Log.e("botombar","yes\n"+clientModels.size()+"\n"+curSIZE);

                        clientModels.remove(curSIZE);
                        adapter.notifyItemRemoved(curSIZE);

                        adapter.notifyItemRangeInserted(curSIZE, clientModels.size() - 1);
                        clientRecycle.scrollToPosition(curSIZE);
                    }
                    // progressBar1.setVisibility(View.GONE);
                    IsLAstLoading = true;
                }

            } else if (resCode.equalsIgnoreCase("1")) {
                if (pageCode>0){
                    pageCode=pageCode-1;

                    Log.e("botombar","yes\n"+clientModels.size()+"\n"+curSIZE);

                    clientModels.remove(clientModels.size() - 1);
                    adapter.notifyItemRemoved(clientModels.size());
                    lyt_not_found.setVisibility(View.GONE);
                }else {
                    lyt_not_found.setVisibility(View.VISIBLE);
                }
                IsLAstLoading = true;
                adapter.notifyDataSetChanged();
                // progressBar1.setVisibility(View.GONE);
                Toast.makeText(getActivity(), resMessage, Toast.LENGTH_SHORT).show();
            }
            isRef = false;
            if (pageCode == 0) {
               /* if (progressDialog.isShowing() && progressDialog != null) {
                    progressDialog.dismiss();
                }*/
                if (progressbar1!=null)
                progressbar1.setVisibility(View.GONE);
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
                    new clientFetchData().execute();
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
            actionBar.setTitle("Clients");
        }
    }

}
