package com.vishnutadimeti.a355_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Chat extends AppCompatActivity {

    Button send;
    EditText input;
    TextView msg, reply;
    String ipa, message, data;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        send = (Button) findViewById(R.id.send);
        input = (EditText) findViewById(R.id.messageInput);
        msg = (TextView) findViewById(R.id.message);
        reply = (TextView) findViewById(R.id.reply);

        // Declare Static IP Address
        ipa = "10.192.21.92";
        i = getIntent();
        data = i.getStringExtra("reply");
        reply.setText(data);
        System.out.println(data);
    }

    // Method to get user input.
    public void addMessage(View v) {
        input = (EditText) findViewById(R.id.messageInput);
        message = input.getText().toString();
        sendMessage(v);
        msg.setText(message);
    }

    // Send message to ChatClient AsyncTask
    public void sendMessage(View v) {
        ChatClient chatClient = new ChatClient(getApplicationContext(), ipa, 8080, message);
        chatClient.execute();
    }
}