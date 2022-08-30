package com.lkwoung.imagesender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private  static final int REQUEST_CODE = 0;
    private ImageView imageView;
    private EditText ipText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button imageBtn = findViewById(R.id.image);
        Button sendBtn = findViewById(R.id.send);
        imageView = findViewById(R.id.imageView);
        ipText = findViewById(R.id.ipText);

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SocketThread thread = new SocketThread( );
                thread.start();
            }
        });
    } // onCreate

    private class SocketThread extends Thread {
        @Override
        public void run() {
            Socket socket = null;
            String ip = String.valueOf(ipText.getText());
            try {
                socket = new Socket(ip, 7777);
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();

                Bitmap imageBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                Delivery.write(outputStream, imageBitmap);
                imageBitmap= Delivery.read(inputStream);

                imageView.setImageBitmap(imageBitmap);

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } // run
    } // socketThread class

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        } // requestCode if
    } // onActivityResult
} // mainActivity