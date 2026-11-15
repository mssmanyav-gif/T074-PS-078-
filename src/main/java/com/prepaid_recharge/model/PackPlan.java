package com.prepaid_recharge.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "pack_plans")
public class PackPlan {

    @Id
    private String packCode;
    private String packType;        // CORE, UNLIMITED, DATA_ADDON, SMS_ADDON, ROAMING_ADDON
    private BigDecimal price;
    private Integer validityDays;
    private BigDecimal dailyDataGb;
    private BigDecimal dataGb;
    private Integer smsCount;
    private Integer smsPerDay;
    private String description;

    public PackPlan() {}

    public String getPackCode() { return packCode; }
    public void setPackCode(String packCode) { this.packCode = packCode; }

    public String getPackType() { return packType; }
    public void setPackType(String packType) { this.packType = packType; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getValidityDays() { return validityDays; }
    public void setValidityDays(Integer validityDays) { this.validityDays = validityDays; }

    public BigDecimal getDailyDataGb() { return dailyDataGb; }
    public void setDailyDataGb(BigDecimal dailyDataGb) { this.dailyDataGb = dailyDataGb; }

    public BigDecimal getDataGb() { return dataGb; }
    public void setDataGb(BigDecimal dataGb) { this.dataGb = dataGb; }

    public Integer getSmsCount() { return smsCount; }
    public void setSmsCount(Integer smsCount) { this.smsCount = smsCount; }

    public Integer getSmsPerDay() { return smsPerDay; }
    public void setSmsPerDay(Integer smsPerDay) { this.smsPerDay = smsPerDay; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
