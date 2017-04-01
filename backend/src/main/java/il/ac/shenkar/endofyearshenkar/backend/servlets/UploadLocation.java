package il.ac.shenkar.endofyearshenkar.backend.servlets;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import il.ac.shenkar.endofyearshenkar.backend.model.Content;
import il.ac.shenkar.endofyearshenkar.backend.OfyService;
import il.ac.shenkar.endofyearshenkar.backend.model.Location;

/**
 * Created by:  Gregory Kondratenko on 13/06/2016.
 * Description: Image show servlet
 */
public class UploadLocation extends HttpServlet {

    private static final Logger log = Logger.getLogger(UploadLocation.class.getName());
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("myFile");

        if (blobKeys == null || blobKeys.isEmpty()) {

            res.sendRedirect("/uploadLocation.jsp?updated=no");
        } else {

            String imageUrl = ImagesServiceFactory.getImagesService().getServingUrl(ServingUrlOptions.Builder.withBlobKey(blobKeys.get(0)));
            Long id = Long.valueOf(req.getParameter("id"));
            Long lat = Long.valueOf(req.getParameter("lat"));
            Long lng = Long.valueOf(req.getParameter("lng"));
            String description = req.getParameter("description");
            log.info("id: " + id + '\n' + "URL: " + imageUrl + '\n');
            Content content = OfyService.ofy().load().type(Content.class).id(id).now();
            if(content != null){
                log.info("Content found: " + content.toString() + '\n');
                Location location;
                if(content.getLocation() == null){
                    location = new Location();
                }
                else{
                    location = content.getLocation();
                }
                location.setUrl(imageUrl);
                location.setLat(lat);
                location.setLng(lng);
                location.setDescription(description);
                content.setLocation(location);
                OfyService.ofy().save().entity(content).now();
            }
            res.sendRedirect("/uploadLocation.jsp?updated=yes");
        }

    }
}