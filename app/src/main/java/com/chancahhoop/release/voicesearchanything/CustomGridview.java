package com.chancahhoop.release.voicesearchanything;

/**
 * Created by hoadt on 14/12/2017.
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class CustomGridview extends ArrayAdapter<QuanLy> {
public static int tt=0;
    ArrayList<QuanLy> data;
    public  static  ArrayList<QuanLy> listarr;
Context contextt;
    public CustomGridview(Context context, int resource, ArrayList<QuanLy> data) {
        super(context, resource, data);
        this.data = data;
        contextt=context;
        listarr = new ArrayList<>();
        listarr.addAll(data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        if(v==null){
            LayoutInflater vi;
            vi=LayoutInflater.from(getContext());
            v=vi.inflate(R.layout.activity_custom_gridview,null);

        }
        final QuanLy sp=getItem(position);
        if(sp!=null){
            ImageView imgView=(ImageView)v.findViewById(R.id.imgview);
            TextView textView=(TextView)v.findViewById(R.id.txttitle);
            imgView.setImageDrawable(sp.getImage());
            if(sp.getName().toString().length()>12){
                textView.setText(sp.getName().toString().substring(0,12)+"...");
            }
            else {textView.setText(sp.getName().toString());}


        }
        return v;
    }
    public ArrayList<QuanLy> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(listarr);
        }
        else
        {
            Log.d("////",listarr.size()+"");
            for (QuanLy ql : listarr)
            {
                if (ql.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    data.add(ql);
                }
                if(ql.getName().toLowerCase(Locale.getDefault()).equals(charText)){
                    tt=1;
                }
            }
        }
        notifyDataSetChanged();
        return data;
    }
}