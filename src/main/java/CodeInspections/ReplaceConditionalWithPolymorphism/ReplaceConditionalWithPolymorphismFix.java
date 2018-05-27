package CodeInspections.ReplaceConditionalWithPolymorphism;

import DialogProviders.ReplaceConditionalWithPolymorphismDialogsProvider;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiSwitchStatement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class ReplaceConditionalWithPolymorphismFix implements LocalQuickFix {

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return "Replace conditional statements with polymorphism";
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
        PsiSwitchStatement element = (PsiSwitchStatement) problemDescriptor.getPsiElement();
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        boolean shouldRefactor = ReplaceConditionalWithPolymorphismDialogsProvider.showStartDialog(project);
        if (shouldRefactor) {
            ReplaceConditionalWithPolymorphismDialogsProvider.showExtractSwitchStatementDialog(psiClass, element);
        }
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }
}
