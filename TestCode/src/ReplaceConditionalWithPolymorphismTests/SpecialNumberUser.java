package ReplaceConditionalWithPolymorphismTests;

import ReplaceConditionalWithPolymorphismTests.example1.SpecialNumber;

public class SpecialNumberUser {

    public static void main(String[] args) {
        //conditional
        SpecialNumber specialNumber = SpecialNumber.createSpecialNumber(1);
        specialNumber.getSpecialNumber();

        //polymorphism
//        int num = 1;
//        SpecialNumber absSpecialNumber;
//        absSpecialNumber = instantiateTheRightThing(num);
//        System.out.println(absSpecialNumber.getSpecialNumber());
    }
}
