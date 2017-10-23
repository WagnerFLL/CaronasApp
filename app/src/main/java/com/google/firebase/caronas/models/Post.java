package com.google.firebase.caronas.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post {

    public String uid;
    public String author;
    public String source;
    public String destiny;
    public String time;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String author, String source, String destiny, String time) {
        this.uid = uid;
        this.author = author;
        this.source = source;
        this.destiny = destiny;
        this.time = time;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", this.uid);
        result.put("author", this.author);
        result.put("source", this.source);
        result.put("destiny", this.destiny);
        result.put("time", this.time);
        result.put("starCount", this.starCount);
        result.put("stars", this.stars);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
