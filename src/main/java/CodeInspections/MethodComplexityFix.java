package CodeInspections;

import Visitors.PsiElementExtractVisitor;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIfStatement;
import com.intellij.psi.PsiMethod;
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
        PsiMethod method = (PsiMethod) problemDescriptor.getPsiElement();
        refactor(method);
    }

    public void refactor(PsiElement element) {
        int maxComplexity = 1;
        PsiElement complexElement = null;
        final CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();
        element.accept(visitor);
        final int methodComplexity = visitor.getComplexity();

        PsiElement[] childrenElement;
        if (element instanceof PsiIfStatement) {
            PsiIfStatement ifStatement = (PsiIfStatement) element;
            childrenElement = ArrayUtils.addAll(
                    ifStatement.getThenBranch().getChildren(),
                    ifStatement.getElseBranch().getChildren()
            );
            childrenElement = ArrayUtils.add(childrenElement, ifStatement.getCondition());
        } else {
            childrenElement = element.getChildren();
        }

        for (PsiElement child : childrenElement) {
            visitor.reset();
            child.accept(visitor);
            int complexity = visitor.getComplexity();
            if (complexity > maxComplexity) {
                complexElement = child;
                maxComplexity = complexity;
            }
        }

        if (complexElement == null) {
            return;
        }

        if (maxComplexity >= methodComplexity) {
            refactor(complexElement);
        } else {
            PsiElementExtractVisitor extractVisitor = new PsiElementExtractVisitor();
            complexElement.accept(extractVisitor);
        }
    }

}
