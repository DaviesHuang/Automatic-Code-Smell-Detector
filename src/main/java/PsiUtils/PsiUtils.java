package PsiUtils;

import Visitors.LocateSwitchStatementVisitor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

public class PsiUtils {

    public static PsiMethod getMethodFromClass(PsiClass psiClass, String targetMethodText) {
        LocateSwitchStatementVisitor visitor = new LocateSwitchStatementVisitor(targetMethodText);
        psiClass.accept(visitor);
        PsiSwitchStatement switchStatement = visitor.getSwitchStatement();
        if (switchStatement != null) {
            return PsiTreeUtil.getParentOfType(switchStatement, PsiMethod.class);
        }
        return null;
    }

    public static void setAccessToProtectedIfPrivate(PsiModifierListOwner element) {
        if (element.hasModifierProperty(PsiModifier.PRIVATE)) {
            PsiModifierList modifierList = element.getModifierList();
            WriteCommandAction.runWriteCommandAction(element.getProject(), () -> {
                modifierList.setModifierProperty(PsiModifier.PRIVATE, false);
                modifierList.setModifierProperty(PsiModifier.PROTECTED, true);
            });
        }
    }
}
