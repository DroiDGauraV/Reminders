package com.example.reminders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<RemindersItems> RemList;

    @Override
    public int getCount() {
        return this.RemList.size();
    }

    @Override
    public Object getItem(int position) {
        return RemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public MyAdapter(Context context, ArrayList<RemindersItems> remList) {
        this.context = context;
        RemList = remList;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        convertView = inflater.inflate(R.layout.row, null);
        TextView tv_rowHead = convertView.findViewById(R.id.tv_rowHead);
        TextView tv_rowMsg = convertView.findViewById(R.id.tv_rowMsg);
        TextView tv_rowTime = convertView.findViewById(R.id.tv_rowTime);
        TextView tv_rowDate = convertView.findViewById(R.id.tv_rowDate);
        TextView tv_id = convertView.findViewById(R.id.tv_id);

        RemindersItems remindersItems = RemList.get(position);

        tv_rowHead.setText(remindersItems.getHead());
        tv_rowMsg.setText(remindersItems.getMsg());
        tv_rowDate.setText(remindersItems.getDate());
        tv_rowTime.setText(remindersItems.getTime());
        tv_id.setText(remindersItems.getId());

        tv_id.setVisibility(View.INVISIBLE);

        return convertView;
    }
}
