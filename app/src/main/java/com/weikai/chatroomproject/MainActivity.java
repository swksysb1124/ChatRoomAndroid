package com.weikai.chatroomproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends Activity{

    private static Handler mHandler = new Handler();
    private TextView tvDialogBlocks;    // dialog block
    private EditText etMessage;         // message block
    private String tmp;                 // template message
    private String username = "Jason";
    private Socket clientSocket;        // socket from client

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDialogBlocks = (TextView) findViewById(R.id.dialogBlocks);
        tvDialogBlocks.setMovementMethod(new ScrollingMovementMethod());
        etMessage=(EditText) findViewById(R.id.message);

        // Create a new thread for reading messages
        Thread t = new Thread(readData);

        // start the thread
        t.start();

        ImageButton bSubmit=(ImageButton) findViewById(R.id.submit);

        // set listener to listen action of button
        bSubmit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // if client's socket is connected
                if(clientSocket.isConnected()){
                    BufferedWriter bw;
                    try {

                        // link the output stream to a buffered writer
                        bw = new BufferedWriter( new OutputStreamWriter(clientSocket.getOutputStream()));
                        // write the message
                        bw.write(username + ":" + etMessage.getText() + "\n");

                        // flush message!
                        bw.flush();
                    } catch (IOException e) {

                    }
                    // clean message block
                    etMessage.setText("");
                }
            }
        });

    }

    // updates the dialog block
    private Runnable updateText = new Runnable() {
        public void run() {
            // update text in dialog block in a new line
            tvDialogBlocks.append(tmp + "\n");
        }
    };

    // read message from the server
    private Runnable readData = new Runnable() {
        public void run() {
            // server's IP
            InetAddress serverIp;
            try {
                //if(!isConnected){
                    serverIp = InetAddress.getByName("10.0.0.100");
                    int serverPort = 5050;
                    clientSocket = new Socket(serverIp, serverPort);
                //}
                // link input stream of client's socket to a buffered reader
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),"utf-8"));
                // when the socket is connected, post messages in the dialog block
                while (clientSocket.isConnected()) {
                    // read message from buffered reader
                    tmp = br.readLine();
                    // if tmp is not null, show the new message.
                    if(tmp!=null){
                        mHandler.post(updateText);
                    }
                }
            } catch (IOException e) {

            }
        }
    };
    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this,
                "You are leaving!",
                Toast.LENGTH_SHORT).show();
        if(clientSocket.isConnected()){
            BufferedWriter bw;
            try {

                // link the output stream to a buffered writer
                bw = new BufferedWriter( new OutputStreamWriter(clientSocket.getOutputStream()));
                // write the message
                bw.write(username + ":" + "END" + "\n");

                // flush message!
                bw.flush();
            } catch (IOException e) {

            }
            // clean message block
            etMessage.setText("");
        }
        finish();

    }


}
