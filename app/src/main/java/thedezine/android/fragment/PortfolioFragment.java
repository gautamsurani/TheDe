package thedezine.android.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArraySet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import thedezine.android.R;

import thedezine.android.activity.PortifolioDetailsActivity;
import thedezine.android.adapter.PortfolioAdapter;
import thedezine.android.model.PortfolioModel;
import thedezine.android.model.Tag;
import thedezine.android.utils.Constant;
import thedezine.android.utils.Global;
import thedezine.android.utils.RequestMethod;
import thedezine.android.utils.RestClient;


public class PortfolioFragment extends Fragment {


    Global global;
    RecyclerView portfolioRecycle;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayoutManager mLinearLayoutManager;
    ProgressDialog progressDialog;
    PortfolioAdapter adapter;
    LinearLayout lyt_not_found;
    FloatingActionMenu fab;
    String[] fabSublist = new String[5];
    int[] fabSubicon = new int[5];
    String typePortfolio = "";
    SharedPreferences mprefs;
    boolean IsLAstLoading = true;
    Boolean isRef = false;
    int pageCode = 0;
    int curSIZE = 0;
    List<PortfolioModel> portfolioModels = new ArrayList<>();
    List<Tag> tags = new ArrayList<>();
    private ProgressBar progressBar1;

    StringBuilder otherIDS;
    StringBuilder likedIDS;
    SharedPreferences.Editor editor;
    String[] likIds=new String[100];
    String sIDS;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_portifolio, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        global = new Global(getActivity());
        mprefs = getActivity().getSharedPreferences(Constant.PREFS_NAME,Context.MODE_PRIVATE);
        editor= mprefs.edit();

        sIDS=mprefs.getString("sIDS","");
        Log.d("idsids",sIDS+"k");

        if (sIDS.length()>0){
            Log.d("idsids",sIDS+"k");
            likIds= sIDS.split(",");
        }
        //

        //  setUpBackPressed();
        fab = (FloatingActionMenu) view.findViewById(R.id.fab);
        lyt_not_found=(LinearLayout)view.findViewById(R.id.lyt_not_found);
        portfolioRecycle = (RecyclerView) view.findViewById(R.id.portfolioRecycle);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        progressBar1 = (ProgressBar) view.findViewById(R.id.progressbar1);


     //   fab.setDimColor(ContextCompat.getColor(getActivity(), R.color.grey_soft));
      //  fab.setMenuButtonColorNormal(ContextCompat.getColor(getActivity(), R.color.fab_color_normal));
       // fab.setMenuButtonColorPressed(ContextCompat.getColor(getActivity(), R.color.fab_color_pressed));
        fab.setClosedOnTouchOutside(true);

        fabSublist[0] = "Website";
        fabSublist[1] = "Logo";
        fabSublist[2] = "Android";
        fabSublist[3] = "Iphone";
        fabSublist[4] = "Custom";

        fabSubicon[0]=R.drawable.ic_fab_web;
        fabSubicon[1]=R.drawable.ic_fab_web;
        fabSubicon[2]=R.drawable.ic_fab_android;
        fabSubicon[3]=R.drawable.ic_fab_ios;
        fabSubicon[4]=R.drawable.ic_fab_ui_ux;

        for (int i = 0; i < 5; i++) {

            final FloatingActionButton fabButton = new FloatingActionButton(getActivity());
            fabButton.setButtonSize(FloatingActionButton.SIZE_MINI);
            fabButton.setLabelText(fabSublist[i]);
            fabButton.setImageResource(fabSubicon[i]);
            fabButton.setColorNormal(R.color.fab_color_normal);
            fabButton.setColorRipple(R.color.White);
            fabButton.setColorPressed(R.color.fab_color_normal);
            fabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFilter(fabButton.getLabelText());
                    fab.toggle(true);
                }
            });
            fab.addMenuButton(fabButton);

        }

        portfolioRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) //check for scroll down
                {
                    int visibleItemCount = mLinearLayoutManager.getChildCount();
                    int totalItemCount = mLinearLayoutManager.getItemCount();
                    int pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();

                    if (IsLAstLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount &&
                                recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom() <= recyclerView.getHeight()) {

                            if (global.isNetworkAvailable()) {

                                IsLAstLoading = false;
                              //  progressBar1.setVisibility(View.VISIBLE);
                                pageCode++;
                                curSIZE = adapter.getItemCount();

                                portfolioModels.add(null);
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyItemInserted(curSIZE);
                                        portfolioRecycle.scrollToPosition(curSIZE);
                                    }
                                });

                                new portfoliaData().execute();

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

                // global.hideKeyboard(Updates.this);

                swipeContainer.setRefreshing(true);
                (new Handler()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (global.isNetworkAvailable()) {
                            isRef = true;
                            pageCode = 0;
                            new portfoliaData().execute();
                        } else {
                            //  Utils.showToastShort("Please Check Your Internet Connection", Updates.this);
                        }
                    }
                });
            }
        });

        /*portfolioRecycle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE) {
                   // fab.setEnable(false);
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                  //  fab.setEnable(true);
                }
                return false;
            }
        });*/

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        portfolioRecycle.setLayoutManager(mLinearLayoutManager);
        portfolioRecycle.setHasFixedSize(true);
        adapter = new PortfolioAdapter(getActivity(), portfolioModels);
       // portfolioRecycle.setItemAnimator(new DefaultItemAnimator());
        portfolioRecycle.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (global.isNetworkAvailable()) {
            new portfoliaData().execute();
        } else {
            retryInternet();
        }

        adapter.setOnItemClickListener(new PortfolioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (view.getId()==R.id.likeImageView){




                }else {

                    Intent i = new Intent(getActivity(), PortifolioDetailsActivity.class);
                    i.putExtra("portfolioTitle",portfolioModels.get(position).getName());
                    i.putExtra("portfolioImage",portfolioModels.get(position).getImage());
                    i.putExtra("portfolioDesc",portfolioModels.get(position).getDesc());
                    startActivity(i);

                }

            }
        });
    }

    private void setFilter(final String labelText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("typetype","");
                portfolioModels.clear();
                adapter.notifyDataSetChanged();

                pageCode = 0;
                if (labelText.equalsIgnoreCase("Website")) {
                    typePortfolio = "Website";
                    new portfoliaData().execute();
                } else if (labelText.equalsIgnoreCase("Logo")) {
                    typePortfolio = "Logo";
                    new portfoliaData().execute();
                } else if (labelText.equalsIgnoreCase("Android")) {
                    typePortfolio = "Android";
                    new portfoliaData().execute();
                } else if (labelText.equalsIgnoreCase("Iphone")) {
                    typePortfolio = "Iphone";
                    new portfoliaData().execute();
                } else if (labelText.equalsIgnoreCase("Custom")) {
                    typePortfolio = "Custom";
                    new portfoliaData().execute();
                }

                if (portfolioModels.size() > 0) {
                    portfolioRecycle.scrollToPosition(0);
                }

            }
        }, 500);
    }

    /*private void setUpBackPressed() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (fab.isOpened()) {
                            fab.toggle(true);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
                return false;
            }
        });
    }
*/
    private class portfoliaData extends AsyncTask<String, Void, String> {

        JSONObject jsonObjectList;
        String resMessage = "";
        String resCode = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (IsLAstLoading && !isRef) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.show();
                progressDialog.setMessage(getString(R.string.loading));
            }
           /* if (!isRef){
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.show();
                progressDialog.setMessage(getString(R.string.loading));
            }*/
        }


        @Override
        protected String doInBackground(String... params) {

            String strPortfolio = Constant.BASE_URL + Constant.PORTFOLIO + pageCode
                    + "&type=" + typePortfolio;

            Log.d("strPortfolio", strPortfolio);

            try {
                RestClient restClient = new RestClient(strPortfolio);
                try {
                    restClient.Execute(RequestMethod.GET);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                String PortUS = restClient.getResponse();
                Log.e("PortUS", PortUS);

                if (PortUS != null && PortUS.length() != 0) {
                    jsonObjectList = new JSONObject(PortUS);

                    if (jsonObjectList.length() != 0) {
                        resMessage = jsonObjectList.getString("message");
                        resCode = jsonObjectList.getString("msgcode");

                        if (resCode.equalsIgnoreCase("0")) {
                            try {
                                JSONArray jsonArray = jsonObjectList.getJSONArray("list");
                                {
                                    if (jsonArray != null && jsonArray.length() != 0) {

                                        if (isRef) {
                                            portfolioModels.clear();
                                        }
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(j);

                                            PortfolioModel pm = new PortfolioModel();

                                            pm.setID(jsonObject.getString("ID"));
                                            pm.setImage(jsonObject.getString("image"));
                                            pm.setName(jsonObject.getString("name"));
                                            pm.setDesc(jsonObject.getString("desc"));

                                            if (sIDS.length()>0){
                                                for (int jj=0;jj<likIds.length;jj++){
                                                    if (Integer.parseInt(pm.getID())==Integer.parseInt(likIds[jj])){
                                                        pm.setIsLiked(1);
                                                    }
                                                }
                                            }
                                           /* if (likedList.contains(jsonObject.getString("ID"))){
                                                pm.setIsLiked(1);
                                            }else {
                                                pm.setIsLiked(0);
                                            }
*/
                                            JSONArray jsonArray2 = jsonObject.getJSONArray("tag");

                                            if (jsonArray2 != null && jsonArray2.length() != 0) {
                                                tags = new ArrayList<>();
                                                for (int k = 0; k < jsonArray2.length(); k++) {
                                                    JSONObject jsonObject2 = jsonArray2.getJSONObject(k);

                                                    Tag tg = new Tag();
                                                    tg.setPlatform(jsonObject2.getString("title"));
                                                    tags.add(tg);
                                                }

                                                pm.setTag(tags);
                                            }
                                            portfolioModels.add(pm);
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


                if (portfolioModels.size() == 0) {

                    lyt_not_found.setVisibility(View.VISIBLE);
                  //  progressBar1.setVisibility(View.GONE);
                    IsLAstLoading = false;
                } else {
                    lyt_not_found.setVisibility(View.GONE);
                    if (isRef) {
                        portfolioRecycle.removeAllViews();
                        portfolioRecycle.removeAllViewsInLayout();
                        portfolioRecycle.clearAnimation();
                    }
                    if (pageCode == 0) {
                        adapter.notifyDataSetChanged();
                    } else {

                        Log.e("botombar","yes\n"+portfolioModels.size()+"\n"+curSIZE);

                        portfolioModels.remove(curSIZE);
                        adapter.notifyItemRemoved(curSIZE);

                        adapter.notifyItemRangeInserted(curSIZE, portfolioModels.size() - 1);
                        portfolioRecycle.scrollToPosition(curSIZE);
                    }
                   // progressBar1.setVisibility(View.GONE);
                    IsLAstLoading = true;
                }

            } else if (resCode.equalsIgnoreCase("1")) {
                if (pageCode>0){
                    pageCode=pageCode-1;

                    Log.e("botombar","yes\n"+portfolioModels.size()+"\n"+curSIZE);

                    portfolioModels.remove(portfolioModels.size() - 1);
                    adapter.notifyItemRemoved(portfolioModels.size());
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
                if (progressDialog.isShowing() && progressDialog != null) {
                    progressDialog.dismiss();
                }
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
                    new portfoliaData().execute();
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
            actionBar.setTitle("Portfolio");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        otherIDS=new StringBuilder();
        likedIDS=new StringBuilder();
        for (int j=0;j<PortfolioAdapter.portfolioModels.size();j++){
            if (PortfolioAdapter.portfolioModels.get(j).getIsLiked()==1){
                likedIDS.append(PortfolioAdapter.portfolioModels.get(j).getID()).append(",");
               // likedIDS.append(j).append(",");
            }
        }
        editor.putString("sIDS", String.valueOf(likedIDS)).apply();
        Log.d("allSelected", String.valueOf(likedIDS));

    }
}
