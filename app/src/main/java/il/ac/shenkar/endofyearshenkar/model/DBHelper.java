package il.ac.shenkar.endofyearshenkar.model;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

import il.ac.shenkar.showshenkar.backend.contentApi.ContentApi;
import il.ac.shenkar.showshenkar.backend.departmentApi.DepartmentApi;
import il.ac.shenkar.showshenkar.backend.projectApi.ProjectApi;
import il.ac.shenkar.showshenkar.backend.routeApi.RouteApi;
import il.ac.shenkar.endofyearshenkar.utils.Constants;

/**
 * Created by Gregory on 27/06/2016.
 */
public class DBHelper{

    ContentApi contentApi;
    DepartmentApi departmentApi;
    RouteApi routeApi;
    ProjectApi projectApi;

    public DBHelper(){
        contentApi = new ContentApi.Builder(AndroidHttp.newCompatibleTransport(),
                new JacksonFactory(),
                new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {

                    }
                }).setRootUrl(Constants.ROOT_URL).build();
        departmentApi = new DepartmentApi.Builder(AndroidHttp.newCompatibleTransport(),
                new JacksonFactory(),
                new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {

                    }
                }).setRootUrl(Constants.ROOT_URL).build();
        routeApi = new RouteApi.Builder(AndroidHttp.newCompatibleTransport(),
                new JacksonFactory(),
                new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {

                    }
                }).setRootUrl(Constants.ROOT_URL).build();
        projectApi = new ProjectApi.Builder(AndroidHttp.newCompatibleTransport(),
                new JacksonFactory(),
                new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {

                    }
                }).setRootUrl(Constants.ROOT_URL).build();
    }

    public ContentApi getContentApi() {
        return contentApi;
    }

    public DepartmentApi getDepartmentApi() {
        return departmentApi;
    }

    public RouteApi getRouteApi() {
        return routeApi;
    }

    public ProjectApi getProjectApi() {
        return projectApi;
    }
}
