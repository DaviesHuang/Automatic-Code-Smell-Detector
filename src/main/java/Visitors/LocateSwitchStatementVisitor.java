package Visitors;

import com.intellij.psi.JavaRecursiveElementWalkingVisitor;
import com.intellij.psi.PsiSwitchStatement;

public class LocateSwitchStatementVisitor extends JavaRecursiveElementWalkingVisitor {

    private PsiSwitchStatement switchStatement;
    private String targetText;

    public LocateSwitchStatementVisitor(String targetText) {
        this.targetText = targetText;
    }

    @Override
    public void visitSwitchStatement(PsiSwitchStatement statement) {
        super.visitStatement(statement);
        if (statement.getText().equals(targetText)) {
            this.switchStatement = statement;
        }
    }

    public PsiSwitchStatement getSwitchStatement() {
        return switchStatement;
    }
}
