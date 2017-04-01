package il.ac.shenkar.endofyearshenkar.backend.api;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;

import il.ac.shenkar.endofyearshenkar.backend.OfyService;
import il.ac.shenkar.endofyearshenkar.backend.model.Content;

/**
 * Created by:  Gregory Kondratenko on 10/06/2016.
 * Description: Backend api for app content
 */
@Api(
        name = "contentApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.showshenkar.shenkar.ac.il",
                ownerName = "backend.showshenkar.shenkar.ac.il",
                packagePath=""
        )
)
public class ContentApi {

    @ApiMethod(
            name = "getContent",
            path = "contentApi/{id}",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public Content getContent(@Named("id") Long id){
        return OfyService.ofy().load().type(Content.class).id(id).now();
    }

    @ApiMethod(
            name = "SetContent",
            path = "contentApi",
            httpMethod = ApiMethod.HttpMethod.POST
    )
    public Content setContent(Content content){
        if (content == null){
            throw new IllegalStateException("Content is null");
        }

        if (content.getId() != null) {
            throw new IllegalStateException("Content already exits");
        }

        OfyService.ofy().save().entity(content).now();
        return content;
    }

    @ApiMethod(
            name = "deleteContent",
            path = "contentApi/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE
    )
    public Content deleteContent(@Named("id") String id){
        //TODO
        return null;
    }

    @ApiMethod(
            name = "updateContent",
            path = "contentApi/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT
    )
    public Content updateContent(@Named("id") String id){
        //TODO
        return null;
    }

}
