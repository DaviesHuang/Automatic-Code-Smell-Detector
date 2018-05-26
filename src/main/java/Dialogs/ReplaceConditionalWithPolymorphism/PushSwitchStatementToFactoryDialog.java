package Dialogs.ReplaceConditionalWithPolymorphism;

import Visitors.LocateSwitchStatementVisitor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiSwitchStatement;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class PushSwitchStatementToFactoryDialog extends DialogWrapper {
    private final PsiClass myContainingClass;
    private final Project myProject;
    private final PsiSwitchStatement mySwitchStatement;

    public PushSwitchStatementToFactoryDialog(PsiClass containingClass, PsiElement switchStatement) {
        super(switchStatement.getProject(), true);
        myContainingClass = containingClass;
        this.myProject = switchStatement.getProject();
        this.mySwitchStatement = (PsiSwitchStatement) switchStatement;

        setTitle("Push Switch Statement To Factory");
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
        panel.add(new JLabel("Step 5:"), gbc);

        //second line
        gbc.gridy++;
        panel.add(new JLabel("In this step, the switch statement is pushed into the factory method"), gbc);

        return panel;
    }

    protected JComponent createCenterPanel() {
        return null;
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
        boolean performed = performAction(mySwitchStatement);
        if (performed) {
            ApplicationManager.getApplication().invokeLater(this::performNextStep);
        }
    }

    private boolean performAction(PsiElement mySwitchStatement) {
        LocateSwitchStatementVisitor visitor = new LocateSwitchStatementVisitor(mySwitchStatement.getText());
        myContainingClass.accept(visitor);
        PsiSwitchStatement switchStatement = visitor.getSwitchStatement();
        System.out.print(switchStatement.getText());
        return false;
    }

    private void performNextStep() {
//        showReplaceConstructorsWithFactoryDialog(psiClass);
    }
}
