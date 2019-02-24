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
import java.util.function.Consumer;

public class UrlMap {
    private String link;
    private Map<String, String> map;
    public static Map<String, Bitmap> bitmapMap = new HashMap<>();

    public UrlMap(String link, Map<String, String> map) {
        this.link = link;
        this.map = map;
    }

    public String getLink() {
        return link;
    }

    public Map<String, String> getMap() {
        return map;
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
        String link = map.get("favicon");
        return new DownloadBitmapTask().execute(link).get();
    }

}