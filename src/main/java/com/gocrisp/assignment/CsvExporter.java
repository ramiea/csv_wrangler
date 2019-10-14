package com.gocrisp.assignment;

import com.opencsv.CSVWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Map;

class CsvExporter implements AutoCloseable {

    private final CSVWriter csvWriter;
    private final String[] headers;
    private int row;

    CsvExporter(Writer writer, String[] headers) {
        this(new CSVWriter(writer), headers);
    }

    // created this to allow mocking in tests
    CsvExporter(CSVWriter csvWriter, String[] headers) {
        this.csvWriter = csvWriter;
        this.headers = headers;
        csvWriter.writeNext(headers);
        this.row = 1;
    }

    void addRow(Map<String, String> csvRow) throws IOException {
        String[] columns = Arrays.stream(headers).map(csvRow::get).toArray(String[]::new);
        csvWriter.writeNext(columns);
        if (row % 10 == 0) {
            csvWriter.flush();
        }
        row++;
    }

    @Override
    public void close() throws Exception {
        csvWriter.close();
    }
}
