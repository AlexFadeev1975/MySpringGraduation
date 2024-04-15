package org.example.services.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Component
public class CsvParser {

    public List<CSVRecord> readFileFromUrl(String link) {

        List<CSVRecord> list = new ArrayList<>();

        try {
            URL url = new URL(link);

            CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();

            CSVParser parser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat);

            list = parser.getRecords();

            parser.close();

        } catch (IOException e) {

            System.out.println("Файл " + e.getMessage() + " недоступен");
        }

        return list;

    }

    public List<String> getCities(String path) throws IOException {

        File file = new File(String.valueOf(Paths.get(path)));
        FileReader rdr = new FileReader(file, Charset.forName("windows-1251"));

        BufferedReader reader = new BufferedReader(rdr);
        List<String> cities = new ArrayList<>();
        String pattern = "\"[а-я](?U)\\s[А-ЯЁа-яё]+\"";

        String line = reader.readLine();
        while (line != null) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            String[] arrData = line.split(";");
            if (arrData[6].matches(pattern)) {
                cities.add(arrData[0] + "-" + arrData[6]);
            }
        }

        return cities;
    }


}

