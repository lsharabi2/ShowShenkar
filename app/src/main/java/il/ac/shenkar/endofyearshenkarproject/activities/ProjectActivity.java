package il.ac.shenkar.endofyearshenkarproject.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import il.ac.shenkar.endofyearshenkarproject.R;
import il.ac.shenkar.endofyearshenkarproject.adapters.ProjectGalleryRecyclerAdapter;
import il.ac.shenkar.endofyearshenkarproject.json.GsonRequest;
import il.ac.shenkar.endofyearshenkarproject.json.JsonURIs;
import il.ac.shenkar.endofyearshenkarproject.json.ProjectJson;
import il.ac.shenkar.endofyearshenkarproject.utils.DownloadImageTask;

/**
 * Project screen, show's all project information
 */
public class ProjectActivity extends ShenkarActivity {

    private static String TAG = "ProjectActivity";

    final Context context = this;
    boolean isPaused = false;
    int length = 0;
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
    private ImageButton btnDisLike;
    private ProgressDialog dialog;
    MediaPlayer.OnPreparedListener preparedListenerPlayer = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            mediaPlayer.start();
        }
    };
    private HashSet<String> projectIdsStr;
    private SharedPreferences.Editor editor;

    /**
     * Like/Dislike button logic
     */
    private View.OnClickListener LikeProject = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageButton btn = (ImageButton) v;
            String tagBtn = (String) v.getTag();
            // set add and remove from favorites logic
            if (tagBtn.equals("1")) { // is liked project, we need to dislike it
                btn.setTag("0");
                btn.setImageResource(R.drawable.disliked);
                removeProjectId(projectId);
                Toast.makeText(ProjectActivity.this, "הוסר ממועדפים", Toast.LENGTH_LONG).show();
            } else {
                btn.setTag("1"); // is disliked project, we need to like it
                btn.setImageResource(R.drawable.like);
                addProjectId(projectId);
                Toast.makeText(ProjectActivity.this, "הוסף למועדפים", Toast.LENGTH_LONG).show();
            }

        }
    };

    private View.OnClickListener shareProject = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        shareVideo();
    }
};
    private ImageView dialogButtonPlay;
    private ImageView imageButtonPause;

    /**
     * Media player logic
     */
    View.OnClickListener mediaPlayerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mediaPlayer.isPlaying()) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
                mediaPlayer.pause();
                dialogButtonPlay.setVisibility(View.VISIBLE);
                imageButtonPause.setVisibility(View.GONE);
                length = mediaPlayer.getCurrentPosition();
                isPaused = true;
            } else if (isPaused) {
                dialogButtonPlay.setVisibility(View.GONE);
                imageButtonPause.setVisibility(View.VISIBLE);
                mediaPlayer.seekTo(length);
                mediaPlayer.start();
            } else if (!isPaused) {
                dialog = ProgressDialog.show(ProjectActivity.this, "", "Loading.Please wait...", true);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                dialogButtonPlay.setVisibility(View.GONE);
                imageButtonPause.setVisibility(View.VISIBLE);
                try {
                    mediaPlayer.setDataSource(mProject.getSoundUrl());
                    mediaPlayer.prepareAsync();

                } catch (Exception e) {
                }

            }
        }
    };

    private void addProjectId(Long projectId) {
        projectIdsStr.add(Long.toString(projectId));

        editor.putStringSet(context.getString(R.string.preference_ids_key), projectIdsStr);
        editor.commit();
    }

    private void removeProjectId(Long projectId) {
        projectIdsStr.remove(Long.toString(projectId));

        editor.putStringSet(context.getString(R.string.preference_ids_key), projectIdsStr);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        sharedPrefInitialization();

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

        checkIfLiked();
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

    private void checkIfLiked() {
        for (String projectID : projectIdsStr) {
            if (projectID.equals(Long.toString(projectId))) {
                btnLike.setImageResource(R.drawable.like);
                btnLike.setTag("1"); // 1 is for like pic
                return;
            }
        }
        btnLike.setImageResource(R.drawable.disliked);
        btnLike.setTag("0"); // 0 is for dislike pic
    }

    private void sharedPrefInitialization() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), MODE_PRIVATE);
        editor = sharedPref.edit();
        projectIdsStr = new HashSet<>(sharedPref.getStringSet(context.getString(R.string.preference_ids_key), new HashSet<String>()));
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
                    dialogButtonPlay = (ImageView) dialogT.findViewById(R.id.imageButtonPlay);
                    imageButtonPause = (ImageView) dialogT.findViewById(R.id.imageButtonPause);
                    imageButtonPause.setOnClickListener(mediaPlayerClickListener);
                    // if button is clicked, close the custom dialog
                    dialogButtonPlay.setOnClickListener(mediaPlayerClickListener);
                    ImageView dialogButtonStop = (ImageView) dialogT.findViewById(R.id.imageButtonStop);
                    // if button is clicked, close the custom dialog
                    dialogButtonStop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isPaused = false;
                            dialogButtonPlay.setVisibility(View.VISIBLE);
                            imageButtonPause.setVisibility(View.GONE);
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

                    refreshProjectData();
                }
            }

        }.execute();
    }

    public void shareProject(View v) {
        shareVideo();
        Toast.makeText(this, "שתף פרויקט", Toast.LENGTH_LONG).show();
    }


    private void shareVideo() {


        if (mProject.getVideoUrl() == null) {

            return;
        }


        String urlYoutube = "זהו פרויקט הגמר שלי אשר עליו עמלתי רבות, אשמח אם תוכלו לשתפו עם אחרים במידה ונהניתם: " + "https://shenkar-show-web-new.herokuapp.com/index.html#/projectDetails/" + mProject.getId();  //"https://www.youtube.com/watch?v=" + mProject.getVideoUrl();
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, urlYoutube);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

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
