package com.google.firebase.caronas.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MyTopPostsFragment extends PostListFragment {

    public MyTopPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        String myUserId = getUid();
        Query myTopPostsQuery = databaseReference.child("posts").child("pedido")
                .orderByChild("starCount");


        return myTopPostsQuery;
    }
}
