package com.akvamarin.friendsappserver.utils;

import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.domain.entity.location.Country;
import com.akvamarin.friendsappserver.domain.entity.location.FederalDistrict;
import com.akvamarin.friendsappserver.domain.entity.location.Region;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                City city = new City(new Country("Россия"));
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
            }
        } catch (IOException e) {
            log.error(String.format("ExcelHelper Cities: %s", e.getMessage()));
        }
        return cities;
    }
}