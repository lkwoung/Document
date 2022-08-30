package com.lkwoung.json;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    String serverUrl = "http://localhost:8080/";
    TextView resultsTextView;
    ProgressDialog progressDialog;
    Button displayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // to hook the components on the onCreate method.
        resultsTextView = (TextView) findViewById(R.id.results);
        displayData = (Button) findViewById(R.id.displayData);

        displayData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                myAsyncTasks.execute();
            }
        });
    }

    // Defining an AsyncTask
    public class MyAsyncTasks extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("processing results");
            progressDialog.setCancelable(false);
            progressDialog.show();
        } // onPreExecute

        @Override
        protected String doInBackground(String... params) {
            // Fetch data from the API in the background.
            String result = "";

            // Retrofit 인스턴스 생성
            // Converter 는 여러개 등록 가능, 등록 순서대로 변환 가능여부 판단, 변환 불가능하면 다음 Converter 확인
            // GsonConverter 는 변환이 불가능해도 가능하다고 함으로 마지막에 넣는게 좋음
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(serverUrl)
                    .addConverterFactory(GsonConverterFactory.create()) // JSON을 변환해줄 Gson 변환기 등록
                    .build();

            // Interface 객체 구현
            // retrofit 을 통한 객체 구현, 추상 메소등 중 사용할 메스드 Call 객체에 등록
            RetrofitService service = retrofit.create(RetrofitService.class);
            Call<PostResult> call = service.getPosts("greeting");

            // 비동기 enqueue 작업으로 실행, 통신종료 후 이벤트 처리를 위해 Callback 등록
            // onResponse 성공 / onFailure 실패 구분하여 메인스레드에서 처리할 작업 등록
            // onResponse 가 무조건 성공을 의미하는 것이 아니므로 isSuccessful() 를 사용해 확인한다.
            call.enqueue(new Callback<PostResult>() {
                @Override
                public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                    if (response.isSuccessful()) {
                        PostResult postResult = response.body();
                        Log.d("200", "onResponse : 성공, 결과\n" + postResult.toString());
                        resultsTextView.setText(postResult.toString());
                    } else {
                        Log.d("4**, 3**", "onResponse : 실패");
                        resultsTextView.setText("fail 4**");
                    }
                }

                @Override
                public void onFailure(Call<PostResult> call, Throwable t) {
                    Log.d("5**, noConnect", "onFailure : " + t.getMessage());
                    resultsTextView.setText("fail 5**");
                }
            });
            return result;
        } // doInBackground

        @Override
        protected void onPostExecute(String result) {
            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
        } // onPostExecute
    } // MyAsyncTasks
}
// 참고 사이트
// https://jaejong.tistory.com/33
// http://devflow.github.io/retrofit-kr/