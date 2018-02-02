package thedezine.android.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.bohush.geometricprogressview.GeometricProgressView;

import thedezine.android.R;

/**
 * Created by welcome on 28-02-2017.
 */

public class Dummt extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy);

        GeometricProgressView progressView = (GeometricProgressView) findViewById(R.id.progressView);
        progressView.setType(GeometricProgressView.TYPE.TRIANGLE);
        progressView.setNumberOfAngles(25);
        progressView.setColor(Color.parseColor("#00A8E2"));
        progressView.setDuration(1100);
        progressView.setFigurePadding(getResources().getDimensionPixelOffset(R.dimen.dummy));

    }
}
