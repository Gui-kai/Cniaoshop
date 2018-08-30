package com.guikai.cniaoshop.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.guikai.cniaoshop.Contants;
import com.guikai.cniaoshop.R;
import com.guikai.cniaoshop.adapter.DividerItemDecortion;
import com.guikai.cniaoshop.adapter.HomeCatgoryAdapter;
import com.guikai.cniaoshop.bean.Banner;
import com.guikai.cniaoshop.bean.HomeCampaign;
import com.guikai.cniaoshop.http.BaseCallback;
import com.guikai.cniaoshop.http.OkHttpHelper;
import com.guikai.cniaoshop.widget.CustomSliderView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private SliderLayout mSliderLayout;
    private RecyclerView mRecyclerView;
    private HomeCatgoryAdapter mAdatper;

    private static final  String TAG="HomeFragment";

    private Gson mGson = new Gson();

    private List<Banner> mBanner;

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container,false);
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        requestImages();

        initRecyclerView(view);
        return view;
    }

    private void requestImages() {


        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";



        httpHelper.get(url, new BaseCallback<List<Banner>>() {

            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onSuccess(Call call, Response response, List<Banner> banners) {

                Log.e("banner",banners.size()+"");
                mBanner = banners;

                initSlider();
            }

            @Override
            public void onError(Call call, Response response, int code, Exception e) {

            }
        });
    }

    private void initRecyclerView(View view) {
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
//
//        List<HomeCategory> datas = new ArrayList<>(5);
//
//        HomeCategory category = new HomeCategory("热门活动", R.drawable.img_big_1, R.drawable.img_0_small1,R.drawable.img_1_small2);
//        datas.add(category);
//
//        category = new HomeCategory("有利可图",R.drawable.img_big_4,R.drawable.img_4_small1,R.drawable.img_4_small2);
//        datas.add(category);
//        category = new HomeCategory("品牌街",R.drawable.img_big_2,R.drawable.img_2_small1,R.drawable.img_2_small2);
//        datas.add(category);
//
//        category = new HomeCategory("金融街 包赚翻",R.drawable.img_big_1,R.drawable.img_3_small1,R.drawable.imag_3_small2);
//        datas.add(category);
//
//        category = new HomeCategory("超值购",R.drawable.img_big_0,R.drawable.img_0_small1,R.drawable.img_0_small2);
//        datas.add(category);
//
//        mAdatper = new HomeCatgoryAdapter(datas);
//
//        mRecyclerView.setAdapter(mAdatper);
//
//        mRecyclerView.addItemDecoration(new DividerItemDecortion());
//
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        httpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onSuccess(Call call, Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
            }


            @Override
            public void onError(Call call, Response response, int code, Exception e) {

            }
        });

    }

    private void initData(List<HomeCampaign> homeCampaigns) {

        mAdatper = new HomeCatgoryAdapter(homeCampaigns, getActivity());
        mRecyclerView.setAdapter(mAdatper);
        mRecyclerView.addItemDecoration(new DividerItemDecortion());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }



    //引入轮播图github框架
    private void initSlider() {

        if (mBanner != null) {
            for (Banner banner : mBanner) {

                CustomSliderView customSliderView = new CustomSliderView(getActivity());
                customSliderView
                        .description(banner.getName())
                        .image(banner.getImgUrl())
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                mSliderLayout.addSlider(customSliderView);
            }
        }

        //原点动画
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        //图片跳转动画
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        //时间
        mSliderLayout.setDuration(3000);


        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Log.d(TAG,"onPageScrolled");
            }

            @Override
            public void onPageSelected(int i) {
                Log.d(TAG,"onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.d(TAG,"onPageScrollStateChanged");
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSliderLayout.stopAutoCycle();
    }
}
