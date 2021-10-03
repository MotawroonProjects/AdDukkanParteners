 package com.addukkanpartener.uis.activity_view;

 import android.Manifest;
 import android.app.AlertDialog;
 import android.content.Context;
 import android.content.Intent;
 import android.content.pm.PackageManager;
 import android.graphics.Bitmap;
 import android.net.Uri;
 import android.os.Build;
 import android.os.Bundle;
 import android.os.Handler;
 import android.provider.MediaStore;
 import android.util.Log;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.webkit.DownloadListener;
 import android.webkit.ValueCallback;
 import android.webkit.WebChromeClient;
 import android.webkit.WebSettings;
 import android.webkit.WebView;
 import android.webkit.WebViewClient;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.core.app.ActivityCompat;
 import androidx.core.content.ContextCompat;
 import androidx.databinding.DataBindingUtil;

 import com.addukkanpartener.R;
 import com.addukkanpartener.databinding.ActivityViewBinding;
 import com.addukkanpartener.language.Language;

 import java.io.ByteArrayOutputStream;
 import java.io.File;
 import java.util.HashMap;

 import io.paperdb.Paper;

 public class ViewActivity extends AppCompatActivity {

     private ActivityViewBinding binding;
     private String url = "";
     private String lang;

     @Override
     protected void attachBaseContext(Context newBase) {
         super.attachBaseContext(Language.updateResources(newBase, "ar"));

     }

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this, R.layout.activity_view);
         getDataFromIntent();
         initView();
     }

     private void getDataFromIntent() {
         Intent intent = getIntent();
         url = intent.getStringExtra("url");

     }

     private void initView() {
         Paper.init(this);
         lang = Paper.book().read("lang", "ar");
         binding.setLang(lang);
         binding.webView.getSettings().setAllowFileAccessFromFileURLs(true);
         binding.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
         binding.webView.getSettings().setAllowContentAccess(true);
         binding.webView.getSettings().setAllowFileAccess(true);
         binding.webView.getSettings().setBuiltInZoomControls(false);
         binding.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
         binding.webView.getSettings().setJavaScriptEnabled(true);
         binding.webView.getSettings().setLoadWithOverviewMode(true);
         binding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

         binding.webView.setWebViewClient(new WebViewClient() {
             @Override
             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                 super.onPageStarted(view, url, favicon);
             }

             @Override
             public void onPageFinished(WebView view, String url) {
                 super.onPageFinished(view, url);
                 binding.progBar.setVisibility(View.GONE);
             }

             @Override
             public void onPageCommitVisible(WebView view, String url) {
                 super.onPageCommitVisible(view, url);
                 binding.progBar.setVisibility(View.GONE);
             }
         });
         binding.webView.setDownloadListener(new DownloadListener() {
             public void onDownloadStart(String url, String userAgent,
                                         String contentDisposition, String mimetype,
                                         long contentLength) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }catch (Exception e){

                }

             }
         });
         binding.webView.loadUrl(url);



         binding.llBack.setOnClickListener(v -> finish());

     }


 }