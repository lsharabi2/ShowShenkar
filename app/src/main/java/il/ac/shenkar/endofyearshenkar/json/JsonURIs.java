package il.ac.shenkar.endofyearshenkar.json;

/**
 * Created by User on 12/04/2017.
 */
public class JsonURIs {
    public static final long SHENKAR_COLLEGE_ID = 1;

    public static final String COLLEGE_CONFIG_BY_ID_URI = "http://shenkar-show.herokuapp.com/guest/institute/id/%d";

    public static final String DEPARTMENTS_BY_COLLEGE_ID_URI = "http://shenkar-show.herokuapp.com/guest/institute/id/%d/departments";
    public static final String DEPARTMENT_BY_ID_URI = "http://shenkar-show.herokuapp.com/guest/department/id/%d";
    public static final String PROJECTS_BY_DEPARTMENT_ID_URI = "http://shenkar-show.herokuapp.com/guest/institute/%d/department/%d/projects";
    public static final String PROJECT_BY_ID_URI = "http://shenkar-show.herokuapp.com/guest/project/id/%d";
    public static final String LOCATION_BY_ID_URI = "http://shenkar-show.herokuapp.com/guest/location/id/%d";
    public static final String ROUTE_BY_ID_URI = "http://shenkar-show.herokuapp.com/guest/route/id/%d";
    public static final String ROUTES_BY_COLLEGE_ID_URI = "http://shenkar-show.herokuapp.com/guest/institute/id/%d/routes";

    public static String getConfigCollegeIdUri(long collegeId) {
        return String.format(COLLEGE_CONFIG_BY_ID_URI, collegeId);
    }

    public static String getDepartmentsByCollegeIdUri(long collegeId) {
        return String.format(DEPARTMENTS_BY_COLLEGE_ID_URI, collegeId);
    }

    public static String getRoutessByCollegeIdUri(long collegeId) {
        return String.format(ROUTES_BY_COLLEGE_ID_URI, collegeId);
    }

    public static String getProjectsByDepartmentIdUri(long collegeId, long departmentId) {
        return String.format(PROJECTS_BY_DEPARTMENT_ID_URI, collegeId, departmentId);
    }

    public static String getProjectByIdUri(long projectId) {
        return String.format(PROJECT_BY_ID_URI, projectId);
    }
}
