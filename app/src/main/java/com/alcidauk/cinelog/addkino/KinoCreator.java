package com.alcidauk.cinelog.addkino;

import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.db.LocalKinoRepository;

public class KinoCreator {

    private LocalKinoRepository localKinoRepository;

    public KinoCreator(LocalKinoRepository localKinoRepository) {
        this.localKinoRepository = localKinoRepository;
    }

    public LocalKino create(String name) {
        LocalKino localKino = new LocalKino(name);

        localKinoRepository.createOrUpdate(localKino);

        return localKino;
    }
}
