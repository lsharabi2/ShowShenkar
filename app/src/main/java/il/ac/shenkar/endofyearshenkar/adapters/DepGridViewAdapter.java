package il.ac.shenkar.endofyearshenkar.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import il.ac.shenkar.endofyearshenkar.activities.MainActivity;
import il.ac.shenkar.endofyearshenkar.activities.StaticCollegeConfigJson;
import il.ac.shenkar.endofyearshenkar.db.DepartmentDbHelper;
import il.ac.shenkar.endofyearshenkar.json.DepartmentJson;
import il.ac.shenkar.endofyearshenkar.json.JsonURIs;

public class DepGridViewAdapter extends ArrayAdapter<DepartmentJson> {

    private final RequestQueue requestQueue;
    private final DepartmentDbHelper mDbHelper;
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
        mDbHelper = new DepartmentDbHelper(context);
        System.out.println("Lior DepGridViewAdapter");
        addAll(mDbHelper.getAll());
    }

    public static StateListDrawable makeSelector(int color) {
        StateListDrawable res = new StateListDrawable();
        res.setExitFadeDuration(400);
        res.setAlpha(45);
        res.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(color));
        res.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(color));
        res.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
        return res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final ViewHolder holder;
        System.out.println("Liron getView adapter");
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //  LayoutInflater inflater = getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.layout = (LinearLayout) row.findViewById(R.id.item_layout);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final DepartmentJson item = data.get(position);
        holder.imageTitle.setText(item.getName());

        if (StaticCollegeConfigJson.mMainConfig != null) {
            holder.imageTitle.setTextColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getMainTextColor()));
            holder.imageTitle.setBackgroundColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getSecondaryColor()));
            holder.image.setBackgroundColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getSecondaryColor()));
            holder.layout.setBackgroundDrawable(makeSelector(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getPrimaryColor())));
        }

        ((MainActivity) getContext()).getImageFetcher().loadImage(item.getImageUrl(), holder.image);
        //new DownloadImageTask(holder.image).execute(item.getImageUrl());

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

                        //show completion in UI
                        //fill grid view with data
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }

                        data.clear();
                        data.addAll(departments);
                        notifyDataSetChanged();
                        replaceDepartmentsDb(departments);
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

    private void replaceDepartmentsDb(List<DepartmentJson> departmentJsons) {
        mDbHelper.clearAll();

        for (DepartmentJson json : departmentJsons) {
            mDbHelper.insertDepartment(json);
        }
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
        LinearLayout layout;
    }
}