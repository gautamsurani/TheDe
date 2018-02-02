package thedezine.android.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import thedezine.android.R;
import thedezine.android.utils.Global;
import thedezine.android.utils.Tools;


public class EnquiryActivity extends AppCompatActivity {


    Context context;
    Global global;
    TextView selectionTEXT;
    ProgressDialog progressDialog;
    private AlertDialog packsizeDialog;
    private Button submitInquiry;
    private EditText et_content;
  //  public static EnquiryAdapter adapter;
    String selectedTEXT="";

    private ListView listview;
    private ActionBar actionBar;
  /*  private Friend friend;
    private List<ChatsDetails> items = new ArrayList<>();*/
    private View parent_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);
        context = this;

        global = new Global(context);
        initToolbar();
        selectionTEXT=(TextView)findViewById(R.id.selectionTEXT);
        submitInquiry=(Button)findViewById(R.id.submitInquiry);
        Tools.systemBarLolipop(this);

        submitInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Toast.makeText(EnquiryActivity.this, "Submit", Toast.LENGTH_SHORT).show();
            }
        });
        selectionTEXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") final View alertlayout = inflater.inflate(R.layout.alertdialog_view, null);

                final RadioGroup radioGroup=(RadioGroup)alertlayout.findViewById(R.id.onemain);

                /*AppCompatRadioButton radioButton = (AppCompatRadioButton)alertlayout.findViewById(R.id.one2);

                radioButton.setSupportButtonTintList(
                        ContextCompat.getColorStateList(EnquiryActivity.this,
                                R.color.rbtn_selector));*/

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (radioGroup.getCheckedRadioButtonId()!= -1){
                            RadioButton t=(RadioButton)alertlayout.findViewById(radioGroup.getCheckedRadioButtonId());
                            selectionTEXT.setText(t.getText().toString().trim());
                            packsizeDialog.dismiss();
                        }

                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        packsizeDialog.dismiss();
                    }
                });

                builder.setView(alertlayout);
                packsizeDialog=builder.create();
                packsizeDialog.show();
            }
        });
      }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
     //   toolbar.setNavigationIcon(R.drawable.ic_back_white);
       /* TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setTextColor(getResources().getColor(R.color.cattextcolour));*/
        toolbar.setTitle("My Inquiry");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

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
