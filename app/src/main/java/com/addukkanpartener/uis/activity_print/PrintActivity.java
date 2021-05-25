package com.addukkanpartener.uis.activity_print;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.PrescriptionItemDetailsAdapter;
import com.addukkanpartener.databinding.ActivityPrescriptionDetails1Binding;
import com.addukkanpartener.databinding.ActivityPrintBinding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.OrderModel;

import java.io.File;
import java.io.FileOutputStream;

import io.paperdb.Paper;

public class PrintActivity extends AppCompatActivity {

    private ActivityPrintBinding binding;
    private String lang = "ar";
    private OrderModel orderModel;
    private PrescriptionItemDetailsAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_print);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        orderModel = (OrderModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(orderModel);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PrescriptionItemDetailsAdapter(this, orderModel.getPrescription_details_fk());
        binding.recView.setAdapter(adapter);
        binding.llBack.setOnClickListener(v -> finish());

        binding.btnPrint.setOnClickListener(v -> {

        });


    }

    public static Bitmap getBitmapFromView(View view) {
        long now = System.currentTimeMillis();
        Bitmap returnBitmap = null;

        try {

            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile,false);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            if (imageFile.exists()) {
                returnBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        Bitmap b = Bitmap.createScaledBitmap(returnBitmap,384,576,true);
        return b;
    }
}