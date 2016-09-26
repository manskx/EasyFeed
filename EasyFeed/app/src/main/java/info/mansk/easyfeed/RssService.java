package info.mansk.easyfeed;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RssService extends IntentService {
    private static final String TAG =   "RssService";
    private static final String RSS_LINK = "http://www.nasa.gov/rss/dyn/educationnews.rss";
    public static final String ITEMS = "items";
    public static final String ACTION_RSS_PARSED = "info.mansk.easyfeed.action.ACTION_RSS_PARSED";



    public RssService() {
        super("RssService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service started");

        List<RssItem> rssItems = null;

            NASARssParser parser = new NASARssParser();
            rssItems = parser.parse(getInputStream(RSS_LINK));

        // Send result
        Intent resultIntent = new Intent(ACTION_RSS_PARSED);
        resultIntent.putExtra(ITEMS, (Serializable) rssItems);
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }

    public InputStream getInputStream(String link) {
        try {
            URL url = new URL(link);
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            Log.w( TAG, "Exception while retrieving the input stream", e);
            return null;
        }
    }


}
