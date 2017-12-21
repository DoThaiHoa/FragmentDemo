package com.chancahhoop.release.voicesearchanything;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by hoadt on 29/9/2017.
 */

public class fragment2 extends Fragment implements MainActivity.OnListeningGrandted{

    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private File root;
//    ImageView btnRight;
    private static ArrayList<String> fileList = new ArrayList<>();
    private static ArrayList<String> data = new ArrayList<>();
    private ListView viewli;
    private Activity activity;
    public static String sLFile;
    int c=1;
    private static TextView edt;
    public static ArrayAdapter arrayAdapter;
    Handler handler;
    //    ProgressBar progressBar;
    int t = 0;

    //    ProgressBar progressBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment2,container,false);
        viewli = (ListView)view.findViewById(R.id.view);
        edt = (TextView)view.findViewById(R.id.edFind);

//        btnRight=(ImageView)view.findViewById(R.id.btnRight);
//        hand();
//        askPermissionAndReadFile();
//        progressBar = (ProgressBar) findViewById(R.id.progress);
//        progressBar.setProgress(0);

        //getting SDcard root path
        root = new File(Environment.getExternalStorageDirectory().getPath());

//        if(edt.getText().toString().equals("")){
//            edt.setText("click me");
//            edt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(getActivity(),MainActivity.class));
//                    getActivity().finish();
//                }
//            });
//        }
        if(MainActivity.isGranted){
            Thread readThread = new Thread(new LoadAllFile(root));
            readThread.start();
            hand();
            intc();
        }

        activity = getActivity();
//.

//.

        viewli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str=fileList.get(position).toString();
                int index = str.lastIndexOf("/");
                 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(str.substring(0, index));

                intent.setDataAndType(uri, "text/csv");
                startActivity(Intent.createChooser(intent, "Open folder"));

            }
        });
//        btnRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "fr2", Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onGranted(Context context) {
        Toast.makeText(context, "listened", Toast.LENGTH_SHORT).show();
        Thread readThread = new Thread(new LoadAllFile(root));
        readThread.start();
        hand();
        intc();

    }

    private class LoadAllFile implements Runnable{
        private File dir;
        public  LoadAllFile(File dir){
            this.dir = dir;
        }
        @Override
        public void run() {

            getfile(dir);
        }
    }
    public void intc(){
        arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,fileList);
        viewli.setAdapter(arrayAdapter);
    }
    public static void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        fileList.clear();
        if (charText.length() == 0) {
            fileList.addAll(data);
        }
        else
        {
            for (String file : data)
            {
                if (file.toLowerCase(Locale.getDefault()).contains(charText.toLowerCase()))
                {
                    fileList.add(file);
                    edt.setText(fileList.size()+" Result");
                }
            }
        }
        arrayAdapter.notifyDataSetChanged();
    }
    public void getfile( File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {

            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
//                    fileList.add(listFile[i]);
                    getfile(listFile[i]);
                } else {
                    t++;
                    Message message = Message.obtain();
                    message.obj = listFile[i].getPath();
//                    mess[1] =  listFile[i].getPath().substring(listFile[i].getPath().lastIndexOf("/"));
                    handler.sendMessage(message);

                    try {
//                       Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }


        }
    }
//    private void askPermissionAndReadFile() {
//        boolean canRead = this.askPermission(REQUEST_ID_READ_PERMISSION,
//                Manifest.permission.READ_EXTERNAL_STORAGE);
//        //
//        if (canRead) {
//           this.hand();
//        }
//    }
    private void hand(){
        try {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                fileList.add((String) msg.obj);
                arrayAdapter.notifyDataSetChanged();
                data.clear();
                data.addAll(fileList);
//                edt.setText(t+" file");
               MainActivity.txtSLFile.setText(t+" ");
//                progressBar.setProgress(t);
            }
        };}catch (Exception e){
            e.printStackTrace();
        }
    }
//    private boolean askPermission(int requestId, String permissionName) {
//        if (android.os.Build.VERSION.SDK_INT >= 23) {
//
//            // Check if we have permission
//            int permission = ActivityCompat.checkSelfPermission(getActivity(), permissionName);
//
//
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                // If don't have permission so prompt the user.
//                this.requestPermissions(
//                        new String[]{permissionName},
//                        requestId
//                );
//                return false;
//            }else {
//                viewli.setAdapter(arrayAdapter);
//                Toast.makeText(activity, "sfsadfasd", Toast.LENGTH_SHORT).show();
//            }
//        }
//        return true;
//    }

    public static fragment2 newInstance(String text) {

        fragment2 f = new fragment2();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}

