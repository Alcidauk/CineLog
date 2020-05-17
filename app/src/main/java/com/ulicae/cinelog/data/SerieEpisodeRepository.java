package com.ulicae.cinelog.data;

import com.ulicae.cinelog.data.dao.DaoSession;
import com.ulicae.cinelog.data.dao.SerieEpisode;
import com.ulicae.cinelog.data.dao.SerieEpisodeDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * CineLog Copyright 2018 Pierre Rognon
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
public class SerieEpisodeRepository {

    protected SerieEpisodeDao dao;

    public SerieEpisodeRepository(DaoSession daoSession) {
        this.dao = daoSession.getSerieEpisodeDao();
    }

    public List<SerieEpisode> findByTmdbSerieId(long tmdbSerieId) {
        Query<SerieEpisode> serieEpisodeQuery = dao.queryBuilder()
                .where(SerieEpisodeDao.Properties.Tmdb_id.eq(tmdbSerieId))
                .build();
        return serieEpisodeQuery.list();
    }

    public void createOrReplace(SerieEpisode episode) {
        dao.insertOrReplace(episode);
    }

    public void delete(Long episodeId) {
        dao.deleteByKey(episodeId);
    }

}
