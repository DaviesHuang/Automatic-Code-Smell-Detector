package DialogProviders;

import Dialogs.ReplaceConditionalWithPolymorphism.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;

import java.util.List;

import static PsiUtils.PsiUtils.getAllConstructors;

public class ReplaceConditionalWithPolymorphismDialogsProvider {

    public static boolean showStartDialog(Project project) {
        StartRefactoringDialog dialog = new StartRefactoringDialog(project, true);
        dialog.show();
        return dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }

    public static boolean showExtractSwitchStatementDialog(PsiClass psiClass, PsiElement element) {
        ExtractSwitchStatementDialog dialog = new ExtractSwitchStatementDialog(psiClass, element, true);
        dialog.show();
        return dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }

    public static boolean showEnsureExtractedMethodVisibilityDialog(PsiClass psiClass, PsiElement element) {
        EnsureExtractedMethodVisibilityDialog dialog = new EnsureExtractedMethodVisibilityDialog(psiClass, element, true);
        dialog.show();
        return dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }

    public static boolean showCreateSubClassDialog(PsiClass psiClass, PsiElement element) {
        CreateSubClassDialog dialog = new CreateSubClassDialog(psiClass, element, true);
        dialog.show();
        return dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }

    public static boolean showReplaceConstructorsWithFactoryDialog(PsiClass psiClass, PsiElement switchStatement) {
        PsiMethod[] constructors = getAllConstructors(psiClass);
        ReplaceAllConstructorsWithFactoryDialog dialog =
                new ReplaceAllConstructorsWithFactoryDialog(psiClass.getProject(), constructors, psiClass, switchStatement);
        dialog.show();
        return dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }

    public static boolean showPushSwitchStatementToFactoryDialog(PsiClass psiClass, PsiElement switchStatement, List<PsiMethod> factoryMethods) {
        PushSwitchStatementToFactoryDialog dialog = new PushSwitchStatementToFactoryDialog(psiClass, switchStatement, factoryMethods);
        dialog.show();
        return dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }
}
