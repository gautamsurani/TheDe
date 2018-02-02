package thedezine.android.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import thedezine.android.R;
import thedezine.android.utils.Global;
import thedezine.android.utils.Tools;

public class PortifolioDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    Context context;
    Global global;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    AlertDialog packsizeDialog;
    ImageView portfolioIv;
    TextView portfolioTv, mainTitle;
    private SliderLayout mDemoSlider;
    PagerIndicator presetIndicators;
    Toolbar toolbar;
    TextView entName,entAbout,entCont,entMail,entTalk,entDesc;
    Button sendBTN;
    String name="",about="",phone="",mail="",talk="",desc="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portifoliodetails);

        portfolioIv = (ImageView) findViewById(R.id.portfolioIv);
        portfolioTv = (TextView) findViewById(R.id.portfolioTv);
        mainTitle = (TextView) findViewById(R.id.mainTitle);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        presetIndicators = (PagerIndicator) findViewById(R.id.custom_indicator);
        context = this;
        global = new Global(context);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        sendBTN = (Button) findViewById(R.id.sendBTN);

        entName= (TextView) findViewById(R.id.entName);
        entAbout= (TextView) findViewById(R.id.entAbout);
        entCont= (TextView) findViewById(R.id.entCont);
        entMail= (TextView) findViewById(R.id.entMail);
        entTalk= (TextView) findViewById(R.id.entTalk);
        entDesc=(TextView) findViewById(R.id.entDesc);

        entName.setText(Html.fromHtml("<u>Enter Name</u>"));
        entAbout.setText(Html.fromHtml("<u>Select</u>"));
        entCont.setText(Html.fromHtml("<u>Enter Contact No</u>"+","));
        entMail.setText(Html.fromHtml("<u>Enter Email</u>"));
        entTalk.setText(Html.fromHtml("<u>When Can We Talk?</u>"));
        entDesc.setText(Html.fromHtml("<u>Please briefly describe your idea</u>"));


        entName.setOnClickListener(this);
        entAbout.setOnClickListener(this);
        entCont.setOnClickListener(this);
        entMail.setOnClickListener(this);
        entTalk.setOnClickListener(this);
        entDesc.setOnClickListener(this);
        sendBTN.setOnClickListener(this);


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            collapsingToolbarLayout.setTitle(b.getString("portfolioTitle"));
            mainTitle.setText(b.getString("portfolioTitle"));
            Glide.with(context)
                    .load(b.getString("portfolioImage").trim())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.default_icon).into(portfolioIv);
            portfolioTv.setText(b.getString("portfolioDesc").trim());

        } else {
            collapsingToolbarLayout.setTitle("");
        }

        Tools.systemBarLolipop(this);

        DefaultSliderView textSliderView = new DefaultSliderView(this);
        textSliderView.image(R.drawable.webdev);

        DefaultSliderView textSliderView2 = new DefaultSliderView(this);
        textSliderView2.image(R.drawable.webdev);

        mDemoSlider.addSlider(textSliderView);
        mDemoSlider.addSlider(textSliderView2);
        mDemoSlider.setCustomIndicator(presetIndicators);
        mDemoSlider.setDuration(4000);

    }

    @Override
    public void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    public void GenerateAlertDialog(final TextView entName,String type,int inputtype){

        final AlertDialog.Builder builder = new AlertDialog.Builder(PortifolioDetailsActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") final View alertlayout = inflater.inflate(R.layout.edt_dialog, null);

        final TextInputEditText anyoneEDT = (TextInputEditText) alertlayout.findViewById(R.id.anyoneEDT);

        if (inputtype==1){
            anyoneEDT.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        }else if (inputtype==2){
            anyoneEDT.setInputType(InputType.TYPE_CLASS_NUMBER);
        }else if (inputtype==3){
            anyoneEDT.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }else if (inputtype==4){
            anyoneEDT.setMaxLines(7);
            anyoneEDT.setLines(5);
            anyoneEDT.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            anyoneEDT.setGravity(GravityCompat.START);
            anyoneEDT.setVerticalScrollBarEnabled(true);
            anyoneEDT.setMovementMethod(ScrollingMovementMethod.getInstance());
        }

        anyoneEDT.setHint(type);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hideKeyBoard(entName);
                if (anyoneEDT.getText().toString().length()>1){
                    entName.setText(anyoneEDT.getText().toString().trim());
                    packsizeDialog.dismiss();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hideKeyBoard(entName);
                packsizeDialog.dismiss();
            }
        });

        builder.setView(alertlayout);
        packsizeDialog = builder.create();
        packsizeDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        packsizeDialog.show();

       /* packsizeDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = packsizeDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                b.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        });*/
    }


    public void hideKeyBoard(final View view){

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 50);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.entName:
                GenerateAlertDialog(entName,"Enter Name",1);
                break;
            case R.id.entAbout:

                PopupMenu popup2 = new PopupMenu(PortifolioDetailsActivity.this, entAbout);
                popup2.getMenu().add("Web Design");
                popup2.getMenu().add("Web Development");
                popup2.getMenu().add("Mobile App");
                popup2.getMenu().add("Branding");
                popup2.getMenu().add("SEO");
                popup2.getMenu().add("Domain");
                popup2.getMenu().add("Hosting");

                popup2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {

                        entAbout.setText(item.getTitle());
                        /*Toast.makeText(PortifolioDetailsActivity.this,"You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();*/

                        return true;
                    }
                });

                popup2.show();


                break;
            case R.id.entCont:
                GenerateAlertDialog(entCont,"Enter Mobile",2);
                break;
            case R.id.entMail:
                GenerateAlertDialog(entMail,"Enter Email",3);
                break;
            case R.id.entTalk:
                PopupMenu popup = new PopupMenu(PortifolioDetailsActivity.this, entTalk);
                popup.getMenu().add("Morning");
                popup.getMenu().add("Afternoon");
                popup.getMenu().add("Evening");
                popup.getMenu().add("Anytime");
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    public boolean onMenuItemClick(MenuItem item) {

                        entTalk.setText(item.getTitle());
                        /*Toast.makeText(PortifolioDetailsActivity.this,"You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();*/

                        return true;
                    }
                });

                popup.show();
                break;
            case R.id.entDesc:
                GenerateAlertDialog(entDesc,"Enter Description",4);
                break;
            case R.id.sendBTN:

                sendingData();
                break;
            default:
                break;

        }
    }

    private void sendingData() {
        name=entName.getText().toString().trim();
        about=entAbout.getText().toString().trim();
        phone=entCont.getText().toString().trim();
        mail=entMail.getText().toString().trim();
        talk=entTalk.getText().toString().trim();
        desc=entDesc.getText().toString().trim();


        Log.e("senddata",name+about+phone+mail+talk+desc);

    }
}
