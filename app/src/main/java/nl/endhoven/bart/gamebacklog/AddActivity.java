package nl.endhoven.bart.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    private EditText mTitle;
    private EditText mPlatform;
    private EditText mNote;
    private Spinner mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Add new game");

        //Initialize
        mTitle = findViewById(R.id.titleEditText);
        mPlatform = findViewById(R.id.platformEditText);
        mNote = findViewById(R.id.noteEditText);
        mStatus = findViewById(R.id.spinner);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get text from view.
                String titel = mTitle.getText().toString();
                String platform = mPlatform.getText().toString();
                String note = mNote.getText().toString();
                String status = mStatus.getSelectedItem().toString();

                //Create date.
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String date = formatter.format(new Date());

                //Required Titel
                if (!TextUtils.isEmpty(titel)) {
                    GameBacklog newGameBacklog = new GameBacklog(titel, platform, note,
                            status, date);
                    Intent resultIntent = new Intent();

                    resultIntent.putExtra(MainActivity.EXTRA_ADD_BACKLOG, newGameBacklog);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Snackbar.make(view, "title is required", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

}
