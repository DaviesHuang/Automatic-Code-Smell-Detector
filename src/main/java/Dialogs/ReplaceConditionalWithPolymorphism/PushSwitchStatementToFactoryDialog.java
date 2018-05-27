package Dialogs.ReplaceConditionalWithPolymorphism;

import Visitors.LocateSwitchStatementVisitor;
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
import java.util.List;

import static PsiUtils.PsiUtils.createCodeBlockFromStatement;

public class PushSwitchStatementToFactoryDialog extends DialogWrapper {
    private final PsiClass myContainingClass;
    private final Project project;
    private final PsiSwitchStatement mySwitchStatement;
    private List<PsiMethod> factoryMethods;

    public PushSwitchStatementToFactoryDialog(PsiClass containingClass, PsiElement switchStatement, List<PsiMethod> factoryMethods) {
        super(switchStatement.getProject(), true);
        myContainingClass = containingClass;
        this.project = switchStatement.getProject();
        this.mySwitchStatement = (PsiSwitchStatement) switchStatement;
        this.factoryMethods = factoryMethods;

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
        for (PsiMethod method : factoryMethods) {
            pushSwitchStatementToFactory(method);
        }
        return true;
    }

    private void performNextStep() {
//        showReplaceConstructorsWithFactoryDialog(psiClass);
    }

    private void pushSwitchStatementToFactory(PsiMethod method) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiSwitchStatement switchStatement = getSwitchStatementCopy();
        PsiStatement[] statements = switchStatement.getBody().getStatements();

        PsiStatement originalReturnStatement = method.getBody().getStatements()[0];

        WriteCommandAction.runWriteCommandAction(method.getProject(), () -> {
            PsiStatement currentReturnStatement = (PsiStatement) originalReturnStatement.copy();
            PsiNewExpression currentNewExpression;
            boolean isFirstStatement = true;
            for (PsiStatement statement : statements) {

                if (statement instanceof PsiSwitchLabelStatement) {
                    isFirstStatement = true;
                    currentReturnStatement = (PsiStatement) originalReturnStatement.copy();
                    currentNewExpression = PsiTreeUtil.getChildOfType(currentReturnStatement, PsiNewExpression.class);
                    PsiSwitchLabelStatement switchLabelStatement = (PsiSwitchLabelStatement) statement;
                    String subClassName = generateSubClassNameFromSwitchLabel(myContainingClass, switchLabelStatement);
                    PsiNewExpression newExpression = replaceNewExpressionClass(factory, currentNewExpression, subClassName);



                    System.out.println("old new expression: " + currentNewExpression.getText());
                    System.out.println("new new expression: " + newExpression.getText());

                    currentNewExpression.replace(newExpression);
                } else {
                    if (isFirstStatement) {
                        isFirstStatement = false;
                        statement.replace(currentReturnStatement);
                    } else {
                        statement.delete();
                    }
                }
            }

            PsiCodeBlock switchStatementCodeBlock = createCodeBlockFromStatement(switchStatement);
            method.getBody().replace(switchStatementCodeBlock);
        });
    }

    private PsiSwitchStatement getSwitchStatementCopy() {
        LocateSwitchStatementVisitor visitor = new LocateSwitchStatementVisitor(mySwitchStatement.getText());
        myContainingClass.accept(visitor);
        return (PsiSwitchStatement) visitor.getSwitchStatement().copy();
    }

    private String generateSubClassNameFromSwitchLabel(PsiClass superClass, PsiSwitchLabelStatement label) {
        return superClass.getName() + (label.isDefaultCase() ? "Default" : label.getCaseValue().getText());
    }

    private PsiNewExpression replaceNewExpressionClass(PsiElementFactory factory, PsiNewExpression expression, String newClassName) {
        String newExpressionText = expression.getText();
        String oldClassName = expression.getClassReference().getReferenceName();
        newExpressionText = newExpressionText.replace(oldClassName, newClassName);
        return (PsiNewExpression) factory.createExpressionFromText(newExpressionText, null);
    }
}
