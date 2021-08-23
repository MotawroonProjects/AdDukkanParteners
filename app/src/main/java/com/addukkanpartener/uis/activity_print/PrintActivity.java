package com.addukkanpartener.uis.activity_print;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;

import com.addukkanpartener.R;
import com.addukkanpartener.adapters.PdfDocumentAdpter;
import com.addukkanpartener.adapters.PrescriptionItemDetailsAdapter;
import com.addukkanpartener.adapters.PrescriptionItemDetailsAdapter2;
import com.addukkanpartener.databinding.ActivityPrescriptionDetails1Binding;
import com.addukkanpartener.databinding.ActivityPrintBinding;
import com.addukkanpartener.language.Language;
import com.addukkanpartener.models.OrderModel;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import io.paperdb.Paper;

import static android.os.Build.VERSION_CODES.KITKAT;

public class PrintActivity extends AppCompatActivity {

    private ActivityPrintBinding binding;
    private String lang = "ar";
    private OrderModel orderModel;
    private PrescriptionItemDetailsAdapter2 adapter;
    private Image image;
    private final String write_perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int write_req = 100;
    private Context context;

    @Override
    protected void attachBaseContext(Context newBase) {
        context = newBase;
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
        adapter = new PrescriptionItemDetailsAdapter2(this, orderModel.getPrescription_details_fk());
        binding.recView.setAdapter(adapter);
        binding.llBack.setOnClickListener(v -> finish());

        binding.btnPrint.setOnClickListener(v -> {
            checkWritePermission();
        });


    }

    private void checkWritePermission() {

        if (ContextCompat.checkSelfPermission(this, write_perm) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{write_perm}, write_req);

        } else {

            takeScreenshot();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == write_req && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takeScreenshot();
            Log.e("33", "33");

        }
    }


    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "share"+ ".jpeg";

            // create bitmap screen capture
            binding.scrollView.setDrawingCacheEnabled(true);
            Bitmap bitmap = getBitmapFromView(binding.scrollView, binding.scrollView.getHeight(), binding.scrollView.getWidth());
            binding.scrollView.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //setting screenshot in imageview
            String filePath = imageFile.getPath();
            Log.e("ddlldld", filePath);
            if(Build.VERSION.SDK_INT>=KITKAT)
                convertPDF(filePath);
//   Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        } catch (Exception e) {
            // Several error may come out with file handling or DOM
            Log.e("ddlldld", e.toString());
        }
    }

    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    //    public void openBT(ScanResult scanResult) throws IOException {
//        try {
//            dialog2.dismiss();
//
//
//            // Standard SerialPortService ID
//            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
//            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
//            mmSocket.connect();
//            mmOutputStream = mmSocket.getOutputStream();
//            inputStream = mmSocket.getInputStream();
//
//            beginListenForData();
//
//             myLabel.setText("Bluetooth Opened");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /*
//     * after opening a connection to bluetooth printer device,
//     * we have to listen and check if a data were sent to be printed.
//     */
//    void sendData(String strPath) throws IOException {
//
//
//        Bitmap imageBit = BitmapFactory.decodeFile(strPath);
//
//        ByteArrayOutputStream blob = new ByteArrayOutputStream();
//        imageBit.compress(Bitmap.CompressFormat.PNG, 0, blob);
//        byte[] bitmapdata = blob.toByteArray();
//
//     //   binding.image.setImageBitmap(imageBit);
//
//        findBT();
//
//     //   mmOutputStream.write(bitmapdata);
//        // tell the user data were sent
//        //  myLabel.setText("Data Sent");
//
//    }
//
//    void beginListenForData() {
////        try {
////            final Handler handler = new Handler();
////
////            // this is the ASCII code for a newline character
////            final byte delimiter = 10;
////
////            stopWorker = false;
////            readBufferPosition = 0;
////            readBuffer = new byte[1024];
////
////            workerThread = new Thread(new Runnable() {
////                public void run() {
////
////                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
////
////                        try {
////
////                            int bytesAvailable = inputStream.available();
////
////                            if (bytesAvailable > 0) {
////
////                                byte[] packetBytes = new byte[bytesAvailable];
////                                inputStream.read(packetBytes);
////
////                                for (int i = 0; i < bytesAvailable; i++) {
////
////                                    byte b = packetBytes[i];
////                                    if (b == delimiter) {
////
////                                        byte[] encodedBytes = new byte[readBufferPosition];
////                                        System.arraycopy(
////                                                readBuffer, 0,
////                                                encodedBytes, 0,
////                                                encodedBytes.length
////                                        );
////
////                                        // specify US-ASCII encoding
////                                        final String data = new String(encodedBytes, "US-ASCII");
////                                        readBufferPosition = 0;
////
////                                        // tell the user data were sent to bluetooth printer device
////                                        handler.post(new Runnable() {
////                                            public void run() {
////                                                // myLabel.setText(data);
////                                            }
////                                        });
////
////                                    } else {
////                                        readBuffer[readBufferPosition++] = b;
////                                    }
////                                }
////                            }
////
////                        } catch (IOException ex) {
////                            stopWorker = true;
////                        }
////
////                    }
////                }
////            });
////
////            workerThread.start();
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//    }
////    void closeBT() throws IOException {
////        try {
////            stopWorker = true;
////            mmOutputStream.close();
////            mmInputStream.close();
////            mmSocket.close();
////            myLabel.setText("Bluetooth Closed");
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//private void checkWifi(){
//    IntentFilter filter = new IntentFilter();
//    filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//    final WifiManager wifiManager =
//            (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);;
//    registerReceiver(new BroadcastReceiver(){
//        @Override
//        public void onReceive(Context arg0, Intent arg1) {
//            // TODO Auto-generated method stub
//            Log.d("wifi","Open Wifimanager");
//
//            String scanList = wifiManager.getScanResults().toString();
//            Log.d("wifi","Scan:"+scanList);
//        }
//    },filter);
//    wifiManager.startScan();
//}
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void convertPDF(String path) {
        String FILE = Environment.getExternalStorageDirectory().toString() + "/FirstPdf.pdf";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();

            try {
                image = Image.getInstance(path);
                // image.getHeight();
                //    document.setPageSize(new Rectangle(image.getAbsoluteX(),image.getAbsoluteY()));
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                        - document.rightMargin() - 0) / image.getWidth()) * 100;
                float scaler1 = ((document.getPageSize().getHeight() - document.bottom()
                        - document.topMargin() - 0) / image.getHeight());// 0 means you have no indentation. If you have any, change it.
                image.scalePercent(scaler);
//                image.setAbsolutePosition(
//                        (document.getPageSize().getWidth() - image.getScaledWidth()) / 2,
//                        (document.getPageSize().getHeight() - image.getScaledHeight()) / 2);
                image.scaleAbsoluteHeight(scaler1);
                image.scaleToFit(document.getPageSize().getWidth(), document.getPageSize().getHeight() - 80);
                image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                //  document.setPageSize(new com.itextpdf.text.Rectangle(image.getWidth(), image.getScaledHeight() * 200));

                document.add(image);
                document.close();
                //  document.add(new Paragraph("My Heading"));
                printpdf();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (DocumentException e) {
            Log.e("message1", e.toString());
        } catch (FileNotFoundException e) {
            Log.e("message2", e.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printpdf() {

        PrintManager printManager = (PrintManager) context.getSystemService(PRINT_SERVICE);
        PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdpter(context, Environment.getExternalStorageDirectory().toString() + "/FirstPdf.pdf");
        printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());


        try {
                 } catch (Exception e) {
            Log.e("sssssss", e.getMessage());
        }
    }
}