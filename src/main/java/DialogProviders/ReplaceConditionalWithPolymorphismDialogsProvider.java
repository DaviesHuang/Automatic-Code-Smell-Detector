package DialogProviders;

import Dialogs.ReplaceConditionalWithPolymorphism.CreateSubClassDialog;
import Dialogs.ReplaceConditionalWithPolymorphism.ExtractSwitchStatementDialog;
import Dialogs.ReplaceConditionalWithPolymorphism.StartRefactoringDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;

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

    public static boolean showCreateSubClassDialog(PsiClass psiClass, PsiElement element) {
        CreateSubClassDialog dialog = new CreateSubClassDialog(psiClass, element, true);
        dialog.show();
        return dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }
}
