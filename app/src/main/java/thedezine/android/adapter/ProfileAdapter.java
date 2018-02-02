package thedezine.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import thedezine.android.R;
import thedezine.android.model.ProfileData;


public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<ProfileData> profileDatas;
    private Context context;
    private  OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        mOnItemClickListener = mItemClickListener;
    }

    public ProfileAdapter(Context context,List<ProfileData> profileDatas) {

        this.profileDatas = profileDatas;
        this.context=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType==1){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_profile_left, parent, false);
            return new LeftView(view);
        }else if (viewType==2){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_profile_right, parent, false);
            return new RightView(view);
        }else {
            return null;
        }



    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position %2==0){
            return 1;
        }else if (position %2!=0){
            return 2;
        }else {
          return 0;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final ProfileData pd=profileDatas.get(holder.getAdapterPosition());

        if (holder instanceof RightView){

            final RightView holderRIGHT=(RightView)holder;

            Glide.with(context)
                    .load(pd.getImage())
                    .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.banner_one).into(holderRIGHT.userMain);

            holderRIGHT.name.setText(pd.getName().trim());
            holderRIGHT.desi.setText(pd.getDesi().trim());
            holderRIGHT.desc.setText(pd.getEmail().trim());

            holderRIGHT.smail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.putExtra(Intent.EXTRA_EMAIL,pd.getEmail().trim());
                    intent.setData(Uri.parse("mailto:"));

                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }else{
                        // If there are no email client installed in this device
                        Toast.makeText(context,"No email client installed in this device.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holderRIGHT.callBTN1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener!=null){
                        mOnItemClickListener.onItemClick(holderRIGHT.callBTN1,holder.getAdapterPosition());
                    }
                }
            });


        }else if (holder instanceof LeftView){
            final LeftView holderLEFT=(LeftView)holder;

            Glide.with(context)
                    .load(pd.getImage())
                    .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.banner_one).into(holderLEFT.userMain);

            holderLEFT.name.setText(pd.getName().trim());
            holderLEFT.desi.setText(pd.getDesi().trim());
            holderLEFT.desc.setText(pd.getEmail().trim());

            holderLEFT.smail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.putExtra(Intent.EXTRA_EMAIL,pd.getEmail().trim());
                    intent.setData(Uri.parse("mailto:"));

                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }else{
                        // If there are no email client installed in this device
                        Toast.makeText(context,"No email client installed in this device.",Toast.LENGTH_SHORT).show();
                    }

                }
            });

            holderLEFT.callBTN1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener!=null){
                        mOnItemClickListener.onItemClick(holderLEFT.callBTN1,holder.getAdapterPosition());
                    }
                }
            });

        }



    }

    @Override
    public int getItemCount() {
        return profileDatas.size();
    }

    private static class RightView extends RecyclerView.ViewHolder{

        TextView name,desi,desc,smail;
        CircleImageView userMain;
        LinearLayout callBTN1;


         RightView(View itemView) {
            super(itemView);

             this.callBTN1=(LinearLayout) itemView.findViewById(R.id.callBTN1);
             this.name=(TextView)itemView.findViewById(R.id.name);
             this.desi=(TextView)itemView.findViewById(R.id.desi);
             this.desc=(TextView)itemView.findViewById(R.id.desc);
             this.smail=(TextView)itemView.findViewById(R.id.smail);
             this.userMain=(CircleImageView)itemView.findViewById(R.id.userMain);
        }
    }

    private static class LeftView extends RecyclerView.ViewHolder{

        TextView name,desi,desc,smail;
        CircleImageView userMain;
        LinearLayout callBTN1;

         LeftView(View itemView) {
            super(itemView);

             this.callBTN1=(LinearLayout) itemView.findViewById(R.id.callBTN1);
             this.name=(TextView)itemView.findViewById(R.id.name);
             this.desi=(TextView)itemView.findViewById(R.id.desi);
             this.desc=(TextView)itemView.findViewById(R.id.desc);
             this.smail=(TextView)itemView.findViewById(R.id.smail);
             this.userMain=(CircleImageView)itemView.findViewById(R.id.userMain);
        }
    }
}
