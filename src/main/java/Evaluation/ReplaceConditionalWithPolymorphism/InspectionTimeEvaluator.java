package Evaluation.ReplaceConditionalWithPolymorphism;

import java.util.Date;

public class InspectionTimeEvaluator {

    private static Date start;
    private String inspectionName;

    public InspectionTimeEvaluator(String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public void start() {
        start = new Date();
    }

    public void end() {
        Date end = new Date();
        long diff = end.getTime() - start.getTime();
        System.out.println(this.inspectionName + " inspection took: " + diff + " mills");
    }
}
