package Dialogs.ReplaceConditionalWithPolymorphism;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class StartRefactoringDialog extends DialogWrapper {

    private JPanel myMainPanel;

    public StartRefactoringDialog(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        setTitle("Replace Conditional with Polymorphism");
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
