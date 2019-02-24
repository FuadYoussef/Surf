package fuad.yousseftech.surf.Firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Web {
    private Map<String, String> info;
    private List<Rank> ranks;
    private static Map<String, Bitmap> bitmapMap = new HashMap<>();

    public Web(Object data) {
        info = new HashMap<>();
        ranks = new ArrayList<>();

        HashMap<String, Object> details = (HashMap) data;
        for(String key: details.keySet()) {
            Object value = details.get(key);
            if (value instanceof String) {
                info.put(key, (String) value);
            } else if (value instanceof List) {
                List<Object> list = (List) value;
                for(Object rank: list) {
                    ranks.add(new Rank(rank));
                }
            } else {
                Log.e("db", value.toString());
            }
        }
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public String getLink() {
        return info.get("url");
    }

    public String getTitle() {
        return info.get("title");
    }

    public String getDescription() {
        return info.get("description");
    }

    public String getfavicon() {
        String link = info.get("favicon");
        return link.replaceFirst("http://", "https://");
    }

    private static class DownloadBitmapTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            for(String link: strings) {

                if(bitmapMap.containsKey(link)) {
                    return bitmapMap.get(link);
                }

                try {
                    URL url = new URL(link);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setInstanceFollowRedirects(true);
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    bitmapMap.put(link, bitmap);
                    return bitmap;
                    //func.accept(bitmap);
                } catch (IOException e) {
                    // Log exception
                    return null;
                }
            }
            return null;

        }

    }

    public Bitmap getBitmap() throws ExecutionException, InterruptedException {
        String link = getfavicon();
        return new DownloadBitmapTask().execute(link).get();
    }
}
