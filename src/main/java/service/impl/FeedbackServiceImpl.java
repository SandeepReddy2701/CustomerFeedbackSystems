package service.impl;

import dao.FeedbackDAO;
import dao.impl.FeedbackDAOImpl;
import entity.Feedback;
import service.FeedbackService;

import java.util.List;

public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackDAO feedbackDAO = new FeedbackDAOImpl();

    @Override
    public boolean addFeedback(Feedback feedback) {
        return feedbackDAO.insertFeedback(feedback);
    }

    @Override
    public boolean updateFeedback(int feedbackId, String comments, Integer rating) {
        return feedbackDAO.updateFeedback(feedbackId, comments, rating);
    }

    @Override
    public boolean deleteFeedback(int feedbackId) {
        return feedbackDAO.deleteFeedback(feedbackId);
    }

    @Override
    public List<Feedback> viewFeedbackForProduct(int productId) {
        return feedbackDAO.getFeedbackByProductId(productId);
    }

    @Override
    public double getAverageRatingForProduct(int productId) {
        return feedbackDAO.getAverageRatingByProductId(productId);
    }
}
