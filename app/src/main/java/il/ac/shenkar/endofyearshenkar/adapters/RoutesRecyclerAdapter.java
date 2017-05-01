package il.ac.shenkar.endofyearshenkar.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.activities.SuggestedRouteActivity;
import il.ac.shenkar.endofyearshenkar.json.JsonURIs;
import il.ac.shenkar.endofyearshenkar.json.RouteJson;

public class RoutesRecyclerAdapter extends RecyclerView.Adapter<RoutesRecyclerAdapter.CustomViewHolder> {
    private static final String TAG = "RoutesRecyclerAdapter";
    private List<RouteJson> mRoutes;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    public RoutesRecyclerAdapter(Context context, List<RouteJson> routes) {
        this.mRoutes = routes;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.route_row, viewGroup, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        RouteJson route = mRoutes.get(i);

        customViewHolder.route = route;
        customViewHolder.txtRouteName.setText(route.getName());
    }

    @Override
    public int getItemCount() {
        return (null != mRoutes ? mRoutes.size() : 0);
    }

    public void refresh() {
        final String url = JsonURIs.getRoutessByCollegeIdUri(JsonURIs.SHENKAR_COLLEGE_ID);

        new AsyncTask<Void, Void, List<RouteJson>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = ProgressDialog.show(mContext, "טוען נתונים", "מעדכן מסלולים", true, true);
            }

            @Override
            protected List<RouteJson> doInBackground(Void... params) {
                try {

                    // Instantiate the RequestQueue.
                    RequestQueue requestQueue = Volley.newRequestQueue(RoutesRecyclerAdapter.this.mContext);

                    RequestFuture<JSONArray> future = RequestFuture.newFuture();

                    JsonArrayRequest req = new JsonArrayRequest(url, future, future);

                    // Add the request to the RequestQueue.
                    requestQueue.add(req);

                    JSONArray response = future.get(3, TimeUnit.SECONDS);

                    List<RouteJson> routes = new ArrayList<RouteJson>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            routes.add(new Gson().fromJson(response.getString(i), RouteJson.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    return routes;
                } catch (InterruptedException e) {
                    Log.d(TAG, "interrupted error");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Log.d(TAG, "execution error");
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<RouteJson> routes) {
                //show complition in UI
                //fill grid view with data
                mProgressDialog.dismiss();
                if (routes != null) {
                    mRoutes.clear();
                    mRoutes.addAll(routes);
                    notifyDataSetChanged();
                }
            }
        }.execute();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView txtRouteName;
        RouteJson route;

        public CustomViewHolder(View view) {
            super(view);
            this.txtRouteName = (TextView) view.findViewById(R.id.route_name);
            txtRouteName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Create intent
            Intent intent = new Intent(mContext, SuggestedRouteActivity.class);
            intent.putExtra("id", route.getId());
            intent.putExtra("title", txtRouteName.getText().toString());
            intent.putExtra("route", route);

            //Start details activity
            mContext.startActivity(intent);
        }
    }
}