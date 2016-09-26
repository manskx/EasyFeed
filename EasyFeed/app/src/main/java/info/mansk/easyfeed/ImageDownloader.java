package info.mansk.easyfeed;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ImageDownloader ua a retained Fragment using to download image using async task
 * Without layout file
 * {@link Fragment} subclass.
 */
public class ImageDownloader extends Fragment {
    private static final String TAG = ImageDownloader.class.getSimpleName();

    public ImageDownloader() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }
    public void DownloadAndShowImage(ImageView imageView, String ImageURL){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new ImageDownloaderTask(imageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ImageURL);
        else
            new ImageDownloaderTask(imageView).execute(ImageURL);
    }


    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder =
                                ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_menu_gallery);
                        //imageView.getContext().getResources().getDrawable();
                        imageView.setImageDrawable(placeholder);
                    }
                }

            }
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();

                int statusCode = urlConnection.getResponseCode();
                if (statusCode != 200) {
                    Log.w("ImageDownloader", "Error downloading image from " + url);
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Log.w("ImageDownloader", "Image downloaded successfully from: " + url);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}
