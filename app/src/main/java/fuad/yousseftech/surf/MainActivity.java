package fuad.yousseftech.surf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fuad.yousseftech.surf.Firebase.Web;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private LinkAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView currentLinkView;
    private Map<String, Web> webMap = new HashMap<>();
    private String currentId;

    public static String getDomainName(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference ref = db.getReference();
        //websRef = ref.child("webs");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentId = (String) dataSnapshot.child("currentId").getValue();
                String currentUrl = (String) dataSnapshot.child("currentUrl").getValue();
                //Log.i("currentId", currentId);
                //Log.i("currentUrl", currentUrl);
                mAdapter.setWeb(currentId);

                currentLinkView = (TextView) findViewById(R.id.currentLinkContent);
                Web currentWeb = webMap.get(currentId);
                String shortTitle = currentWeb.getTitle();
                if (shortTitle.length() > 45)
                    shortTitle = shortTitle.substring(0, 42) + "...";
                String shortUrl = getDomainName(currentWeb.getLink());
                String sourceText = "<b>" + shortTitle + "</b><br /><small>"
                        + shortUrl + "</small>";
                currentLinkView.setText(Html.fromHtml(sourceText));

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference rref = ref.child("webs");
        rref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> webs = (HashMap) dataSnapshot.getValue();
                for(String key: webs.keySet()) {
                    Object val = webs.get(key);
                    webMap.put(key, new Web(val));
                }
                mAdapter.setWebMap(webMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.link_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new LinkAdapter();
        recyclerView.setAdapter(mAdapter);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();

        mAdapter.setOnLinkClickListener(new LinkAdapter.OnLinkClickListener() {
            @Override
            public void onLinkClicked(String link) {
                //System.out.println("Clicked link: " + link);
                //Intent intent = new Intent(ViewAllStudentsActivity.this, EditStudentActivity.class);
                //intent.putExtra(STUDENT_DATA, student);
                //startActivityForResult(intent, EDIT_REQUEST);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.nav_myLinks) {

        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void computerLink(View view) {
    }

    public void openCurrentLink(View view) {
        if(currentId != null && currentId != "") {
            Web currentWeb = webMap.get(currentId);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentWeb.getLink()));
            view.getContext().startActivity(browserIntent);
        }

    }
}
