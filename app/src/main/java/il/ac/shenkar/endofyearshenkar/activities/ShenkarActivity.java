package il.ac.shenkar.endofyearshenkar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import il.ac.shenkar.endofyearshenkar.R;

public class ShenkarActivity extends AppCompatActivity {

    public String Qrlocation;
    Long rContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.shenkar_actionbar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        ImageButton mapBtn = (ImageButton) mCustomView.findViewById(R.id.toolbarmap);
        mapBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showMap();
            }
        });

        ImageButton qrBtn = (ImageButton) mCustomView.findViewById(R.id.toolbarqr);
        qrBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openScanner();  //call for the scanner func
            }
        });
    }

    private void showMap() {
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("objectId", "general");
        i.putExtra("objectType", "general");
        startActivity(i);
    }

    /*
    * calling for the scanner to start and using zxing package for different customize options
    * */
    public void openScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("waiting for code to scan");
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    /*
    * working the result of the scan*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
//                String locationId;
                //DBHelper helper = new DBHelper();
                try {
                    String oType = result.getContents().split(";")[0];
                    Long oID = Long.valueOf(result.getContents().split(";")[1]);
                    //rContent = Long.valueOf(result.getContents());
                    //locationId = result.getContents();
                    Intent to_mapActivity = new Intent(this, MapActivity.class);
                    to_mapActivity.putExtra("objectId",oID);
                    to_mapActivity.putExtra("objectType", oType);
                    startActivity(to_mapActivity);

                } catch (NumberFormatException e){

                    //TODO: location id, goto map activity
                }
                /*
                projectApi = helper.getProjectApi();
                new AsyncTask<Void, Void, Project>() {
                    @Override
                    protected Project doInBackground(Void... params) {
                        try {
                            project = projectApi.getProject(rContent).execute();
                            publishProgress(params);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return project;
                    }

                    @Override
                    protected void onPostExecute(Project project) {
                        super.onPostExecute(project);
                        Intent i = new Intent(ShenkarActivity.this, ProjectActivity.class);
                        String students = "";
                        for(String name: project.getStudentNames()){
                            students += name + " ";
                        }
                        i.putExtra("id", project.getId());
                        i.putExtra("project", project.getName());
                        i.putExtra("students", students);
                        startActivity(i);
                    }
                }.execute();

                */
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            Qrlocation = result.getContents();
        }

        /*
            read the project id from the QR code then add it to my route using the following code
            MyRouteActivity.addProjectId(this, projectId);
         */
    }
}