package com.akvamarin.friendsappserver.utils;

import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.domain.entity.location.Country;
import com.akvamarin.friendsappserver.domain.entity.location.FederalDistrict;
import com.akvamarin.friendsappserver.domain.entity.location.Region;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Slf4j
public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), TYPE);
    }

    public static List<City> getCityDataFromExcel(InputStream inputStream) {
        List<City> cities = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheet("city");
            int rowIndex = 0;

            for (Row row : sheet) {
                if (rowIndex == 0) {      // skip header
                    rowIndex++;
                    continue;
                }

                String cityName = row.getCell(0).getStringCellValue();
                double lat = row.getCell(1).getNumericCellValue();
                double lon = row.getCell(2).getNumericCellValue();
                String regionName = row.getCell(3).getStringCellValue();
                String districtName = row.getCell(4).getStringCellValue();
                String countryName = row.getCell(5).getStringCellValue();

                City city = City.builder()
                        .name(cityName)
                        .lat(lat)
                        .lon(lon)
                        .build();
                Region region = new Region(regionName);
                FederalDistrict district = new FederalDistrict(districtName);
                Country country = new Country(countryName);

                region.setFederalDistrict(district);
                district.addRegionToFederalDistrict(region);
                country.addCityToCountry(city);
                region.addCityToRegion(city);

                cities.add(city);
         /*       Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                Country country = new Country("Россия");
                City city = new City();
                Region region = new Region();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    switch (cellIndex) {
                        case 0 -> city.setName(cell.getStringCellValue());
                        case 1 -> city.setLat(cell.getNumericCellValue());
                        case 2 -> city.setLon(cell.getNumericCellValue());
                        case 3 -> region.setName(cell.getStringCellValue());
                        case 4 -> {
                            region.setFederalDistrict(new FederalDistrict(cell.getStringCellValue()));
                            city.setRegion(region);
                        }
                        default -> {
                        }
                    }
                    cellIndex++;
                }
                cities.add(city);
         */   }
        } catch (IOException e) {
            log.error(String.format("ExcelHelper Cities: %s", e.getMessage()));
        }
        return cities;
    }
}