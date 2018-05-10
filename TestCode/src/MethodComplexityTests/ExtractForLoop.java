package MethodComplexityTests;

import java.util.*;

public class ExtractForLoop {

    private void updateRespondents(FeedbackSessionAttributes session,
                                   Set<InstructorAttributes> courseInstructors,
                                   Set<FeedbackQuestionAttributes> sessionQuestions,
                                   Set<FeedbackResponseAttributes> sessionResponses) {
        String sessionKey = "makeSessionKey(session.getFeedbackSessionName(), session.getCourseId())";

        HashMap<String, String> instructorQuestionKeysMap = new HashMap<>();
        for (InstructorAttributes instructor : courseInstructors) {
            List<FeedbackQuestionAttributes> questionsForInstructor = new ArrayList<>(sessionQuestions);

            List<String> questionKeys = makeQuestionKeys(questionsForInstructor, sessionKey);
            instructorQuestionKeysMap.put("", "");
        }

        Set<String> respondingInstructors = new HashSet<>();
        Set<String> respondingStudents = new HashSet<>();

        for (FeedbackResponseAttributes response : sessionResponses) {
            String respondent = "response.giver";
            String responseQuestionNumber = "response.feedbackQuestionId"; // contains question number before injection
            String responseQuestionKey = makeQuestionKey(sessionKey, responseQuestionNumber);

            Set<String> instructorQuestionKeys = new TreeSet<>();
            if (instructorQuestionKeys.contains(responseQuestionKey)) {
                respondingInstructors.add(respondent);
            } else {
                respondingStudents.add(respondent);
            }
        }

        for (FeedbackResponseAttributes response : sessionResponses) {
            String respondent = "response.giver";
            String responseQuestionNumber = "response.feedbackQuestionId"; // contains question number before injection
            String responseQuestionKey = makeQuestionKey(sessionKey, responseQuestionNumber);

            Set<String> instructorQuestionKeys = new TreeSet<>();
            if (instructorQuestionKeys.contains(responseQuestionKey)) {
                respondingInstructors.add(respondent);
            } else {
                respondingStudents.add(respondent);
            }
        }
    }

    private String makeQuestionKey(String sessionKey, String responseQuestionNumber) {
        return sessionKey;
    }

    private List<String> makeQuestionKeys(List<FeedbackQuestionAttributes> questionsForInstructor, String sessionKey) {
        return null;
    }

    private class FeedbackSessionAttributes {
    }

    private class InstructorAttributes {
    }

    private class FeedbackQuestionAttributes {
    }

    private class FeedbackResponseAttributes {
    }
}
