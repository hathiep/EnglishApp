package com.example.applayout.core.learn;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.applayout.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlashCardActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String topic;
    private List<FlashCardEntity> flashCardEntityList;
    private ImageView back_button;
    private ImageView sound;
    private TextView headerView;

    private ViewFlipper viewFlipper;
    private Button nextFlipperButton;
    private Button backFlipperButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.learn_activity_flash_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        topic = intent.getStringExtra("topicName");
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("NewWords" + "/" + topic);
        flashCardEntityList = new ArrayList<>();
        headerView = findViewById(R.id.header_name);
        headerView.setText(topic);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                flashCardEntityList.clear();
                viewFlipper.removeAllViews();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.getValue() != null) {
                        FlashCardEntity flashCard = dataSnapshot.getValue(FlashCardEntity.class);
                        flashCardEntityList.add(flashCard);

                        View cardView = LayoutInflater.from(FlashCardActivity.this)
                                .inflate(R.layout.learn_viewflipper1, viewFlipper, false);

                        TextView wordTextView = cardView.findViewById(R.id.word);
                        TextView spellTextView = cardView.findViewById(R.id.spell_word);
                        TextView meaningTextView = cardView.findViewById(R.id.meaning_word);
                        ImageView imageView = cardView.findViewById(R.id.image_word);
                        TextView exampleTextView = cardView.findViewById(R.id.example_word);

                        wordTextView.setText(flashCard.getWord());
                        meaningTextView.setText(flashCard.getMeaning());
                        spellTextView.setText(flashCard.getSpell());

                        if (TextUtils.isEmpty(flashCard.getImage())) {
                            imageView.setVisibility(View.GONE);
                            exampleTextView.setVisibility(View.VISIBLE);
                            exampleTextView.setText(flashCard.getExample());
                        } else {
                            imageView.setVisibility(View.VISIBLE);
                            exampleTextView.setVisibility(View.GONE);
                            Picasso.get().load(flashCard.getImage()).into(imageView);
                        }

                        viewFlipper.addView(cardView);
                    } else {
                        Toast.makeText(FlashCardActivity.this, "null", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        ImageView imV_home = findViewById(R.id.imV_learn);
        TextView tv_home = findViewById(R.id.tv_learn);
        imV_home.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imV_home.setImageResource(R.drawable.icon_learn2);
        tv_home.setTextAppearance(R.style.menu_text);

        viewFlipper = findViewById(R.id.view_flipper);
        nextFlipperButton = findViewById(R.id.learn_next_btn1);
        backFlipperButton = findViewById(R.id.learn_back_btn1);
        back_button = findViewById(R.id.learn_back_button);
        sound = findViewById(R.id.sound);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LearnMain.class);
                startActivity(intent);
                finish();
            }
        });

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View currentView = viewFlipper.getCurrentView();
                TextView wordTextView = currentView.findViewById(R.id.word);
                String currentWord = wordTextView.getText().toString();
                callAPISound(currentWord);
            }
        });

        nextFlipperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        });

        backFlipperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showPrevious();
            }
        });
    }

    private  void callAPISound(String word) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ai-powered-text-to-speech1.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TextToSpeechApi api = retrofit.create(TextToSpeechApi.class);

        TextToSpeechRequestBody requestBody = new TextToSpeechRequestBody(word, "en-US", "Matthew", "neural", false);

        Call<TextToSpeechResponse> call = api.synthesizeSpeech(requestBody);
        call.enqueue(new Callback<TextToSpeechResponse>() {
            @Override
            public void onResponse(Call<TextToSpeechResponse> call, Response<TextToSpeechResponse> response) {
                if (response.isSuccessful()) {
                    TextToSpeechResponse responseBody = response.body();
                    if (responseBody != null) {
                        String url = responseBody.getFileDownloadUrl();
                        System.out.println(url);

                        playSoundFromUrl(url);
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<TextToSpeechResponse> call, Throwable t) {
                // Handle API call failure
            }
        });
    }

    private void playSoundFromUrl(String url) {
        try {
            mediaPlayer = new MediaPlayer();

            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();

            /*mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    Toast.makeText(FlashCardActivity.this, "start", Toast.LENGTH_SHORT).show();
                }
            });*/

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // Xử lý lỗi ở đây
                    Log.e("MediaPlayerError", "Error code: " + what + ", Extra code: " + extra);
                    mp.release();
                    return false; // Trả về false để cho MediaPlayer xử lý lỗi mặc định
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            // Thay Toast bằng Log để xem lỗi trong Logcat
            Log.e("MediaPlayerError", "Error: " + e.getMessage());
        }
    }

}
