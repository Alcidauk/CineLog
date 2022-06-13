package com.ulicae.cinelog.data.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.parceler.Parcel;
import org.greenrobot.greendao.annotation.Generated;

@Parcel
@Entity
public class Tag {

    @Id(autoincrement = true)
    Long id;

    @NotNull
    String name;

    @NotNull
    String color;

    boolean forMovies;
    boolean forSeries;


    @Generated(hash = 1404380189)
    public Tag(Long id, @NotNull String name, @NotNull String color,
            boolean forMovies, boolean forSeries) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.forMovies = forMovies;
        this.forSeries = forSeries;
    }

    @Generated(hash = 1605720318)
    public Tag() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean getForMovies() {
        return this.forMovies;
    }

    public void setForMovies(boolean forMovies) {
        this.forMovies = forMovies;
    }

    public boolean getForSeries() {
        return this.forSeries;
    }

    public void setForSeries(boolean forSeries) {
        this.forSeries = forSeries;
    }

}
