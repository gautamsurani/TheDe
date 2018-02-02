package thedezine.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.regex.Pattern;

import thedezine.android.R;
import thedezine.android.model.ClientModel;
import thedezine.android.model.PortfolioModel;
import thedezine.android.model.Tag;
import thedezine.android.utils.TagGroup;


public class ClientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int lastPosition = -1;
    private static OnItemClickListener mOnItemClickListener;
    private List<ClientModel> clientModels;

    public ClientAdapter(Context context, List<ClientModel> bean) {

        this.context = context;
        this.clientModels = bean;
    }


    private static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView clientImg;
        TextView clientName;
        TextView clientDesc;
        TagGroup tagGroupClient;

        DataObjectHolder(View itemView) {
            super(itemView);

            this.clientImg = (ImageView) itemView.findViewById(R.id.clientImg);
            this.clientName = (TextView) itemView.findViewById(R.id.clientName);
            this.clientDesc = (TextView) itemView.findViewById(R.id.clientDesc);
            this.tagGroupClient = (TagGroup) itemView.findViewById(R.id.tagGroupClient);

            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
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
                    .inflate(R.layout.row_clients, parent, false);

            return new DataObjectHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_progbar, parent, false);

            return new Footer(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ClientModel cm = clientModels.get(holder.getAdapterPosition());

        if (holder instanceof DataObjectHolder) {

            DataObjectHolder holder1 = (DataObjectHolder) holder;
            Glide.with(context)
                    .load(cm.getImage())
                    .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.default_icon).into(holder1.clientImg);

            holder1.clientName.setText(cm.getName().trim());
            holder1.clientDesc.setText(cm.getDesc().trim());

            if (cm.getTag() != null) {
                holder1.tagGroupClient.setTags(cm.getTag());
                holder1.tagGroupClient.setOnTagClickListener(mTagClickListener);
            }

            setAnimation(holder1.itemView, holder.getAdapterPosition());
        }
    }


    private void setAnimation(View viewToAnimate, int position) {
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
    }

    @Override
    public int getItemViewType(int position) {
        if (clientModels.get(position) == null) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return clientModels.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        mOnItemClickListener = mItemClickListener;
    }

    private TagGroup.OnTagClickListener mTagClickListener = new TagGroup.OnTagClickListener() {
        @Override
        public void onTagClick(String text) {

        }

        @Override
        public void onTagClick(Tag tag) {
            browseUrl(context, tag.getUrl().trim());
        }
    };

    private static void browseUrl(Context context, String url) {

        if (!url.startsWith("http") && !url.startsWith("https"))
            url = "http://" + url;

        if (!TextUtils.isEmpty(url)) {
            Pattern URL_PATTERN = Patterns.WEB_URL;
            if (URL_PATTERN.matcher(url).matches()) {

                try {
                    Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW);
                    openBrowserIntent.setData(Uri.parse(url));
                    context.startActivity(openBrowserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


       /* if (URLUtil.isValidUrl(url)) {
            Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW);
            openBrowserIntent.setData(Uri.parse(url));
            context.startActivity(openBrowserIntent);
        }*/

    }

}

