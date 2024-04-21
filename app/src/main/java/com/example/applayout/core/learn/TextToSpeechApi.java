package com.example.applayout.core.learn;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;



public interface TextToSpeechApi {
    @Headers({
            "content-type: application/json",
            "X-RapidAPI-Key: fb162dd0f3msh5846e815fcd2ad6p129fc6jsn1dad6c0b4fa1",
            "X-RapidAPI-Host: ai-powered-text-to-speech1.p.rapidapi.com"
    })
    @POST("synthesize-speech")
    Call<TextToSpeechResponse> synthesizeSpeech(@Body TextToSpeechRequestBody requestBody);
}