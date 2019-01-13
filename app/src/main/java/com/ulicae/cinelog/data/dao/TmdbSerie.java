package com.ulicae.cinelog.data.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.parceler.Parcel;

import java.util.Objects;

/**
 * CineLog Copyright 2018 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
@Parcel
@Entity
public class TmdbSerie {

    @Id
    Long serie_id;

    String poster_path;
    String overview;
    int year;
    String release_date;


    @Generated(hash = 962756978)
    public TmdbSerie() {
    }

    @Generated(hash = 118982129)
    public TmdbSerie(Long serie_id, String poster_path, String overview, int year,
            String release_date) {
        this.serie_id = serie_id;
        this.poster_path = poster_path;
        this.overview = overview;
        this.year = year;
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Long getSerie_id() {
        return this.serie_id;
    }

    public void setSerie_id(Long serie_id) {
        this.serie_id = serie_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TmdbSerie tmdbSerie = (TmdbSerie) o;
        return year == tmdbSerie.year &&
                Objects.equals(serie_id, tmdbSerie.serie_id) &&
                Objects.equals(poster_path, tmdbSerie.poster_path) &&
                Objects.equals(overview, tmdbSerie.overview) &&
                Objects.equals(release_date, tmdbSerie.release_date);
    }

    @Override
    public int hashCode() {

        return Objects.hash(serie_id, poster_path, overview, year, release_date);
    }
}
