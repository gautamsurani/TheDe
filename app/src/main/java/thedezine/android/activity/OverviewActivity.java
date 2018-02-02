package thedezine.android.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import thedezine.android.R;
import thedezine.android.activity.EnquiryActivity;
import thedezine.android.utils.Tools;


public class OverviewActivity extends AppCompatActivity {


    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tvvv);
        /*toolbar=(Toolbar)findViewById(R.id.toolbar);
        Tools.systemBarLolipop(this);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/


    }

    public void set(View view){
       // onShareClick(view);
        /*Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"divyesh_asd@ymail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Hi");
        email.putExtra(Intent.EXTRA_TEXT, "Hi,This");

        email.setType("text/plain");*/

      //  final Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("+919409663162"));

      final  Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"divyesh_asd@ymail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hi");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi,This is Test");

        intent.setType("text/plain");

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View alertlayout = inflater.inflate(R.layout.getintents_view, null);

        ListView listview=(ListView)alertlayout.findViewById(R.id.listview);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> launchables=pm.queryIntentActivities(intent, 0);
        List<LabeledIntent> otherAppIntentList = new ArrayList<LabeledIntent>();

        for (int i = 0; i < launchables.size(); i++) {
            ResolveInfo ri = launchables.get(i);
            String packageName = ri.activityInfo.packageName;
            Intent intentToAdd = new Intent();
            Log.e("EICONS",ri.getIconResource()+"");
            otherAppIntentList.add(new LabeledIntent(intentToAdd, packageName,ri.loadLabel(pm), ri.getIconResource()));
        }

        Log.e("GETww33",otherAppIntentList.size()+"");
        final AppAdapter adapter=new AppAdapter(pm, launchables);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ResolveInfo launchable=adapter.getItem(i);
                ActivityInfo activity=launchable.activityInfo;
                ComponentName name=new ComponentName(activity.applicationInfo.packageName,
                        activity.name);

                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intent.setComponent(name);

                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.setContentView(alertlayout);
        dialog.show();
    }

    public void onShareClick(View v) {
        Resources resources = getResources();

        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "NATIVE");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SUBNATIVE");
        emailIntent.setType("message/rfc822");

        PackageManager pm = getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");

        Intent openInChooser = Intent.createChooser(emailIntent, "CHOOSE");

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        Log.e("GETww33",resInfo.size()+"");

        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;

            }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

     class AppAdapter extends ArrayAdapter<ResolveInfo> {

        List<ResolveInfo> launchables;
        PackageManager pm=null;
        private LayoutInflater inflater;

         AppAdapter(PackageManager pm, List<ResolveInfo> apps) {
            super(OverviewActivity.this,R.layout.sections_items,apps);
            this.launchables=apps;
            this.pm=pm;
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

         @Override
         public View getView(int position, View convertView,
                             ViewGroup parent) {
             if (convertView==null) {
                 convertView=newView(parent);
             }

             bindView(position, convertView);

             return(convertView);
         }

         private View newView(ViewGroup parent) {
             return(getLayoutInflater().inflate(R.layout.sections_items, parent, false));
         }

         private void bindView(int position, View row) {
             TextView label=(TextView)row.findViewById(R.id.name);

             label.setText(getItem(position).loadLabel(pm));

             ImageView icon=(ImageView)row.findViewById(R.id.img);

             icon.setImageDrawable(getItem(position).loadIcon(pm));
         }
     }

}
