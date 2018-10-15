package nl.endhoven.bart.gamebacklog;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
@Dao
public interface GameBacklogDao {
    @Query("SELECT * FROM gamebacklog")
    public List<GameBacklog> getAllGames();

    @Insert
    public void insertGames(GameBacklog reminders);

    @Delete
    public void deleteGames(GameBacklog reminders);

    @Update
    public void updateGames(GameBacklog reminders);
}
