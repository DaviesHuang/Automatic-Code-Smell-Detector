package CodeInspections;

import com.intellij.codeInspection.InspectionToolProvider;
import org.jetbrains.annotations.NotNull;

public class CodeInspectionProvider implements InspectionToolProvider {

    @NotNull
    @Override
    public Class[] getInspectionClasses() {
        return new Class[]{
                ObjectComparisonInspection.class,
                MethodComplexityInspection.class,
                ReturnPrivateMutableFieldInspection.class
        };
    }
}
