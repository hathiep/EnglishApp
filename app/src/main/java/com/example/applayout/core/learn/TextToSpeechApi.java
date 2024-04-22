package com.example.applayout.core.learn;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;



public interface TextToSpeechApi {
    @Headers({
            "content-type': 'application/json",
            "X-RapidAPI-Key': '92fa9c0d25mshbe6f0d82c25b687p14f130jsn62a7e2fa8ae6",
            "X-RapidAPI-Host': 'ai-powered-text-to-speech1.p.rapidapi.com"
    })
    @POST("synthesize-speech")
    Call<TextToSpeechResponse> synthesizeSpeech(@Body TextToSpeechRequestBody requestBody);
}