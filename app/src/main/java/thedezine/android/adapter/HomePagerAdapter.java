package thedezine.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import thedezine.android.R;
import thedezine.android.model.Project;
import thedezine.android.model.Tag;
import thedezine.android.utils.TagGroup;


public class HomePagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ImageView sliderImageView = null;
    private TextView headlineTextView = null;
    private RelativeLayout tagView = null;
    private TagGroup mTagGroup = null;
    private final static double TAG_VIEW_ASPECT_RATIO = 1d / 9d;
    private int displaySize;
    private List<Project> projectList = null;

    public HomePagerAdapter(Activity context, List<Project> projectList) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        displaySize = getWindowSize(context).y - getActionBarSize(context);
        this.projectList = projectList;
    }


    @Override
    public int getCount() {
        return projectList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.layout_home, container, false);
        sliderImageView = (ImageView) itemView.findViewById(R.id.sliderImageView);
        headlineTextView = (TextView) itemView.findViewById(R.id.headingTv);
        tagView = (RelativeLayout) itemView.findViewById(R.id.tagView);
        tagView.getLayoutParams().height = (int) (displaySize * TAG_VIEW_ASPECT_RATIO);
        mTagGroup = (TagGroup) itemView.findViewById(R.id.tagGroupHome);
//        mTagGroup.getLayoutParams().height = scrollTagView.getLayoutParams().height/2;
//        mTagGroup.getLayoutParams().height = (int) (displaySize * TAG_VIEW_ASPECT_RATIO);
        if(projectList.get(position).getTagList()!=null) {
            mTagGroup.setTags(projectList.get(position).getTagList());

        }
        sliderImageView.setImageResource(projectList.get(position).getImage());
        headlineTextView.setText(projectList.get(position).getTitle());
        headlineTextView.setTextColor(Color.WHITE);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }


    public static Point getWindowSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int getActionBarSize(Context context) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
}
