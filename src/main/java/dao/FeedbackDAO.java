package dao;

import entity.Feedback;
import java.util.List;

public interface FeedbackDAO {
    boolean insertFeedback(Feedback feedback);
    boolean updateFeedback(int feedbackId, String comments, Integer rating);
    boolean deleteFeedback(int feedbackId);
    List<Feedback> getFeedbackByProductId(int productId);
    double getAverageRatingByProductId(int productId);
}
