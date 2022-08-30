package com.lkwoung.socket;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button socketConnectBtn = findViewById(R.id.socketConnectBtn);

        socketConnectBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SocketThread thread = new SocketThread( );
                thread.start();
            }
        });
    }// onCreate

    private class SocketThread extends Thread {
        @Override
        public void run() {
            try {
                Socket socket = new Socket("127.0.0.1", 7777);
                DataOutputStream dataOutputStream= new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("Message From Android");

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }// run
    }// socketThread class
}// MainActivity
