package thedezine.android.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import thedezine.android.R;
import thedezine.android.activity.SplashActivity;
import thedezine.android.utils.Global;


public class MapFragment extends Fragment implements OnMapReadyCallback {


    Global global;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        global = new Global(getActivity());


    }

    private boolean _hasLoadedOnces = false; // your boolean field

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);


        if (this.isVisible()) {
            if (isFragmentVisible_ && !_hasLoadedOnces) {
                if (global.isNetworkAvailable()) {

                    FragmentManager fragmentManager = this.getChildFragmentManager();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager
                            .findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(this);

                } else {
                    // Utils.showToastShort("Please Check Your Internet Connection", getActivity());
                }
                _hasLoadedOnces = true;
            }
        }
    }

    /*@Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        114);

            } else {
                map.setMyLocationEnabled(true);
                map.setTrafficEnabled(true);
                map.setIndoorEnabled(true);

                map.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                map.getUiSettings().setZoomControlsEnabled(true);
            }
        } else {

            map.setMyLocationEnabled(true);
            map.setTrafficEnabled(true);
            map.setIndoorEnabled(true);

            map.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
            map.getUiSettings().setZoomControlsEnabled(true);
        }

    }
*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {
            case 114: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {


                        Log.d("Permissions MAP", "Permission Granted: " + permissions[i]);

                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions MAP", "Permission Denied: " + permissions[i]);
                        Toast.makeText(getActivity(),"Location Permission Denied",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            break;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {


        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        if (Build.VERSION.SDK_INT >= 23) {

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        114);

            } else {
                getMAP(map);

            }
        } else {
            getMAP(map);

        }


    }

    public void getMAP(GoogleMap map) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.setIndoorEnabled(true);
       // map.setBuildingsEnabled(true);

       /* MarkerOptions markerOption = new MarkerOptions().position(
                new LatLng(Double.parseDouble(AddressFragment.latitude), Double.parseDouble(AddressFragment.logitude)));
        markerOption.title("The Dezine");
        markerOption.snippet("Description");*/

        LatLng TD = new LatLng(Double.parseDouble(AddressFragment.latitude), Double.parseDouble(AddressFragment.logitude));
        map.addMarker(new MarkerOptions().position(TD).visible(true).title("The Dezine").snippet("Description")).showInfoWindow();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(TD).zoom(15).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


      //  Marker currentMarker = map.addMarker(markerOption);

       // currentMarker.showInfoWindow();
      //  map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(AddressFragment.latitude), Double.parseDouble(AddressFragment.logitude))));
     //   map.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        map.getUiSettings().setZoomControlsEnabled(true);


    }

}
