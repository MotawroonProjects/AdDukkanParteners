package com.addukkanpartener.general_ui_method;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.addukkanpartener.R;
import com.addukkanpartener.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }








    @BindingAdapter("image")
    public static void image(View view, String endPoint) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).into(imageView);
            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).fit().into(imageView);
            }
        }

    }


    @BindingAdapter("user_image")
    public static void user_image(View view, String endPoint) {

        Log.e("Ddd", Tags.IMAGE_URL + endPoint);
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.ic_avatar).into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.ic_avatar).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (endPoint != null) {

                Picasso.get().load(Uri.parse(Tags.IMAGE_URL + endPoint)).placeholder(R.drawable.ic_avatar).fit().into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_avatar).into(imageView);

            }
        }

    }


    @BindingAdapter("date")
    public static void date(TextView textView,long date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy",Locale.ENGLISH);
        String d = dateFormat.format(new Date(date * 1000));
        textView.setText(d);
    }

    @BindingAdapter("date2")
    public static void date2(TextView textView,String date){
        String d = date.split(" ")[0];
        textView.setText(d);
    }

    @BindingAdapter("time")
    public static void time(TextView textView,String time){
        Log.e("time",time+"_");

        String t = time.split(" ").length>=2?time.split(" ")[1]:"";
        textView.setText(t);
    }
}










