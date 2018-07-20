package com.infoicon.powercoin.main;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import infoicon.com.powercoin.R;

/**
 * Created by HP-PC on 3/19/2018.
 */

public class DownLoads extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            Uri Download_Uri = Uri.parse("http://52.67.139.216/lab/public/images/report/2.pdf");
        long downloadReference;
        // Create request for android download manager
        DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        //Setting title of request
        request.setTitle("Data Download");
        //Setting description of request
        request.setDescription("Report DownLoading");
            request.setDestinationInExternalFilesDir(DownLoads.this, Environment.DIRECTORY_DOWNLOADS,+System.currentTimeMillis()+"AndroidTutorialPoint.pdf");
        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);
        //return downloadReference;


    }}
