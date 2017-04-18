package il.ac.shenkar.endofyearshenkar.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Set;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.activities.ProjectActivity;
import il.ac.shenkar.endofyearshenkar.json.JsonURIs;
import il.ac.shenkar.endofyearshenkar.json.ProjectJson;

public class DepProjectsRecyclerAdapter extends RecyclerView.Adapter<DepProjectsRecyclerAdapter.CustomViewHolder> {
    private final RequestQueue requestQueue;
    private List<ProjectJson> depProjectList;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    public DepProjectsRecyclerAdapter(Context context, List<ProjectJson> depProjectList) {
        this.depProjectList = depProjectList;
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
        if (depProject == null)
        {
            return;
        }

        customViewHolder.projectId = depProject.getId();
        customViewHolder.txtProjectName.setText(depProject.getName());
        List<String> names = depProject.getStudentNames();

        String namesStr = "";
        for (String name : names) {
            namesStr += name + "\n";
        }

        customViewHolder.txtProjectStudent.setText(namesStr);
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

                        depProjectList.clear();
                        depProjectList.addAll(projects);
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

    public void refresh(final Set<String> projectIds) {
        /*
        final ProjectApi projectApi = new ProjectApi.Builder(
                AndroidHttp.newCompatibleTransport(),
                new JacksonFactory(),
                new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {

                    }
                }).setRootUrl(Constants.ROOT_URL).build();

        new AsyncTask<Void, Void, List<Project>>() {
            @Override
            protected List<Project> doInBackground(Void... params) {
                List<Project> projects = new ArrayList<Project>();
                try {
                    for (String projectId : projectIds) {
                        projects.add(projectApi.getProject(Long.parseLong(projectId)).execute());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return projects;
            }

            @Override
            protected void onPostExecute(List<Project> projects) {
                //show complition in UI
                //fill grid view with data
                if (projects != null) {
                    depProjectList.clear();
                    depProjectList.addAll(projects);
                    notifyDataSetChanged();
                }
            }
        }.execute();
        */
    }

    public void clear()
    {
        depProjectList.clear();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected Long projectId;
        protected TextView txtProjectName;
        protected TextView txtProjectStudent;

        public CustomViewHolder(View view) {
            super(view);
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
}