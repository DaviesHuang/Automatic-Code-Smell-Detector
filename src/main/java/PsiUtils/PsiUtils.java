package PsiUtils;

import Visitors.LocateSwitchStatementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiSwitchStatement;
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
}
