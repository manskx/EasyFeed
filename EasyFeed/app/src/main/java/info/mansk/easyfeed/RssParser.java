package info.mansk.easyfeed;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Ahmed on 9/15/2016.
 */
public interface RssParser {
    List<RssItem> parse(InputStream inputStream);

}
