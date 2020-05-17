package com.ulicae.cinelog.data.dto;

import org.parceler.Parcel;

import java.util.Date;

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

    Date viewDate;

    String name;
    Date airDate;
    Integer seasonNumber;
    Integer episodeNumber;

    public SerieEpisodeDto() {
    }


    public SerieEpisodeDto(Long episodeId, Integer tmdbEpisodeId, Long tmdbSerieId, Date viewDate,
                           String name, Date airDate, Integer seasonNumber, Integer episodeNumber) {
        this.episodeId = episodeId;
        this.tmdbEpisodeId = tmdbEpisodeId;
        this.tmdbSerieId = tmdbSerieId;
        this.viewDate = viewDate;
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

    public Date getViewDate() {
        return viewDate;
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
}
