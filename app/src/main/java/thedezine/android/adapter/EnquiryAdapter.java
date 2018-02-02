package thedezine.android.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import java.util.ArrayList;

import thedezine.android.R;


public class EnquiryAdapter extends RecyclerView.Adapter<EnquiryAdapter.DataObjectHolder>
{

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<String> bean;
    private Context context;
    private static MyClickListener myClickListener;
    LayoutInflater inflater;



    public EnquiryAdapter(Context context, ArrayList<String> bean) {

        this.context = context;
        this.bean = bean;
        this.inflater = (LayoutInflater.from(context));
    }




    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener
    {


        ImageView image;
        TextView tvOffertitle;
        TextView etaddedOn;
        RelativeLayout relaedroot;

        public DataObjectHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void onClick(View v) {

        }

    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_offers, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {


    }

        @Override
    public int getItemCount() {
        return bean.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);



    }
}