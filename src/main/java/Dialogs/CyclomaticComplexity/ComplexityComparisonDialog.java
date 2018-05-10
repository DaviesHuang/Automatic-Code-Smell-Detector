package Dialogs.CyclomaticComplexity;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;

public class ComplexityComparisonDialog extends DialogWrapper {

    private JPanel myMainPanel;
    private JLabel originalComplexityLabel;
    private JLabel newComplexityLabel;

    public ComplexityComparisonDialog(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        setTitle("Cyclomatic Complexity Refactoring Outcome");
        init();
    }

    protected void init() {
        super.init();
    }

    public void setOriginalComplexity(int complexity) {
        originalComplexityLabel.setText(String.valueOf(complexity));
    }

    public void setNewComplexity(int complexity) {
        newComplexityLabel.setText(String.valueOf(complexity));
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return myMainPanel;
    }
}
