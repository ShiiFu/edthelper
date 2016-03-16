package com.application.android.edthelper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.text.ICalReader;


public class MainActivity extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        File folder = new File(Environment.getExternalStorageDirectory() + "/edthelper");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            Log.w("Information", "folder created");
        } else {
            Log.w("Information", "folder not created");
        }

        updateNextEvent();
    }

    public void synchronizeEDT(View v) {
        TextView texte = (TextView) findViewById(R.id.notif);
        texte.setText("to do");
        Log.w("Information", "en cours");
        new DownloadFileFromURL().execute(Constantes.EDTURL);
    }

    /**
     * Showing Dialog
     * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                String basicAuth = "Basic " + new String(android.util.Base64.encode(Constantes.USERPASS.getBytes(), android.util.Base64.NO_WRAP));
                connection.setRequestProperty("Authorization", basicAuth);
                connection.connect();

                if (((HttpURLConnection)connection).getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e("Error: ", "Server returned HTTP " + ((HttpURLConnection)connection).getResponseCode()
                            + " " + ((HttpURLConnection)connection).getResponseMessage());
                }

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = connection.getInputStream();

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/edthelper/edt.ics");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            TextView texte = (TextView) findViewById(R.id.notif);
            texte.setText("Done");

            EvenementBdd evenementBdd = new EvenementBdd(MainActivity.context);
            evenementBdd.openForWrite();
            evenementBdd.eraseBdd();

            File file = new File(Environment.getExternalStorageDirectory().toString() + "/edthelper/edt.ics");

            ICalReader reader = null;
            try {
                reader = new ICalReader(file);
                ICalendar ical;
                while ((ical = reader.readNext()) != null){
                    List<VEvent> events = ical.getEvents();
                    for (int i=0 ; i<events.size() ; i++) {
                        Log.w("Calendar", events.get(i).getSummary().getValue()
                                + " : " + events.get(i).getDescription().getValue()
                                + " : " + events.get(i).getLocation().getValue()
                                + " : " + (Date) events.get(i).getDateStart().getValue()
                                + " : " + (Date) events.get(i).getDateEnd().getValue());
                        Evenement evenement = new Evenement();
                        evenement.setId(i);
                        evenement.setDescription(events.get(i).getDescription().getValue());
                        evenement.setLieu(events.get(i).getLocation().getValue());
                        evenement.setDebut((Date) events.get(i).getDateStart().getValue());
                        evenement.setFin((Date) events.get(i).getDateEnd().getValue());
                        evenementBdd.insertEvent(evenement);
                    }
                }
            } catch (IOException e) {
            Log.e("Parser", "e: " + e.toString());
            }

            evenementBdd.close();

            updateNextEvent();
        }
    }

    public void boutonEdt(View v){
        startActivity(new Intent(MainActivity.this, EdtActivity.class));
    }

    public void boutonSettings(View v){
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    public void updateNextEvent() {
        EvenementBdd evenementBdd = new EvenementBdd(this);
        evenementBdd.openForRead();

        ArrayList<String> evenements = evenementBdd.getAllEventString();
        if (evenements != null) {
            evenementBdd.close();
            TextView texte = (TextView) findViewById(R.id.nextEvent);
            texte.setText(evenements.get(0));
        }
    }
}
