package thedezine.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

import thedezine.android.R;
import thedezine.android.model.PortfolioModel;
import thedezine.android.utils.TagGroup;


public class PortfolioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private int lastPosition = -1;
    private static OnItemClickListener mOnItemClickListener;
    private LayoutInflater inflater;
    public static List<PortfolioModel> portfolioModels;
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);


    public PortfolioAdapter(Context context, List<PortfolioModel> bean) {

        this.context = context;
        this.portfolioModels = bean;
        this.inflater = (LayoutInflater.from(context));
    }


    private class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView portfolioImageView;
        ImageView likeImageView;
        TextView headingTv;
        TextView tagTv;
        TagGroup tagGroup;

        DataObjectHolder(View itemView) {
            super(itemView);

            this.portfolioImageView = (ImageView) itemView.findViewById(R.id.portfolioImageView);
            this.likeImageView = (ImageView) itemView.findViewById(R.id.likeImageView);
            this.headingTv = (TextView) itemView.findViewById(R.id.headingTv);
            this.tagTv = (TextView) itemView.findViewById(R.id.tagTv);
            this.tagGroup = (TagGroup) itemView.findViewById(R.id.tagGroup);

            this.itemView.setOnClickListener(this);
            this.likeImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                if (v.getId()==R.id.likeImageView){

                    AnimatorSet animatorSet = new AnimatorSet();

                    ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(likeImageView, "scaleX", 0.2f, 1f);
                    bounceAnimX.setDuration(300);
                    bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                    ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(likeImageView, "scaleY", 0.2f, 1f);
                    bounceAnimY.setDuration(300);
                    bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                    bounceAnimY.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                            if (portfolioModels.get(getAdapterPosition()).getIsLiked()==1){
                                portfolioModels.get(getAdapterPosition()).setIsLiked(0);
                                likeImageView.setImageResource(R.drawable.ic_heart_outline_grey);


                            }else {
                                portfolioModels.get(getAdapterPosition()).setIsLiked(1);
                                likeImageView.setImageResource(R.drawable.ic_heart_red);


                            }

                        }
                    });
                    animatorSet.play(bounceAnimX).with(bounceAnimY);
                    animatorSet.start();

                    mOnItemClickListener.onItemClick(v, getAdapterPosition());

                }else {

                    mOnItemClickListener.onItemClick(v, getAdapterPosition());
                }

            }
        }

        void clearAnimation() {
            itemView.clearAnimation();
        }
    }

    private static class Footer extends RecyclerView.ViewHolder {

        ProgressBar prog;

        Footer(View itemView) {
            super(itemView);
            this.prog = (ProgressBar) itemView.findViewById(R.id.prog);
            this.prog.getIndeterminateDrawable().setColorFilter(Color.parseColor("#1565C0"), PorterDuff.Mode.SRC_ATOP);
            this.prog.setIndeterminate(true);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_portfolio, parent, false);

            return new DataObjectHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_progbar, parent, false);

            return new Footer(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PortfolioModel pm = portfolioModels.get(holder.getAdapterPosition());

        if (holder instanceof DataObjectHolder) {

            DataObjectHolder holder1 = (DataObjectHolder) holder;
            Glide.with(context)
                    .load(pm.getImage())
                    .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.default_icon).into(holder1.portfolioImageView);

            if (pm.getIsLiked() == 0) {
                holder1.likeImageView.setImageResource(R.drawable.ic_heart_outline_grey);
            } else {
                holder1.likeImageView.setImageResource(R.drawable.ic_heart_red);
            }

            holder1.headingTv.setText(pm.getName().trim());
            holder1.tagTv.setText(pm.getDesc().trim());

            //  Log.e("mydta",pm.getTag().size()+"    p");
            if (pm.getTag() != null) {
                holder1.tagGroup.setTags(pm.getTag());
                // holder.tagGroup.setOnTagClickListener(mTagClickListener);
            }

            setAnimation(holder1.itemView, holder.getAdapterPosition());
        }
    }

   /* @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {

        PortfolioModel pm=portfolioModels.get(holder.getAdapterPosition());

        Glide.with(context)
                .load(pm.getImage())
                .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_icon).into(holder.portfolioImageView);

        holder.headingTv.setText(pm.getName().trim());
        holder.tagTv.setText(pm.getDesc().trim());

        Log.e("mydta",pm.getTag().size()+"    p");
        if (pm.getTag() != null) {
            holder.tagGroup.setTags(pm.getTag());
           // holder.tagGroup.setOnTagClickListener(mTagClickListener);
        }

        setAnimation(holder.itemView, holder.getAdapterPosition());
    }*/

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    (position > lastPosition) ? R.anim.up_from_bottom
                            : R.anim.down_from_top);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof DataObjectHolder)
            ((DataObjectHolder) holder).clearAnimation();
        //  super.onViewDetachedFromWindow(holder);
    }

    /*@Override
    public void onViewDetachedFromWindow(final DataObjectHolder holder)
    {
        ((DataObjectHolder)holder).clearAnimation();
    }*/

    @Override
    public int getItemViewType(int position) {
        if (portfolioModels.get(position) == null) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return portfolioModels.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

}
