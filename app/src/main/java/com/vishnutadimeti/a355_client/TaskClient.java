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

class TaskClient extends AsyncTask<Void, Void, Void> {

    private final Context context;
    private String dstAddress, jsonData, task;
    private int dstPort;
    private ArrayList<String> listData = new ArrayList<>();

    TaskClient(Context context, String address, int port, String todo) {
        this.context = context;
        dstAddress = address;
        dstPort = port;
        task = todo;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        
            Socket taskSocket = null;
            try {
                taskSocket = new Socket(dstAddress, dstPort);
                OutputStream out = taskSocket.getOutputStream();
                PrintWriter outPrint = new PrintWriter(out,true);
                outPrint.println("tsk" + task);
                Log.d("Print Task", task);
                String text;

                InputStream inStream = taskSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
                StringBuilder builder = new StringBuilder();

                while ((text = reader.readLine()) != null) builder.append(text);
                jsonData = builder.toString();

//                JSONArray jArray = new JSONArray(jsonData);

//                for (int i=0; i < jArray.length(); i++){
//                    listData.add(jArray.get(i).toString());
//                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (taskSocket != null) {
                    try {
                        taskSocket.close();
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
        Intent i = new Intent(context, Home.class);
        i.putExtra("json", jsonData);
    }
}
