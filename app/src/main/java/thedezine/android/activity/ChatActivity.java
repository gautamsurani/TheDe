package thedezine.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import thedezine.android.R;
import thedezine.android.activity.EnquiryActivity;
import thedezine.android.utils.Global;
import thedezine.android.utils.Tools;


public class ChatActivity extends AppCompatActivity {


    Context context;
    Global global;
    ProgressDialog progressDialog;

    private Button btn_send;
    private EditText et_content;
  //  public static EnquiryAdapter adapter;
    FloatingActionButton chatFAB;
    private ListView listview;
    private ActionBar actionBar;
  /*  private Friend friend;
    private List<ChatsDetails> items = new ArrayList<>();*/
    private View parent_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        context = this;
        chatFAB=(FloatingActionButton)findViewById(R.id.chatFAB);
        global = new Global(context);


        initToolbar();

        initComp();

        Tools.systemBarLolipop(this);

        chatFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, EnquiryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setTitle("My Chat History");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

      }

    private void initComp() {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            // Toast.makeText(MainCategoriesActivity.this, "BackWorking", Toast.LENGTH_SHORT).show();
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




}
