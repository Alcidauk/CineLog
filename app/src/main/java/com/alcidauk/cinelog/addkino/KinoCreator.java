package com.alcidauk.cinelog.addkino;

import com.alcidauk.cinelog.dao.LocalKino;
import com.alcidauk.cinelog.db.LocalKinoRepository;

public class KinoCreator {

    private LocalKinoRepository localKinoRepository;

    public KinoCreator(LocalKinoRepository localKinoRepository) {
        this.localKinoRepository = localKinoRepository;
    }

    public LocalKino create(String name) {
        LocalKino localKino = new LocalKino(
                name,
                null,
                null,
                null,
                0,
                0
        );

        localKinoRepository.create(localKino);

        return localKino;
    }
}
