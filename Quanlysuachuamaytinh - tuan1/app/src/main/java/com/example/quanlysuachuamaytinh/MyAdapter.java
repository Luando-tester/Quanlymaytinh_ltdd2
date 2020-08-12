package com.example.quanlysuachuamaytinh;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by IT on 10/1/2018.
 */

public class MyAdapter extends ArrayAdapter<PhieuThu> {
    Context context;
    ArrayList<PhieuThu> objects;
    public MyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<PhieuThu> objects) {
        super(context, resource, objects);
        this.context=context;
        this.objects= objects;
    }


    public static class ViewHolder {
        TextView tvmaphieu,tvmakhachang,tvngaytaophieu,tvloaiphieu,tvsotien;
        CheckBox cbChon;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        PhieuThu rowItem =  objects.get(position);
        LayoutInflater mInflater = ((Activity)context).getLayoutInflater();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_list_item, null);
            holder = new ViewHolder();
            holder.tvmaphieu = (TextView) convertView.findViewById(R.id.tvMaPT);
            holder.tvmakhachang = (TextView) convertView.findViewById(R.id.tvmakhachhang);
            holder.tvngaytaophieu = (TextView) convertView.findViewById(R.id.tvngaytaophieu);
            holder.tvloaiphieu = (TextView) convertView.findViewById(R.id.tvloaiphieu);
            holder.tvsotien = (TextView) convertView.findViewById(R.id.tvsotien);
            holder.cbChon = (CheckBox) convertView.findViewById(R.id.cbChon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tvmaphieu.setText(rowItem.getSoPT());
        holder.tvmakhachang.setText(rowItem.getMaKh());
        holder.tvngaytaophieu.setText(rowItem.getNgay());
        holder.tvloaiphieu.setText(rowItem.getLoaphieu());
        holder.tvsotien.setText(String.valueOf(rowItem.getSotien()));
        holder.cbChon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(holder.cbChon.isChecked()){
                    objects.get(position).setChon(true);
                }else {
                    objects.get(position).setChon(false);
                }
            }
        });
        return  convertView;
    }
}
