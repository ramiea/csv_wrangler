package com.gocrisp.assignment;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

class CsvIterator implements Iterator<Map<String, String>> {

    private final CSVReader csvReader;
    private final String[] headers;

    private Map<String, String> nextRow;

    public CsvIterator(CSVReader csvReader) throws IOException {
        this.csvReader = csvReader;
        this.headers = csvReader.readNext();
        this.nextRow = transformRow(csvReader.readNext());
    }


    @Override
    public boolean hasNext() {
        return nextRow != null;
    }

    @Override
    public Map<String,String> next() {
        Map<String,String> tmp = this.nextRow;
        try {
            this.nextRow = transformRow(csvReader.readNext());
            return tmp;
        } catch (IOException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private Map<String,String> transformRow(String[] row) {
        if (row == null) {
            return  null;
        }
        Map<String,String> rowData = new HashMap<>();
        for (int i = 0; i < row.length; i++) {
            rowData.put(headers[i], row[i]);
        }

        return Collections.unmodifiableMap(rowData);
    }

}
