package il.ac.shenkar.endofyearshenkar.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.activities.ProjectActivity;
import il.ac.shenkar.endofyearshenkar.activities.StaticCollegeConfigJson;
import il.ac.shenkar.endofyearshenkar.json.GsonRequest;
import il.ac.shenkar.endofyearshenkar.json.JsonURIs;
import il.ac.shenkar.endofyearshenkar.json.ProjectJson;
import il.ac.shenkar.endofyearshenkar.json.ProjectJsonStatic;

public class DepProjectsRecyclerAdapter extends RecyclerView.Adapter<DepProjectsRecyclerAdapter.CustomViewHolder> implements Filterable {
    private static String TAG = "DepProjectsRecyclerAdapter";
    private final RequestQueue requestQueue;
    private List<ProjectJson> depProjectList;
    private List<ProjectJson> filterDepProjectList;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private ValueFilter valueFilter;

    public DepProjectsRecyclerAdapter(Context context, List<ProjectJson> depProjectList) {
        this.depProjectList = depProjectList;
        this.filterDepProjectList = depProjectList;
        this.mContext = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dep_project_row, viewGroup, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        ProjectJson depProject = depProjectList.get(i);
        if (depProject == null) {
            return;
        }

        customViewHolder.projectId = depProject.getId();
        customViewHolder.txtProjectName.setText(depProject.getName());

        customViewHolder.dep_project_Line.setBackgroundColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getLineColor()));
        List<String> names = depProject.getStudentNames();

        String namesStr = "";
        for (String name : names) {
            namesStr += name + "\n";
        }

        customViewHolder.txtProjectStudent.setText(namesStr);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return (null != depProjectList ? depProjectList.size() : 0);
    }

    /*
        public void refresh(final Long routeId) {
            final RouteApi routeApi = new RouteApi.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    new JacksonFactory(),
                    new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) throws IOException {

                        }
                    }).setRootUrl(Constants.ROOT_URL).build();

            new AsyncTask<Void, Void, Route>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    mProgressDialog = ProgressDialog.show(mContext, "טוען נתונים", "מעדכן פרויקטים", true, true);
                }


                @Override
                protected Route doInBackground(Void... params) {
                    Route route = null;
                    try {
                        route = routeApi.getRoute(routeId).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return route;
                }

                @Override
                protected void onPostExecute(Route route) {
                    //show complition in UI
                    //fill grid view with data
                    mProgressDialog.dismiss();
                    if (route != null) {
                        refresh(new HashSet<String>(route.getProjectIds()));
                    }
                }
            }.execute();
        }
    */
    public void refresh(final long departmentId) {

        mProgressDialog = ProgressDialog.show(mContext, "טוען נתונים", "מעדכן פרויקטים", true, true);

        JsonArrayRequest req = new JsonArrayRequest(JsonURIs.getProjectsByDepartmentIdUri(JsonURIs.SHENKAR_COLLEGE_ID, departmentId),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        final List<ProjectJson> projects = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                projects.add(new Gson().fromJson(response.getString(i), ProjectJson.class));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        //show complition in UI
                        //fill grid view with data
                        mProgressDialog.dismiss();

                        clearAndAddProjects(projects);


//                        depProjectList.clear();
//                        depProjectList.addAll(projects);
//                        filterDepProjectList.clear();
//                        filterDepProjectList.addAll(projects);

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


    private void clearAndAddProjects(List<ProjectJson> projects) {

        ProjectJsonStatic.getProjectJsonList().clear();
        ProjectJsonStatic.setProjectJsonList(projects);

        depProjectList.clear();
        depProjectList.addAll(projects);
        filterDepProjectList.clear();
        filterDepProjectList.addAll(projects);
    }

    public ProjectJson getProjectById(long projectId) {
        try {
            final String url = JsonURIs.getProjectByIdUri(projectId);

            RequestFuture<ProjectJson> future = RequestFuture.newFuture();

            GsonRequest req = new GsonRequest(url, ProjectJson.class, null, future, future);

            RequestQueue tempQueue = Volley.newRequestQueue(mContext);

            // Add the request to the RequestQueue.
            tempQueue.add(req);

            ProjectJson response = future.get(10, TimeUnit.SECONDS);

            return response;
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

    public void refresh(final Collection<Long> projectIds) {

        new AsyncTask<Void, Void, List<ProjectJson>>() {
            @Override
            protected List<ProjectJson> doInBackground(Void... params) {
                List<ProjectJson> projects = new ArrayList<ProjectJson>();

                for (long projectId : projectIds) {
                    ProjectJson project = getProjectById(projectId);

                    if (project != null) {
                        projects.add(project);
                    }
                }
                return projects;
            }

            @Override
            protected void onPostExecute(List<ProjectJson> projects) {
                //show complition in UI
                //fill grid view with data
                if (projects != null) {

                    clearAndAddProjects(projects);

//                    depProjectList.clear();
//                    depProjectList.addAll(projects);
//                    filterDepProjectList.clear();
//                    filterDepProjectList.addAll(projects);
                    notifyDataSetChanged();
                }
            }
        }.execute();

    }

    public void clear() {
        depProjectList.clear();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected Long projectId;
        protected TextView txtProjectName;
        protected TextView txtProjectStudent;
        protected LinearLayout dep_project_Line;

        public CustomViewHolder(View view) {
            super(view);
            this.dep_project_Line = (LinearLayout) view.findViewById(R.id.dep_project_Line);
            this.txtProjectName = (TextView) view.findViewById(R.id.project_name);
            this.txtProjectStudent = (TextView) view.findViewById(R.id.project_student);
            txtProjectName.setOnClickListener(this);
            txtProjectStudent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Create intent
            String studentNames = txtProjectStudent.getText().toString();
            studentNames = studentNames.replace('\n', ' ');
            Intent intent = new Intent(mContext, ProjectActivity.class);
            intent.putExtra("project", txtProjectName.getText().toString());
            intent.putExtra("students", studentNames);
            intent.putExtra("id", projectId);

            //Start details activity
            mContext.startActivity(intent);
        }
    }


    private class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<ProjectJson> tempArr = new ArrayList<>(filterDepProjectList);
            if (constraint != null && constraint.length() > 0) {
                List filterList = new ArrayList();
                // for searching projects
                for (int i = 0; i < filterDepProjectList.size(); i++) {
                    if ((filterDepProjectList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(filterDepProjectList.get(i));
                        tempArr.remove(filterDepProjectList.get(i));
                    }
                }
                //searching students in projects
                for (int i = 0; i < tempArr.size(); i++) {
                    List<String> studentNames = tempArr.get(i).getStudentNames();
                    for (String studensName : studentNames) {
                        if ((studensName.toUpperCase()).contains(constraint.toString().toUpperCase())) {
                            filterList.add(tempArr.get(i));

                            break;
                        }
                    }
                }

                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = filterDepProjectList.size();
                results.values = filterDepProjectList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            depProjectList = (List) results.values;
            notifyDataSetChanged();
        }
    }

}