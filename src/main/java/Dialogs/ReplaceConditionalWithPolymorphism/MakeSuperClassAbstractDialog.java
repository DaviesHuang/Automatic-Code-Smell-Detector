package Dialogs.ReplaceConditionalWithPolymorphism;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MakeSuperClassAbstractDialog extends DialogWrapper {
    private final PsiClass myClass;
    private final Project project;
    private final PsiSwitchStatement mySwitchStatement;

    public MakeSuperClassAbstractDialog(PsiClass myClass, PsiElement switchStatement) {
        super(switchStatement.getProject(), true);
        this.myClass = myClass;
        this.project = switchStatement.getProject();
        this.mySwitchStatement = (PsiSwitchStatement) switchStatement;

        setTitle("Make Super Class Abstract");
        init();
    }

    @Override
    protected JComponent createNorthPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = JBUI.insets(4, 0, 4, 4);

        //first line
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Step 6:"), gbc);

        //second line
        gbc.gridy++;
        panel.add(new JLabel("The method containing switch statement is made abstract"), gbc);

        //third line
        gbc.gridy++;
        panel.add(new JLabel("This class is also made abstract"), gbc);

        return panel;
    }

    protected JComponent createCenterPanel() {
        return null;
    }

    @NotNull
    @Override
    protected Action getOKAction() {
        Action okAction = super.getOKAction();
        okAction.putValue(Action.NAME, "Complete refactoring");
        return okAction;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        boolean performed = performAction();
        if (performed) {
            ApplicationManager.getApplication().invokeLater(this::performNextStep);
        }
    }

    private boolean performAction() {
        PsiMethod method = PsiTreeUtil.getParentOfType(mySwitchStatement, PsiMethod.class);
        WriteCommandAction.runWriteCommandAction(method.getProject(), () -> {
            method.getModifierList().setModifierProperty(PsiModifier.ABSTRACT, true);
            method.getBody().delete();
            myClass.getModifierList().setModifierProperty(PsiModifier.ABSTRACT, true);
        });
        return false;
    }

    private void performNextStep() {
        return;
    }


}
