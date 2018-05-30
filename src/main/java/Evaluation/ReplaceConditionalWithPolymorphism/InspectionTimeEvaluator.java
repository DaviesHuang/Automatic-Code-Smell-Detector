package Evaluation.ReplaceConditionalWithPolymorphism;

import java.util.Date;

public class InspectionTimeEvaluator {

    private static Date start;

    public static void start() {
        start = new Date();
    }

    public static void end() {
        Date end = new Date();
        long diff = end.getTime() - start.getTime();
        System.out.println("Replace conditional with polymorphism inspection took: " + diff + " mills");
    }
}
