package com.gocrisp.assignment;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

class CsvImporter implements Iterable<Map<String,String>> {

    private final CSVReader csvReader;

    CsvImporter(Reader reader) {
        this.csvReader = new CSVReader(reader);
    }

    @Override
    public Iterator<Map<String,String>> iterator() {
        try {
            return new CsvIterator(csvReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}