package edmt.dev.androidonlinequizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edmt.dev.androidonlinequizapp.Common.Common;
import edmt.dev.androidonlinequizapp.Model.Question;

public class Start extends AppCompatActivity {

    Button btnPlay;

    FirebaseDatabase database;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        loadQuestions(Common.categoryId);

        btnPlay = (Button) findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Start.this, Playing.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadQuestions(String categoryId) {

        //Сначала очистить список, вдруг вопросы остались
        if (Common.questionList.size() > 0)
            Common.questionList.clear();

        questions.orderByChild("CategoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Question ques = postSnapshot.getValue(Question.class);
                            Common.questionList.add(ques);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        //Random list
        // Collections.shuffle(Common.questionList);
    }
}
