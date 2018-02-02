package thedezine.android.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thedezine.android.R;
import thedezine.android.adapter.AllServiceAdapter;
import thedezine.android.adapter.ServiceAdapter;
import thedezine.android.model.ClientModel;
import thedezine.android.model.ServiceModel;
import thedezine.android.model.Tag;
import thedezine.android.utils.Constant;
import thedezine.android.utils.Global;
import thedezine.android.utils.RequestMethod;
import thedezine.android.utils.RestClient;


public class ServiceFragment extends Fragment {

    Global global;
    RecyclerView serviceRecycle;
    GridLayoutManager gridLayoutManager;
    AllServiceAdapter adapter;
    LinearLayout lyt_not_found;
    ProgressBar progressbar1;
    List<ServiceModel> serviceModels;

    boolean IsLAstLoading = true;
    int pageCode = 0;
    int curSIZE = 0;
    FloatingActionButton serviceFAB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.service_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        global = new Global(getActivity());
        progressbar1 = (ProgressBar) view.findViewById(R.id.progressbar1);
        serviceRecycle = (RecyclerView) view.findViewById(R.id.serviceRecycle);
        lyt_not_found = (LinearLayout) view.findViewById(R.id.lyt_not_found);

        serviceFAB= (FloatingActionButton) view.findViewById(R.id.serviceFAB);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
     //   serviceRecycle.addItemDecoration(new SpacesItemDecoration(1));
        serviceRecycle.setHasFixedSize(true);
        serviceModels = new ArrayList<>();
        adapter = new AllServiceAdapter(getActivity(), serviceModels);

        serviceRecycle.setAdapter(adapter);

        serviceRecycle.setLayoutManager(gridLayoutManager);

        if (global.isNetworkAvailable()) {
            progressbar1.setVisibility(View.VISIBLE);
            new servicesFetch().execute();
        } else {
            retryInternet();
        }
        serviceFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ChatFragment.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        /*serviceRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (IsLAstLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount &&
                                recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom() <= recyclerView.getHeight()) {

                            if (global.isNetworkAvailable()) {

                                IsLAstLoading = false;
                                pageCode++;
                                curSIZE = adapter.getItemCount();

                                serviceModels.add(null);
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyItemInserted(curSIZE);
                                        serviceRecycle.scrollToPosition(curSIZE);
                                    }
                                });

                                new servicesFetch().execute();

                            } else {
                                Toast.makeText(getActivity(), R.string.noconnection, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
            }
        });*/

    }

    public static class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        @SuppressWarnings("deprecation")
        public SimpleDividerItemDecoration(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int mSpace;
        SpacesItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = mSpace;
        }
    }

    private class servicesFetch extends AsyncTask<String, Void, String> {

        JSONObject jsonObjectList;
        String resMessage = "";
        String resCode = "";


        @Override
        protected String doInBackground(String... strings) {

            String strServices = Constant.BASE_URL + Constant.SERVICES;
            Log.d("strServices", strServices);

            try {
                RestClient restClient = new RestClient(strServices);
                try {
                    restClient.Execute(RequestMethod.GET);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                String Sers = restClient.getResponse();
                Log.e("Sers", Sers);

                if (Sers != null && Sers.length() != 0) {
                    jsonObjectList = new JSONObject(Sers);

                    if (jsonObjectList.length() != 0) {
                        resMessage = jsonObjectList.getString("message");
                        resCode = jsonObjectList.getString("msgcode");

                        if (resCode.equalsIgnoreCase("0")) {
                            try {

                                JSONArray jsonArray = jsonObjectList.getJSONArray("services_list");
                                {
                                    if (jsonArray != null && jsonArray.length() != 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            serviceModels.add(new ServiceModel(jsonObject.getString("ID"),
                                                    jsonObject.getString("title"),
                                                    jsonObject.getString("icon"),
                                                    jsonObject.getString("color")));

                                        }
                                    }
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
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar1.setVisibility(View.GONE);

            if (s != null) {

                if (serviceModels.size() > 0) {
                    adapter.notifyDataSetChanged();
                    lyt_not_found.setVisibility(View.GONE);
                    serviceRecycle.setVisibility(View.VISIBLE);

                } else {
                    serviceRecycle.setVisibility(View.GONE);
                    lyt_not_found.setVisibility(View.VISIBLE);
                }

            }else {
                serviceRecycle.setVisibility(View.GONE);

                Toast.makeText(getActivity(), R.string.noconnection, Toast.LENGTH_SHORT).show();
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
                    new servicesFetch().execute();
                } else {
                    Toast.makeText(getActivity(), R.string.nonetwork, Toast.LENGTH_SHORT).show();

                }
            }
        });
        dialog.show();
    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        public static final int LIST_HORIZONTAL = LinearLayoutManager.HORIZONTAL;

        public static final int LIST_VERTICAL = LinearLayoutManager.VERTICAL;

        public static final int GRID_STROKE = 3;

        public static final int GRID_FILL = 4;


        private Drawable mDivider;

        private int mOrientation;

        public DividerItemDecoration(Context context, int orientation) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
            setOrientation(orientation);
        }

        public void setOrientation(int orientation) {
            if (orientation != LIST_HORIZONTAL &&
                    orientation != LIST_VERTICAL &&
                    orientation != GRID_STROKE &&
                    orientation != GRID_FILL) {
                throw new IllegalArgumentException("invalid orientation");
            }
            mOrientation = orientation;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent) {
            switch (mOrientation) {
                case LIST_VERTICAL:
                    drawVertical(c, parent);
                    break;
                case LIST_HORIZONTAL:
                    drawHorizontal(c, parent);
                    break;
                case GRID_FILL:
                    drawGridFill(c, parent);
                    break;
                case GRID_STROKE:
                    drawGridStroke(c, parent);
                    break;
                default:
                    throw new IllegalArgumentException("invalid orientation");
            }
        }

        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        public void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        public void drawGridStroke(Canvas c, RecyclerView parent) {
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getLeft() - params.leftMargin;
                final int right = child.getRight() + params.rightMargin;
                final int top = child.getTop() - params.topMargin;
                final int bottom = child.getBottom() + params.bottomMargin;
                mDivider.setBounds(left, top, left + mDivider.getIntrinsicHeight(), bottom);
                mDivider.draw(c);
                mDivider.setBounds(right - mDivider.getIntrinsicHeight(), top, right, bottom);
                mDivider.draw(c);
                mDivider.setBounds(left, top, right, top + mDivider.getIntrinsicHeight());
                mDivider.draw(c);
                mDivider.setBounds(left, bottom - mDivider.getIntrinsicHeight(), right, bottom);
                mDivider.draw(c);
                mDivider.draw(c);
            }
        }

        public void drawGridFill(Canvas c, RecyclerView parent) {
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getLeft() - params.leftMargin;
                final int right = child.getRight() + params.rightMargin;
                final int top = child.getTop() - params.topMargin;
                final int bottom = child.getBottom() + params.bottomMargin;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {

            switch (mOrientation) {
                case LIST_VERTICAL:
                    outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
                    break;
                case LIST_HORIZONTAL:
                    outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
                    break;
                case GRID_FILL:
                    outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicWidth(), mDivider.getIntrinsicWidth(), mDivider.getIntrinsicWidth());
                    break;
                case GRID_STROKE:
                    outRect.set(0, 0, 0, 0);
                    break;
                default:
                    throw new IllegalArgumentException("invalid orientation");
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Services");
        }
    }
}
