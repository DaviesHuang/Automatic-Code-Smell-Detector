package Dialogs.ReplaceConditionalWithPolymorphism;

import DialogProviders.ReplaceConditionalWithPolymorphismDialogsProvider;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;

public class StartRefactoringDialog extends DialogWrapper {

    private JPanel myMainPanel;

    public StartRefactoringDialog(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        setTitle("Start Method Refactoring to Replace Conditional with Polymorphism.");
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
        okAction.putValue(Action.NAME, "Start Refactoring");
        return okAction;
    }
}
