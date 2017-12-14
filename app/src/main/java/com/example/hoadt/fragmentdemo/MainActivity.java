package com.example.hoadt.fragmentdemo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button button;
    EditText editText;
    PackageManager pm;
    int flag = 0;
    ArrayList<ApplicationInfo> packages;
    ApplicationInfo packageInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.edTim);
        button = (Button) findViewById(R.id.btnTim);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment2 fragment2 = new fragment2();
        fragmentTransaction.add(R.id.layoutParent,new fragment1(),"fg1");
        fragmentTransaction.add(R.id.layoutParent,fragment2,"fg2");
        fragmentTransaction.hide(fragment2);
        fragmentTransaction.commit();
        packages = (ArrayList<ApplicationInfo>) pm.getInstalledApplications(PackageManager.GET_META_DATA);
        pm = getPackageManager();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                if (intent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                    startActivityForResult(intent, 10);
                    flag = 1;
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Thiết bị chưa được hỗ trợ!");
                    builder.setMessage("Bạn cần cài đặt trình chạy Google Hiện hành");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Cài đặt", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("market://details?id=com.google.android.googlequicksearchbox"));
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                if (flag == 1) {
                    try {
                        findApp();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    flag = 0;
                }
//                if(editText.getText().toString().equals("")){
//                    name.clear();
//                    Imageid.clear();
//                    adapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }
        });
    }

    public void btn1Click(View v){
        Fragment fragment1 = getFragmentManager().findFragmentByTag("fg1");
        Fragment fragment2 = getFragmentManager().findFragmentByTag("fg2");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment2);
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).show(fragment1);
        fragmentTransaction.commit();
    }

    public void btn2Click(View v){
        Fragment fragment1 = getFragmentManager().findFragmentByTag("fg1");
        Fragment fragment2 = getFragmentManager().findFragmentByTag("fg2");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment1);
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).show(fragment2);
        fragmentTransaction.commit();
    }
    public void findApp() throws PackageManager.NameNotFoundException {

        for (int i = 0; i < packages.size(); i++) {
            try {
                packageInfo = pm.getApplicationInfo(packages.get(i).packageName, 0);
            } catch (final PackageManager.NameNotFoundException e) {
                Toast.makeText(this, "sdfasdf", Toast.LENGTH_SHORT).show();
            }
            String title = (String) ((packageInfo != null) ? pm.getApplicationLabel(packageInfo) : "???");
            Drawable appIcon = getPackageManager().getApplicationIcon(packages.get(i).packageName);

            if (title.toLowerCase().equals(editText.getText().toString().toLowerCase())) {

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packages.get(i).packageName);
                if (launchIntent != null) {

                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }

//            if (!title.toLowerCase().equals(editText.getText().toString().toLowerCase())) {
//
//                if (title.toLowerCase().contains(editText.getText().toString().toLowerCase())) {
//
//                    name.add(title);
//                    Imageid.add(appIcon);
//
//                }
//
//            }

        }
//        adapter = new CustomGridview(MainActivity.this, name, Imageid);
//
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this,name.get(position).toString(), Toast.LENGTH_SHORT).show();
////                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packages.get(position).packageName);
////                if (launchIntent != null) {
////                    startActivity(launchIntent);//null pointer check in case package name was not found
////                }
//            }
//        });


    }

}
