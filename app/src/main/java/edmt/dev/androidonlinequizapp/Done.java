package edmt.dev.androidonlinequizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edmt.dev.androidonlinequizapp.Common.Common;
import edmt.dev.androidonlinequizapp.Model.QuestionScore;

public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore, getTxtResultQuestion, getResult;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        txtResultScore = (TextView) findViewById(R.id.txtScore); //or txtTotalScore??
        getTxtResultQuestion = (TextView) findViewById((R.id.txtTotalQuestion));
        getResult = (TextView) findViewById((R.id.txtResult));
        progressBar = (ProgressBar) findViewById(R.id.doneProgressBar);
        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Done.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        //get data from bundle and set view
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");
            String a1 = "Как показал тест, у вас  низкий уровень счастья!";
            String a2 = "Как показал тест, у вас  пониженный уровень счастья!";
            String a3 = "Как показал тест, у вас  средний уровень счастья!";
            String a4 = "Как показал тест, у вас  повышенный уровень счастья!";
            String a5 = "Как показал тест, у вас  высокий уровень счастья!";
            String result = "";

            if (score <= 20) {
                result = a1;
            }
            if (score >= 21 & score <= 40) {
                result = a2;
            }
            if (score >= 41 & score <= 60) {
                result = a3;
            }
            if (score >= 61 & score <= 80) {
                result = a4;
            }
            if (score >= 81 & score <= 100) {
                result = a5;
            }

            txtResultScore.setText(String.format("Колличество ваших баллов: %d", score));
            getTxtResultQuestion.setText((String.format("Количество отвеченых вопросов: %d / %d", correctAnswer, totalQuestion)));
            getResult.setText(result);
            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);
            //upload point to BD
            question_score.child(String.format("%s_%s", Common.currentUser.getUserName(),
                    Common.categoryId))
                    .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUserName(),
                            Common.categoryId),
                            Common.currentUser.getUserName(),
                            String.valueOf(score)));

        }
    }
}
