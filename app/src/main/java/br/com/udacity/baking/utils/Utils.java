package br.com.udacity.baking.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import br.com.udacity.baking.R;

public class Utils {

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static int getMeasureResource(String measure) {
        measure = measure.toUpperCase();

        int resource;
        switch (measure) {
            case "CUP":
                resource = R.string.measure_cup;
                break;
            case "TBLSP":
                resource = R.string.measure_tblsp;
                break;
            case "TSP":
                resource = R.string.measure_tsp;
                break;
            case "UNIT":
                resource = R.string.measure_unit;
                break;
            case "G":
                resource = R.string.measure_grams;
                break;
            case "K":
                resource = R.string.measure_kilo;
                break;
            case "OZ":
                resource = R.string.measure_oz;
                break;
            default:
                resource = -1; // unknown measure
        }

        return resource;
    }
}
