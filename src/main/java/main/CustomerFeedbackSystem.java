package main;

import operations.FeedbackOperations;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CustomerFeedbackSystem {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        FeedbackOperations ops = new FeedbackOperations();

        boolean running = true;
        while (running) {
            System.out.println("\n===== █▓▒▒░░░ Customer Feedback System ░░░▒▒▓█ =====");
            System.out.println("1. Submit Feedback 👍");
            System.out.println("2. View Feedback for Product 👁️");
            System.out.println("3. Update Feedback Comments / Rating 🛠️");
            System.out.println("4. Delete Feedback 🗑️");
            System.out.println("5. Exit 👋");
            System.out.print("Enter your choice: ");

            try {
                int ch = Integer.parseInt(input.nextLine().trim());

                switch (ch) {
                    case 1 -> ops.insertFeedback();
                    case 2 -> ops.viewFeedbackForProduct();
                    case 3 -> ops.updateFeedback();
                    case 4 -> ops.deleteFeedback();
                    case 5 -> running = false;
                    default -> System.out.println("❌ Invalid choice! Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number (1–5).");
            }
        }

        input.close();
        System.out.println("👋 Application closed. Thank you!");
    }
}
