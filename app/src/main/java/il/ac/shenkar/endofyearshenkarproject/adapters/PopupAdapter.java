package il.ac.shenkar.endofyearshenkarproject.adapters;

/**
 * Created by oranmoshe on 7/8/16.
 */

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import il.ac.shenkar.endofyearshenkarproject.R;
import il.ac.shenkar.endofyearshenkarproject.utils.DownloadImageTask;

public class PopupAdapter implements InfoWindowAdapter {
    private View popup=null;
    private LayoutInflater inflater=null;
    private String imageUrl = null;

    public PopupAdapter(LayoutInflater inflater,String imageUrl) {
        this.inflater=inflater;
        this.imageUrl = imageUrl;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup=inflater.inflate(R.layout.popup, null);
        }

        TextView tv=(TextView)popup.findViewById(R.id.title);
        tv.setText(marker.getTitle());

        tv=(TextView)popup.findViewById(R.id.snippet);
        tv.setText(marker.getSnippet());

        ImageView imageView = (ImageView)popup.findViewById(R.id.iconDepartment);
        new DownloadImageTask(imageView).execute(imageUrl);
        return(popup);
    }
}