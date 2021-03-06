package Dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public interface ChainedDialog {

    boolean performAction(PsiElement element);

    void performNextStep();

}
