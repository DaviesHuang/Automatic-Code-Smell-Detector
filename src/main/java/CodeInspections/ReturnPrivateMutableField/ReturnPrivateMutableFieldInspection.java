package CodeInspections.ReturnPrivateMutableField;

import Evaluation.ReplaceConditionalWithPolymorphism.InspectionTimeEvaluator;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.siyeh.ig.psiutils.ClassUtils;
import org.jetbrains.annotations.NotNull;

import static Constants.Constants.CODE_SMELL;

public class ReturnPrivateMutableFieldInspection extends BaseJavaLocalInspectionTool {

    private final LocalQuickFix quickFix = new ReturnPrivateMutableFieldFix();
    private InspectionTimeEvaluator inspectionTimeEvaluator =
            new InspectionTimeEvaluator("Return private mutable field");

    @Override
    public void inspectionStarted(@NotNull LocalInspectionToolSession session, boolean isOnTheFly) {
        inspectionTimeEvaluator.start();
    }

    @Override
    public void inspectionFinished(@NotNull LocalInspectionToolSession session, @NotNull ProblemsHolder problemsHolder) {
        inspectionTimeEvaluator.end();
    }

    @NotNull
    public String getDisplayName() {
        return "Returning direct reference to private mutable field";
    }

    @NotNull
    public String getGroupDisplayName() {
        return CODE_SMELL;
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
            public void visitReturnStatement(PsiReturnStatement statement) {
                super.visitReturnStatement(statement);
                PsiExpression returnExpression = statement.getReturnValue();
                if (isReference(returnExpression)) {
                    PsiReferenceExpression referenceExpression = (PsiReferenceExpression) returnExpression;
                    PsiElement referenceElement = referenceExpression.resolve();
                    if (isPrivateMutableField(referenceElement)) {
                        holder.registerProblem(statement, getDisplayName(), quickFix);
                    }
                }
            }
        };
    }

    private boolean isReference(PsiExpression expression) {
        return expression instanceof PsiReferenceExpression;
    }

    private boolean isPrivateMutableField(PsiElement element) {
        if (element instanceof PsiField) {
            PsiField field = (PsiField) element;
            boolean isPrivate = field.hasModifierProperty(PsiModifier.PRIVATE);
            boolean isImmutable = ClassUtils.isImmutable(field.getType());
            return isPrivate && !isImmutable;
        }
        return false;
    }
}
