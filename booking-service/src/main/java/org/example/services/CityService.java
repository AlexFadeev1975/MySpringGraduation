package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.example.data.model.City;
import org.example.data.statics.StaticData;
import org.example.repository.CityRepository;
import org.example.search.filters.CityFilterBuilder;
import org.example.search.utils.Filter;
import org.example.search.utils.SpecificationBuilder;
import org.example.services.utils.CsvParser;
import org.example.services.utils.FileLoaderFromURI;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityService {


    private final FileLoaderFromURI loader;
    private final CsvParser csvParser;
    private final CityRepository cityRepository;
    private final CityFilterBuilder cityFilterBuilder;
    private final SpecificationBuilder specificationBuilder;
    private Boolean isModified = false;
    private City city;

     @Scheduled(cron = "@monthly")
   // @Scheduled(fixedDelay = 70000)
    private void checkVersionData() throws IOException {
        /** Список городов РФ хранится в БД и заполняется из официальных данных Росстата (загрузка справочника ОКАТО)
         Список регионов неизменный хранится в папке districts **/

        List<City> listCity = cityRepository.findByCityName("г Моcква");
        City savedCity = new City();
        if (listCity.isEmpty()) {
            City newCity = new City();
            newCity.setCityName("г Москва");
            savedCity = cityRepository.save(newCity);
        } else {
            savedCity = listCity.get(0);
        }

        city = savedCity;

        // Парсим данные с сайта Росстата о наличии обновлений списка ОКАТО //
        List<CSVRecord> recordList = csvParser.readFileFromUrl(StaticData.METADATA_URL);

        recordList.forEach(strings -> {
            Map<String, String> str = strings.toMap();

            if (str.get("property").equals("modified") &
                    !str.get("value").equals(city.getModifiedData())) {
                city.setModifiedData(str.get("value"));
                isModified = true;
                log.info("Список городов не изменился");
            }
            if (str.get("property").contains("data") & !isModified) {
                try {
                    log.info("Загрузка файла .csv");
                    String path = loader.loadFile(str.get("property"), str.get("value")); // скачивание нового списка ОКАТО .csv
                    city.setDataFileName(str.get("property"));
                    isModified = false;
                    cityRepository.save(city); // сохраняем данные обновления
                    log.info("Обновляем список городов");
                    updateCities(path);            // обновляем города

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void updateCities(String path) throws IOException {

        City checkedCity = cityRepository.findByCityName("г Москва").get(0);
        log.info("Парсим файл с новым списком городов");
        List<String> stringCities = csvParser.getCities(path);
        log.info("Получаем список регионов");
        Map<String, String> regions = getDistricts(); // получаем мапу код региона - название региона
        log.info("Добавляем к городу его регион");
        List<City> cityList = stringCities.stream().map(c -> {
            String[] arrString = c.split("-");
            City city = new City();
            city.setCityName(arrString[1].replaceAll("\"", ""));

            String district = arrString[0].replaceAll("\"", "");
            String dis = Integer.valueOf(district).toString();
            city.setDistrict(regions.get(dis));                   // подставляем в City название региона вместо кода
            city.setModifiedData(checkedCity.getModifiedData());
            city.setDataFileName(checkedCity.getDataFileName());
            return city;
        }).toList();
        log.info("Удаляем устаревший список городов");
        cityRepository.deleteAll();
        log.info("Сохраняем новый список городов");
        cityRepository.saveAll(cityList);  // сохраняем новый список прежде удалив старый
        log.info("База данных городов обновлена");
        Files.delete(Path.of(path));
        log.info("Затерли временный файл с данными");
    }

    private Map<String, String> getDistricts() throws IOException {

        log.info("Чтение списка регионов");
        List<CSVRecord> recordList = csvParser.readFileFromUrl(Objects.requireNonNull(getClass().getResource(StaticData.DISTRICTS)).toString()); // парсим список регионов из папки districts .csv

        Map<String, String> regions = new HashMap<>();

        recordList.forEach(strings -> {
            Map<String, String> str = strings.toMap();
            String code = str.get("region_code");
            String region = str.get("name_with_type");

            regions.put(code, region);

        });
        return regions;
    }

    public City searchCity(String city, String district) {

        if (city == null || district == null) {
            return null;
        }

        List<Filter> filters = cityFilterBuilder.createFilter(city, district);
        Specification<City> specification = (Specification<City>) specificationBuilder.getSpecificationFromFilters(filters);
        List<City> cities = cityRepository.findAll(specification);

        return cities.stream().findFirst().orElseThrow(() -> new NoSuchElementException("Такой город не найден"));
    }
}



