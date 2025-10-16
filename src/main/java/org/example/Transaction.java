package org.example;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction
{
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount)
    {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    // Converts to CSV format
    public String toCSV() {
        return String.format("%s|%s|%s|%s|%.2f",
                formatDate(date),
                formatTime(time),
                description,
                vendor,
                amount);
    }

    // Converts to string format
    @Override
    public String toString() {
        return String.format("%s %s | %-30s | %-20s | $%,.2f",
                formatDate(date),
                formatTime(time),
                description,
                vendor,
                amount);
    }

    // Formats a LocalDate to "yyyy-MM-dd" format
    private String formatDate(LocalDate date) {
        return String.format("%04d-%02d-%02d",
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth());
    }

    // Formats a LocalTime to "HH:mm:ss" format
    private String formatTime(LocalTime time) {
        return String.format("%02d:%02d:%02d",
                time.getHour(),
                time.getMinute(),
                time.getSecond());
    }

    // Checks if this is a deposit (positive amount)
    public boolean isDeposit() {
        return amount > 0;
    }

    // Checks if this is a payment (negative amount)
    public boolean isPayment() {
        return amount < 0;
    }
}