package com.zet.adapter;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zet.R;

import java.util.List;

/**
 * Created by macchen on 15/4/10.
 */
public class YearAdapter extends BaseAdapter {

    private List<String> years;

    public YearAdapter(List<String> years) {
        this.years = years;
    }

    @Override
    public int getCount() {
        return years == null ? 0 : years.size();
    }

    @Override
    public Object getItem(int position) {
        return years.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            TextView tvName = new TextView(parent.getContext());
            Resources resources = parent.getContext().getResources();
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.text_small));
            tvName.setTextColor(Color.BLACK);
            tvName.setGravity(Gravity.LEFT);
            int paddingLeft = resources.getDimensionPixelOffset(R.dimen.activity_select_item_padding_left);
            int paddingTop = resources.getDimensionPixelOffset(R.dimen.activity_select_item_padding_top);
            tvName.setPadding(paddingTop,paddingLeft,paddingTop,paddingLeft);
            convertView = tvName;
        }
        ((TextView) convertView).setText(years.get(position));
        return convertView;
    }

}
