package com.gocrisp.assignment;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Performs transformations a row-by-row transformations on a given csv, defined by a config, and exports them to a given output file.
 */
public class CsvTransformer {

    public static void main(String[] args) throws IOException {
        CsvTransformer csvTransformer = new CsvTransformer();
        File config = new File(args[0]);
        File input = new File(args[1]);
        File output = new File(args[2]);
        Reader reader = Files.newBufferedReader(getPath(input));
        Writer writer = Files.newBufferedWriter(getPath(output));

        csvTransformer.transformCsv(config, reader, writer);
    }

    /**
     * Transform the CSV with series of transformations defined in transformer.
     * @param config yaml config file that contains transformations
     * @param reader reader for the source csv file
     * @param writer writer for the destination csv file
     * @throws IOException unable to parse config file
     */
    @SuppressWarnings("WeakerAccess")
    public void transformCsv(File config, Reader reader, Writer writer) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        RowTransformer transformer;
        try {
            transformer = mapper.readValue(config, RowTransformer.class);
        } catch (JsonMappingException e) {
            System.out.println(
                "Error Reading config (" + e.getLocation().getLineNr() +
                    ", " + e.getLocation().getColumnNr() + "): " +
                    e.getOriginalMessage());
            return;
        }
        transformCsv(transformer, reader, writer);
    }


    /**
     * Transform the CSV with series of transformations defined in transformer.
     * Use this if you wish to have more control than can be given from config file
     * (such as custom commands or dynamically defined transformations).
     * @param transformer DSL Config containing transformations to perform
     * @param reader reader for the source csv file
     * @param writer writer for the destination csv file
     */
    @SuppressWarnings("WeakerAccess")
    public void transformCsv(RowTransformer transformer, Reader reader, Writer writer) {
        CsvImporter importer = new CsvImporter(reader);
        String[] outputHeaders = transformer.getOutputHeaders();
        try (CsvExporter exporter = new CsvExporter(writer, outputHeaders)) {
            int rowNumber = 1;
            for (Map<String, String> inputRow : importer) {
                Map<String, String> outputRow = transformer.transformRow(rowNumber, inputRow);
                exporter.addRow(outputRow);
                rowNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Path getPath(File file) {
        return Paths.get(file.toURI());
    }
}
