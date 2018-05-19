package Dialogs.ReplaceConditionalWithPolymorphism;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CreateFactoryMethodDialog extends DialogWrapper {

    private JPanel myMainPanel;
    private Project project;
    private PsiElement element;
    private PsiClass psiClass;

    public CreateFactoryMethodDialog(PsiClass psiClass, @Nullable PsiElement element, boolean canBeParent) {
        super(element.getProject(), canBeParent);
        this.project = element.getProject();
        this.element = element;
        this.psiClass = psiClass;
        setTitle("Create Abstract Class");
        init();
    }

    protected void init() {
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return myMainPanel;
    }

    @NotNull
    @Override
    protected Action getOKAction() {
        Action okAction = super.getOKAction();
        okAction.putValue(Action.NAME, "Create Abstract Class");
        return okAction;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        boolean performed = performAction(this.element);
        if (performed) {
            ApplicationManager.getApplication().invokeLater(this::performNextStep);
        }
    }

    private boolean performAction(PsiElement element) {
        return false;
    }

    private void performNextStep() {

    }
}
