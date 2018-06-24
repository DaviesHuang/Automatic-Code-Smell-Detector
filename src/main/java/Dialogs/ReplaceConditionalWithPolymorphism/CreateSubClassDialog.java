package Dialogs.ReplaceConditionalWithPolymorphism;

import Actions.CreateConstructorsMatchingSuperAction;
import Actions.CreateSubclassFromSuperAction;
import Dialogs.ChainedDialog;
import Utils.NameUtils;
import Visitors.LocateSwitchStatementVisitor;
import Visitors.PrivateFieldVisitor;
import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static DialogProviders.ReplaceConditionalWithPolymorphismDialogsProvider.showReplaceConstructorsWithFactoryDialog;
import static Utils.NameUtils.generateSubclassName;

public class CreateSubClassDialog extends DialogWrapper implements ChainedDialog {

    private JPanel myMainPanel;
    private Project project;
    private PsiElement element;
    private PsiClass psiClass;
    private PsiClass subClass;

    public CreateSubClassDialog(PsiClass psiClass, @Nullable PsiElement element, boolean canBeParent) {
        super(element.getProject(), canBeParent);
        this.project = element.getProject();
        this.element = element;
        this.psiClass = psiClass;
        setTitle("Create  Subclasses");
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
        okAction.putValue(Action.NAME, "Derive Subclasses");
        return okAction;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        boolean performed = performAction(this.element);
        if (performed) {
            ApplicationManager.getApplication().invokeLater(this::performNextStep);
        }
    }

    public boolean performAction(PsiElement element) {
        if (element instanceof PsiSwitchStatement) {
            String superClassName = psiClass.getName();
            PsiSwitchStatement switchStatement = (PsiSwitchStatement) element;
            PsiMethod method = getMethod();
            PsiStatement[] statements = switchStatement.getBody().getStatements();
            List<PsiSwitchLabelStatement> switchLabelStatements = new ArrayList<>();
            List<List<PsiStatement>> statementLists = new ArrayList<>();
            List<PsiStatement> currentBlock = new ArrayList<>();
            for (PsiStatement statement : statements) {
                if (statement instanceof PsiSwitchLabelStatement) {
                    switchLabelStatements.add((PsiSwitchLabelStatement) statement);
                    currentBlock = new ArrayList<>();
                    statementLists.add(currentBlock);
                } else {
                    currentBlock.add(statement);
                }
            }
            WriteCommandAction.runWriteCommandAction(project, () -> {
                for (int i = 0; i < switchLabelStatements.size(); i++) {
                    PsiSwitchLabelStatement switchLabelStatement = switchLabelStatements.get(i);
                    List<PsiStatement> currentStatements = statementLists.get(i);
                    String caseName = switchLabelStatement.isDefaultCase() ?
                            "Default" :
                            switchLabelStatement.getCaseValue().getText();
                    String subclassName = NameUtils.generateSubclassName(superClassName, caseName);
                    PsiDirectory directory = psiClass.getContainingFile().getContainingDirectory();
                    ApplicationManager.getApplication().invokeLater(() -> {
                        subClass = CreateSubclassFromSuperAction.createSubclass(psiClass, directory, subclassName, false);
                        if (method != null) {
                            addMethodToClass(method, subClass, currentStatements);
                        }
                    });
                }
            });
            return true;
        } else {
            System.out.println("It is not a switch statement");
            return false;
        }
    }



    public void performNextStep() {
        showReplaceConstructorsWithFactoryDialog(psiClass, element);
    }

    private PsiMethod getMethod() {
        LocateSwitchStatementVisitor visitor = new LocateSwitchStatementVisitor(element.getText());
        psiClass.accept(visitor);
        PsiSwitchStatement switchStatement = visitor.getSwitchStatement();
        if (switchStatement != null) {
            return PsiTreeUtil.getParentOfType(switchStatement, PsiMethod.class);
        }
        return null;
    }

    private void addMethodToClass(PsiMethod method, PsiClass aClass, List<PsiStatement> statements) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiMethod newMethod = factory.createMethodFromText(method.getText(), null);
        String codeBlockText = "";
        for (PsiStatement statement : statements) {
            updatePrivateAccessToProtected(statement);
            codeBlockText += statement.getText() + "\n";
        }
        codeBlockText = "{ " + codeBlockText + " }";

        PsiCodeBlock codeBlock = factory.createCodeBlockFromText(codeBlockText, null);
        PsiAnnotation annotation = factory.createAnnotationFromText("@Override", null);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            newMethod.getBody().replace(codeBlock);
            newMethod.addAfter(annotation, null);
            aClass.add(newMethod);
            addConstructorMethodFromSuper(aClass);
        });
    }

    private void updatePrivateAccessToProtected(PsiStatement statement) {
        PrivateFieldVisitor visitor = new PrivateFieldVisitor();
        statement.accept(visitor);
    }

    private void addConstructorMethodFromSuper(PsiClass subClass) {
        CreateConstructorsMatchingSuperAction createConstructorsMatchingSuperAction = new CreateConstructorsMatchingSuperAction(subClass);
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        createConstructorsMatchingSuperAction.invoke(subClass.getProject(), editor);
    }
}
