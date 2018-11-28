package edmt.dev.androidonlinequizapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edmt.dev.androidonlinequizapp.Common.Common;
import edmt.dev.androidonlinequizapp.Interface.ItemClickListener;
import edmt.dev.androidonlinequizapp.Interface.RankingCallBack;
import edmt.dev.androidonlinequizapp.Model.QuestionScore;
import edmt.dev.androidonlinequizapp.Model.Ranking;
import edmt.dev.androidonlinequizapp.ViewHolder.RankingViewHolder;


public class RankingFragment extends Fragment {
    View myFragment;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference questionScore, rankingTbl;

    int sum = 0;

    public static RankingFragment newInstance() {
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTbl = database.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_ranking, container, false);

        //init view
        rankingList = (RecyclerView) myFragment.findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                //update rankingTABLE
                rankingTbl.child(ranking.getUserName())
                        .setValue(ranking);
                //showRanking(); //после загрузки надо отсортировать ранкинг табле и показать результаты
            }
        });

        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class,
                R.layout.layout_ranking,
                RankingViewHolder.class,
                rankingTbl.orderByChild("score")

        ) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, Ranking model, int position) {

                viewHolder.txt_name.setText(model.getUserName());
                viewHolder.txt_score.setText((String.valueOf(model.getScore())));

                //фиксим краши на клике на айтем
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);

        return myFragment;
    }

    private void updateScore(final String userName, final RankingCallBack<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            QuestionScore ques = data.getValue(QuestionScore.class);
                            sum += Integer.parseInt(ques.getScore());

                            //После суммы всех скоре, мы должны просумироть все переменные тут,
                            // потому что файрбейс это асинхронная БД

                            Ranking ranking = new Ranking(userName, sum);
                            callback.callBack(ranking);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
