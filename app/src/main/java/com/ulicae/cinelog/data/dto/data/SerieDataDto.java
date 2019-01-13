package com.ulicae.cinelog.data.dto.data;

import org.parceler.Parcel;

import java.util.Objects;

@Parcel
public class SerieDataDto {

    private Long id;
    private Integer tmdbId;

    private String title;

    private String posterPath;
    private String overview;
    private int firstYear;
    private String releaseDate;

    /**
     * needed for Parcel
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public SerieDataDto() {
    }

    public SerieDataDto(String title) {
        this.title = title;
    }

    public SerieDataDto(Long id, Integer tmdbId, String title, String posterPath, String overview, int firstYear, String releaseDate) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.firstYear = firstYear;
        this.releaseDate = releaseDate;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getFirstYear() {
        return firstYear;
    }

    public void setFirstYear(int firstYear) {
        this.firstYear = firstYear;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerieDataDto that = (SerieDataDto) o;
        return firstYear == that.firstYear &&
                Objects.equals(id, that.id) &&
                Objects.equals(tmdbId, that.tmdbId) &&
                Objects.equals(title, that.title) &&
                Objects.equals(posterPath, that.posterPath) &&
                Objects.equals(overview, that.overview) &&
                Objects.equals(releaseDate, that.releaseDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, tmdbId, title, posterPath, overview, firstYear, releaseDate);
    }

    @Override
    public String toString() {
        return "SerieDataDto{" +
                "id=" + id +
                ", tmdbId=" + tmdbId +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", overview='" + overview + '\'' +
                ", firstYear=" + firstYear +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
