package CodeInspections;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

public class ConditionalStatementInspection extends BaseJavaLocalInspectionTool {

    private final LocalQuickFix quickFix = new ConditionalStatementFix();

    @NotNull
    public String getDisplayName() {
        return "Conditional Statements instead of using polymorphism";
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
            public void visitSwitchStatement(PsiSwitchStatement statement) {
                super.visitStatement(statement);
                holder.registerProblem(statement, getDisplayName(), quickFix);
            }
        };
    }
}
