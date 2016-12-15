package com.vishnutadimeti.a355_client;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Cal extends AppCompatActivity {

    EditText eventTitle;
    String savedTitle, savedDate, result, ipa, json, output;
    int year, day, month;
    String event;
    ListView listView;
    ArrayAdapter adapter;
    List<String> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        eventTitle = (EditText) findViewById(R.id.eventTitle);
        eventTitle = new EditText(Cal.this);
        listView = (ListView) findViewById(R.id.eventsList);
        events = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, events);

        // Declare Static IP Address
        ipa = "10.192.21.92";


        // Using Intent to get information from the Cal Client class
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            json = intent.getExtras().getString("json");
            try {
                JSONArray jArray = new JSONArray(json);
                for(int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonChildNode = jArray.getJSONObject(i);
                    output = jsonChildNode.optString("text");
                    events.add(output);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Adding event to Calendar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(Cal.this);
                alert.setTitle("Add event title");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                eventTitle.setLayoutParams(lp);
                alert.setView(eventTitle);

                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savedTitle = eventTitle.getText().toString();
                        dialog.dismiss();

                        // Get Calendar Instance

                        Calendar c = Calendar.getInstance();
                        year = c.get(Calendar.YEAR);
                        month = c.get(Calendar.MONTH);
                        day = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog dateDialog = new DatePickerDialog
                                (Cal.this, new DateSetListener(), year, month, day);
                        dateDialog.show();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });
    }

    // Date listener
    class DateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            savedDate = String.valueOf(new StringBuilder().append(month + 1).
                            append("/").append(day).
                            append("/").append(year));
            event = String.valueOf(new StringBuilder().append(month + 1).append(day).append(year));
            result = savedTitle + " ON " + savedDate;
            events.add(result);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            sendDate(view);
            Log.d("Calendar", result);
        }
    }

    // Send calendar event to CalClient AsyncTask
    public void sendDate(View v){
        CalClient client = new CalClient(getApplicationContext(), ipa, 8080, event, savedTitle);
        client.execute();
    }
}
