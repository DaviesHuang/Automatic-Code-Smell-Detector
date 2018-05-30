package MethodComplexityTests;

import java.util.Map;

public class ExtractIfStatement {

    public boolean extractQuestionDetails(Map<String, String[]> requestParameters) {

        String distributeToRecipientsString = "";
        String pointsPerOptionString = "";
        String totalPointsString = "";
        String pointsForEachOptionString = "HttpRequestHelper.getValueFromParamMap(requestParameters, " +
                "Const.ParamsNames.FEEDBACK_QUESTION_CONSTSUMPOINTSFOREACHOPTION)";
        String pointsForEachRecipientString = "HttpRequestHelper.getValueFromParamMap(requestParameters, Const.P" +
                "aramsNames.FEEDBACK_QUESTION_CONSTSUMPOINTSFOREACHRECIPIENT)";

        String forceUnevenDistributionString = "HttpRequestHelper.getValueFromParamMap(requestParameters, Const.P" +
                "aramsNames.FEEDBACK_QUESTION_CONSTSUMPOINTSFOREACHRECIPIENT)";
        String distributePointsOption = "HttpRequestHelper.getValueFromParamMap(requestParameters, Const.P" +
                "aramsNames.FEEDBACK_QUESTION_CONSTSUMPOINTSFOREACHRECIPIENT)";

        boolean distributeToRecipients = "true".equals(distributeToRecipientsString);
        boolean pointsPerOption = "true".equals(pointsPerOptionString);

        int points = 0;
        if (pointsPerOption) {
            String pointsString = distributeToRecipients ? pointsForEachRecipientString : pointsForEachOptionString;
            points = Integer.parseInt(pointsString);
        } else {
            points = Integer.parseInt(totalPointsString);
        }

        boolean forceUnevenDistribution = "on".equals(forceUnevenDistributionString);

        if (distributeToRecipients) {
            this.setConstantSumQuestionDetails(pointsPerOption, points, forceUnevenDistribution,
                    distributePointsOption);
        } else {
            String numConstSumOptionsCreatedString =
                    "HttpRequestHelper.getValueFromParamMap(requestParameters," +
                            "Const.ParamsNames.FEEDBACK_QUESTION_NUMBEROFCHOICECREATED)";
            int numConstSumOptionsCreated = Integer.parseInt(numConstSumOptionsCreatedString);

            for (int i = 0; i < numConstSumOptionsCreated; i++) {
                String constSumOption =
                        "HttpRequestHelper.getValueFromParamMap(" +
                                "requestParameters, Const.ParamsNames.FEEDBACK_QUESTION_CONSTSUMOPTION +  + i)";
                if (constSumOption != null && !constSumOption.trim().isEmpty()) {
                    int numOfConstSumOptions = 0;
                    numOfConstSumOptions++;
                }
            }
            this.setConstantSumQuestionDetails(pointsPerOption, points, forceUnevenDistribution,
                    distributePointsOption);
        }
        return true;
    }

    private void setConstantSumQuestionDetails(boolean pointsPerOption, int points, boolean forceUnevenDistribution, String distributePointsOption) {
    }

}
