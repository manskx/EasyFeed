package info.mansk.easyfeed;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import info.mansk.easyfeed.cache.ImageCache;
import info.mansk.easyfeed.cache.ImageFetcher;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NasaNewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NasaNewsFragment extends Fragment  implements AdapterView.OnItemClickListener  {

    // now using image fetcher -- v0.2
    // private static ImageDownloader ImageDownloaderFragment;
    private int mImageThumbSize;
    private static final String IMAGE_CACHE_DIR = "thumbs";

    private ListView listView;
    private Parcelable listViewState;
    private ImageFetcher mImageFetcher;

    private static final String TAG = NasaNewsFragment.class.getSimpleName();



    private OnFragmentInteractionListener mListener;

    public NasaNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);;


        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.ic_menu_gallery);
        mImageFetcher.addImageCache(getActivity().getFragmentManager(), cacheParams);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nasa_news, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {

        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save ListView state
        Log.d(TAG, "saving listview state @ onPause");
        if (listView != null){
            listViewState = listView.onSaveInstanceState();
            savedInstanceState.putParcelable("listViewState", listViewState);
        }
            super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ArrayList<HotelItem> listData = getListData();
        listView = (ListView) getActivity().findViewById(R.id.nasa_news_list);
        if (savedInstanceState != null) {
            listViewState   =   savedInstanceState.getParcelable("listViewState");
        }
        IntentFilter intentFilter = new IntentFilter(RssService.ACTION_RSS_PARSED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(resultReceiver, intentFilter);
        listView.setOnItemClickListener(this);
        startService();
    }

    private void startService() {
        Intent intent = new Intent(getActivity(), RssService.class);
        getActivity().startService(intent);
    }

    private BroadcastReceiver resultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<RssItem> items = (ArrayList<RssItem>) intent.getSerializableExtra(RssService.ITEMS);

            if (items != null) {

                listView.setAdapter(new ItemListAdapter(getActivity(), items, mImageFetcher));
                if(listViewState != null) {
                    Log.d(TAG, "trying to restore listview state..");
                    listView.onRestoreInstanceState(listViewState);
                }
            } else {
                Toast.makeText(getActivity(), "An error occurred while downloading the rss feed.",
                        Toast.LENGTH_LONG).show();
            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
    }
    @Override
    public void onPause() {
        // Save ListView state @ onPause
        Log.d(TAG, "saving listview state @ onPause");
        listViewState = listView.onSaveInstanceState();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // Notify the parent activity of selected item
        ItemListAdapter adapter = (ItemListAdapter) parent.getAdapter();
        RssItem item = (RssItem) adapter.getItem(position);
        mListener.onRssItemSelected(item);

        // Set the item as checked to be highlighted when in two-pane layout
       // getListView().setItemChecked(position, true);
    }
    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(resultReceiver);
        super.onStop();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onRssItemSelected(RssItem selectedItem);
    }
}
