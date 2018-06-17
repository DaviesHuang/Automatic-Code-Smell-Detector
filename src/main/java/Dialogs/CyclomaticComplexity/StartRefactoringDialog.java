package Dialogs.CyclomaticComplexity;

import Dialogs.ReplaceConditionalWithPolymorphism.ReplaceAllConstructorsWithFactoryDialog;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.replaceConstructorWithFactory.ReplaceConstructorWithFactoryHandler;
import com.intellij.refactoring.ui.NameSuggestionsField;
import com.intellij.refactoring.ui.RefactoringDialog;
import com.intellij.ui.JavaReferenceEditorUtil;
import com.intellij.util.ui.JBUI;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import static Constants.Constants.COMPLEXITY_THRESHOLD;
import static Constants.Constants.DEFAULT_THRESHOLD;
import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class StartRefactoringDialog extends DialogWrapper {

    public static String DISMISSED = "DISMISSED";
    private PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();

    public StartRefactoringDialog(Project project) {
        super(project, true);
        setTitle("Start Method Refactoring to Reduce Cyclomatic Complexity.");
        init();
    }

    protected void init() {
        super.init();

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
        panel.add(new JLabel("Cyclomatic complexity is a metric used to indicate the complexity of a program."), gbc);

        //second line
        gbc.gridy++;
        panel.add(new JLabel("Excessive cyclomatic complexity normally means the code is harder to understand."), gbc);

        //third line
        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Click Start Refactoring to begin step by step method refactoring."), gbc);


        //third line
        gbc.gridwidth = 1;
        gbc.gridy = gbc.gridy + 2;
        JLabel thresholdLabel = new JLabel("Current complexity threshold:");
        panel.add(thresholdLabel, gbc);


        //line 4
        gbc.gridwidth = 1;
        gbc.gridy++;
        int complexityThreshold = propertiesComponent.getInt(COMPLEXITY_THRESHOLD, DEFAULT_THRESHOLD);
        JTextField textField = new JTextField(String.valueOf(complexityThreshold));
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onChange();
            }

            public void changedUpdate(DocumentEvent e) {
                onChange();
            }

            void onChange() {
                if (StringUtils.isNumeric(textField.getText())) {
                    int newThreshold = Integer.valueOf(textField.getText());
                    propertiesComponent.setValue(COMPLEXITY_THRESHOLD, newThreshold, DEFAULT_THRESHOLD);
                }
            }
        });
        panel.add(textField, gbc);


        //line 5
        gbc.gridwidth = 1;
        gbc.gridy++;
        JCheckBox checkBox = new JCheckBox("Don't show again");
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getID() == ActionEvent.ACTION_PERFORMED) {
                    boolean dismiss = propertiesComponent.getBoolean(StartRefactoringDialog.DISMISSED);
                    propertiesComponent.setValue(StartRefactoringDialog.DISMISSED, !dismiss);
                    System.out.print("clicked");
                }
            }
        });
        panel.add(checkBox, gbc);

        return panel;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return null;
    }

    @NotNull
    @Override
    protected Action getOKAction() {
        Action okAction = super.getOKAction();
        okAction.putValue(Action.NAME, "Start Refactoring");
        return okAction;
    }

}
