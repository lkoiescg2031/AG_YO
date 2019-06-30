package e.lkoie.ag_yo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

public class JokeListActivity extends AppCompatActivity {

    private FloatingActionButton fab[] = new FloatingActionButton[4];
    private ListView solvedJokeList;
    private ArrayAdapter<String> solvedJokeListAdapter;
    private ListView unsolvedJokeList;
    private ArrayAdapter<String> unsolvedJokeListAdapter;
    private DBController dbController =new DBController(this);
    private DialogController dialogController = new DialogController(JokeListActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_list);
        //Tab Host 설정
        TabHost tabHost = (TabHost)findViewById(R.id.joke_list_tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Tab1");
        tabSpec1.setIndicator("푼 문제"); // Tab Subject
        tabSpec1.setContent(R.id.joke_list_tabView1);
        tabHost.addTab(tabSpec1);
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Tab2");
        tabSpec2.setIndicator("안 푼 문제"); // Tab Subject
        tabSpec2.setContent(R.id.joke_list_tabView2); // Tab Content
        tabHost.addTab(tabSpec2);
        //ListView 설정
        dbController.openRead();
        ArrayList<String> solvedJokeString = dbController.selectSolveProblem();
        solvedJokeList = (ListView) findViewById(R.id.joke_list_listView1);
        solvedJokeListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,solvedJokeString);
        solvedJokeList.setAdapter(solvedJokeListAdapter);
        ArrayList<String> unsolvedJokeString = dbController.selectUnsolveProblem();
        unsolvedJokeList = (ListView) findViewById(R.id.joke_list_listView2);
        unsolvedJokeListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,unsolvedJokeString);
        unsolvedJokeList.setAdapter(unsolvedJokeListAdapter);
        dbController.close();
        //ActionMenu
        //Floating Action Menu 설정
        fab[0] = (FloatingActionButton) findViewById(R.id.joke_list_delete);
        fab[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogController.deleteQuizDialog();
            }
        });
        fab[1] = (FloatingActionButton) findViewById(R.id.joke_list_insert);
        fab[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogController.insertQuizDialog();
            }
        });
        fab[2] = (FloatingActionButton) findViewById(R.id.joke_list_update);
        fab[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogController.updateQuizDialog();
            }
        });
        fab[3] = (FloatingActionButton) findViewById(R.id.joke_list_play);
        fab[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogController.showPlayDialog(10);
            }
        });
    }
}
