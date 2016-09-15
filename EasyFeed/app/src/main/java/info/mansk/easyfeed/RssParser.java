package info.mansk.easyfeed;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 9/15/2016.
 */
public class RssParser {
//http://www.nasa.gov/rss/dyn/educationnews.rss
    private static final String TAG_TITLE = "title";
    private static final String TAG_LINK = "link";
    private static final String TAG_RSS = "rss";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_IMAGE = "enclosure";
    private static final String TAG_DATE = "pubDate";

    private final String NAME_SPACE = null;

    public List<RssItem> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            inputStream.close();
        }
    }

    private List<RssItem> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, TAG_RSS);

        // parser.require(XmlPullParser.START_TAG, null, "channel");
        List<RssItem> items = new ArrayList<RssItem>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                items.add(readEntry(parser));
            }
            else if(name.equals("channel")){
                continue;
            } else {
                skip(parser);
            }
        }
        return items;
    }


    private RssItem readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, "item");
        String title = null;
        String link = null;
        String description = null;
        String image = null;
        String date = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(TAG_TITLE)) {
                title = readTitle(parser);
            } else if (name.equals(TAG_LINK)) {
                link = readLink(parser);
            }
            else if (name.equals(TAG_DESCRIPTION)) {
                description = readDescription(parser);
            }
            else if (name.equals(TAG_IMAGE)) {
                image = readImage(parser);
            }
            else if (name.equals(TAG_DATE)) {
                date = readDate(parser);
            } else {
                skip(parser);
            }
        }
        return new RssItem(image, title, link, description, date);

    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    private String readLink(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, TAG_LINK);
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, TAG_LINK);
        return link;
    }

    private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, TAG_TITLE);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, TAG_TITLE);
        return title;
    }

    private String readDescription(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, TAG_DESCRIPTION);
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, TAG_DESCRIPTION);
        return description;
    }

    private String readImage(XmlPullParser parser) throws XmlPullParserException, IOException {
        String imageLink = "";
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, TAG_IMAGE);
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "type");
        if (tag.equals(TAG_IMAGE)) {
            if (relType.contains("image")){
                imageLink = parser.getAttributeValue(null, "url");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, TAG_IMAGE);
        return imageLink;
    }

    private String readDate(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAME_SPACE, TAG_DATE);
        String date = readText(parser);
        parser.require(XmlPullParser.END_TAG, NAME_SPACE, TAG_DATE);
        return date;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

}
