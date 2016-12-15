package com.vishnutadimeti.a355_client;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatClient extends AsyncTask<Void, Void, Void> {

    private final Context context;
    String dstAddress, message, reply, data;
    private int dstPort;

    ChatClient(Context context, String address, int port, String input) {
        this.context = context;
        dstAddress = address;
        dstPort = port;
        message = input;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        
            Socket chatSocket = null;
            try {
                chatSocket = new Socket(dstAddress, dstPort);
                OutputStream out = chatSocket.getOutputStream();
                PrintWriter outPrint = new PrintWriter(out,true);
                outPrint.println("cht" + message);
                Log.d("Chat Client", message);

                InputStream inStream = chatSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
                StringBuilder builder = new StringBuilder();

                String text;
                while ((text = reader.readLine()) != null) builder.append(text);
                reply = builder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (chatSocket != null) {
                    try {
                        chatSocket.close();
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
        Intent i = new Intent(context, Chat.class);
        i.putExtra("reply", reply);
    }

}
