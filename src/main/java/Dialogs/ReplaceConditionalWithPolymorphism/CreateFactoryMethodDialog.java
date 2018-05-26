package Dialogs.ReplaceConditionalWithPolymorphism;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CreateFactoryMethodDialog extends DialogWrapper {

    private JPanel myMainPanel;
    private Project project;
    private PsiElement element;
    private PsiClass aClass;

    public CreateFactoryMethodDialog(PsiClass aClass, @Nullable PsiElement element, boolean canBeParent) {
        super(element.getProject(), canBeParent);
        this.project = element.getProject();
        this.element = element;
        this.aClass = aClass;
        setTitle("Create Factory Method");
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
        okAction.putValue(Action.NAME, "Create Factory Method");
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
//        for (PsiMethod method : aClass.getAllMethods()) {
//            if (method.isConstructor()) {
//                new ReplaceAllConstructorsWithFactoryDialog(project, method, method.getContainingClass()).show();
//            }
//        }
        return false;
    }

    private void performNextStep() {

    }
}
