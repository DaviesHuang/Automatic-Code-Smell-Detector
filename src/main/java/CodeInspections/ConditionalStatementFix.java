package CodeInspections;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class ConditionalStatementFix implements LocalQuickFix {

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return "Replace conditional statements with polymorphism";
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {

    }
}
