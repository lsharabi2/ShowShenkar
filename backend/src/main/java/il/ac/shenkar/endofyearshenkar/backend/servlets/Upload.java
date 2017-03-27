package il.ac.shenkar.endofyearshenkar.backend.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import il.ac.shenkar.endofyearshenkar.backend.model.Media;
import il.ac.shenkar.endofyearshenkar.backend.OfyService;
import il.ac.shenkar.endofyearshenkar.backend.model.Content;
import il.ac.shenkar.endofyearshenkar.backend.model.Department;

public class Upload extends HttpServlet {

    private static final Logger log = Logger.getLogger(Upload.class.getName());

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("myFile");

        if (blobKeys == null || blobKeys.isEmpty()) {
            
            res.sendRedirect("/uploadImage.jsp?updated=no");
        } else {
            Long id = Long.valueOf(req.getParameter("id"));
            String type = req.getParameter("type");
            String url = req.getParameter("url");
            if(!Objects.equals(url, "")){
                Content content = OfyService.ofy().load().type(Content.class).id(id).now();
                if (content != null) {
                    log.info("Content found: " + content.toString() + '\n');
                    Media media = new Media();
                    media.setUrl(url);
                    media.setType("video");
                    if (content.getMedia() == null) {
                        List<Media> mediaList = new ArrayList<>();
                        content.setMedia(mediaList);
                    }
                    content.getMedia().add(media);
                    OfyService.ofy().save().entity(content).now();
                }
            }
            else {
                String imageUrl = ImagesServiceFactory.getImagesService().getServingUrl(ServingUrlOptions.Builder.withBlobKey(blobKeys.get(0)));
                log.info("id: " + id + '\n' + "type: " + type + '\n' + "URL: " + imageUrl + '\n');
                if (Objects.equals(type, "")) {
                    Content content = OfyService.ofy().load().type(Content.class).id(id).now();
                    if (content != null) {
                        log.info("Content found: " + content.toString() + '\n');
                        Media media = new Media();
                        media.setUrl(imageUrl);
                        media.setType("Image");
                        if (content.getMedia() == null) {
                            List<Media> mediaList = new ArrayList<>();
                            content.setMedia(mediaList);
                        }
                        content.getMedia().add(media);
                        OfyService.ofy().save().entity(content).now();
                    }
                } else if (Objects.equals(type, "dep")) {
                    Department department = OfyService.ofy().load().type(Department.class).id(id).now();
                    if (department != null) {
                        log.info("Department found: " + department.toString() + '\n');
                        department.setImageUrl(imageUrl);
                        OfyService.ofy().save().entity(department).now();
                    }
                } else {
                    Department department = OfyService.ofy().load().type(Department.class).id(id).now();
                    if (department != null) {
                        log.info("Department found: " + department.toString() + '\n');
                        department.setLargeImageUrl(imageUrl);
                        OfyService.ofy().save().entity(department).now();
                    }
                }
            }
            res.sendRedirect("/uploadImage.jsp?updated=yes");
        }
    }
}
