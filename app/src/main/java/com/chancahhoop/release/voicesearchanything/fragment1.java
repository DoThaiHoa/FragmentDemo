package com.chancahhoop.release.voicesearchanything;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by hoadt on 29/9/2017.
 */

public class fragment1  extends Fragment  {
    static GridView listView;
//    ImageView btnLeft;
    static ArrayList<QuanLy>ql;
    ArrayList<ApplicationInfo> packages;
    QuanLy quanLy;
    PackageManager pm;
    ApplicationInfo packageInfo;
    static CustomGridview adapter;
    public int i=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.layout_fragment1,container,false);
//       btnLeft=(ImageView)view.findViewById(R.id.btnLeft) ;
        listView = (GridView)view.findViewById(R.id.listview);
        pm = getActivity().getPackageManager();

        packages = (ArrayList<ApplicationInfo>) pm.getInstalledApplications(PackageManager.GET_META_DATA);
      ql=new ArrayList<>();
        try {
            findApp("");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        btnLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "fr1", Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;

    }
    public  void findApp(String find) throws PackageManager.NameNotFoundException {

        for ( i = 0; i < packages.size(); i++) {
            quanLy=new QuanLy();
            try {
                packageInfo = pm.getApplicationInfo(packages.get(i).packageName, 0);
            } catch (final PackageManager.NameNotFoundException e) {
                Toast.makeText(getActivity(), "sdfasdf", Toast.LENGTH_SHORT).show();
            }
            String title = (String) ((packageInfo != null) ? pm.getApplicationLabel(packageInfo) : "???");
            String pakage = packages.get(i).packageName;
            Drawable appIcon = getActivity().getPackageManager().getApplicationIcon(packages.get(i).packageName);
                quanLy.setImage(appIcon);
                quanLy.setName(title);
                quanLy.packageName = pakage;
                ql.add(quanLy);
            if (title.toLowerCase().equals(find)) {

                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(packages.get(i).packageName);
                if (launchIntent != null) {

                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        }
        MainActivity.txtSLApp.setText(packages.size()+" ");

        adapter = new CustomGridview(getActivity(),R.layout.activity_custom_gridview,ql);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(ql.get(position).packageName);
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }
    public static fragment1 newInstance(String text) {

        fragment1 f = new fragment1();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
