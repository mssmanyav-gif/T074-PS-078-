package com.prepaid_recharge.service;

import com.prepaid_recharge.model.Subscriber;
import com.prepaid_recharge.repository.SubscriberRepository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Component
public class SubscriberCsvImport implements CommandLineRunner {

    private final SubscriberRepository subscriberRepository;

    public SubscriberCsvImport(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        ClassPathResource resource =
                new ClassPathResource("datasets/subscribers.csv");

        try (Reader reader = new InputStreamReader(
                resource.getInputStream(), StandardCharsets.UTF_8);

             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreSurroundingSpaces(true)   // handles spaces/tabs around quotes
                     .setTrim(true)                      // trims the values themselves
                     .setIgnoreEmptyLines(true)
                     .get()                              // use .build() on commons-csv < 1.13
                     .parse(reader)) {

            int imported = 0;
            int skipped = 0;
            int failed = 0;

            for (CSVRecord record : parser) {
                try {
                    String subscriberId = record.get("msisdn");

                    // Avoid duplicate records
                    if (subscriberRepository.existsById(subscriberId)) {
                        skipped++;
                        continue;
                    }

                    Subscriber subscriber = new Subscriber(
                            subscriberId,
                            record.get("subscriber_name"),
                            record.get("home_circle"),
                            LocalDate.parse(record.get("activation_date")),
                            new BigDecimal(record.get("initial_core_balance"))
                    );

                    subscriber.setCoreBalanceValidUntil(
                            LocalDate.parse(record.get("initial_core_valid_till")));

                    subscriber.setRolloverDataGB(
                            new BigDecimal(record.get("initial_rollover_gb")));

                    subscriber.setStatus(record.get("initial_state"));

                    subscriberRepository.save(subscriber);
                    imported++;

                } catch (Exception e) {
                    failed++;
                    System.err.println("Failed to import row "
                            + record.getRecordNumber() + ": " + record
                            + " -> " + e.getMessage());
                }
            }

            System.out.println("Subscriber CSV import completed!");
            System.out.println("Imported: " + imported);
            System.out.println("Skipped: " + skipped);
            System.out.println("Failed: " + failed);
        }
    }
}