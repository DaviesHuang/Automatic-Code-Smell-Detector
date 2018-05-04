package CodeInspections;

import com.intellij.codeInspection.InspectionsBundle;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.refactoring.extractMethod.ExtractMethodHandler;
import com.intellij.refactoring.extractMethod.ExtractMethodProcessor;
import com.siyeh.ig.classmetrics.CyclomaticComplexityVisitor;
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
        refactorMethod(method);
    }

    public void refactorMethod(PsiMethod method) {
        PsiCodeBlock body = method.getBody();
        PsiElement[] methodChildren = body.getChildren();
        int maxComplexity = 0;
        PsiElement complexElement = null;
        final CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();
        method.accept(visitor);
        final int methodComplexity = visitor.getComplexity();

        System.out.println("method: " + method.getName() + ": " + methodComplexity);
        for (PsiElement child : methodChildren) {
            visitor.reset();
            child.accept(visitor);
            int complexity = visitor.getComplexity() - 1;
            if (complexity > maxComplexity) {
                complexElement = child;
                maxComplexity = complexity;
            }
            System.out.println(child.getText() + ": " + visitor.getComplexity());
        }

        if (complexElement == null) {
            return;
        }

        if (maxComplexity + 1 >= methodComplexity) {
            System.out.println("one big child contains all the complexity");
        } else {
            PsiElementExtractVisitor extractVisitor = new PsiElementExtractVisitor();
            complexElement.accept(extractVisitor);
        }
    }

    private class PsiElementExtractVisitor extends JavaRecursiveElementWalkingVisitor {

        @Override
        public void visitIfStatement(@NotNull PsiIfStatement ifStatement) {
            System.out.println("visited if statement");
        }

        @Override
        public void visitMethodCallExpression(PsiMethodCallExpression expression) {
            System.out.println("visited method call statement");
            ExtractMethodProcessor processor = ExtractMethodHandler.getProcessor(
                    expression.getProject(),
                    new PsiElement[]{expression},
                    expression.getContainingFile(),
                    false
            );
            assert processor != null;
            ExtractMethodHandler.invokeOnElements(expression.getProject(), processor, expression.getContainingFile(), false);
        }
    }
}
