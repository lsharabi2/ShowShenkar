package il.ac.shenkar.endofyearshenkar.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import il.ac.shenkar.endofyearshenkar.backend.model.Content;
import il.ac.shenkar.endofyearshenkar.backend.model.Route;
import il.ac.shenkar.endofyearshenkar.backend.model.Department;
import il.ac.shenkar.endofyearshenkar.backend.model.Project;

/**
 * Objectify service wrapper so we can statically register our persistence classes
 * More on Objectify here : https://code.google.com/p/objectify-appengine/
 *
 */
public class OfyService {

    static {
        ObjectifyService.register(RegistrationRecord.class);
        ObjectifyService.register(Content.class);
        ObjectifyService.register(Project.class);
        ObjectifyService.register(Department.class);
        ObjectifyService.register(Route.class);

    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
