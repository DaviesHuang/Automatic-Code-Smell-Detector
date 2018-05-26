package Visitors;

import com.intellij.psi.*;

import static PsiUtils.PsiUtils.setAccessToProtectedIfPrivate;

public class PrivateFieldVisitor extends JavaRecursiveElementWalkingVisitor {

    @Override
    public void visitReferenceElement(PsiJavaCodeReferenceElement reference) {
        super.visitElement(reference);
        PsiElement originalElement = reference.resolve();
        if (originalElement instanceof PsiField) {
            PsiField field = (PsiField) originalElement;
            setAccessToProtectedIfPrivate(field);
        } else if (originalElement instanceof PsiMethod) {
            PsiMethod method = (PsiMethod) originalElement;
            setAccessToProtectedIfPrivate(method);
        }
    }

}
