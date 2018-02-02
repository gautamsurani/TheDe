package thedezine.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import thedezine.android.R;
import thedezine.android.activity.ServicesDetailActivity;
import thedezine.android.model.ServiceDetailsModel;
import thedezine.android.model.TechList;


public class ServiceDetailAdapter extends RecyclerView.Adapter<ServiceDetailAdapter.ViewHolder>  {

    private ServiceAdapter.OnItemClickListener mOnItemClickListener;
    private Context context;
    private List<TechList> techList;

    public ServiceDetailAdapter(Context servicesDetailActivity, List<TechList> techList) {

        this.context=servicesDetailActivity;
        this.techList=techList;
    }


    @Override
    public ServiceDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_serdetails, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceDetailAdapter.ViewHolder holder, int position) {

        LayerDrawable bgDrawable = (LayerDrawable) holder.serdelIMG.getBackground();
        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.shape_id);
        shape.setColor(Color.parseColor(techList.get(holder.getAdapterPosition()).getColor().trim()));


        Picasso.with(context)
                .load(techList.get(holder.getAdapterPosition()).getIcon().trim())
                .error(R.drawable.default_icon)
                .into(holder.serdelIMG);
    }

    @Override
    public int getItemCount() {
        return techList.size();
    }




    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView serdelIMG;

        ViewHolder(View itemView) {
            super(itemView);
            serdelIMG = (ImageView) itemView.findViewById(R.id.serdelIMG);
        }
    }
}
