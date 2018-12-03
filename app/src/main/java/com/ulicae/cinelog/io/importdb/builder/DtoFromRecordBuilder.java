package com.ulicae.cinelog.io.importdb.builder;

import com.ulicae.cinelog.io.importdb.ImportException;

import org.apache.commons.csv.CSVRecord;

public interface DtoFromRecordBuilder<Dto> {

    Dto build(CSVRecord csvRecord) throws ImportException;
}
