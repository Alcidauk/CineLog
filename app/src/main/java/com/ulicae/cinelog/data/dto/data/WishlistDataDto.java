package com.ulicae.cinelog.data.dto.data;

import org.parceler.Parcel;

import java.util.Objects;

@Parcel
public class WishlistDataDto {

    private Long id;
    private Integer tmdbId;

    private String title;

    private String posterPath;
    private String overview;
    private int firstYear;
    private String releaseDate;

    private WishlistItemType wishlistItemType;

    /**
     * needed for Parcel
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public WishlistDataDto() {
    }

    public WishlistDataDto(String title, WishlistItemType wishlistItemType) {
        this.title = title;
        this.wishlistItemType = wishlistItemType;
    }

    public WishlistDataDto(Long id, Integer tmdbId, String title, String posterPath, String overview, int firstYear, String releaseDate, WishlistItemType wishlistItemType) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.firstYear = firstYear;
        this.releaseDate = releaseDate;
        this.wishlistItemType = wishlistItemType;
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

    public WishlistItemType getWishlistItemType() {
        return wishlistItemType;
    }

    public void setWishlistItemType(WishlistItemType wishlistItemType) {
        this.wishlistItemType = wishlistItemType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistDataDto that = (WishlistDataDto) o;
        return firstYear == that.firstYear &&
                Objects.equals(id, that.id) &&
                Objects.equals(tmdbId, that.tmdbId) &&
                Objects.equals(title, that.title) &&
                Objects.equals(posterPath, that.posterPath) &&
                Objects.equals(overview, that.overview) &&
                Objects.equals(releaseDate, that.releaseDate) &&
                wishlistItemType == that.wishlistItemType;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, tmdbId, title, posterPath, overview, firstYear, releaseDate, wishlistItemType);
    }
}
