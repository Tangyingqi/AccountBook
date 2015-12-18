package com.tyq.accountbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tyq.greendao.Person;

import java.util.List;

/**
 * Created by tyq on 2015/12/17.
 */
public class MyAdapter extends BaseAdapter {
    private List<Person> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public MyAdapter(List<Person> list, Context context) {
        this.mData = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_cell, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tvName);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tvMoney);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_money.setText(mData.get(position).getMoney());


        return convertView;
    }

    public class ViewHolder {
        TextView tv_name, tv_money;
    }
}
