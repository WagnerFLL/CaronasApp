package com.google.firebase.caronas;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.caronas.models.User;
import com.google.firebase.caronas.models.Post;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends BaseActivity  {

    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mPostReference;
    private DatabaseReference fbDb;
    private ValueEventListener mPostListener;
    private String mPostKey;

    private TextView mAuthorView;
    private TextView mSourceView;
    private TextView mDestinyView;
    private TextView mTimeView;
    private TextView mRideCountView;
    private Button share;
    private String stringPost;
    public long size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts/all").child(mPostKey);

        share = findViewById(R.id.shareID);
        mAuthorView = findViewById(R.id.post_author);
        mSourceView = findViewById(R.id.post_source);
        mDestinyView = findViewById(R.id.post_destiny);
        mTimeView = findViewById(R.id.post_time);
        mRideCountView = findViewById(R.id.user_ride_count);

        /*
        var ref = firebase.database().ref("users/ada");
        ref.once("value")
          .then(function(snapshot) {
            var key = snapshot.key; // "ada"
            var childKey = snapshot.child("name/last").key; // "last"
          });
         */


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://the-dank-network.herokuapp.com/post?content="+stringPost)));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
/*
        fbDb = FirebaseDatabase.getInstance().getReference();

        fbDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Post post = dataSnapshot.getValue(Post.class);
                    //dataSnapshot.child("user-posts").child("oferta").child(post.uid).getChildrenCount();
                    mAuthorView.setText(post.author);
                    mSourceView.setText(post.source);
                    mDestinyView.setText(post.destiny);
                    mTimeView.setText(post.time);
                    mRideCountView.setText(String.valueOf(size));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }
        );
        */
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Post post = dataSnapshot.getValue(Post.class);
                long size = dataSnapshot.child("user-posts").child(post.uid).child("oferta").getChildrenCount();
                mAuthorView.setText(post.author);
                mSourceView.setText(post.source);
                mDestinyView.setText(post.destiny);
                mTimeView.setText(post.time);
                mRideCountView.setText(String.valueOf(size));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        mPostReference.addValueEventListener(postListener);

        mPostListener = postListener;

    }


    @Override
    public void onStop() {
        super.onStop();

        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }

    }



}
