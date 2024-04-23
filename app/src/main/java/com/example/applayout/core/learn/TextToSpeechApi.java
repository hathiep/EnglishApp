package com.example.applayout.core.learn;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;



public interface TextToSpeechApi {
    @Headers({
            "content-type: application/json",
            "X-RapidAPI-Key: 8df0ce3dcdmsh9bb16beb49b1d21p1cc793jsn1c4e07d814dd",
            "X-RapidAPI-Host: ai-powered-text-to-speech1.p.rapidapi.com"
    })
    @POST("synthesize-speech")
    Call<TextToSpeechResponse> synthesizeSpeech(@Body TextToSpeechRequestBody requestBody);
}