package il.ac.shenkar.endofyearshenkar.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.List;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.activities.SuggestedRouteActivity;

import il.ac.shenkar.showshenkar.backend.routeApi.RouteApi;
import il.ac.shenkar.showshenkar.backend.routeApi.model.Route;
import il.ac.shenkar.endofyearshenkar.utils.Constants;

public class RoutesRecyclerAdapter extends RecyclerView.Adapter<RoutesRecyclerAdapter.CustomViewHolder> {
    private List<Route> mRoutes;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    public RoutesRecyclerAdapter(Context context, List<Route> routes) {
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
        Route route = mRoutes.get(i);

        customViewHolder.routeId = route.getId();
        customViewHolder.txtRouteName.setText(route.getName());
    }

    @Override
    public int getItemCount() {
        return (null != mRoutes ? mRoutes.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView txtRouteName;
        protected Long routeId;

        public CustomViewHolder(View view) {
            super(view);
            this.txtRouteName = (TextView) view.findViewById(R.id.route_name);
            txtRouteName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Create intent
            Intent intent = new Intent(mContext, SuggestedRouteActivity.class);
            intent.putExtra("id", routeId);
            intent.putExtra("title", txtRouteName.getText().toString());

            //Start details activity
            mContext.startActivity(intent);
        }
    }

    public void refresh() {
        final RouteApi routeApi = new RouteApi.Builder(
                AndroidHttp.newCompatibleTransport(),
                new JacksonFactory(),
                new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {

                    }
                }).setRootUrl(Constants.ROOT_URL).build();

        new AsyncTask<Void, Void, List<Route>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = ProgressDialog.show(mContext, "טוען נתונים", "מעדכן מסלולים", true, true);
            }

            @Override
            protected List<Route> doInBackground(Void... params) {
                List<Route> routes = null;
                try {
                    routes = routeApi.getRoutes().execute().getItems();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return routes;
            }

            @Override
            protected void onPostExecute(List<Route> routes) {
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
}