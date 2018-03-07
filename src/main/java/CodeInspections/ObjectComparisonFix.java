package CodeInspections;

import com.intellij.codeInspection.InspectionsBundle;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class ObjectComparisonFix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return InspectionsBundle.message("inspection.comparing.references.use.quickfix");
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        PsiBinaryExpression binaryExpression = (PsiBinaryExpression) problemDescriptor.getPsiElement();
        IElementType operator = binaryExpression.getOperationTokenType();
        PsiExpression lOperand = binaryExpression.getLOperand();
        PsiExpression rOperand = binaryExpression.getROperand();
        if (rOperand == null) {
            return;
        }
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiMethodCallExpression equalsCall = (PsiMethodCallExpression) factory.createExpressionFromText("a.equals(b)", null);
        equalsCall.getMethodExpression().getQualifierExpression().replace(lOperand);
        equalsCall.getArgumentList().getExpressions()[0].replace(rOperand);
        PsiExpression fixedExpression = (PsiExpression) binaryExpression.replace(equalsCall);

        if (operator == JavaTokenType.NE) {
            PsiPrefixExpression negation = (PsiPrefixExpression) factory.createExpressionFromText("!a", null);
            negation.getOperand().replace(fixedExpression);
            fixedExpression.replace(negation);
        }
    }
}
