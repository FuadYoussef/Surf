package fuad.yousseftech.surf;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UrlMap {
    private Map<String, String> map;
    public static Map<String, Bitmap> bitmapMap = new HashMap<>();

    public UrlMap(Map<String, String> map) {
        this.map = map;
    }

    public String getLink() {
        return map.get("url");
    }

    public String getTitle() {
        return map.get("title");
    }

    public String getDescription() {
        return map.get("description");
    }

    public String getfavicon() {
        String link = map.get("favicon");
        return link.replaceFirst("http://", "https://");
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
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