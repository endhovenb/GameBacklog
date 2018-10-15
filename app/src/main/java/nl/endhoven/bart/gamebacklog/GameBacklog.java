package nl.endhoven.bart.gamebacklog;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "gamebacklog")
public class GameBacklog implements Parcelable {

    //Create Db Fields
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo (name = "gametitle")
    private String gameTitle;

    @ColumnInfo (name = "gameplatform")
    private String gamePlatform;

    @ColumnInfo (name = "gamenote")
    private String gameNote;

    @ColumnInfo (name = "playstatus")
    private String playStatus;

    @ColumnInfo (name = "adddate")
    private String addDate;

    public GameBacklog(String gameTitle, String gamePlatform, String gameNote, String playStatus, String addDate) {
        this.gameTitle = gameTitle;
        this.gamePlatform = gamePlatform;
        this.gameNote = gameNote;
        this.playStatus = playStatus;
        this.addDate = addDate;
    }
    protected GameBacklog(Parcel in) {
        this.id = in.readLong();
        this.gameTitle = in.readString();
        this.gamePlatform = in.readString();
        this.gameNote = in.readString();
        this.playStatus = in.readString();
        this.addDate = in.readString();
    }

    public static final Creator<GameBacklog> CREATOR = new Creator<GameBacklog>() {
        @Override
        public GameBacklog createFromParcel(Parcel source) {
            return new GameBacklog(source);
        }

        @Override
        public GameBacklog[] newArray(int size) {
            return new GameBacklog[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.gameTitle);
        dest.writeString(this.gamePlatform);
        dest.writeString(this.gameNote);
        dest.writeString(this.playStatus);
        dest.writeString(this.addDate);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getGamePlatform() {
        return gamePlatform;
    }

    public void setGamePlatform(String gamePlatform) {
        this.gamePlatform = gamePlatform;
    }

    public String getGameNote() {
        return gameNote;
    }

    public void setGameNote(String gameNote) {
        this.gameNote = gameNote;
    }

    public String getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(String playStatus) {
        this.playStatus = playStatus;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public static Creator<GameBacklog> getCREATOR() {
        return CREATOR;
    }
}
