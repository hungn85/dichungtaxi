package com.dichungtaxi.main;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.dichungtaxi.main.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new LoadViewTask().execute();

        setContentView(R.layout.activity_main);

    }


    //    public class LoadViewTask extends AsyncTask<Void, Integer, Void>
//    {
//
//        //A ProgressDialog object
//        private ProgressDialog progressDialog;
//        //Before running code in separate thread
//        @Override
//        protected void onPreExecute()
//        {
////            //Create a new progress dialog
////            progressDialog = new ProgressDialog(MainActivity.this);
////            //Set the progress dialog to display a horizontal progress bar
////            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
////            //Set the dialog title to 'Loading...'
////            progressDialog.setTitle("Đang tải dữ liệu...");
////            //Set the dialog message to 'Loading application View, please wait...'
////            //progressDialog.setMessage("Đang tải dữ liệu...");
////            //This dialog can't be canceled by pressing the back key
////            progressDialog.setCancelable(false);
////            //This dialog isn't indeterminate
////            progressDialog.setIndeterminate(false);
////            //The maximum number of items is 100
////            progressDialog.setMax(100);
////            //Set the current progress to zero
////            progressDialog.setProgress(0);
////            //Display the progress dialog
////            progressDialog.show();
//
//            progressDialog = ProgressDialog.show(MainActivity.this,"Đang tải dữ liệu",
//                    "Bạn vui lòng chờ...", false, false);
//        }
//
//        //The code to be executed in a background thread.
//        @Override
//        protected Void doInBackground(Void... params)
//        {
//            /* This is just a code that delays the thread execution 4 times,
//             * during 850 milliseconds and updates the current progress. This
//             * is where the code that is going to be executed on a background
//             * thread must be placed.
//             */
//            try
//            {
//                //Get the current thread's token
//                synchronized (this)
//                {
//                    //Initialize an integer (that will act as a counter) to zero
//                    int counter = 0;
//                    //While the counter is smaller than four
//                    while(counter <= 4)
//                    {
//                        //Wait 850 milliseconds
//                        this.wait(850);
//                        //Increment the counter
//                        counter++;
//                        //Set the current progress.
//                        //This value is going to be passed to the onProgressUpdate() method.
//                        publishProgress(counter*25);
//                    }
//                }
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        //Update the progress
//        @Override
//        protected void onProgressUpdate(Integer... values)
//        {
//            //set the current progress of the progress dialog
//            progressDialog.setProgress(values[0]);
//        }
//
//        //after executing the code in the thread
//        @Override
//        protected void onPostExecute(Void result)
//        {
//            try {
//                //close the progress dialog
//                progressDialog.dismiss();
//            }
//            catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
