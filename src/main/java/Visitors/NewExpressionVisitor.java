package Visitors;

import com.intellij.psi.JavaRecursiveElementWalkingVisitor;
import com.intellij.psi.PsiNewExpression;

public class NewExpressionVisitor extends JavaRecursiveElementWalkingVisitor {

    @Override
    public void visitNewExpression(PsiNewExpression expression) {
        super.visitCallExpression(expression);
        System.out.println("visit: " + expression.getText());

    }

}
