package com.vishnutadimeti.a355_client;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

class CalClient extends AsyncTask<Void, Void, Void> {

    private final Context context;
    private String dstAddress, eventTitle, jsonData, eventDate;
    private int dstPort;

    CalClient(Context context, String address, int port, String event, String title) {
        this.context = context;
        dstAddress = address;
        dstPort = port;
        eventDate = event;
        eventTitle = title;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        
            Socket socket = null;
            try {
                socket = new Socket(dstAddress, dstPort);
                OutputStream out = socket.getOutputStream();
                PrintWriter outPrint = new PrintWriter(out,true);

                // Send data to the Server
                outPrint.println("cal" + eventDate + eventTitle);
                Log.d("Cal Client", eventDate + eventTitle);

                // Retrieve data from the Server in terms of JSON
                InputStream inStream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
                StringBuilder builder = new StringBuilder();

                String text;
                while ((text = reader.readLine()) != null) builder.append(text);
                jsonData = builder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Intent i = new Intent(context, Cal.class);
        i.putExtra("json", jsonData);
    }
}
