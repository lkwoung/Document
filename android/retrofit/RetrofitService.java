package com.lkwoung.json;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {

    // @GET( EndPoint-자원위치(URI) )
    @GET("/{post}")
    // 반환타입 Call<PostResult> - Call 은 응답이 왔을 때 Callback 으로 불려질 타입
    // PostResult 는 요청에 대한 응답데이터를 받아서 DTO 객체화할 클래스 타입 지정
    Call<PostResult> getPosts(@Path("post") String post); //매개변수 post 가 @Path("post")를 보고 {post}부분을 대치함

}