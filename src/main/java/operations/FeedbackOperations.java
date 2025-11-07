package operations;

import entity.Feedback;
import service.FeedbackService;
import service.impl.FeedbackServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class FeedbackOperations {
    private final FeedbackService service = new FeedbackServiceImpl();
    private final Scanner input = new Scanner(System.in);

    public void insertFeedback() {
        System.out.print("Enter Feedback ID (6 digits): ");
        int id = Integer.parseInt(input.nextLine().trim());
        System.out.print("Enter Customer ID (6 digits): ");
        int cid = Integer.parseInt(input.nextLine().trim());
        System.out.print("Enter Product ID (6 digits): ");
        int pid = Integer.parseInt(input.nextLine().trim());
        System.out.print("Enter Comments: ");
        String comments = input.nextLine().trim();
        System.out.print("Enter Rating (1-5): ");
        int rating = Integer.parseInt(input.nextLine().trim());

        Feedback feedback = new Feedback(id, cid, pid, LocalDate.now(), comments, rating);
        service.addFeedback(feedback);
        System.out.println("✅ Feedback added!");
    }

    public void viewFeedbackForProduct() {
        System.out.print("Enter Product ID: ");
        int pid = Integer.parseInt(input.nextLine().trim());
        List<Feedback> list = service.viewFeedbackForProduct(pid);

        if (list.isEmpty()) {
            System.out.println("⚠️ No feedback found.");
        } else {
            list.forEach(f ->
                    System.out.printf("Feedback #%d | Customer %d | Rating %d⭐ | %s%n",
                            f.getFeedbackId(), f.getCustomerId(), f.getRating(), f.getComments()));
        }

        double avg = service.getAverageRatingForProduct(pid);
        System.out.printf("📊 Average Rating: %.2f%n", avg);
    }

    public void updateFeedback() {
        System.out.print("Enter Feedback ID to update: ");
        int fid = Integer.parseInt(input.nextLine().trim());
        System.out.print("Enter new comments (or blank): ");
        String c = input.nextLine().trim();
        System.out.print("Enter new rating (1-5 or 0): ");
        int r = Integer.parseInt(input.nextLine().trim());
        service.updateFeedback(fid, c.isEmpty() ? null : c, (r >= 1 && r <= 5) ? r : null);
    }

    public void deleteFeedback() {
        System.out.print("Enter Feedback ID: ");
        int fid = Integer.parseInt(input.nextLine().trim());
        service.deleteFeedback(fid);
    }
}
