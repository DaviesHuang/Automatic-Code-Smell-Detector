package Dialogs.ReplaceConditionalWithPolymorphism;

import DialogProviders.ReplaceConditionalWithPolymorphismDialogsProvider;
import PsiUtils.PsiUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import com.intellij.psi.util.MethodSignature;
import com.intellij.refactoring.extractMethod.ExtractMethodHandler;
import com.intellij.refactoring.extractMethod.ExtractMethodProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.Modifier;

public class EnsureExtractedMethodVisibilityDialog extends DialogWrapper {

    private JPanel myMainPanel;
    private PsiElement element;
    private PsiClass psiClass;

    public EnsureExtractedMethodVisibilityDialog(PsiClass psiClass, @Nullable PsiElement element, boolean canBeParent) {
        super(element.getProject(), canBeParent);
        this.element = element;
        this.psiClass = psiClass;
        setTitle("Ensure the New Method Is Atleast Package Visible");
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
        okAction.putValue(Action.NAME, "Next");
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
        PsiMethod method = PsiUtils.getMethodFromClass(psiClass, element.getText());
        PsiModifierList modifierList = method.getModifierList();
        if (modifierList.hasModifierProperty(PsiModifier.PRIVATE)) {
            WriteCommandAction.runWriteCommandAction(method.getProject(), () -> {
                modifierList.setModifierProperty(PsiModifier.PRIVATE, false);
                modifierList.setModifierProperty(PsiModifier.PROTECTED, true);
            });

        }
        return true;
    }

    private void performNextStep() {
        ReplaceConditionalWithPolymorphismDialogsProvider.showCreateSubClassDialog(psiClass, element);
    }
}
