package com.ya.mei.nba.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thefinestartist.finestwebview.FinestWebView;
import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.R;
import com.ya.mei.nba.model.Games;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenliang3 on 2016/3/9.
 */
public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.GamesViewHolder> {

    private final static Map<String, Integer> teamIconMap = Constant.getTeamIcons();
    private List<Games.GamesEntity> games;
    private Context context;
    private LayoutInflater inflater;
    private static final int GAMES_DATE = 0;

    public GamesAdapter(Context context, List<Games.GamesEntity> games){
        super();
        this.context = context;
        this.games = games;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return games.get(position).getType();
    }

    @Override
    public GamesAdapter.GamesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GamesViewHolder gamesViewHolder;
        if (viewType == GAMES_DATE){
            gamesViewHolder = new GameTitleViewHolder(inflater.inflate(R.layout.item_fragment_teamsort_title, parent, false));
        }else {
            gamesViewHolder = new GameEntityViewHolder(inflater.inflate(R.layout.item_fragment_games, parent, false));
        }

        return gamesViewHolder;
    }

    @Override
    public void onBindViewHolder(GamesAdapter.GamesViewHolder holder, int position) {
        holder.updateItem(position);
    }

    @Override
    public int getItemCount() {
        return games == null ? 0 : games.size();
    }

    abstract class GamesViewHolder extends RecyclerView.ViewHolder {
        public GamesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        abstract void updateItem(int position);
    }

    class GameTitleViewHolder extends GamesViewHolder{
        @Bind(R.id.teams_title)
        TextView mDate_tv;

        public GameTitleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void updateItem(int position) {
            mDate_tv.setText(games.get(position).getDate());
        }
    }

    class GameEntityViewHolder extends GamesViewHolder implements View.OnClickListener {
        @Bind(R.id.left_teamName_tv)
        TextView leftTeamNameTv;
        @Bind(R.id.right_teamName_tv)
        TextView rightTeamNameTv;
        @Bind(R.id.left_teamIcon_iv)
        ImageView leftTeamIconIv;
        @Bind(R.id.right_teamIcon_iv)
        ImageView rightTeamIconIv;
        @Bind(R.id.gamestaus)
        TextView gameStatusTv;
        @Bind(R.id.scores)
        TextView scoresTv;
        @Bind(R.id.stats)
        TextView statsTv;
        @Bind(R.id.status_indicate)
        View statusIndicate;

        private Games.GamesEntity gamesEntity;

        public GameEntityViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            statsTv.setOnClickListener(this);
        }

        @Override
        void updateItem(int position) {
            gamesEntity = games.get(getLayoutPosition());
            if (gamesEntity.getStateText() != ""){
                statusIndicate.setVisibility(View.VISIBLE);
                if ("已结束".equals(gamesEntity.getStatus())){
                    statusIndicate.setBackgroundColor(Color.parseColor("#448AFF"));
                }else {
                    statusIndicate.setBackgroundColor(Color.parseColor("#f44336"));
                }
                statsTv.setVisibility(View.VISIBLE);
                statsTv.setText(gamesEntity.getStateText());
            }else {
                statusIndicate.setVisibility(View.GONE);
                statsTv.setVisibility(View.GONE);
            }
            leftTeamNameTv.setText(gamesEntity.getLeftTeam());
            leftTeamIconIv.setImageResource(teamIconMap.get(gamesEntity.getLeftTeam()));
            gameStatusTv.setText(gamesEntity.getStatus());
            rightTeamNameTv.setText(gamesEntity.getRightTeam());
            rightTeamIconIv.setImageResource(teamIconMap.get(gamesEntity.getRightTeam()));
            scoresTv.setText(gamesEntity.getLeftScore() + "-" + gamesEntity.getRightScore());
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.stats:
                    new FinestWebView.Builder((Activity)context)
                            .gradientDivider(false)
                            .show(gamesEntity.getStateUrl());
                    break;
                default:
                    new FinestWebView.Builder((Activity)context)
                            .gradientDivider(false)
                            .show(gamesEntity.getStatusUrl());
                    break;
            }
        }
    }

}
