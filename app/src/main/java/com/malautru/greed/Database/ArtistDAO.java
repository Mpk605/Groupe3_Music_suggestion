package com.malautru.greed.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.malautru.greed.Database.Entities.Artist;

import java.util.List;

@Dao
public abstract class ArtistDAO {
    @Query("DELETE FROM artists")
    public abstract void goodbye();

    @Insert
    public abstract void insert(Artist artist);

    @Query("SELECT name FROM artists WHERE liked = 1")
    public abstract List<String> getSavedLikedArtists();

    @Query("SELECT name FROM artists WHERE liked = 0")
    public abstract List<String> getDislikedArtists();

    @Query("SELECT name FROM artists WHERE name = :tagName")
    public abstract String doesArtistExist(String tagName);
}
