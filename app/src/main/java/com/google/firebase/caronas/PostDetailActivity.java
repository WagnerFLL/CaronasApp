package com.google.firebase.caronas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.caronas.models.Post;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostDetailActivity extends BaseActivity  {

    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;
    private String mPostKey;
    private Button acpt;
    private Button chat;
    private TextView mAuthorView;
    private TextView mSourceView;
    private TextView mDestinyView;
    private TextView mTimeView;
    private TextView mRideCountView;
    private Button share;
    private String stringPost;
    private DatabaseReference mDatabase;
    private ArrayList<String> passageiros;
    private int nPassageiros;
    private int maxP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts/all").child(mPostKey);

        acpt =  findViewById(R.id.accept_ride);
        share = findViewById(R.id.shareID);
        chat = findViewById(R.id.chat_button);
        mAuthorView = findViewById(R.id.post_author);
        mSourceView = findViewById(R.id.post_source);
        mDestinyView = findViewById(R.id.post_destiny);
        mTimeView = findViewById(R.id.post_time);
        mRideCountView = findViewById(R.id.user_ride_count);
        passageiros = new ArrayList<>();

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    stringPost = URLEncoder.encode("Olá pessoal, acabei de fazer uma oferta de carona no app. Venham conferir!", "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://the-dank-network.herokuapp.com/post?content="+stringPost)));
            }
        });

        final Map<String,Object> update = new HashMap<>();

        acpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passageiros.contains(getUid()) ){
                    Toast.makeText(PostDetailActivity.this, "Você já está nesta viagem!", Toast.LENGTH_SHORT).show();
                }else if(maxP == -1){
                    Toast.makeText(PostDetailActivity.this, "Essa não é uma oferta.", Toast.LENGTH_SHORT).show();
                }else if ( nPassageiros == maxP) {
                    Toast.makeText(PostDetailActivity.this, "A viagem está lotada!", Toast.LENGTH_SHORT).show();
                }else if(mAuthorView.getText().equals(getUid())){
                    Toast.makeText(PostDetailActivity.this, "Você é o otorista aqui.", Toast.LENGTH_SHORT).show();
                }else{
                        passageiros.add(getUid());
                        update.put("/posts/all/"+mPostKey+"/passageiros",passageiros);
                        update.put("/posts/all/"+mPostKey+"/nPassageiros",nPassageiros+1);
                        mDatabase.updateChildren(update);
                        Toast.makeText(PostDetailActivity.this, "O motorista será notificado.", Toast.LENGTH_SHORT).show();
                    }
                }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

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
                passageiros = post.passageiros;
                nPassageiros = post.nPassageiros;
                maxP = post.maxP;

                final String destinyUid = post.uid;
                final String sourceUid = getUid();

                final String email = getEmail();
                final String username = usernameFromEmail(email);
                final String chatWith = post.author;

                chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ChatActivity.class);

                        intent.putExtra("destiny_uid", destinyUid);
                        intent.putExtra("source_uid", sourceUid);

                        intent.putExtra("user_name", username);
                        intent.putExtra("chat_with", chatWith);

                        startActivity(intent);
                    }
                });
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

    public String getEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }



}
