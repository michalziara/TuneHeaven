package org.example;

import org.example.service.CsvImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TuneHeavenApp
{
    public static void main( String[] args ) {
        SpringApplication.run(TuneHeavenApp.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(CsvImportService csvImportService) {
        return args -> {
            csvImportService.importCsvFiles();
        };
    }
}
