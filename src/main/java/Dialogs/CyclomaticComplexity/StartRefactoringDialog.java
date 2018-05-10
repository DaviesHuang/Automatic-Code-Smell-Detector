package Dialogs.CyclomaticComplexity;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class StartRefactoringDialog extends DialogWrapper {

    private JPanel myMainPane;

    public StartRefactoringDialog(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        setTitle("Start Method Refactoring to Reduce Cyclomatic Complexity.");
        init();
    }

    protected void init() {
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return myMainPane;
    }

}
