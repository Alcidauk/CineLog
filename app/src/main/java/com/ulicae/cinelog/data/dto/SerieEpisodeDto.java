package com.ulicae.cinelog.data.dto;

import org.parceler.Parcel;

import java.util.Date;
import java.util.Objects;

/**
 * CineLog Copyright 2020 Pierre Rognon
 * <p>
 * <p>
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 */
@Parcel
public class SerieEpisodeDto {

    Long episodeId;

    Integer tmdbEpisodeId;

    Long tmdbSerieId;

    Date watchingDate;

    String name;
    Date airDate;
    Integer seasonNumber;
    Integer episodeNumber;

    public SerieEpisodeDto() {
    }


    public SerieEpisodeDto(Long episodeId, Integer tmdbEpisodeId, Long tmdbSerieId, Date watchingDate,
                           String name, Date airDate, Integer seasonNumber, Integer episodeNumber) {
        this.episodeId = episodeId;
        this.tmdbEpisodeId = tmdbEpisodeId;
        this.tmdbSerieId = tmdbSerieId;
        this.watchingDate = watchingDate;
        this.name = name;
        this.airDate = airDate;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }

    public Long getEpisodeId() {
        return episodeId;
    }

    public Integer getTmdbEpisodeId() {
        return tmdbEpisodeId;
    }

    public Long getTmdbSerieId() {
        return tmdbSerieId;
    }

    public Date getWatchingDate() {
        return watchingDate;
    }

    public String getName() {
        return name;
    }

    public Date getAirDate() {
        return airDate;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAirDate(Date airDate) {
        this.airDate = airDate;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public void setWatchingDate(Date watchingDate) {
        this.watchingDate = watchingDate;
    }

    public void setEpisodeId(Long episodeId) {
        this.episodeId = episodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerieEpisodeDto that = (SerieEpisodeDto) o;
        return Objects.equals(episodeId, that.episodeId) &&
                Objects.equals(tmdbEpisodeId, that.tmdbEpisodeId) &&
                Objects.equals(tmdbSerieId, that.tmdbSerieId) &&
                Objects.equals(watchingDate, that.watchingDate) &&
                Objects.equals(name, that.name) &&
                Objects.equals(airDate, that.airDate) &&
                Objects.equals(seasonNumber, that.seasonNumber) &&
                Objects.equals(episodeNumber, that.episodeNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(episodeId, tmdbEpisodeId, tmdbSerieId, watchingDate, name, airDate, seasonNumber, episodeNumber);
    }
}
