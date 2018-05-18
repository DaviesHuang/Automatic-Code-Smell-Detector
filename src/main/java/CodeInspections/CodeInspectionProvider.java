package CodeInspections;

import CodeInspections.CyclomaticComplexity.MethodComplexityInspection;
import CodeInspections.ObjectComparison.ObjectComparisonInspection;
import CodeInspections.ReplaceConditionalWithPolymorphism.ReplaceConditionalWithPolymorphismInspection;
import CodeInspections.ReturnPrivateMutableField.ReturnPrivateMutableFieldInspection;
import com.intellij.codeInspection.InspectionToolProvider;
import org.jetbrains.annotations.NotNull;

public class CodeInspectionProvider implements InspectionToolProvider {

    @NotNull
    @Override
    public Class[] getInspectionClasses() {
        return new Class[]{
                ObjectComparisonInspection.class,
                MethodComplexityInspection.class,
                ReturnPrivateMutableFieldInspection.class,
                ReplaceConditionalWithPolymorphismInspection.class
        };
    }
}
