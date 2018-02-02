package thedezine.android.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

import thedezine.android.R;
import thedezine.android.adapter.ServiceAdapter;
import thedezine.android.model.ServiceModel;
import thedezine.android.utils.Global;
import thedezine.android.utils.Tools;


public class ServiceActivity extends AppCompatActivity {

    Global global;
    RecyclerView serviceRecycle;
    GridLayoutManager gridLayoutManager;
    ServiceAdapter adapter;
    List<ServiceModel> serviceModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        global = new Global(this);

        Tools.systemBarLolipop(this);

        serviceRecycle=(RecyclerView)findViewById(R.id.serviceRecycle);
        gridLayoutManager=new GridLayoutManager(this,2);
        serviceRecycle.setHasFixedSize(true);

        serviceModels=new ArrayList<>();

       // serviceModels.add();
        adapter=new ServiceAdapter(this,serviceModels);


        serviceRecycle.setAdapter(adapter);
        serviceRecycle.setLayoutManager(gridLayoutManager);

        adapter.setOnItemClickListener(new ServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int type) {
                if (type==0){

                    Toast.makeText(ServiceActivity.this,"Clicked", Toast.LENGTH_SHORT).show();

                }else {

                }
            }
        });

    }
}
