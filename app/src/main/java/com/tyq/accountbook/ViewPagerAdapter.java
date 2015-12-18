package com.tyq.accountbook;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by tyq on 2015/12/17.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<View> mData;
    private Context mContext;

    public ViewPagerAdapter(List<View> list,Context context){
        this.mData = list;
        this.mContext = context;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mData.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mData.get(position));
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}
