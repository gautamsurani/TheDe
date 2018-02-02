package thedezine.android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.util.List;

import thedezine.android.R;
import thedezine.android.model.ServiceModel;


public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private Context context;
    private List<ServiceModel> serviceModels;
    private OnItemClickListener mOnItemClickListener;


    public ServiceAdapter(Context serviceActivity, List<ServiceModel> serviceModels) {
        this.context = serviceActivity;
        this.serviceModels = serviceModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.service_items, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ServiceModel serviceDATA = serviceModels.get(holder.getAdapterPosition());

        holder.serviceTV.setText(serviceDATA.getTitle().trim());

        Picasso.with(context)
                .load(serviceDATA.getIcon())
                .error(R.drawable.noti)
                .into( holder.serviceIMG, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.serviceIMG.setColorFilter(Color.parseColor(serviceDATA.getColor()));
                    }

                    @Override
                    public void onError() {

                    }
                });

       /* Glide.with(context)
                .load(serviceDATA.getIcon().trim())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_product)
                 .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.serviceIMG.setImageBitmap(resource);
                        holder.serviceIMG.setColorFilter(Color.RED);
                        return false;
                    }
                });
*/
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
                        holder.serviceLinear.setBackgroundColor(Color.parseColor(serviceDATA.getColor().trim()));
                        holder.serviceTV.setTextColor(Color.WHITE);
                      //  holder.serviceIMG.setBackgroundResource(R.drawable.imgfocused);
                        holder.serviceIMG.setColorFilter(Color.WHITE);

                        break;
                    case MotionEvent.ACTION_UP:

                        holder.serviceLinear.setBackgroundColor(Color.WHITE);
                        holder.serviceTV.setTextColor(Color.BLACK);
                       // holder.serviceIMG.setBackgroundResource(R.drawable.notidefault);
                        holder.serviceIMG.setColorFilter(Color.parseColor(serviceDATA.getColor().trim()));
                        break;

                    case MotionEvent.ACTION_CANCEL:

                        holder.serviceLinear.setBackgroundColor(Color.WHITE);
                        holder.serviceTV.setTextColor(Color.BLACK);
                        // holder.serviceIMG.setBackgroundResource(R.drawable.notidefault);
                        holder.serviceIMG.setColorFilter(Color.parseColor(serviceDATA.getColor().trim()));
                        break;
                }
                return false;
            }
        });

    }



    public interface OnItemClickListener {
        void onItemClick(int position, View view, int type);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
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
