package org.example;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FileManager
{
    private static final String FILE_NAME = "transactions.csv";

    // Saves a transaction to the CSV file
    public static void saveTransaction(Transaction transaction)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true)))
        {
            writer.write(transaction.toCSV());
            writer.newLine();
        }
        catch (IOException e)
        {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    // Loads transactions from the CSV file
    public static List<Transaction> loadTransactions()
    {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(FILE_NAME);

        // If file doesn't exist, return empty list
        if (!file.exists())
            return transactions;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME)))
        {
            String line;
            while ((line = reader.readLine()) != null) {
                try
                {
                    Transaction transaction = parseTransaction(line);
                    transactions.add(transaction);
                }
                catch (Exception e)
                {
                    System.out.println("Error parsing line: " + line);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Error reading transactions: " + e.getMessage());
        }

        return transactions;
    }

    /**
     * Parses a CSV line into a Transaction object
     * Format: date|time|description|vendor|amount
     */
    private static Transaction parseTransaction(String line)
    {
        String[] parts = line.split("\\|");
        if (parts.length != 5)
            throw new IllegalArgumentException("Invalid transaction format");

        LocalDate date = parseDate(parts[0]);
        LocalTime time = parseTime(parts[1]);
        String description = parts[2];
        String vendor = parts[3];
        double amount = Double.parseDouble(parts[4]);

        return new Transaction(date, time, description, vendor, amount);
    }

    /**
     * Parses a date string in the format "yyyy-MM-dd"
     * Example: "2023-04-15"
     */
    private static LocalDate parseDate(String dateString)
    {
        String[] parts = dateString.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        return LocalDate.of(year, month, day);
    }

    /**
     * Parses a time string in the format "HH:mm:ss"
     * Example: "10:13:25"
     */
    private static LocalTime parseTime(String timeString)
    {
        String[] parts = timeString.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        int second = Integer.parseInt(parts[2]);
        return LocalTime.of(hour, minute, second);
    }
}