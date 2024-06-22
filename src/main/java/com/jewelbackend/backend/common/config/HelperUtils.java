package com.jewelbackend.backend.common.config;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HelperUtils {

    private HelperUtils() {
    }

    static final Logger logger = LogManager.getLogger();

    public static void logMessage(Level level, String message) {
        logger.log(level, message);
    }

    public static String localDateToString(LocalDate localDate) {
        return String.format("%s/%s/%s", localDate.getDayOfMonth(), localDate.getMonthValue(),
                localDate.getYear());
    }

    public static void saveReportLocally(byte[] workbookBytes, String reportName, String format) {

        // Construct file name
        StringBuilder sb = new StringBuilder(reportName);
        sb.append(" ")

                .append(java.time.LocalDateTime.now()

                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")))

                .append(".").append(format);

        String fileName = sb.toString();

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(workbookBytes);
            System.out.println(String.format("%s created successfully!", fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public static Map<String,String> listToMap(String filter){
        if(filter.isBlank())
            return new HashMap<>();
        String[] strings = filter.split(",");
        Map<String,String> map = new HashMap<>();
        for (String string : strings) {
            String[] filters = string.split("=");
            if(filters[0].contains("Less"))
                filters[0] = filters[0].replace("Less","<");
            else if(filters[0].contains("Greater"))
                filters[0] = filters[0].replace("Greater",">");
            map.put(filters[0], filters[1]);
        }
        return map;
    }

    public static HttpHeaders generateHeadersForReport(String reportName,String format) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (format.equals("pdf")) {
            httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
            String fileName = reportName + ".pdf";
            httpHeaders.setContentDispositionFormData(fileName, fileName);
        } else {
            httpHeaders.setContentType(
                    MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            String fileName = reportName + ".xlsx";
            httpHeaders.setContentDispositionFormData(fileName, fileName);
        }
        httpHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return httpHeaders;
    }
}
