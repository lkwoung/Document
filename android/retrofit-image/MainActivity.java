package com.lkwoung.image_sender_json;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView ipText;
    ProgressDialog progressDialog;
    String serverUrl = "http://localhost:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipText = (TextView) findViewById(R.id.ipText);
        imageView = (ImageView) findViewById(R.id.imageView);
        Button imgBtn = (Button) findViewById(R.id.image);
        Button sendBtn = (Button) findViewById(R.id.send);

        // bring the image from gallery
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);
            }
        });

        // send the image to server
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                myAsyncTasks.execute();
            }
        });
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("processing results");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }//onPreExecute

        @Override
        protected String doInBackground(String... params) {
            // Fetch data from the API in the background.
            String result = "";
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray());

            MultipartBody.Part uploadImg = MultipartBody.Part.createFormData("image", "image", requestBody);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(serverUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitService service = retrofit.create(RetrofitService.class);
            Call<PostResult> call = service.postData(uploadImg);

            call.enqueue(new Callback<PostResult>() {
                @Override
                public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                    if (response.isSuccessful()) {
                        PostResult result = response.body();
                        Log.d("200", "onResponse : 성공, 결과\n" + result.toString());
                    } else {
                        Log.d("400", "onResponse : 실패");
                    }
                }

                @Override
                public void onFailure(Call<PostResult> call, Throwable t) {
                    Log.d("500", "onFailure : " + t.getMessage());
                }
            });
            return result;
        }//doInBackground

        @Override
        protected void onPostExecute(String result) {
            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
        }//onPostExecute
    }//MyAsyncTasks

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
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
}