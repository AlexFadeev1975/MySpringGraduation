package org.example.services.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

@Component
@Slf4j
public class FileLoaderFromURI {


    public String loadFile(String fileName, String url) throws IOException {

        String path = new File("").getAbsolutePath() + "/temp";
        log.info("папка " + path);


        URL link = new URL(url);
        File file = new File(String.valueOf(Paths.get(path + fileName)));
        log.info("копирование файла данных");
        org.apache.commons.io.FileUtils.copyURLToFile(link, file);

        return path + fileName;
    }

}
