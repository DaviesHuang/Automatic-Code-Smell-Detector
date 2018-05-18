package Dialogs.CyclomaticComplexity;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class StartRefactoringDialog extends DialogWrapper {

    public static String DISMISSED = "DISMISSED";
    private JPanel myMainPane;
    private JCheckBox donTShowAgainCheckBox;

    public StartRefactoringDialog(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        setTitle("Start Method Refactoring to Reduce Cyclomatic Complexity.");
        init();
    }

    protected void init() {
        super.init();
        donTShowAgainCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getID() == ActionEvent.ACTION_PERFORMED) {
                    PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
                    boolean dismiss = propertiesComponent.getBoolean(StartRefactoringDialog.DISMISSED);
                    propertiesComponent.setValue(StartRefactoringDialog.DISMISSED, !dismiss);
                }
            }
        });
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return myMainPane;
    }

    @NotNull
    @Override
    protected Action getOKAction() {
        Action okAction = super.getOKAction();
        okAction.putValue(Action.NAME, "Start Refactoring");
        return okAction;
    }
}
