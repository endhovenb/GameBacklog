package nl.endhoven.bart.gamebacklog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GameBacklogAdapter extends RecyclerView.Adapter<GameBacklogAdapter.ViewHolder> {

    public List<GameBacklog> mGameBackLogList;
    private GameBacklogOnClickListener mGameBacklogOnClickListener;

    public interface GameBacklogOnClickListener {
        void GameBacklogClick(int position);
    }

    public GameBacklogAdapter(List<GameBacklog> gameBacklogObjects, GameBacklogOnClickListener mGameBacklogOnClickListener) {
        mGameBackLogList = gameBacklogObjects;
        this.mGameBacklogOnClickListener = mGameBacklogOnClickListener;
    }

    @Override
    public GameBacklogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new GameBacklogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameBacklogAdapter.ViewHolder holder, int position) {
        // Gets a single item in the list from its position
        final GameBacklog gameBacklog = mGameBackLogList.get(position);
        // The holder argument is used to reference the views inside the viewHolder
        // Populate the views with the data from the list
        holder.titelText.setText(gameBacklog.getGameTitle());
        holder.platformText.setText(gameBacklog.getGamePlatform());
        holder.statusText.setText(gameBacklog.getPlayStatus());
        holder.noteText.setText(gameBacklog.getGameNote());
        holder.dateText.setText(gameBacklog.getAddDate());
    }

    @Override
    public int getItemCount() {
        return mGameBackLogList.size();
    }

    public void swapList (List<GameBacklog> newList) {
        mGameBackLogList = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titelText;
        public TextView platformText;
        public TextView statusText;
        public TextView dateText;
        public TextView noteText;


        public ViewHolder(View itemView) {
            super(itemView);
            this.titelText = itemView.findViewById(R.id.gameTitle);
            this.platformText = itemView.findViewById(R.id.platformText);
            this.statusText = itemView.findViewById(R.id.statusText);
            this.dateText = itemView.findViewById(R.id.dateText);
            this.noteText = itemView.findViewById(R.id.noteText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mGameBacklogOnClickListener.GameBacklogClick(clickedPosition);
        }
    }
}
