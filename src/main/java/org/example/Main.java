package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main
{
    private static Scanner scanner = new Scanner(System.in);
    private static Ledger ledger = new Ledger(scanner);

    public static void main(String[] args)
    {
        boolean running = true;

        while (running)
        {
            displayHomeScreen();
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice)
            {
                case "D":
                    addDeposit();
                    break;
                case "P":
                    makePayment();
                    break;
                case "L":
                    ledger.display();
                    break;
                case "X":
                    System.out.println("\nThank you for using our Ledger Application. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    // Displays the home screen
    private static void displayHomeScreen()
    {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║  HOME SCREEN                       ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║  D) Add Deposit                    ║");
        System.out.println("║  P) Make Payment (Debit)           ║");
        System.out.println("║  L) Ledger                         ║");
        System.out.println("║  X) Exit                           ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.print("► Please select an option: ");
    }

    // Adds a new deposit to the ledger from user input
    private static void addDeposit()
    {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║  ADD DEPOSIT                       ║");
        System.out.println("╚════════════════════════════════════╝");

        try
        {
            LocalDate date = promptForDate();
            LocalTime time = promptForTime();
            String description = promptForDescription();
            String vendor = promptForVendor();
            double amount = promptForAmount();

            Transaction transaction = new Transaction(date, time, description, vendor, Math.abs(amount));
            FileManager.saveTransaction(transaction);

            System.out.println("✓ Deposit added successfully!");
        }
        catch (Exception e)
        {
            System.out.println("✗ Error adding deposit: " + e.getMessage());
        }
    }

    // Makes a new payment to the ledger from user input
    private static void makePayment()
    {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║  MAKE PAYMENT                      ║");
        System.out.println("╚════════════════════════════════════╝");

        try
        {
            LocalDate date = promptForDate();
            LocalTime time = promptForTime();
            String description = promptForDescription();
            String vendor = promptForVendor();
            double amount = promptForAmount();

            Transaction transaction = new Transaction(date, time, description, vendor, -Math.abs(amount));
            FileManager.saveTransaction(transaction);

            System.out.println("✓ Payment recorded successfully!");
        }
        catch (Exception e)
        {
            System.out.println("✗ Error recording payment: " + e.getMessage());
        }
    }

    // Prompt user for a date
    private static LocalDate promptForDate()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true)
        {
            System.out.print("Enter date (yyyy-MM-dd) or press Enter for today: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty())
                return LocalDate.now();

            try
            {
                return LocalDate.parse(input, formatter);
            }
            catch (DateTimeParseException e)
            {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }

    // Prompt user for a time
    private static LocalTime promptForTime()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        while (true)
        {
            System.out.print("Enter time (HH:mm:ss) or press Enter for current time: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty())
                return LocalTime.now();

            try
            {
                return LocalTime.parse(input, formatter);
            }
            catch (DateTimeParseException e)
            {
                System.out.println("Invalid time format. Please use HH:mm:ss.");
            }
        }
    }

    // Prompt user for a description
    private static String promptForDescription()
    {
        while (true)
        {
            System.out.print("Enter description: ");
            String description = scanner.nextLine().trim();

            if (!description.isEmpty())
                return description;

            System.out.println("Description cannot be empty.");
        }
    }

    // Prompt user for a vendor
    private static String promptForVendor()
    {
        while (true)
        {
            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine().trim();

            if (!vendor.isEmpty())
                return vendor;

            System.out.println("Vendor cannot be empty.");
        }
    }

    // Prompt user for an amount
    private static double promptForAmount()
    {
        while (true)
        {
            System.out.print("Enter amount: $");
            String input = scanner.nextLine().trim();

            try
            {
                double amount = Double.parseDouble(input);

                if (amount <= 0)
                {
                    System.out.println("Amount must be greater than 0.");
                    continue;
                }

                return amount;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid amount. Please enter a valid number.");
            }
        }
    }
}