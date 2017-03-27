package il.ac.shenkar.endofyearshenkar.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import il.ac.shenkar.endofyearshenkar.R;
import il.ac.shenkar.endofyearshenkar.adapters.ProjectGalleryRecyclerAdapter;

import il.ac.shenkar.showshenkar.backend.contentApi.ContentApi;
import il.ac.shenkar.showshenkar.backend.contentApi.model.Content;
import il.ac.shenkar.showshenkar.backend.contentApi.model.Info;
import il.ac.shenkar.showshenkar.backend.contentApi.model.Media;
import il.ac.shenkar.showshenkar.backend.projectApi.ProjectApi;
import il.ac.shenkar.showshenkar.backend.projectApi.model.Project;
import il.ac.shenkar.endofyearshenkar.model.DBHelper;
import il.ac.shenkar.endofyearshenkar.utils.Constants;

public class ProjectActivity extends ShenkarActivity {

    final Context context = this;
    private ProjectGalleryRecyclerAdapter adapter;
    private List<Media> mProjectTumbs;
    private DBHelper dbhelper;
    private ContentApi contentApi;
    private Content content;
    private ProjectApi projectApi;
    private List<Media> listM;
    private String urlVideo;
    private String urlAudio;
    private String idContent;
    private ProgressDialog mProgressDialog;

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
                    if (adapter == null)
                    {
                        return;
                    }

                    Intent i = new Intent(ProjectActivity.this, ProjectImageActivity.class);
                    i.putExtra("url", adapter.getCurrentMainImageUrl());
                    startActivity(i);
                }
            });
        }
    }

    private ImageButton playVd;
    private ImageButton playSD;
    private ImageButton playVdGray;
    private ImageButton playSDGray;
    private MediaPlayer mediaPlayer;
    private ProjectViewHolder views;
    private String project;
    private Long projectId;
    private Project mProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        // initialize all the project's views
        views = new ProjectViewHolder(this);

        project = getIntent().getStringExtra("project");
        views.txtProjectName.setText(project);
        String students = getIntent().getStringExtra("students");
        views.txtStudentName.setText(students);

        projectId = getIntent().getLongExtra("id", 0);
        playVd = (ImageButton) findViewById(R.id.imageButtonVideo);
        playSD = (ImageButton) findViewById(R.id.imageButtonSound);
        playVdGray = (ImageButton) findViewById(R.id.imageButtonVideo2);
        playSDGray = (ImageButton) findViewById(R.id.imageButtonSound2);
        // Initialize recycler view
        RecyclerView rvProjects = (RecyclerView) findViewById(R.id.project_tumbs);
        rvProjects.setLayoutManager(new LinearLayoutManager(this));

        mProjectTumbs = new ArrayList<>();
        adapter = new ProjectGalleryRecyclerAdapter(this, views.imgScreenShot, mProjectTumbs);
        rvProjects.setAdapter(adapter);
        mediaPlayer = new MediaPlayer();
        playSD.setVisibility(View.GONE);
        playVd.setVisibility(View.GONE);
        playVdGray.setVisibility(View.GONE);
        playSDGray.setVisibility(View.GONE);
        refreshMedia();
    }

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
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

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
    }


    public void shareProject(View v) {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse("android.resource://il.ac.shenkar.showshenkar.activities/*");
        try {
            InputStream stream = getContentResolver().openInputStream(screenshotUri);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sharingIntent.setType("image/jpeg");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "Share Project Using"));
        // TODO: implement share project
        Toast.makeText(this, "שתף פרויקט", Toast.LENGTH_LONG).show();
        MyRouteActivity.addProjectId(this, projectId);
    }

    public void showLocation(View v) {
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("objectId", projectId);
        i.putExtra("objectType", "project");
        startActivity(i);
    }

    public void sendEmail(View v) {
        if (mProject.getStudentEMail() == null || mProject.getStudentEMail().isEmpty()) {
            Toast.makeText(this, "אין מיילים זמינים לפרויקט", Toast.LENGTH_LONG).show();
            return;
        }

        List<String> emails = new ArrayList<>();
        for (String email : mProject.getStudentEMail()) {
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
}
