package CodeInspections.CyclomaticComplexity;

import CodeInspections.CyclomaticComplexity.MethodComplexityFix;
import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.siyeh.ig.classmetrics.CyclomaticComplexityVisitor;
import org.jetbrains.annotations.NotNull;

public class MethodComplexityInspection extends BaseJavaLocalInspectionTool {

    private final LocalQuickFix quickFix = new MethodComplexityFix();
    private final int threshold = 8;

    @NotNull
    public String getDisplayName() {
        return "Excessive cyclomatic complexity in method";
    }

    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.BUGS_GROUP_NAME;
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            final CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();

            @Override
            public void visitMethod(PsiMethod method) {
                visitor.reset();
                method.accept(visitor);
                final int complexity = visitor.getComplexity();
                if (complexity >= threshold) {
                    holder.registerProblem(method, getProblemDescription(complexity), quickFix);
                }
            }
        };
    }

    private String getProblemDescription(int complexity) {
        return getDisplayName() + " (Complexity: " + complexity + ")";
    }
}
