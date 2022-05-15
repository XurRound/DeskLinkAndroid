package me.xurround.desklink.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.xurround.desklink.R;
import me.xurround.desklink.models.SlideComment;

public class SlideCommentListAdapter extends RecyclerView.Adapter<SlideCommentListAdapter.ViewHolder>
{
    private final List<SlideComment> comments;

    public SlideCommentListAdapter(List<SlideComment> comments)
    {
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_comment_entry, parent, false);
        return new SlideCommentListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        SlideComment comment = comments.get(position);
        holder.authorTV.setText(comment.getAuthor());
        holder.messageTV.setText(comment.getMessage());
    }

    @Override
    public int getItemCount()
    {
        return comments.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView authorTV;
        private final TextView messageTV;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            authorTV = itemView.findViewById(R.id.cmt_author);
            messageTV = itemView.findViewById(R.id.cmt_message);
        }
    }
}
