package com.example.iot_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String temp = "3";
        String humid = "3";
        String Window = "open";

        setContentView(R.layout.activity_main);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("FirebaseSettingEx", "getInstanceId failed", task.getException());
                return;
            }
            else{
                String token = task.getResult().getToken();
                System.out.println(token);
            }
        });
        TextView text_temp=findViewById(R.id.textView);
        TextView text_humid=findViewById(R.id.textView2);
        TextView text_window=findViewById(R.id.textView3);
        Button button_open=findViewById(R.id.button);
        Button button_close=findViewById(R.id.button2);
        text_temp.setText("온도 : " + temp);
        text_humid.setText("습도 : " + humid);
        text_window.setText("창문 : " +Window);
        button_open.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        button_close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

    }
    private void downloadWithTransferUtility(){
        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance()))
                        .build();
        File file = new File(getApplicationContext().getFilesDir(), "download.txt");
        String string=null;
        TransferObserver downloadObserver =
                transferUtility.download(
                        string,
                        file);
        downloadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                try {
                    BufferedReader buf = new BufferedReader(new FileReader(file));
                    String line=null;
                    ArrayList<String> tv = new ArrayList<>();
                    while ((line=buf.readLine())!=null){
                        tv.add(line);
                    }
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(int id, Exception ex) {

            }
        });
    }
}