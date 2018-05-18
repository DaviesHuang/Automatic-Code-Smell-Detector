package CodeInspections.ObjectComparison;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.InspectionsBundle;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class ObjectComparisonInspection extends BaseJavaLocalInspectionTool {

    private final LocalQuickFix quickFix = new ObjectComparisonFix();

    @NotNull
    public String getDisplayName() {
        return "Object comparison using '==' or '!=' instead of 'equals()'";
    }

    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.BUGS_GROUP_NAME;
    }

    private static boolean isNullLiteral(PsiExpression expr) {
        return expr instanceof PsiLiteralExpression && "null".equals(expr.getText());
    }

    private boolean isObject(PsiType type) {
        return type instanceof PsiClassType;
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
            public void visitBinaryExpression(PsiBinaryExpression expression) {
                super.visitBinaryExpression(expression);
                IElementType opSign = expression.getOperationTokenType();
                if (opSign == JavaTokenType.EQEQ || opSign == JavaTokenType.NE) {
                    PsiExpression lOperand = expression.getLOperand();
                    PsiExpression rOperand = expression.getROperand();
                    if (rOperand == null || isNullLiteral(lOperand) || isNullLiteral(rOperand)) return;

                    PsiType lType = lOperand.getType();
                    PsiType rType = rOperand.getType();

                    if (isObject(lType) || isObject(rType)) {
                        holder.registerProblem(expression, InspectionsBundle.message("inspection.comparing.references.problem.descriptor"), quickFix);
                    }
                }
            }
        };
    }
}
