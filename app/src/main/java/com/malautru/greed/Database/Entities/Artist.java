package com.malautru.greed.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "artists")
public class Artist {
    @NonNull
    @PrimaryKey
    public String name;

    @NonNull
    public boolean liked;

    public Artist(@NonNull String name, @NonNull boolean liked) {
        this.name = name;
        this.liked = liked;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public boolean isLiked() {
        return liked;
    }

    public void setLiked(@NonNull boolean liked) {
        this.liked = liked;
    }
}
