package com.google.firebase.caronas.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.caronas.R;
import com.google.firebase.caronas.models.Post;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView sourceView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView destinyView;
    public TextView timeView;

    public PostViewHolder(View itemView) {
        super(itemView);

        sourceView = itemView.findViewById(R.id.post_source);
        authorView = itemView.findViewById(R.id.post_author);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.post_num_stars);
        destinyView = itemView.findViewById(R.id.post_destiny);
        timeView = itemView.findViewById(R.id.post_time);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener) {
        sourceView.setText(post.source);
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        destinyView.setText(post.destiny);
        timeView.setText(post.time);

        starView.setOnClickListener(starClickListener);
    }
}
