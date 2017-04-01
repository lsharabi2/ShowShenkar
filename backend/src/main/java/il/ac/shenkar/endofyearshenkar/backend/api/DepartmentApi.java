package il.ac.shenkar.endofyearshenkar.backend.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;

import java.util.List;

import il.ac.shenkar.endofyearshenkar.backend.OfyService;
import il.ac.shenkar.endofyearshenkar.backend.model.Department;

/**
 * Created by:  Gregory Kondratenko on 10/06/2016.
 * Description: Backend api for app projects
 */
@Api(
        name = "departmentApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.showshenkar.shenkar.ac.il",
                ownerName = "backend.showshenkar.shenkar.ac.il",
                packagePath=""
        )
)
public class DepartmentApi {

    @ApiMethod(
            name = "getDepartment",
            path = "departmentApi/{id}",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public Department getDepartment(@Named("id") Long id){
        return OfyService.ofy().load().type(Department.class).id(id).now();
    }

    @ApiMethod(
            name = "getDepartments",
            path = "departmentApi",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public List<Department> getDepartments(){
        return OfyService.ofy().load().type(Department.class).list();
    }

    @ApiMethod(
            name = "setDepartment",
            path = "departmentApi",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public Department setDepartment(Department department){
        if (department == null){
            throw new IllegalStateException("Department is null");
        }

        if (department.getId() != null) {
            throw new IllegalStateException("Department already exits");
        }

        OfyService.ofy().save().entity(department).now();
        return department;
    }

    @ApiMethod(
            name = "deleteDepartment",
            path = "departmentApi/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE
    )
    public void deleteDepartment(@Named("id") Long id){
        OfyService.ofy().delete().type(Department.class).id(id).now();
    }

}
