package fuad.yousseftech.surf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import fuad.yousseftech.surf.Firebase.Rank;
import fuad.yousseftech.surf.Firebase.Web;

import static android.content.ContentValues.TAG;


/**
 * Adapts the list of students in the model to be a list of graphical elements in view
 */
public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.LinkViewHolder> {

    /** a listener for a touch event on the student */
    private OnLinkClickListener listener;
    private OnLinkLongClickListener lListener;

    private Map<String, Web> webMap;
    private String key;

    public void setWebMap(Map webMap) {
        this.webMap = webMap;
    }

    public void setWeb(String key) {
        this.key = key;
    }

    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        // hook up to the view for a single student in the system
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.link_item, parent, false);

        return new LinkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder holder, int position) {

        Web web = webMap.get(key);
        List<Rank> ranks = web.getRanks();
        Rank rank = ranks.get(position);
        Web futureWeb = webMap.get(rank.getWeb());

        //Log.d("APP", "Binding: " + position + " " + linkList.get(position));

        //holder.studentMajor.setText("to Replace");
        holder.linkText.setText(futureWeb.getTitle());
        holder.webIcon.setImageResource(R.mipmap.computerscreen);
        try {
            Bitmap bmp = futureWeb.getBitmap();
            holder.webIcon.setImageBitmap(bmp);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        if (webMap == null)
            return 0;
        Web web = webMap.get(key);
        List<Rank> ranks = web.getRanks();
        return ranks.size();
    }

    /**
     *
     * This is a holder for the widgets associated with a single entry in the list of students
     */
    class LinkViewHolder extends RecyclerView.ViewHolder {
        private TextView linkText;
        private ImageView webIcon;



        public LinkViewHolder(@NonNull View itemView) {
            super(itemView);
            linkText = itemView.findViewById(R.id.linkURL);
            webIcon = itemView.findViewById(R.id.websiteIcon);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        Web web = webMap.get(key);
                        List<Rank> ranks = web.getRanks();
                        Rank rank = ranks.get(position);
                        Web futureWeb = webMap.get(rank.getWeb());

                        String link = futureWeb.getLink();
                        listener.onLinkClicked(link);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        view.getContext().startActivity(browserIntent);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();

                    if (lListener != null && position != RecyclerView.NO_POSITION) {
                        lListener.onLinkLongClicked(position);
                        //System.out.println("long clicked position: " + position);
                        return true;
                    }
                    return false;
                }
            });


        }
    }

    public interface OnLinkClickListener {
        void onLinkClicked(String link);
    }
    public interface OnLinkLongClickListener {
        boolean onLinkLongClicked(int position);
    }
    public void setOnLinkClickListener(OnLinkClickListener listener) {
        this.listener = listener;
    }



}
