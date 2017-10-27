package com.example.unitec.sendqrcode;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQR extends AppCompatActivity implements ZXingScannerView.ResultHandler,View.OnClickListener {
    private ZXingScannerView mScannerView;
    Button btnStart,btnNext;
    TextView txtResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        txtResult = (TextView) findViewById(R.id.textResult);
        btnStart = (Button)findViewById(R.id.scanQR);
        btnNext = (Button)findViewById(R.id.next);

        btnStart.setOnClickListener(this);
        btnNext.setOnClickListener(this);


    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);  // Register ourselves as a handler for scan results.
        mScannerView.startCamera(); // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
    @Override
    public void handleResult(Result result) {

        txtResult.setText("Contents = " + result.getText() +
                ", Format = " + result.getBarcodeFormat().toString());
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        //   Handler handler = new Handler();
        //   handler.postDelayed(new Runnable() {
        //       @Override
        //       public void run() {
        //           mScannerView.resumeCameraPreview(ScanQR.this);  // If you would like to resume scanning, call this method below:
        //       }
        //   }, 2000);
//
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.scanQR:
                txtResult.setText("scanning...");
                onResume();
                break;
            case R.id.next:
                String url = "http://192.168.0.99:8686/LotMaterial?division_cd=FAN&process_cd=422&product_no=100076048&lot_no=761301A";
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    // Chrome is not installed
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
        }
    }
}
