package org.example.config;

import org.example.service.CsvImportService;
import org.example.service.CsvReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScheduledTasks {

    @Autowired
    private CsvReportService csvReportService;

    @Autowired
    private CsvImportService csvImportService;

    @Scheduled(cron = "0 15 23 L * ?")
    public void generateMonthlyReports() {
        try {
            csvReportService.generateMonthlyReports();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 23 * * *")
    public void importDailyRatings() {
        try {
            csvImportService.importCsvFiles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
