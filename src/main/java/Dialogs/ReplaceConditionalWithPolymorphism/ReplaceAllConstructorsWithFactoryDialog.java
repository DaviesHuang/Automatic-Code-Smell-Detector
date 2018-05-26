package Dialogs.ReplaceConditionalWithPolymorphism;

import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.help.HelpManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.replaceConstructorWithFactory.ReplaceConstructorWithFactoryHandler;
import com.intellij.refactoring.replaceConstructorWithFactory.ReplaceConstructorWithFactoryProcessor;
import com.intellij.refactoring.ui.NameSuggestionsField;
import com.intellij.refactoring.ui.RefactoringDialog;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.intellij.ui.JavaReferenceEditorUtil;
import com.intellij.ui.ReferenceEditorWithBrowseButton;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ReplaceAllConstructorsWithFactoryDialog extends RefactoringDialog {
    private NameSuggestionsField myNameField;
    private final ReferenceEditorWithBrowseButton myTfTargetClassName;
    private JComboBox myTargetClassNameCombo;
    private final PsiClass myContainingClass;
    private final PsiMethod[] myConstructors;
    private final boolean myIsInner;
    private NameSuggestionsField.DataChanged myNameChangedListener;

    public ReplaceAllConstructorsWithFactoryDialog(Project project, PsiMethod[] constructors, PsiClass containingClass) {
        super(project, true);
        myContainingClass = containingClass;
        myConstructors = constructors;
        myIsInner = myContainingClass.getContainingClass() != null
                && !myContainingClass.hasModifierProperty(PsiModifier.STATIC);

        setTitle(ReplaceConstructorWithFactoryHandler.REFACTORING_NAME);

        myTfTargetClassName = JavaReferenceEditorUtil.createReferenceEditorWithBrowseButton(new ChooseClassAction(), "", project, true);

        init();
    }

    protected void dispose() {
        myNameField.removeDataChangedListener(myNameChangedListener);
        super.dispose();
    }

    public String getName() {
        return myNameField.getEnteredName();
    }

    protected void doHelpAction() {
        HelpManager.getInstance().invokeHelp(HelpID.REPLACE_CONSTRUCTOR_WITH_FACTORY);
    }

    public JComponent getPreferredFocusedComponent() {
        return myNameField.getFocusableComponent();
    }

    public String getTargetClassName() {
        if (!myIsInner) {
            return myTfTargetClassName.getText();
        }
        else {
            return (String)myTargetClassNameCombo.getSelectedItem();
        }
    }

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
        panel.add(new JLabel("Step 4:"), gbc);

        //second line
        gbc.gridy++;
        panel.add(new JLabel("Replace constructor with factory method to instantiate the correct object"), gbc);

        //third line
        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel(RefactoringBundle.message("factory.method.name.label")), gbc);

        gbc.gridy++;
        gbc.weightx = 1.0;
        @NonNls final String[] nameSuggestions = new String[]{
                "create" + myContainingClass.getName(),
                "new" + myContainingClass.getName(),
                "getInstance",
                "newInstance"
        };
        myNameField = new NameSuggestionsField(nameSuggestions, getProject());
        myNameChangedListener = () -> validateButtons();
        myNameField.addDataChangedListener(myNameChangedListener);
        panel.add(myNameField.getComponent(), gbc);

        JPanel targetClassPanel = createTargetPanel();
        //panel.add(targetClassPanel, gbc);


        return panel;

    }

    private JPanel createTargetPanel() {
        JPanel targetClassPanel = new JPanel(new BorderLayout());
        if (!myIsInner) {
            JLabel label = new JLabel(RefactoringBundle.message("replace.constructor.with.factory.target.fq.name"));
            label.setLabelFor(myTfTargetClassName);
            targetClassPanel.add(label, BorderLayout.NORTH);
            targetClassPanel.add(myTfTargetClassName, BorderLayout.CENTER);
            myTfTargetClassName.setText(myContainingClass.getQualifiedName());
        }
        else {
            ArrayList<String> list = new ArrayList<>();
            PsiElement parent = myContainingClass;
            while (parent instanceof PsiClass) {
                list.add(((PsiClass)parent).getQualifiedName());
                parent = parent.getParent();
            }

            myTargetClassNameCombo = new JComboBox(ArrayUtil.toStringArray(list));
            JLabel label = new JLabel(RefactoringBundle.message("replace.constructor.with.factory.target.fq.name"));
            label.setLabelFor(myTargetClassNameCombo.getEditor().getEditorComponent());
            targetClassPanel.add(label, BorderLayout.NORTH);
            targetClassPanel.add(myTargetClassNameCombo, BorderLayout.CENTER);
        }
        return targetClassPanel;
    }

    protected String getDimensionServiceKey() {
        return "#com.intellij.refactoring.replaceConstructorWithFactory.ReplaceConstructorWithFactoryDialog";
    }

    private class ChooseClassAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            TreeClassChooser chooser = TreeClassChooserFactory.getInstance(getProject()).createProjectScopeChooser(
                    RefactoringBundle.message("choose.destination.class"));
            chooser.selectDirectory(myContainingClass.getContainingFile().getContainingDirectory());
            chooser.showDialog();
            PsiClass aClass = chooser.getSelected();
            if (aClass != null) {
                myTfTargetClassName.setText(aClass.getQualifiedName());
            }
        }
    }


    protected JComponent createCenterPanel() {
        return null;
    }

    protected void doAction() {
        final Project project = getProject();
        final PsiManager manager = PsiManager.getInstance(project);
        final String targetClassName = getTargetClassName();
        final PsiClass targetClass =
                JavaPsiFacade.getInstance(manager.getProject()).findClass(targetClassName, GlobalSearchScope.allScope(project));
        if (targetClass == null) {
            String message = RefactoringBundle.getCannotRefactorMessage(RefactoringBundle.message("class.0.not.found", targetClassName));
            CommonRefactoringUtil.showErrorMessage(ReplaceConstructorWithFactoryHandler.REFACTORING_NAME,
                    message, HelpID.REPLACE_CONSTRUCTOR_WITH_FACTORY, project);
            return;
        }

        if (!CommonRefactoringUtil.checkReadOnlyStatus(project, targetClass)) return;

        for (PsiMethod method : myConstructors) {
            invokeRefactoring(new ReplaceConstructorWithFactoryProcessor(project, method, myContainingClass, targetClass, getName()));
        }
    }


    @Override
    protected void canRun() throws ConfigurationException {
        final String name = myNameField.getEnteredName();
        final PsiNameHelper nameHelper = PsiNameHelper.getInstance(myContainingClass.getProject());
        if (!nameHelper.isIdentifier(name)) {
            throw new ConfigurationException("\'" + name + "\' is invalid factory method name");
        }
    }
}
