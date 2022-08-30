package com.lkwoung.json;

// REST API 응답데이터 구조
import com.google.gson.annotations.SerializedName;

// DTO 모델 - PostResult Class 선언
public class PostResult {
    /*
     *  JSON
     *  @SerializedName() 으로 일치 시켜주지 않는 경우에는 클래스 변수명이 일치해야함
     *  @SerializedName() 로 변수명을 일치시켜주면 변수명이 달라도 알아서 매핑시켜줌
     *  XML
     *  @Element(name="속성명") @Element 어노테이션 사용
     */

    @SerializedName("id")
    private int id;

    @SerializedName("content")
    private String content;

    // toString()을 Override 해주지 않으면 객체 주소값을 출력함

    @Override
    public String toString() {
        return "PostResult{" +
                "userId=" + id +
                ", bodyValue='" + content + '\'' +
                '}';
    }
}
