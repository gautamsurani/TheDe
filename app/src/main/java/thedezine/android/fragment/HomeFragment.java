package thedezine.android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import thedezine.android.R;
import thedezine.android.adapter.HomePagerAdapter;
import thedezine.android.model.Project;
import thedezine.android.model.Tag;
import thedezine.android.utils.FixedSpeedScroller;
import thedezine.android.utils.ParallaxPageTransformer;


public class HomeFragment extends Fragment {

    private ViewPager homeViewPager;
    private List<Tag> tagList;
    private List<Project> projectList;
    String[] name=new String[3];
    int[] image=new int[3];
  //  ImageView pageONE,pageTWO,pageTHREE;


    private Handler handler;
    private int delay = 5000; //milliseconds
    private int page = 0;
    Runnable runnable;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        handler = new Handler();
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewPager = (ViewPager) view.findViewById(R.id.homeViewPager);


        name[0]="Android";
        name[1]="Website";
        name[2]="Design";

        image[0]= R.drawable.homeoneimg;
        image[1]=R.drawable.hometwoimg;
        image[2]=R.drawable.homethreeimg;

        ParallaxPageTransformer pageTransformer = new ParallaxPageTransformer()
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.sliderImageView, 2, 2))
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.tagGroupHome, -0.65f,
                        ParallaxPageTransformer.ParallaxTransformInformation.PARALLAX_EFFECT_DEFAULT));
        homeViewPager.setPageTransformer(true, pageTransformer);


        getHomeData();
        homeViewPager.setAdapter(new HomePagerAdapter(getActivity(), projectList));

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(homeViewPager.getContext());
            // scroller.setFixedDuration(5000);
            mScroller.set(homeViewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        runnable = new Runnable() {
            public void run() {
                if (3 == page) {
                    page = 0;
                } else {
                    page++;
                }
                homeViewPager.setCurrentItem(page, true);
                handler.postDelayed(this, delay);
            }
        };


        homeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    private void getHomeData() {

            try {
                projectList = new ArrayList<Project>();
                for (int i = 0; i < 3; i++) {
                    Project project = new Project();
                        tagList = new ArrayList<Tag>();
                        for (int j = 0; j < 2; j++) {
                            Tag tag = new Tag();
                            tag.setPlatform("Android");
                            tagList.add(tag);
                        }
                    project.setTagList(tagList);
                    project.setTitle(name[i]);
                    project.setImage(image[i]);
                    projectList.add(project);
                }
            } catch (Exception e) {
                e.printStackTrace();

        }
    }

}

