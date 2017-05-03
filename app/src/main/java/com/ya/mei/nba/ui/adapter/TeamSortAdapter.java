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
import com.ya.mei.nba.model.TeamSorts;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenliang3 on 2016/3/8.
 */
public class TeamSortAdapter extends RecyclerView.Adapter<TeamSortAdapter.TeamHolder> {

    private final Map<String, Integer> teamIconMap = Constant.getTeamIcons();
    private List<TeamSorts.TeamsortEntity> teams;
    private Context context;
    private LayoutInflater inflater;
    private static final int TEAMS_TITLE = 0;
    private static final int TEAMS_ENTITY = 1;

    public TeamSortAdapter(Context context, List<TeamSorts.TeamsortEntity> teams){
        this.context = context;
        this.teams = teams;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 17 == 0){
            return TEAMS_TITLE;
        }else {
            return TEAMS_ENTITY;
        }
    }

    @Override
    public TeamSortAdapter.TeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TeamHolder teamHolder;
        if (viewType == TEAMS_TITLE){
            teamHolder = new TeamTitle(inflater.inflate(R.layout.item_fragment_teamsort_title, parent, false));
        }else {
            teamHolder = new TeamEntity(inflater.inflate(R.layout.item_fragment_teamsort_entity, parent, false));
        }

        return teamHolder;
    }

    @Override
    public void onBindViewHolder(TeamSortAdapter.TeamHolder holder, int position) {
        holder.updateItem(position);
    }

    @Override
    public int getItemCount() {
        return teams == null ? 0 : teams.size() + 2;
    }

    abstract class TeamHolder extends RecyclerView.ViewHolder {
        public TeamHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        abstract void updateItem(int position);
    }

    class TeamTitle extends TeamHolder{

        @Bind(R.id.teams_title)
        TextView teamTitle_tv;

        public TeamTitle(View itemView) {
            super(itemView);
        }

        @Override
        void updateItem(int position) {
            if (teams == null || teams.size() == 0){
                return;
            }
            if (position == 0){
                teamTitle_tv.setText("东部球队");
            }else {
                teamTitle_tv.setText("西部球队");
            }
        }
    }

    class TeamEntity extends TeamHolder implements View.OnClickListener {
        @Bind(R.id.place)
        TextView teamPlace_tv;
        @Bind(R.id.team_icon)
        ImageView teamIcon_tv;
        @Bind(R.id.team_name)
        TextView teamName_tv;
        @Bind(R.id.win)
        TextView teamWin_tv;
        @Bind(R.id.lose)
        TextView teamLose_tv;
        @Bind(R.id.win_percent)
        TextView teamWinPer_tv;
        @Bind(R.id.gap)
        TextView teamGap_tv;
        @Bind(R.id.divider)
        View divider;
        @Bind(R.id.itemLayout)
        View itemLayout;

        private TeamSorts.TeamsortEntity team;

        public TeamEntity(View itemView) {
            super(itemView);
            itemLayout.setOnClickListener(this);
        }

        @Override
        void updateItem(int position) {
            if (teams == null || teams.size() == 0){
                return;
            }
            int index = 0;
            if (position > 17){
                index = position - 2;
            }else if (position > 0){
                index = position - 1;
            }
            team = teams.get(index);
            teamPlace_tv.setText(team.getSort());
            teamName_tv.setText(team.getTeam());
            teamWin_tv.setText(team.getWin());
            teamLose_tv.setText(team.getLose());
            teamWinPer_tv.setText(team.getWinPercent());
            teamGap_tv.setText(team.getGap());
            if (index == 0 || index == 16){
                teamPlace_tv.setText("");
                divider.setVisibility(View.VISIBLE);
                teamIcon_tv.setVisibility(View.INVISIBLE);
                team = null;
            }else {
                teamIcon_tv.setVisibility(View.VISIBLE);
                teamIcon_tv.setImageResource(teamIconMap.get(team.getTeam()));
                divider.setVisibility(View.GONE);
            }

            if ((index > 0 && index < 9) || (index > 16 && index < 25)){
                teamPlace_tv.setTextColor(Color.parseColor("#f44336"));
            }else {
                teamPlace_tv.setTextColor(Color.parseColor("#212121"));
            }
        }

        @Override
        public void onClick(View v) {
            if (team == null){
                return;
            }
            new FinestWebView.Builder((Activity)context)
                    .gradientDivider(false)
                    .show(team.getTeamurl());
        }
    }

}
