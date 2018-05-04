package CodeInspections;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.siyeh.ig.classmetrics.CyclomaticComplexityVisitor;
import org.jetbrains.annotations.NotNull;

public class MethodComplexityInspection extends BaseJavaLocalInspectionTool {

    private final LocalQuickFix quickFix = new MethodComplexityFix();
    private final int threshold = 5;

    @NotNull
    public String getDisplayName() {
        return "Method cyclomatic complexity analysis and refactoring";
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
            @Override
            public void visitMethod(PsiMethod method) {
                final CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();
                method.accept(visitor);
                final int complexity = visitor.getComplexity();
                if (complexity >= threshold) {
                    holder.registerProblem(method, getDisplayName(), quickFix);
                }
            }
        };
    }
}
