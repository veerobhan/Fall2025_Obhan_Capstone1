package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ledger
{
    private final Scanner scanner;

    public Ledger(Scanner scanner)
    {
        this.scanner = scanner;
    }

    // display menu and handle user input
    public void display()
    {
        boolean running = true;

        while (running)
        {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║  LEDGER MENU                       ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║  A) All Entries                    ║");
            System.out.println("║  D) Deposits                       ║");
            System.out.println("║  P) Payments                       ║");
            System.out.println("║  R) Reports                        ║");
            System.out.println("║  H) Home                           ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("\n► Please select an option: ");


            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice)
            {
                case "A":
                    displayAllEntries();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    displayReportsMenu();
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter A, D, P, R, or H.");
            }
        }
    }

    // Display all entries
    public void displayAllEntries()
    {
        List<Transaction> transactions = FileManager.loadTransactions();
        displayTransactions(transactions, "All Entries");
    }

    // Display only deposits
    private void displayDeposits()
    {
        List<Transaction> allTransactions = FileManager.loadTransactions();
        List<Transaction> deposits = new ArrayList<>();

        for (Transaction transaction : allTransactions)
        {
            if (transaction.isDeposit())
                deposits.add(transaction);
        }

        displayTransactions(deposits, "Deposits");
    }

    // Display only payments
    private void displayPayments()
    {
        List<Transaction> allTransactions = FileManager.loadTransactions();
        List<Transaction> payments = new ArrayList<>();

        for (Transaction transaction : allTransactions)
        {
            if (transaction.isPayment())
                payments.add(transaction);
        }

        displayTransactions(payments, "Payments");
    }

    // Display transactions
    private void displayTransactions(List<Transaction> transactions, String title)
    {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║  "+ title.toUpperCase());
        System.out.println("╚════════════════════════════════════╝");

        if (transactions.isEmpty())
            System.out.println("No transactions found.");
        else
        {
            sortTransactionsNewestFirst(transactions);
            for (Transaction transaction : transactions)
            {
                System.out.println(transaction);
            }
        }
    }

    // Sort transactions by date and time (newest first)
    private void sortTransactionsNewestFirst(List<Transaction> transactions)
    {
        int n = transactions.size();

        // Bubble sort algorithm
        for (int i = 0; i < n - 1; i++)
        {
            for (int j = 0; j < n - i - 1; j++)
            {
                Transaction t1 = transactions.get(j);
                Transaction t2 = transactions.get(j + 1);

                // Compare dates first
                if (t1.getDate().isBefore(t2.getDate()))
                {
                    // Swap if t1 is before t2 (we want the newest first)
                    swap(transactions, j, j + 1);
                }
                else if (t1.getDate().isEqual(t2.getDate()))
                {
                    // If dates are equal, compare times
                    if (t1.getTime().isBefore(t2.getTime()))
                    {
                        swap(transactions, j, j + 1);
                    }
                }
            }
        }
    }

    // Method to swap two elements in a list
    private void swap(List<Transaction> list, int i, int j)
    {
        Transaction temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    // Display reports a menu and handle user input
    private void displayReportsMenu()
    {
        boolean running = true;

        while (running)
        {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║  REPORTS MENU                      ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║  1) Month To Date                  ║");
            System.out.println("║  2) Previous Month                 ║");
            System.out.println("║  3) Year To Date                   ║");
            System.out.println("║  4) Previous Year                  ║");
            System.out.println("║  5) Search by Vendor               ║");
            System.out.println("║  0) Back                           ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("\n► Please select an option: ");


            String choice = scanner.nextLine().trim();

            switch (choice)
            {
                case "1":
                    monthToDateReport();
                    break;
                case "2":
                    previousMonthReport();
                    break;
                case "3":
                    yearToDateReport();
                    break;
                case "4":
                    previousYearReport();
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number between 0 and 5.");
            }
        }
    }

    // Display transactions from the current month
    private void monthToDateReport()
    {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);

        List<Transaction> allTransactions = FileManager.loadTransactions();
        List<Transaction> filtered = new ArrayList<>();

        for (Transaction transaction : allTransactions)
        {
            // Check if the transaction date is on or after start of current month
            if (!transaction.getDate().isBefore(startOfMonth))
                filtered.add(transaction);
        }

        displayTransactions(filtered, "Month To Date");
    }

    // Display transactions from the previous month
    private void previousMonthReport()
    {
        LocalDate now = LocalDate.now();
        LocalDate startOfPreviousMonth = now.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfPreviousMonth = now.withDayOfMonth(1).minusDays(1);

        List<Transaction> allTransactions = FileManager.loadTransactions();
        List<Transaction> filtered = new ArrayList<>();

        for (Transaction transaction : allTransactions)
        {
            LocalDate transactionDate = transaction.getDate();
            // Check if transaction is within previous month
            if (!transactionDate.isBefore(startOfPreviousMonth) && !transactionDate.isAfter(endOfPreviousMonth))
                filtered.add(transaction);
        }

        displayTransactions(filtered, "Previous Month");
    }

    // Display transactions from the current year
    private void yearToDateReport()
    {
        LocalDate now = LocalDate.now();
        LocalDate startOfYear = now.withDayOfYear(1);

        List<Transaction> allTransactions = FileManager.loadTransactions();
        List<Transaction> filtered = new ArrayList<>();

        for (Transaction transaction : allTransactions)
        {
            // Check if the transaction date is on or after start of current year
            if (!transaction.getDate().isBefore(startOfYear))
                filtered.add(transaction);
        }

        displayTransactions(filtered, "Year To Date");
    }

    // Display transactions from the previous year
    private void previousYearReport()
    {
        LocalDate now = LocalDate.now();
        int previousYear = now.getYear() - 1;

        List<Transaction> allTransactions = FileManager.loadTransactions();
        List<Transaction> filtered = new ArrayList<>();

        for (Transaction transaction : allTransactions)
        {
            // Check if transaction is from previous year
            if (transaction.getDate().getYear() == previousYear)
                filtered.add(transaction);
        }

        displayTransactions(filtered, "Previous Year");
    }

    // Search for transactions by vendor name
    private void searchByVendor()
    {
        System.out.print("Enter vendor name: ");
        String vendorName = scanner.nextLine().trim();

        List<Transaction> allTransactions = FileManager.loadTransactions();
        List<Transaction> filtered = new ArrayList<>();

        for (Transaction transaction : allTransactions)
        {
            // Check if the vendor matches (case-insensitive)
            if (transaction.getVendor().equalsIgnoreCase(vendorName))
                filtered.add(transaction);
        }

        displayTransactions(filtered, "Transactions for Vendor: " + vendorName);
    }
}