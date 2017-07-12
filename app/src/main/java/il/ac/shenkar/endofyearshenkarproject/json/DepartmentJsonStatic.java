package il.ac.shenkar.endofyearshenkarproject.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 21/06/2017.
 */
public class DepartmentJsonStatic {
    static private List<DepartmentJson> departmentJsonList = new ArrayList<>();

    public static List<DepartmentJson> getDepartmentJsonList() {
        return departmentJsonList;
    }

    public static void setDepartmentJsonList(List<DepartmentJson> departmentJsonList) {
        DepartmentJsonStatic.departmentJsonList = departmentJsonList;
    }
}
