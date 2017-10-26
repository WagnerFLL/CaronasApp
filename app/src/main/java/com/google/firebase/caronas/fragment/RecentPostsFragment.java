package com.google.firebase.caronas.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class RecentPostsFragment extends PostListFragment {

    public RecentPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        Query recentPostsQuery = databaseReference.child("user-posts/" + getUid() + "/all")
                .limitToFirst(100);
        return recentPostsQuery;
    }
}
