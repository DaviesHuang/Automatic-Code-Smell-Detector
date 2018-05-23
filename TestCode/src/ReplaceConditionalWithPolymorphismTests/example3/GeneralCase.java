package ReplaceConditionalWithPolymorphismTests.example3;

public class GeneralCase {

    private boolean conditional1;
    private EnumeratedType conditional2;

    public GeneralCase(boolean cond1, EnumeratedType cond2)
    {
        conditional1 = cond1;
        conditional2 = cond2;
    }

    public void method1()
    {
        if(conditional1)
        {
            //do something
        }
        else
        {
            //do something else
        }
    }

    public void method2()
    {
        switch(conditional2)
        {
            case CASE1:
                //do something
                break;
            case CASE2:
                //do something else
                break;
            case CASE3:
                //do something entirely different
                break;
        }
    }

    enum EnumeratedType
    {
        CASE1,
        CASE2,
        CASE3
    }
}


