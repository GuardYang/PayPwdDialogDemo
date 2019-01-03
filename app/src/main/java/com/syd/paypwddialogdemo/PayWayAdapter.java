package com.syd.paypwddialogdemo;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class PayWayAdapter extends ArrayAdapter<PayWayItem> {

    private int resourceId;

    public PayWayAdapter(Context context, int textViewResourceId, List<PayWayItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PayWayItem item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView ivPayWayIcon = (ImageView) view.findViewById(R.id.iv_pay_way_icon);
        ivPayWayIcon.setImageResource(item.getPayWayIcon());
        TextView tvPayWayName = (TextView) view.findViewById(R.id.tv_pay_way_name);
        tvPayWayName.setText(item.getPayWayName());

        ImageView ivPayWayOk = (ImageView) view.findViewById(R.id.iv_pay_way_ok);
        if (item.isSelected()) {
            ivPayWayOk.setVisibility(View.VISIBLE);
        } else {
            ivPayWayOk.setVisibility(View.GONE);
        }

        return view;
    }
}
