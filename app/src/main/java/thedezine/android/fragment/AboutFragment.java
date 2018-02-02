package thedezine.android.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thedezine.android.R;
import thedezine.android.activity.EnquiryActivity;
import thedezine.android.activity.InquiryActivity;
import thedezine.android.adapter.ServiceAdapter;
import thedezine.android.model.ServiceModel;
import thedezine.android.utils.Constant;
import thedezine.android.utils.Global;
import thedezine.android.utils.RequestMethod;
import thedezine.android.utils.RestClient;


public class AboutFragment extends Fragment {

    Global global;
    RecyclerView serviceRecycle;
    GridLayoutManager gridLayoutManager;
    ServiceAdapter adapter;
    List<ServiceModel> serviceModels;
    ProgressDialog progressDialog;
    private ImageView imgAboutUsbanner;
    private TextView tvAboutusline;
    ImageView viewfb, viewgplus, viewin, viewinstagram, viewtwitter;
    FloatingActionButton aboutFAB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        global = new Global(getActivity());

        imgAboutUsbanner = (ImageView) view.findViewById(R.id.imgAboutUsbanner);
        tvAboutusline = (TextView) view.findViewById(R.id.tvAboutusline);
        viewfb = (ImageView) view.findViewById(R.id.viewfb);
        viewgplus = (ImageView) view.findViewById(R.id.viewgplus);
        viewin = (ImageView) view.findViewById(R.id.viewin);
        viewinstagram = (ImageView) view.findViewById(R.id.viewinstagram);
        viewtwitter = (ImageView) view.findViewById(R.id.viewtwitter);

        aboutFAB = (FloatingActionButton)view.findViewById(R.id.aboutFAB);
        serviceRecycle = (RecyclerView) view.findViewById(R.id.serviceRecycle);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        serviceRecycle.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.GRID_STROKE));
        serviceRecycle.setHasFixedSize(true);
        serviceModels = new ArrayList<>();
        serviceRecycle.setNestedScrollingEnabled(false);
        adapter = new ServiceAdapter(getActivity(), serviceModels);

        serviceRecycle.setAdapter(adapter);

        serviceRecycle.setLayoutManager(gridLayoutManager);

        if (global.isNetworkAvailable()) {
            new aboutCompany().execute();
        } else {
            retryInternet();
        }

       /* adapter.setOnItemClickListener(new ServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int type) {

                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();

                ss);
                //  i.putExtra("serviceID",serviceModels.get(position).getId());
                //  startActivity(i);
            }
        });*/

        aboutFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatFragment.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


    }

    private class aboutCompany extends AsyncTask<String, Void, String> {

        JSONObject jsonObjectList;
        String resMessage = "";
        String resCode = "";
        String resImage = "";
        String resAboutUs = "";

        String resfacebook_link = "";
        String resgoogle_link = "";
        String reslinkdin_link = "";
        String restwitter_link = "";
        String resinsta_link = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.loading));
        }


        @Override
        protected String doInBackground(String... params) {

            String strAboutUs = Constant.BASE_URL + Constant.ABOUT;
            Log.d("strAboutUs", strAboutUs);

            try {
                RestClient restClient = new RestClient(strAboutUs);
                try {
                    restClient.Execute(RequestMethod.GET);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                String AboutUs = restClient.getResponse();
                Log.e("AboutUs", AboutUs);

                if (AboutUs != null && AboutUs.length() != 0) {
                    jsonObjectList = new JSONObject(AboutUs);

                    if (jsonObjectList.length() != 0) {
                        resMessage = jsonObjectList.getString("message");
                        resCode = jsonObjectList.getString("msgcode");

                        if (resCode.equalsIgnoreCase("0")) {
                            try {
                                JSONArray jsonArray = jsonObjectList.getJSONArray("about");
                                {
                                    if (jsonArray != null && jsonArray.length() != 0) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        resImage = jsonObject.getString("image");
                                        resAboutUs = jsonObject.getString("text");
                                        resfacebook_link = jsonObject.getString("facebook_link");
                                        resgoogle_link = jsonObject.getString("google_link");
                                        reslinkdin_link = jsonObject.getString("linkdin_link");
                                        restwitter_link = jsonObject.getString("twitter_link");
                                        resinsta_link = jsonObject.getString("insta_link");
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }

                            try {
                                JSONArray jsonArray2 = jsonObjectList.getJSONArray("list");
                                {
                                    if (jsonArray2 != null && jsonArray2.length() != 0) {
                                        for (int i = 0; i < jsonArray2.length(); i++) {

                                            JSONObject jsonObject = jsonArray2.getJSONObject(i);
                                            serviceModels.add(new ServiceModel("1",
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing() && progressDialog != null) {
                progressDialog.dismiss();

                if (s != null) {

                    if (resImage != null) {
                        tvAboutusline.setText(Html.fromHtml(resAboutUs));
                        Glide.with(getActivity())
                                .load(resImage.trim())
                                .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.banner_one).into(imgAboutUsbanner);
                    }

                    if (serviceModels.size() > 0) {
                        serviceRecycle.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();

                    } else {
                        serviceRecycle.setVisibility(View.GONE);
                    }
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
                    serviceRecycle.setVisibility(View.GONE);
                    getActivity().findViewById(R.id.relsocialmedia).setVisibility(View.GONE);
                    Toast.makeText(getActivity(), R.string.noconnection, Toast.LENGTH_SHORT).show();
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
                    new aboutCompany().execute();
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
            actionBar.setTitle("About Us");
        }
    }
}
