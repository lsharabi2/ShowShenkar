package il.ac.shenkar.endofyearshenkarproject.json;

import java.util.ArrayList;
import java.util.List;

/**
 * DepartmentJsonStatic hold the departments list
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
