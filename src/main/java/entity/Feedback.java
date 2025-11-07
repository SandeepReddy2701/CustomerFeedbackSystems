package entity;

import java.time.LocalDate;

public class Feedback {
    private int feedbackId;
    private int customerId;
    private int productId;
    private LocalDate feedbackDate;
    private String comments;
    private int rating;

    public Feedback() {}

    public Feedback(int feedbackId, int customerId, int productId, LocalDate feedbackDate, String comments, int rating) {
        this.feedbackId = feedbackId;
        this.customerId = customerId;
        this.productId = productId;
        this.feedbackDate = feedbackDate;
        this.comments = comments;
        this.rating = rating;
    }

    public int getFeedbackId() { return feedbackId; }
    public void setFeedbackId(int feedbackId) { this.feedbackId = feedbackId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public LocalDate getFeedbackDate() { return feedbackDate; }
    public void setFeedbackDate(LocalDate feedbackDate) { this.feedbackDate = feedbackDate; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}
