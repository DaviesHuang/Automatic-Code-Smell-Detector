package Dialogs.ReplaceConditionalWithPolymorphism;

import DialogProviders.ReplaceConditionalWithPolymorphismDialogsProvider;
import Dialogs.ChainedDialog;
import Visitors.LocateSwitchStatementVisitor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.extractMethod.ExtractMethodHandler;
import com.intellij.refactoring.extractMethod.ExtractMethodProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ExtractSwitchStatementDialog extends DialogWrapper implements ChainedDialog {

    private JPanel myMainPanel;
    private PsiSwitchStatement element;
    private PsiClass psiClass;

    public ExtractSwitchStatementDialog(PsiClass psiClass, @Nullable PsiSwitchStatement element, boolean canBeParent) {
        super(element.getProject(), canBeParent);
        this.element = element;
        this.psiClass = psiClass;
        setTitle("Extract Switch Statement");
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
        okAction.putValue(Action.NAME, "Next Step");
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

    public boolean performAction(PsiElement element) {
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        if (method.getBody().getStatements().length > 1) {
            ExtractMethodProcessor processor = ExtractMethodHandler.getProcessor(
                    element.getProject(),
                    new PsiElement[]{element},
                    element.getContainingFile(),
                    false
            );
            assert processor != null;
            return ExtractMethodHandler.invokeOnElements(element.getProject(), processor, element.getContainingFile(), true);
        } else {
            return true;
        }
    }

    public void performNextStep() {
        LocateSwitchStatementVisitor visitor = new LocateSwitchStatementVisitor(element.getText());
        psiClass.accept(visitor);
        PsiSwitchStatement switchStatement = visitor.getSwitchStatement();
        ReplaceConditionalWithPolymorphismDialogsProvider.showEnsureExtractedMethodVisibilityDialog(psiClass, switchStatement);
    }
}
