package com.prepaid_recharge.service;

import com.prepaid_recharge.model.PackPlan;
import com.prepaid_recharge.repository.PackPlanRepository;

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

@Component
public class PackCsvImport implements CommandLineRunner {

    private final PackPlanRepository planRepository;

    public PackCsvImport(PackPlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        ClassPathResource resource =
                new ClassPathResource("datasets/packs.csv");

        try (Reader reader = new InputStreamReader(
                resource.getInputStream(), StandardCharsets.UTF_8);

             CSVParser parser = CSVFormat.DEFAULT.builder()                 
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreSurroundingSpaces(true)
                     .setTrim(true)
                     .setIgnoreEmptyLines(true)
                     .get()                               
                     .parse(reader)) {

            int imported = 0;
            int skipped = 0;
            int failed = 0;

            for (CSVRecord record : parser) {
                try {
                    String packCode = record.get("pack_code");

                    // Avoid duplicate records
                    if (planRepository.existsById(packCode)) {
                        skipped++;
                        continue;
                    }

                    PackPlan plan = new PackPlan();
                    plan.setPackCode(packCode);
                    plan.setPackType(record.get("pack_type"));
                    plan.setPrice(toDecimal(record.get("price")));
                    plan.setValidityDays(toInt(record.get("validity_days")));
                    plan.setDailyDataGb(toDecimal(record.get("daily_data_gb")));
                    plan.setDataGb(toDecimal(record.get("data_gb")));
                    plan.setSmsCount(toInt(record.get("sms_count")));
                    plan.setSmsPerDay(toInt(record.get("sms_per_day")));
                    plan.setDescription(record.get("description"));

                    planRepository.save(plan);
                    imported++;

                } catch (Exception e) {
                    failed++;
                    System.err.println("Failed to import pack row "
                            + record.getRecordNumber() + ": " + record
                            + " -> " + e.getMessage());
                }
            }

            System.out.println("Pack CSV import completed!");
            System.out.println("Imported: " + imported);
            System.out.println("Skipped: " + skipped);
            System.out.println("Failed: " + failed);
        }
    }

    // Empty cells (e.g. data_gb on CORE packs) become null instead of crashing
    private BigDecimal toDecimal(String value) {
        return (value == null || value.isBlank()) ? null : new BigDecimal(value);
    }

    private Integer toInt(String value) {
        return (value == null || value.isBlank()) ? null : Integer.valueOf(value);
    }
}
