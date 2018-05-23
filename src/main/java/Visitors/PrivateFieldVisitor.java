package Visitors;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;

import static PsiUtils.PsiUtils.setAccessToProtectedIfPrivate;

public class PrivateFieldVisitor extends JavaRecursiveElementWalkingVisitor {

    @Override
    public void visitReferenceElement(PsiJavaCodeReferenceElement reference) {
        super.visitElement(reference);
        System.out.println("Reference element: " + reference.getText());
        PsiElement originalElement = reference.resolve();
        if (originalElement instanceof PsiField) {
            System.out.println("it is a field");
            PsiField field = (PsiField) originalElement;
            setAccessToProtectedIfPrivate(field);
        } else if (originalElement instanceof PsiMethod) {
            System.out.println("it is a method");
            PsiMethod method = (PsiMethod) originalElement;
            setAccessToProtectedIfPrivate(method);
        }
    }

}
