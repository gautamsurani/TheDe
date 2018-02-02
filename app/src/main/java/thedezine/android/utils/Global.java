package thedezine.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by theonetech25 on 8/31/2015.
 */
public class Global {

    Context mContext;

    public Global(Context mCon) {
        this.mContext = mCon;
    }

    public synchronized boolean isNetworkAvailable() {
        boolean flag = false;

        if (checkNetworkAvailable()) {
            flag = true;

        } else
        {
            flag = false;
          //  Toast.makeText(mContext,"No network available!",Toast.LENGTH_SHORT).show();
            Log.d("", "No network available!");
        }
        return flag;
    }

    private boolean checkNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void setPrefBoolean(String Tag, Boolean isBool) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Tag, isBool);
        editor.commit();
    }

    public boolean getPrefBoolean(String Tag, Boolean isBool)
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getBoolean(Tag, isBool);
    }

    public void setPrefString(String Tag, String value) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Tag, value);
        editor.commit();
    }

    public String getPrefString(String Tag, String value) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(Tag, value);
    }


    public void showMessage(String message)
    {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public Date convertStringToDate(String datetime) {
        Log.i("Convert time to date", datetime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMM, yyyy hh:mm a");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(datetime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }
}
