package Dialogs.ReplaceConditionalWithPolymorphism;

import Visitors.LocateSwitchStatementVisitor;
import com.intellij.codeInsight.generation.JavaOverrideMethodsHandler;
import com.intellij.codeInsight.generation.OverrideImplementUtil;
import com.intellij.codeInsight.generation.PsiMethodMember;
import com.intellij.codeInsight.intention.impl.CreateSubclassAction;
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
        okAction.putValue(Action.NAME, "Create Abstract Class");
        return okAction;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        boolean performed = performAction(this.element);
        if (performed) {
            performNextStep();
        }
    }

    private boolean performAction(PsiElement element) {
        if (element instanceof PsiSwitchStatement) {
            String classPrefix = psiClass.getName();
            PsiSwitchStatement switchStatement = (PsiSwitchStatement) element;
            PsiMethod method = getMethod();
            PsiStatement[] statements = switchStatement.getBody().getStatements();
            for (PsiStatement statement : statements) {
                if (statement instanceof PsiSwitchLabelStatement) {
                    PsiSwitchLabelStatement switchLabelStatement = (PsiSwitchLabelStatement) statement;
                    String subClassName = classPrefix + (switchLabelStatement.isDefaultCase() ?
                            "Default" :
                            switchLabelStatement.getCaseValue().getText());
                    ApplicationManager.getApplication().invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            PsiClass subClass = CreateSubclassAction.createSubclass(psiClass, psiClass.getContainingFile().getContainingDirectory(), subClassName);
                            if (method != null) {
//                                Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
//                                Collection<PsiMethodMember> methodMembers = new ArrayList<PsiMethodMember>();
//                                methodMembers.add(new PsiMethodMember(method));
//                                OverrideImplementUtil.overrideOrImplementMethodsInRightPlace(editor, subClass, methodMembers, false);
                                addMethodToClass(method, subClass);
                            }
                        }
                    }, ModalityState.NON_MODAL);
                }
            }
            return true;
        } else {
            System.out.println("It is not a switch statement");
            return false;
        }
    }

    private void performNextStep() {

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

    private void addMethodToClass(PsiMethod method, PsiClass psiClass) {
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiMethod newMethod = factory.createMethodFromText(method.getText(), null);
        PsiAnnotation annotation = factory.createAnnotationFromText("@Override", null);
        WriteCommandAction.runWriteCommandAction(method.getProject(), new Runnable() {
            @Override
            public void run() {
                newMethod.addAfter(annotation, null);
                psiClass.add(newMethod);
            }
        });
    }
}
