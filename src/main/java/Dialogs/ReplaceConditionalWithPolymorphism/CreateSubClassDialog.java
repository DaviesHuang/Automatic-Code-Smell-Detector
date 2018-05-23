package Dialogs.ReplaceConditionalWithPolymorphism;

import DialogProviders.ReplaceConditionalWithPolymorphismDialogsProvider;
import Visitors.LocateSwitchStatementVisitor;
import Visitors.PrivateFieldVisitor;
import a.i.A;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateConstructorFromSuperFix;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateConstructorMatchingSuperFix;
import com.intellij.codeInsight.generation.ConstructorBodyGenerator;
import com.intellij.codeInsight.generation.JavaOverrideMethodsHandler;
import com.intellij.codeInsight.generation.OverrideImplementUtil;
import com.intellij.codeInsight.generation.PsiMethodMember;
import com.intellij.codeInsight.intention.impl.CreateSubclassAction;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import com.intellij.psi.search.ProjectScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiClassUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.extractMethod.ExtractMethodHandler;
import com.intellij.refactoring.extractMethod.ExtractMethodProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CreateSubClassDialog extends DialogWrapper {

    private JPanel myMainPanel;
    private Project project;
    private PsiElement element;
    private PsiClass psiClass;

    public CreateSubClassDialog(PsiClass psiClass, @Nullable PsiElement element, boolean canBeParent) {
        super(element.getProject(), canBeParent);
        this.project = element.getProject();
        this.element = element;
        this.psiClass = psiClass;
        setTitle("Create Abstract Class");
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

    private boolean performAction(PsiElement element) {
        if (element instanceof PsiSwitchStatement) {
            String classPrefix = psiClass.getName();
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

            for (int i = 0; i < switchLabelStatements.size(); i++) {
                PsiSwitchLabelStatement switchLabelStatement = switchLabelStatements.get(i);
                List<PsiStatement> currentStatements = statementLists.get(i);
                String subClassName = classPrefix + (switchLabelStatement.isDefaultCase() ?
                        "Default" :
                        switchLabelStatement.getCaseValue().getText());
                PsiDirectory directory = psiClass.getContainingFile().getContainingDirectory();
                ApplicationManager.getApplication().invokeLater(() -> {
                    PsiClass subClass = CreateSubclassAction.createSubclass(psiClass, directory, subClassName);
                    if (method != null) {
                        addMethodToClass(method, subClass, currentStatements);
                    }
                });
            }
            return true;
        } else {
            System.out.println("It is not a switch statement");
            return false;
        }
    }

    private void performNextStep() {
        ReplaceConditionalWithPolymorphismDialogsProvider.showCreateFactoryMethodDialog(psiClass, element);
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
        WriteCommandAction.runWriteCommandAction(method.getProject(), () -> {
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
        CreateConstructorMatchingSuperFix createConstructorMatchingSuperFix = new CreateConstructorMatchingSuperFix(subClass);
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        createConstructorMatchingSuperFix.invoke(subClass.getProject(), editor, subClass.getContainingFile());
    }
}
