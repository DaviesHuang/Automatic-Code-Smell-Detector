package DialogProviders;

import Dialogs.CyclomaticComplexity.ComplexityComparisonDialog;
import Dialogs.CyclomaticComplexity.IdentifyComplexElementDialog;
import Dialogs.CyclomaticComplexity.StartRefactoringDialog;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;

public class CyclomaticComplexityDialogsProvider {

    public static boolean showStartDialog(Project project) {
        if (PropertiesComponent.getInstance().getBoolean(StartRefactoringDialog.DISMISSED)) {
            return true;
        }
        StartRefactoringDialog dialog = new StartRefactoringDialog(project, true);
        dialog.show();
        return dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }

    public static boolean showIdentifyComplexElementDialog(Project project, PsiElement element) {
        IdentifyComplexElementDialog dialog = new IdentifyComplexElementDialog(project, true);
        dialog.setElement(element);
        dialog.show();
        return dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }

    public static boolean showIdentifyComplexElementsDialog(Project project, PsiElement[] elements) {
        IdentifyComplexElementDialog dialog = new IdentifyComplexElementDialog(project, false);
        dialog.setElement(elements);
        dialog.show();
        return dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }

    public static boolean showComplexityComparisonDialog(Project project, int originalComplexity, int newComplexity) {
        ComplexityComparisonDialog dialog = new ComplexityComparisonDialog(project, true);
        dialog.setOriginalComplexity(originalComplexity);
        dialog.setNewComplexity(newComplexity);
        dialog.show();
        return dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE;
    }
}
