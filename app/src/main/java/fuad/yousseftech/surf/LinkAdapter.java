package fuad.yousseftech.surf;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Adapts the list of students in the model to be a list of graphical elements in view
 */
public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.LinkViewHolder> {

    /** a copy of the list of students in the model */
    private List<String> linkList = new ArrayList<>();

    /** a listener for a touch event on the student */
    private OnLinkClickListener listener;

    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        // hook up to the view for a single student in the system
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.link_item, parent, false);

        return new LinkViewHolder(itemView);
    }

    public void setLinkList(List<String>links) {
        linkList = links;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder holder, int position) {

        //bind the student data for one student
        String link = linkList.get(position);

        Log.d("APP", "Binding: " + position + " " + linkList.get(position));

        holder.studentMajor.setText("to Replace");
        holder.studentName.setText(link);

    }

    @Override
    public int getItemCount() {
        return linkList.size();
    }




    /**
     * This is a holder for the widgets associated with a single entry in the list of students
     */
    class LinkViewHolder extends RecyclerView.ViewHolder {
        private TextView studentName;
        private TextView studentMajor;



        public LinkViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.text_student_name);
            studentMajor = itemView.findViewById(R.id.text_student_major);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onLinkClicked(linkList.get(position));
                    }
                }
            });

        }
    }

    public interface OnLinkClickListener {
        void onLinkClicked(String link);
    }

    public void setOnLinkClickListener(OnLinkClickListener listener) {
        this.listener = listener;
    }
}
