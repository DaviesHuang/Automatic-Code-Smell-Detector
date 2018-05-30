package CodeInspections.ReplaceConditionalWithPolymorphism;

import Evaluation.ReplaceConditionalWithPolymorphism.InspectionTimeEvaluator;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.siyeh.ig.psiutils.SwitchUtils;
import org.jetbrains.annotations.NotNull;

import static Constants.Constants.CODE_SMELL;

public class ReplaceConditionalWithPolymorphismInspection extends BaseJavaLocalInspectionTool {

    private final LocalQuickFix quickFix = new ReplaceConditionalWithPolymorphismFix();
    private final int branchThreshold = 3;

    public ReplaceConditionalWithPolymorphismInspection() {
        super();
    }

    @NotNull
    public String getDisplayName() {
        return "Replace conditional with polymorphism";
    }

    @NotNull
    public String getGroupDisplayName() {
        return CODE_SMELL;
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    private void registerError(ProblemsHolder holder, PsiElement element) {
        holder.registerProblem(element, getDisplayName(), quickFix);
        InspectionTimeEvaluator.end();
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        InspectionTimeEvaluator.start();
        return new JavaElementVisitor() {
            @Override
            public void visitSwitchStatement(PsiSwitchStatement statement) {
                super.visitStatement(statement);

                if (withinBranchCountLimit(statement)) {
                    return;
                }

                PsiExpression expression = statement.getExpression();
                if (isField(expression) || isMethodCall(expression)) {
                    registerError(holder, statement);
                }
            }
        };
    }

    private boolean withinBranchCountLimit(PsiSwitchStatement statement) {
        final int branchCount = SwitchUtils.calculateBranchCount(statement);
        final int branchCountIncludingDefault = (branchCount < 0) ? -branchCount + 1 : branchCount;
        return branchCountIncludingDefault <= branchThreshold;
    }

    private boolean isField(PsiExpression expression) {
        if (expression instanceof PsiReferenceExpression) {
            PsiReferenceExpression referenceExpression = (PsiReferenceExpression) expression;
            PsiElement resolvedElement = referenceExpression.resolve();
            return resolvedElement instanceof PsiField;
        }
        return false;
    }

    private boolean isMethodCall(PsiExpression expression) {
        return expression instanceof PsiMethodCallExpression;
    }
}
