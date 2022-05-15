package me.xurround.desklink.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.List;

import me.xurround.desklink.R;
import me.xurround.desklink.models.Slide;

public class SlideListAdapter extends RecyclerView.Adapter<SlideListAdapter.ViewHolder>
{
    private final List<Slide> slides;

    public SlideListAdapter(List<Slide> slides)
    {
        this.slides = slides;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_content_layout, parent, false);
        return new SlideListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Slide slide = slides.get(position);
        holder.slideImage.setImageBitmap(slide.getImage());
        holder.slideIdTV.setText(String.valueOf(slide.getId()));
        holder.slideHeaderTV.setText(slide.getHeader());
        RecyclerView slideCommentsList = holder.itemView.findViewById(R.id.slide_comments_list);
        slideCommentsList.setLayoutManager(new GridLayoutManager(slideCommentsList.getContext(), 4, RecyclerView.HORIZONTAL, false));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(slideCommentsList);
        slideCommentsList.setAdapter(new SlideCommentListAdapter(slide.getComments()));
    }

    @Override
    public int getItemCount()
    {
        return slides.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView slideImage;
        private final TextView slideIdTV;
        private final TextView slideHeaderTV;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            slideImage = itemView.findViewById(R.id.slide_img_header);
            slideIdTV = itemView.findViewById(R.id.slide_id_tv);
            slideHeaderTV = itemView.findViewById(R.id.slide_header_tv);
        }
    }
}
