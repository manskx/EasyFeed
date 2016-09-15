package info.mansk.easyfeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NasaNewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NasaNewsFragment extends Fragment {

    private static ImageDownloader ImageDownloaderFragment;
    private  ListView listView;
    private Parcelable listViewState;

    private static final String TAG = NasaNewsFragment.class.getSimpleName();



    private OnFragmentInteractionListener mListener;

    public NasaNewsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setRetainInstance(true);
        IntentFilter intentFilter = new IntentFilter(RssService.ACTION_RSS_PARSED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(resultReceiver, intentFilter);
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
        // Save ListView state @ onPause
        Log.d(TAG, "saving listview state @ onPause");
        listViewState = listView.onSaveInstanceState();
        savedInstanceState.putParcelable("listViewState", listViewState);
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

                listView.setAdapter(new ItemListAdapter(getActivity(), items, ImageDownloaderFragment));
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

    public void setImageDownloaderFragment( ImageDownloader imageDownloaderFragment){
        ImageDownloaderFragment =   imageDownloaderFragment;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(resultReceiver);
        super.onStop();


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
        void onFragmentInteraction(Uri uri);
    }
}
