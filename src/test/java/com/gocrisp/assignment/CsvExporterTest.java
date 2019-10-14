package com.gocrisp.assignment;

import com.opencsv.CSVWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CsvExporterTest {

    CsvExporter csvExporter;

    @Mock
    CSVWriter csvWriter;

    @BeforeEach
    void setUp() {
        csvExporter = new CsvExporter(csvWriter, new String[]{"foo", "bar"});
    }


    @Test
    void shouldAddFirstRow() throws IOException {
        Map<String,String> rowData = new HashMap<>();
        rowData.put("foo", "val1");
        rowData.put("bar", "val2");
        try {
            csvExporter.addRow(rowData);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        verifyRowSet();
        verify(csvWriter, never()).flush();
    }

    @Test
    void shouldAddTenthRow() throws NoSuchFieldException, IllegalAccessException, IOException {
        Field rowNumber = csvExporter.getClass().getDeclaredField("row");
        rowNumber.setAccessible(true);
        rowNumber.set(csvExporter, 10);

        Map<String,String> rowData = new HashMap<>();
        rowData.put("foo", "val1");
        rowData.put("bar", "val2");
        try {
            csvExporter.addRow(rowData);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        verifyRowSet();
        verify(csvWriter, times(1)).flush();
    }

    private void verifyRowSet() throws IOException {
        ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);
        verify(csvWriter, times(2)).writeNext(captor.capture());
        List<String[]> rows = captor.getAllValues();
        assertArrayEquals(new String[]{"foo", "bar"}, rows.get(0));
        assertArrayEquals(new String[]{"val1", "val2"}, rows.get(1));
    }
}