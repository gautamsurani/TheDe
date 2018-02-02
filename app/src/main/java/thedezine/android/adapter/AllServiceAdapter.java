package thedezine.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import thedezine.android.R;
import thedezine.android.activity.ServicesDetailActivity;
import thedezine.android.model.ServiceModel;


public class AllServiceAdapter extends RecyclerView.Adapter<AllServiceAdapter.ViewHolder> {

    private Context context;
    private List<ServiceModel> serviceModels;
    private ServiceAdapter.OnItemClickListener mOnItemClickListener;


    public AllServiceAdapter(Context serviceActivity, List<ServiceModel> serviceModels) {
        this.context = serviceActivity;
        this.serviceModels = serviceModels;
    }

    @Override
    public AllServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.allservice_items, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ServiceModel serviceDATA = serviceModels.get(holder.getAdapterPosition());

        holder.serviceTV.setText(serviceDATA.getTitle().trim());

        Picasso.with(context)
                .load(serviceDATA.getIcon())
                .placeholder(R.drawable.default_icon)
                .error(R.drawable.noti)
                .into(holder.serviceIMG);

        holder.serviceLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(holder.getAdapterPosition(),view,0);
                }
            }
        });

        holder.serviceLinear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        Log.e("colorr",serviceDATA.getColor().trim());
                     //   holder.serviceLinear.setBackgroundColor(Color.parseColor(serviceDATA.getColor().trim()));
                        holder.serviceTV.setTextColor(Color.parseColor(serviceDATA.getColor().trim()));
                     //   holder.serviceIMG.setColorFilter(Color.WHITE);

                        break;
                    case MotionEvent.ACTION_UP:

                     //   holder.serviceLinear.setBackgroundColor(Color.WHITE);
                        holder.serviceTV.setTextColor(context.getResources().getColor(R.color.Tex));
                     //   holder.serviceIMG.setColorFilter(Color.parseColor(serviceDATA.getColor().trim()));
                        break;

                    case MotionEvent.ACTION_CANCEL:

                      //  holder.serviceLinear.setBackgroundColor(Color.WHITE);
                        holder.serviceTV.setTextColor(context.getResources().getColor(R.color.Tex));
                      //  holder.serviceIMG.setColorFilter(Color.parseColor(serviceDATA.getColor().trim()));
                        break;
                }
                return false;
            }
        });

        holder.serviceLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Toast.makeText(context,"click",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, ServicesDetailActivity.class);
                intent.putExtra("sid",serviceModels.get(holder.getAdapterPosition()).getId());
                intent.putExtra("title",serviceModels.get(holder.getAdapterPosition()).getTitle());
                context.startActivity(intent);
            }
        });

    }



    public interface OnItemClickListener {
        void onItemClick(int position, View view, int type);
    }

    public void setOnItemClickListener(final ServiceAdapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return serviceModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceTV;
        ImageView serviceIMG;
        LinearLayout serviceLinear;

        ViewHolder(View itemView) {
            super(itemView);
            serviceTV = (TextView) itemView.findViewById(R.id.serviceTV);
            serviceIMG = (ImageView) itemView.findViewById(R.id.serviceIMG);
            serviceLinear = (LinearLayout) itemView.findViewById(R.id.serviceLinear);
        }
    }

}
