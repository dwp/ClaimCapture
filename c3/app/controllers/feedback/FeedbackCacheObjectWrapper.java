package controllers.feedback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedbackCacheObjectWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(FeedbackCacheObjectWrapper.class);

    public FeedbackCacheObject getFeedbackFromJson(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        FeedbackCacheObject feedbackCacheObject = null;
        try {
            feedbackCacheObject = objectMapper.readValue(jsonString, FeedbackCacheObject.class);
            return feedbackCacheObject;
        } catch (Exception e) {
            LOG.error("Failed to parse jsonString:" + jsonString + " exception:" + e.toString());
        }
        return feedbackCacheObject;
    }

    public String jsonFromFeedback(FeedbackCacheObject feedbackCacheObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(feedbackCacheObject);
        } catch (JsonProcessingException e) {
            LOG.error("Failed to create jsonString from feedback object exception:" + e.toString());
        }
        return jsonString;
    }
}
