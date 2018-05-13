package CodeInspections.ReturnPrivateMutableField;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class ReturnPrivateMutableFieldFix implements LocalQuickFix {

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return "Use defensive copy to avoid return direct reference to private field";
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        PsiReturnStatement returnStatement = (PsiReturnStatement) problemDescriptor.getPsiElement();
        PsiExpression returnExpression = returnStatement.getReturnValue();

        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();

        //return a => return a.clone();
        PsiMethodCallExpression cloneExpression = (PsiMethodCallExpression) factory.createExpressionFromText("a.clone()", null);
        cloneExpression.getMethodExpression().getQualifierExpression().replace(returnExpression);

        //return a.clone() => return (Type) a.clone();
        PsiTypeCastExpression typeCast = (PsiTypeCastExpression)factory.createExpressionFromText("(Type) value", null);
        typeCast.getCastType().replace(factory.createTypeElement(returnExpression.getType()));
        typeCast.getOperand().replace(cloneExpression);

        //return a => return (Type) a.clone();
        returnExpression.replace(typeCast);
    }
}
