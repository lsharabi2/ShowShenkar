package il.ac.shenkar.endofyearshenkar.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.adapters.ProjectGalleryRecyclerAdapter;
import il.ac.shenkar.endofyearshenkar.json.GsonRequest;
import il.ac.shenkar.endofyearshenkar.json.JsonURIs;
import il.ac.shenkar.endofyearshenkar.json.ProjectJson;
import il.ac.shenkar.endofyearshenkar.utils.DownloadImageTask;

public class ProjectActivity extends ShenkarActivity {

    private static String TAG = "ProjectActivity";

    final Context context = this;
    private ProjectGalleryRecyclerAdapter adapter;
    private List<String> mProjectImages;
    private RequestQueue mRequestQueue;
    private ImageButton playVd;
    private ImageButton playSD;
    private ImageButton playVdGray;
    private ImageButton playSDGray;
    private MediaPlayer mediaPlayer;
    private ProjectViewHolder views;
    private String project;
    private Long projectId;
    private ProjectJson mProject;
    private ImageButton btnShare;
    private ImageButton btnLike;
    private ProgressDialog dialog;
    MediaPlayer.OnPreparedListener preparedListenerPlayer = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            System.out.println("Liron onPrepared");
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            mediaPlayer.start();
        }
    };
    private View.OnClickListener LikeProject = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: implement share project
            Toast.makeText(ProjectActivity.this, "הוסף למועדפים", Toast.LENGTH_LONG).show();
            MyRouteActivity.addProjectId(ProjectActivity.this, projectId);
        }
    };
    private View.OnClickListener shareProject = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            shareVideo();
        }
    };

/*
    public void refreshMedia() {
        dbhelper = new DBHelper();
        projectApi = dbhelper.getProjectApi();
        new AsyncTask<Void, Void, Project>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Project doInBackground(Void... params) {
                try {
                    mProject = projectApi.getProject(projectId).execute();
                    idContent = mProject.getContentId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return mProject;
            }
        }.execute();

        contentApi = dbhelper.getContentApi();
        new AsyncTask<Void, Void, Content>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Content doInBackground(Void... params) {
                try {
                    content = contentApi.getContent(Long.valueOf(idContent)).execute();
                    publishProgress(params);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return content;
            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            protected void onPostExecute(Content contents) {
                super.onPostExecute(contents);

                if ((contents != null) && (content.getMedia() != null)) {
                    listM = contents.getMedia();
                    for (int i = 0; i < listM.size(); ++i) {
                        if (Objects.equals(listM.get(i).getType(), "video")) {
                            urlVideo = listM.get(i).getUrl();
                        }
                        if (Objects.equals(listM.get(i).getType(), "audio")) {
                            urlAudio = listM.get(i).getUrl();
                        }
                    }
                }
                if (urlVideo != null) {
                    playVd.setVisibility(View.VISIBLE);
                    playVd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ProjectActivity.this, YouTubeActivity.class);
                            i.putExtra("url", urlVideo);
                            startActivity(i);
                        }
                    });
                }
                if (urlVideo == null) {
                    playVdGray.setVisibility(View.VISIBLE);
                    playVdGray.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ProjectActivity.this, "אין וידאו לפרוייקט", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if (urlAudio == null) {
                    playSDGray.setVisibility(View.VISIBLE);
                    playSDGray.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ProjectActivity.this, "אין קטע שמיעה", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if (urlAudio != null) {
                    playSD.setVisibility(View.VISIBLE);
                    playSD.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialogT = new Dialog(context);
                            dialogT.setContentView(R.layout.custom);
                            ImageButton dialogButtonPlay = (ImageButton) dialogT.findViewById(R.id.imageButtonPlay);
                            // if button is clicked, close the custom dialog
                            dialogButtonPlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //  String url = "http://programmerguru.com/android-tutorial/wp-content/uploads/2013/04/hosannatelugu.mp3";
                                    String url = urlAudio;
                                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    try {
                                        mediaPlayer.setDataSource(url);
                                    } catch (IllegalArgumentException e) {

                                    } catch (SecurityException e) {

                                    } catch (IllegalStateException e) {

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        mediaPlayer.prepare();
                                    } catch (IllegalStateException e) {

                                    } catch (IOException e) {

                                    }
                                    mediaPlayer.start();
                                }
                            });
                            ImageButton dialogButtonStop = (ImageButton) dialogT.findViewById(R.id.imageButtonStop);
                            // if button is clicked, close the custom dialog
                            dialogButtonStop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mediaPlayer.stop();
                                }
                            });
                            dialogT.show();
                        }
                    });
                }
            }

            @Override
            protected void onProgressUpdate(Void... params) {
                super.onProgressUpdate();
            }
        }.execute();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);


        // initialize all the project's views
        views = new ProjectViewHolder(this);

        project = getIntent().getStringExtra("project");
        views.txtProjectName.setText(project);
        String students = getIntent().getStringExtra("students");
        System.out.println("Liron students =" + students);
        views.txtStudentName.setText(students);

        projectId = getIntent().getLongExtra("id", 0);
        playVd = (ImageButton) findViewById(R.id.imageButtonVideo);
        playSD = (ImageButton) findViewById(R.id.imageButtonSound);
        playVdGray = (ImageButton) findViewById(R.id.imageButtonVideo2);
        playSDGray = (ImageButton) findViewById(R.id.imageButtonSound2);
        btnLike = (ImageButton) findViewById(R.id.btnLike);
        btnLike.setOnClickListener(LikeProject);


        btnShare = (ImageButton) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(shareProject);
        // Initialize recycler view
        RecyclerView rvProjects = (RecyclerView) findViewById(R.id.project_tumbs);
        rvProjects.setLayoutManager(new LinearLayoutManager(this));

        mProjectImages = new ArrayList<>();
        adapter = new ProjectGalleryRecyclerAdapter(this, views.imgScreenShot, mProjectImages);
        rvProjects.setAdapter(adapter);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(preparedListenerPlayer);


        playSD.setVisibility(View.GONE);
        playVd.setVisibility(View.GONE);
        playVdGray.setVisibility(View.GONE);
        playSDGray.setVisibility(View.GONE);
        initialize();

        refreshProjectById();
        setObjectID();
    }

    private void initialize() {
        if (StaticCollegeConfigJson.mMainConfig != null) {
            new DownloadImageTask((ImageView) findViewById(R.id.toolbaricon)).execute(StaticCollegeConfigJson.mMainConfig.getLogoUrl());
            TextView ProjectName_Headline = (TextView) findViewById(R.id.txtProjectName);
            ProjectName_Headline.setTextColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getMainTextColor()));

            TextView StudentName_Headline = (TextView) findViewById(R.id.txtStudentName);
            StudentName_Headline.setTextColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getMainTextColor()));

            LinearLayout LLayout = (LinearLayout) findViewById(R.id.LinarProject);
            LLayout.setBackgroundColor(Color.parseColor(StaticCollegeConfigJson.mMainConfig.getSecondaryColor()));

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshProjectById();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    public void refreshProjectData() {
        if (mProject == null) {
            return;
        }

        views.txtProjectName.setText(mProject.getName());

        List<String> names = mProject.getStudentNames();
        String namesStr = "";
        for (String name : names) {
            namesStr += name + " ";
        }
        System.out.println("Liron namesStr =" + namesStr);
        views.txtStudentName.setText(namesStr);

        views.txtProjectDesc.setText(mProject.getDescription());

        adapter.refresh(mProject.getImagesUrls());

        if ((mProject.getVideoUrl() == null) || mProject.getVideoUrl().isEmpty()) {
            playVdGray.setVisibility(View.VISIBLE);
            playVdGray.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ProjectActivity.this, "אין וידאו לפרוייקט", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            playVd.setVisibility(View.VISIBLE);
            playVd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ProjectActivity.this, YouTubeActivity.class);
                    i.putExtra("url", mProject.getVideoUrl());
                    startActivity(i);
                }
            });
        }

        if ((mProject.getSoundUrl() == null) || mProject.getSoundUrl().isEmpty()) {
            playSDGray.setVisibility(View.VISIBLE);
            playSDGray.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ProjectActivity.this, "אין קטע שמיעה", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            playSD.setVisibility(View.VISIBLE);
            playSD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialogT = new Dialog(context);
                    dialogT.setContentView(R.layout.custom);
                    ImageView dialogButtonPlay = (ImageView) dialogT.findViewById(R.id.imageButtonPlay);
                    // if button is clicked, close the custom dialog
                    dialogButtonPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                                dialog = null;
                            }

                            dialog = ProgressDialog.show(ProjectActivity.this, "", "Loading.Please wait...", true);
                            System.out.println("Liron setOnClickListener");

                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            try {
                                mediaPlayer.setDataSource(mProject.getSoundUrl());
                                mediaPlayer.prepareAsync();

                            } catch (Exception e) {
                            }


                        }
                    });
                    ImageView dialogButtonStop = (ImageView) dialogT.findViewById(R.id.imageButtonStop);
                    // if button is clicked, close the custom dialog
                    dialogButtonStop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mediaPlayer.reset();
                        }
                    });
                    dialogT.show();
                }
            });
        }
    }

    public void refreshProjectById() {

        final String url = JsonURIs.getProjectByIdUri(projectId);

        new AsyncTask<Void, Void, ProjectJson>() {
            private ProgressDialog mProgressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = ProgressDialog.show(ProjectActivity.this, "טוען נתונים", "מעדכן נתוני פרויקט", true, true);
            }

            @Override
            protected ProjectJson doInBackground(Void... params) {
                try {

                    // Instantiate the RequestQueue.
                    RequestQueue requestQueue = Volley.newRequestQueue(ProjectActivity.this);

                    RequestFuture<ProjectJson> future = RequestFuture.newFuture();

                    GsonRequest req = new GsonRequest(url, ProjectJson.class, null, future, future);

                    // Add the request to the RequestQueue.
                    requestQueue.add(req);

                    ProjectJson response = future.get(3, TimeUnit.SECONDS);

                    return response;
                } catch (InterruptedException e) {
                    Log.d(TAG, "interrupted error");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Log.d(TAG, "execution error");
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ProjectJson project) {
                mProgressDialog.dismiss();
                if (project != null) {
                    mProject = project;

                    //TODO may be here add to static list
                    refreshProjectData();
                }
            }

        }.execute();
    }

    public void shareProject(View v) {

//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        Uri screenshotUri = Uri.parse("android.resource://il.ac.shenkar.showshenkar.endofyearshenkar.activities/*");
//        try {
//            InputStream stream = getContentResolver().openInputStream(screenshotUri);
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        sharingIntent.setType("image/jpeg");
//        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//        startActivity(Intent.createChooser(sharingIntent, "Share Project Using"));

        shareVideo();


        // TODO: implement share project
        Toast.makeText(this, "שתף פרויקט", Toast.LENGTH_LONG).show();
    }


    private void shareVideo() {


        if (mProject.getVideoUrl() == null) {

            return;
        }
        String urlYoutube = "https://www.youtube.com/watch?v=" + mProject.getVideoUrl();
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, urlYoutube);
        startActivity(Intent.createChooser(shareIntent, "Share via"));

//        ShareVideo shareVideo = new ShareVideo.Builder().
//                setLocalUrl(Uri.parse(mProject.getVideoUrl())).build();
//        ShareContent shareContent = new ShareMediaContent.Builder()
//                .addMedium(shareVideo).build();

//        ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentUrl(Uri.parse(mProject.getVideoUrl()))
//                .setQuote("Test")
//              .build();
//
//
//        ShareDialog shareDialog = new ShareDialog(this);
//        shareDialog.show(linkContent, ShareDialog.Mode.AUTOMATIC);

    }

    private void sharePhotos() {


//        for(String mProjectImage : mProjectImages) {
//
//            ShareLi sharePhoto = new SharePhoto.Builder().
//        }
    }


 /*
    public void refresh() {
        final ProjectApi projectApi = new ProjectApi.Builder(
                AndroidHttp.newCompatibleTransport(),
                new JacksonFactory(),
                new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {

                    }
                }).setRootUrl(Constants.ROOT_URL).build();
        new AsyncTask<Void, Void, Project>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = ProgressDialog.show(ProjectActivity.this, "טוען נתונים", "מעדכן נתוני פרויקט", true, true);
            }

            @Override
            protected Project doInBackground(Void... params) {
                try {
                    return projectApi.getProject(projectId).execute();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Project project) {
                mProgressDialog.dismiss();
                if (project != null) {
                    mProject = project;
                    refreshContent(Long.parseLong(mProject.getContentId()));
                }
            }

            @Override
            protected void onProgressUpdate(Void... params) {
                super.onProgressUpdate();
            }

        }.execute();
    }

    private void refreshContent(final Long contentId) {
        final ContentApi contentApi = new ContentApi.Builder(
                AndroidHttp.newCompatibleTransport(),
                new JacksonFactory(),
                new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {

                    }
                }).setRootUrl(Constants.ROOT_URL).build();
        new AsyncTask<Void, Void, Content>() {
            @Override
            protected Content doInBackground(Void... params) {
                try {
                    return contentApi.getContent(contentId).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Content result) {
                if (result != null) {
                    adapter.refresh(result.getMedia());
                    Info info = result.getInfo();
                    if (info != null) {
                        views.txtProjectDesc.setText(info.getText());
                    }
                }
            }
        }.execute();
    }*/


    @Override
    void setObjectID() {
        this.objectType = "project";
        this.objectId = projectId;
    }
    public void showLocation(View v) {
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("objectId", projectId);
        i.putExtra("objectType", "project");
        startActivity(i);
    }

    public void sendEmail(View v) {
        if (mProject.getStudentEmails() == null || mProject.getStudentEmails().isEmpty()) {
            Toast.makeText(this, "אין מיילים זמינים לפרויקט", Toast.LENGTH_LONG).show();
            return;
        }

        List<String> emails = new ArrayList<>();
        for (String email : mProject.getStudentEmails()) {
            emails.add(email);
        }

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("application/octet-stream");
        i.putExtra(Intent.EXTRA_EMAIL, emails.toArray(new String[emails.size()]));
        i.putExtra(Intent.EXTRA_SUBJECT, "הודעה משנקר");

        try {
            startActivity(Intent.createChooser(i, "Send Email"));
            Toast.makeText(this, "שלחו מייל ליוצר/ת", Toast.LENGTH_LONG).show();
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients installed", Toast.LENGTH_SHORT).show();
        }
    }

    class ProjectViewHolder {
        TextView txtProjectName;
        TextView txtStudentName;
        TextView txtProjectDesc;
        ImageView imgScreenShot;

        public ProjectViewHolder(Activity activity) {
            txtProjectName = (TextView) activity.findViewById(R.id.txtProjectName);
            txtStudentName = (TextView) activity.findViewById(R.id.txtStudentName);
            txtProjectDesc = (TextView) activity.findViewById(R.id.txtProjectDesc);
            imgScreenShot = (ImageView) activity.findViewById(R.id.imgScreenShot);
            imgScreenShot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter == null) {
                        return;
                    }

                    Intent i = new Intent(ProjectActivity.this, ProjectImageActivity.class);
                    i.putExtra("url", adapter.getCurrentMainImageUrl());
                    startActivity(i);
                }
            });
        }
    }
}
