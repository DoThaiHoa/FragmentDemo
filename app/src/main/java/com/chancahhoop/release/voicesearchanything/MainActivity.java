package com.chancahhoop.release.voicesearchanything;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    OnListeningGrandted onListeningGrandted;
    Button button,btn1;
    ImageView imgLeft,imgRight;
    EditText editText;
    PackageManager pm;
    ImageView imageView1;
    int flag = 0;
    private static final int REQUEST_ID_READ_PERMISSION = 100;
    ArrayList<ApplicationInfo> packages;
    ApplicationInfo packageInfo;
    CustomGridview adapter;
    fragment2 fr2;
    fragment1 fr1;
    public static boolean isGranted =false;
    int q=0;
    ViewPager pager;
    ArrayList<QuanLy> ql=new ArrayList<>();
    private static final int REQUEST_WRITE_PERMISSION = 786;
    public static TextView txtSLApp,txtSLFile;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        imgLeft=(ImageView)findViewById(R.id.btnLeft);
        imgRight=(ImageView)findViewById(R.id.btnRight);
        imgLeft.setVisibility(View.VISIBLE);
        imgRight.setVisibility(View.GONE);
        pager = (ViewPager)findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        int MyVersion = Build.VERSION.SDK_INT;
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                q = position;

                if(position==0){
                    imgLeft.setVisibility(View.VISIBLE);
                    imgRight.setVisibility(View.GONE);
                }
                else {
                    imgLeft.setVisibility(View.GONE);
                    imgRight.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        editText = (EditText) findViewById(R.id.edTim);
        button = (Button) findViewById(R.id.btnTim);
//        btn1=(Button)findViewById(R.id.btn1);
        imageView1=(ImageView)findViewById(R.id.imageView1);
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fr1=new fragment1();

//        fragmentTransaction.add(R.id.layoutParent,new fragment1(),"fg1");
//        fragmentTransaction.add(R.id.layoutParent,fr2,"fg2");
//        fragmentTransaction.hide(fr2);
//        fragmentTransaction.commit();
        pm = getPackageManager();
        onListeningGrandted =  fr2;

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.actionbar_view);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
         txtSLApp = (TextView)findViewById(R.id.txtSLApp);
        txtSLFile = (TextView)findViewById(R.id.txtSLFile);
        try{
            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (!checkIfAlreadyhavePermission()) {
                    requestForSpecificPermission();
                }else{
                    isGranted = true;
                }
            }else isGranted = true;
        }catch (NoSuchMethodError e) {
            Log.d("////", "" + e);
        }
        packages = (ArrayList<ApplicationInfo>) pm.getInstalledApplications(PackageManager.GET_META_DATA);
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
                    builder.setTitle("the device is not supported!");
                    builder.setMessage("You need to install the Google Now launcher");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Install", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("market://details?id=com.google.android.googlequicksearchbox"));
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(flag==1||editText.getText().toString().equals("")){
                    switch (q){
                        case 0:
                            fragment1.adapter.filter(editText.getText().toString());
                            if(CustomGridview.tt==1){

                                fragment1.listView.performItemClick(fragment1.listView,0,fragment1.listView.getItemIdAtPosition(0));
                                CustomGridview.tt=0;
                            }
                            break;
                        case 1:
                            fr2.filter(editText.getText().toString());
                            break;
                    }
                    flag=0;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           switch (q){
               case 0:

                   fragment1.ql = fragment1.adapter.filter(editText.getText().toString());
                   break;
               case 1:

                   fragment2.filter(editText.getText().toString());
                   break;
           }
            }
        });
    imgRight.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pager.setCurrentItem(getItem(-1), true);
        }
    });
    imgLeft.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            pager.setCurrentItem(getItem(+1), true);
        }
    });

    }
    private int getItem(int i) {
        return pager.getCurrentItem() + i;
    }

//    public void btn1Click(View v){
//
//        Fragment fragment1 = getFragmentManager().findFragmentByTag("fg1");
//        Fragment fragment2 = getFragmentManager().findFragmentByTag("fg2");
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.hide(fragment2);
//        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).show(fragment1);
//        fragmentTransaction.commit();
//    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }
//    public void btn2Click(View v){
//
//        Fragment fragment1 = getFragmentManager().findFragmentByTag("fg1");
//        Fragment fragment2 = getFragmentManager().findFragmentByTag("fg2");
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.hide(fragment1);
//        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).show(fragment2);
//        fragmentTransaction.commit();
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(result.get(0));
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onListeningGrandted.onGranted(getApplicationContext());
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
//                    onListeningGrandted.onGranted();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    interface OnListeningGrandted{
        void onGranted(Context context);
    }
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    q=0;

                    return fragment1.newInstance("FirstFragment, Instance 1");

                case 1:

                    return  fr2 = fragment2.newInstance("SecondFragment, Instance 1");
                default:

                    return fragment2.newInstance("SecondFragment, Instance 1");

            }
        }

        @Override
        public int getCount() {

                return 2;


        }
    }

}

