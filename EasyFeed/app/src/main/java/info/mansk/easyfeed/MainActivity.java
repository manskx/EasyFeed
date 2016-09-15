package info.mansk.easyfeed;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NasaNewsFragment.OnFragmentInteractionListener {
    private static final String TAG_TASK_FRAGMENT = "image_downloader_fragment";
    private ImageDownloader imageDownloaderFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getFragmentManager();
        imageDownloaderFragment = (ImageDownloader) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (imageDownloaderFragment == null) {
            imageDownloaderFragment = new ImageDownloader();
            fm.beginTransaction().add(imageDownloaderFragment, TAG_TASK_FRAGMENT).commit();
        }
    }


    public void onRssItemSelected(RssItem selectedRssItem) {
        // If the frag is not available, we're in the one-pane layout and must swap frags...

        // Create fragment and give it an argument for the selected article
        RssItemDetailsFragment newFragment = new RssItemDetailsFragment();
        newFragment.setSelectedRssItem(selectedRssItem);
        newFragment.setImageDownloaderFragment(imageDownloaderFragment);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        ft.replace(R.id.mainFrame, newFragment);
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fm = getFragmentManager();
        Fragment CurrentFragment    = null;
        FragmentTransaction ft = fm.beginTransaction();
        int id = item.getItemId();
        // Handle navigation fragments
        if (id == R.id.nav_nasa_news) {
            CurrentFragment =   new NasaNewsFragment();
            ((NasaNewsFragment)CurrentFragment).setImageDownloaderFragment(imageDownloaderFragment);
        }
        ft.replace(R.id.mainFrame, CurrentFragment);
        ft.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
