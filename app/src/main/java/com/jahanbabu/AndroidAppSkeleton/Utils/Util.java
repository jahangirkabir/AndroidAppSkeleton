package com.jahanbabu.AndroidAppSkeleton.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Created by jahangirkabir on 4/25/17.
 */

public class Util {

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static void showNoInternetDialog(final Context context, final Activity activity) {
        AlertDialog dailog;
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setMessage("This application requires Internet connection.\nWould you connect to internet ?");
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        build.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog dailog;
//				ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.MyAlertDialogStyle);
                AlertDialog.Builder build = new AlertDialog.Builder(context);
                build.setMessage("Are sure you want to exit?");
                build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                });
                build.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dailog = build.create();
                dailog.show();
            }
        });
        dailog = build.create();
        dailog.show();
    }
}
