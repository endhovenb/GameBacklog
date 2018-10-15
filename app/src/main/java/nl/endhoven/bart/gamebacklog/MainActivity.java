package nl.endhoven.bart.gamebacklog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements GameBacklogAdapter.GameBacklogOnClickListener{

    private RecyclerView mRecyclerView;
    private List<GameBacklog> mGameBackLogs;
    private GameBacklogAdapter mAdapter;

    public static final String EXTRA_ADD_BACKLOG = "AddBacklog";
    public static final String EXTRA_UPDATE_BACKLOG = "UpdateBacklog";
    public static final int ADD_REQUESTCODE = 1234;
    public static final int UPDATE_REQUESTCODE = 4321;

    public final static int GET_ALL_GAMES = 0;
    public final static int DELETE_GAME = 1;
    public final static int UPDATE_GAME = 2;
    public final static int INSERT_NEW_GAME = 3;

    static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.recyclerView);
        mGameBackLogs = new ArrayList<>();

        db = AppDatabase.getInstance(this);
        new GameBacklogAsyncTask(GET_ALL_GAMES).execute();

        //Set layoutmanager with span of 1
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(1,
                LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GameBacklogAdapter(mGameBackLogs,this);
        mRecyclerView.setAdapter(mAdapter);

        updateUI();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, ADD_REQUESTCODE);
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());
                        new GameBacklogAsyncTask(DELETE_GAME).execute(mGameBackLogs.get(position));
                        Toast.makeText(MainActivity.this, "Game is deleted", Toast.LENGTH_SHORT).show();
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void GameBacklogClick(int position) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        intent.putExtra(EXTRA_UPDATE_BACKLOG, mGameBackLogs.get(position));
        startActivityForResult(intent, UPDATE_REQUESTCODE);
    }

    public void onGameBacklogDbUpdated(List list) {
        mGameBackLogs = list;
        updateUI();
    }

    public void updateUI() {
        if (mAdapter == null) {
            mAdapter = new GameBacklogAdapter(mGameBackLogs, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mGameBackLogs);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                case ADD_REQUESTCODE:
                    GameBacklog addedGameBacklog = data.getParcelableExtra(MainActivity.EXTRA_ADD_BACKLOG);
                    new GameBacklogAsyncTask(INSERT_NEW_GAME).execute(addedGameBacklog);
                    break;
                case UPDATE_REQUESTCODE:
                    GameBacklog updatedGameBacklog = data.getParcelableExtra(MainActivity.EXTRA_UPDATE_BACKLOG);
                    new GameBacklogAsyncTask(UPDATE_GAME).execute(updatedGameBacklog);
                    updateUI();
                    break;
            }
        }
    }

    public class GameBacklogAsyncTask extends AsyncTask<GameBacklog, Void, List> {

        private int taskCode;

        public GameBacklogAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }

        @Override
        protected List doInBackground(GameBacklog... gameBacklogs) {

            switch (taskCode){
                case DELETE_GAME:
                    db.gameBacklogDao().deleteGames(gameBacklogs[0]);
                    break;
                case UPDATE_GAME:
                    db.gameBacklogDao().updateGames(gameBacklogs[0]);
                    break;
                case INSERT_NEW_GAME:
                    db.gameBacklogDao().insertGames(gameBacklogs[0]);
                    break;
            }

            //To return a new list with the updated data, we get all the data from the database again.
            return db.gameBacklogDao().getAllGames();
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            onGameBacklogDbUpdated(list);
        }
    }

}