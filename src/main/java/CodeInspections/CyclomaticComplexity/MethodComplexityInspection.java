package CodeInspections.CyclomaticComplexity;

import Evaluation.ReplaceConditionalWithPolymorphism.InspectionTimeEvaluator;
import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethod;
import com.siyeh.ig.classmetrics.CyclomaticComplexityVisitor;
import org.jetbrains.annotations.NotNull;

import static Constants.Constants.CODE_SMELL;

public class MethodComplexityInspection extends BaseJavaLocalInspectionTool {

    private final LocalQuickFix quickFix = new MethodComplexityFix();
    private final int threshold = 5;
    private InspectionTimeEvaluator inspectionTimeEvaluator =
            new InspectionTimeEvaluator("Excessive cyclomatic complexity");

    @Override
    public void inspectionStarted(@NotNull LocalInspectionToolSession session, boolean isOnTheFly) {
        inspectionTimeEvaluator.start();
    }

    @Override
    public void inspectionFinished(@NotNull LocalInspectionToolSession session, @NotNull ProblemsHolder problemsHolder) {
        inspectionTimeEvaluator.end();
    }

    @NotNull
    public String getDisplayName() {
        return "Excessive cyclomatic complexity in method";
    }

    @NotNull
    public String getGroupDisplayName() {
        return CODE_SMELL;
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            final CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();

            @Override
            public void visitMethod(PsiMethod method) {
                visitor.reset();
                method.accept(visitor);
                final int complexity = visitor.getComplexity();
                if (complexity >= threshold) {
                    holder.registerProblem(method, getProblemDescription(complexity), quickFix);
                }
            }
        };
    }

    private String getProblemDescription(int complexity) {
        return getDisplayName() + " (Complexity: " + complexity + ")";
    }
}
