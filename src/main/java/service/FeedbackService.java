package service;

import entity.Feedback;
import java.util.List;

public interface FeedbackService {
    boolean addFeedback(Feedback feedback);
    boolean updateFeedback(int feedbackId, String comments, Integer rating);
    boolean deleteFeedback(int feedbackId);
    List<Feedback> viewFeedbackForProduct(int productId);
    double getAverageRatingForProduct(int productId);
}
