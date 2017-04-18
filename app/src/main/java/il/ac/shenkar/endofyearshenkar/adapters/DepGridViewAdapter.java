package il.ac.shenkar.endofyearshenkar.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.json.DepartmentJson;
import il.ac.shenkar.endofyearshenkar.json.JsonURIs;
import il.ac.shenkar.endofyearshenkar.utils.DownloadImageTask;

public class DepGridViewAdapter extends ArrayAdapter<DepartmentJson> {

    private final RequestQueue requestQueue;
    private Context context;
    private int layoutResourceId;
    private List<DepartmentJson> data;
    private ProgressDialog mProgressDialog;

    public DepGridViewAdapter(Context context, int layoutResourceId, List<DepartmentJson> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final DepartmentJson item = data.get(position);
        holder.imageTitle.setText(item.getName());

        new DownloadImageTask(holder.image).execute(item.getImageUrl());

        return row;
    }

    public void refresh() {

        mProgressDialog = ProgressDialog.show(context, "טוען נתונים", "מעדכן מחלקות", true, true);

        JsonArrayRequest req = new JsonArrayRequest(JsonURIs.getDepartmentsByCollegeIdUri(JsonURIs.SHENKAR_COLLEGE_ID),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        final List<DepartmentJson> departments = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                departments.add(new Gson().fromJson(response.getString(i), DepartmentJson.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //show complition in UI
                        //fill grid view with data
                        mProgressDialog.dismiss();

                        data.clear();
                        data.addAll(departments);
                        notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        mProgressDialog.dismiss();
                    }
                });

        requestQueue.add(req);
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}