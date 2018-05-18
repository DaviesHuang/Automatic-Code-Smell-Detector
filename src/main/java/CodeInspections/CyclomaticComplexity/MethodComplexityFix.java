package CodeInspections.CyclomaticComplexity;

import DialogProviders.CyclomaticComplexityDialogsProvider;
import Visitors.PsiElementExtractVisitor;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIfStatement;
import com.intellij.psi.PsiMethodCallExpression;
import com.siyeh.ig.classmetrics.CyclomaticComplexityVisitor;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class MethodComplexityFix implements LocalQuickFix {

    private static final Logger LOG = Logger.getInstance(MethodComplexityFix.class.getName());

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return "Refactor this method to reduce excessive cyclomatic complexity";
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        boolean shouldRefactor = CyclomaticComplexityDialogsProvider.showStartDialog(project);
        if (shouldRefactor) {
            PsiElement element = problemDescriptor.getPsiElement();
            final int originalComplexity = getComplexity(element);
            boolean refactored = refactor(element);
            if (refactored) {
                final int newComplexity = getComplexity(element);
                CyclomaticComplexityDialogsProvider.showComplexityComparisonDialog(project, originalComplexity, newComplexity);
            }
        }
    }

    private boolean refactor(PsiElement element) {
        int maxComplexity = 1;
        PsiElement complexElement = null;
        final int totalComplexity = getComplexity(element);
        PsiElement[] childrenElement = getChildren(element);

        for (PsiElement child : childrenElement) {
            int complexity = getComplexity(child);
            if (complexity > maxComplexity) {
                complexElement = child;
                maxComplexity = complexity;
            }
        }

        if (complexElement == null) {
            return false;
        }

        if (maxComplexity >= totalComplexity) {
            return refactor(complexElement);
        } else {
            PsiElementExtractVisitor extractVisitor = new PsiElementExtractVisitor();
            complexElement.accept(extractVisitor);
            return extractVisitor.isRefactored();
        }
    }

    private int getComplexity(PsiElement element) {
        CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();
        element.accept(visitor);
        return visitor.getComplexity();
    }

    private PsiElement[] getChildren(PsiElement element) {
        PsiElement[] childrenElement;
        if (element instanceof PsiIfStatement) {
            PsiIfStatement ifStatement = (PsiIfStatement) element;
            childrenElement = ArrayUtils.addAll(
                    ifStatement.getThenBranch().getChildren(),
                    ifStatement.getElseBranch().getChildren()
            );
            childrenElement = ArrayUtils.add(childrenElement, ifStatement.getCondition());
        } else if (element instanceof PsiMethodCallExpression) {
            PsiMethodCallExpression expression = (PsiMethodCallExpression) element;
            childrenElement = expression.getArgumentList().getExpressions();
        } else {
            childrenElement = element.getChildren();
        }
        return childrenElement;
    }
}
