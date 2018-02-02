package thedezine.android.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import thedezine.android.R;
import thedezine.android.activity.JivoDelegate;
import thedezine.android.activity.JivoSdk;
import thedezine.android.activity.MainActivity;


public class ChatFragment extends AppCompatActivity implements JivoDelegate {

    JivoSdk jivoSdk;
    Toolbar toolbar;
    String myEMAIL="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);

        Bundle b=getIntent().getExtras();
        if (b!=null){
            myEMAIL=b.getString("EMAIL");
        }
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        jivoSdk = new JivoSdk((WebView)findViewById(R.id.webview), "en");
        jivoSdk.delegate=this;
        jivoSdk.prepare();

        toolbar.setTitle("The Dezine");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public void onEvent(String name, String data) {

        if(name.equals("url.click")){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
            startActivity(browserIntent);
        }else if (name.equalsIgnoreCase("connection.connect")){
            Log.e("eventsreceive",name+"\n"+data);
            jivoSdk.execJS("window._store('client_name','PATEL')");
            jivoSdk.execJS("window._store('email','"+myEMAIL+"')");
            jivoSdk.execJS("window._store('phone','125412541')");
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
        finish();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);


    }
}
