package info.mansk.easyfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ahmed on 9/15/2016.
 */
public class ItemListAdapter extends BaseAdapter {
    private ArrayList<RssItem> listItems;
    private LayoutInflater layoutInflater;
    private ImageDownloader ImageDownloaderFragment;
    public ItemListAdapter(Context context, ArrayList<RssItem> listData, ImageDownloader imageDownloader) {
        this.listItems = listData;
        layoutInflater = LayoutInflater.from(context);
        ImageDownloaderFragment =   imageDownloader;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }
    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewItem currViewItem;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_detailed, null);
            currViewItem   =   new ViewItem();
            currViewItem.titleView =   (TextView) convertView.findViewById(R.id.item_Title);
            currViewItem.descriptionView =   (TextView) convertView.findViewById(R.id.item_description);
            currViewItem.dateView =   (TextView) convertView.findViewById(R.id.item_description);
            currViewItem.imageView = (ImageView) convertView.findViewById(R.id.item_icon_image);
            convertView.setTag(currViewItem);
        } else {
            currViewItem = (ViewItem) convertView.getTag();
        }

        RssItem currItem =   listItems.get(position);
        currViewItem.titleView.setText(currItem.getTitle());
        currViewItem.descriptionView.setText("By, " + currItem.getDescription());
        currViewItem.dateView.setText(currItem.getDate());

        if (currViewItem.imageView != null) {
            ImageDownloaderFragment.DownloadAndShowImage(currViewItem.imageView,currItem.getImageURL());
        }
        return convertView;
    }
    class ViewItem{
        TextView titleView;
        TextView descriptionView;
        TextView dateView;
        ImageView imageView;
    }
}
