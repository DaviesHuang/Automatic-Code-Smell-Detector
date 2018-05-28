package Actions;

import com.intellij.codeInsight.FileModificationService;
import com.intellij.codeInsight.generation.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.undo.UndoUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiUtil;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CreateConstructorsMatchingSuperAction {

    private final PsiClass myClass;

    public CreateConstructorsMatchingSuperAction(@NotNull PsiClass aClass) {
        myClass = aClass;
    }

    public void invoke(@NotNull final Project project, final Editor editor) {
        if (!FileModificationService.getInstance().prepareFileForWrite(myClass.getContainingFile())) return;
        PsiClass baseClass = myClass.getSuperClass();
        PsiSubstitutor substitutor = TypeConversionUtil.getSuperClassSubstitutor(baseClass, myClass, PsiSubstitutor.EMPTY);
        List<PsiMethodMember> baseConstructors = new ArrayList<>();
        PsiMethod[] baseConstrs = baseClass.getConstructors();
        for (PsiMethod baseConstr : baseConstrs) {
            if (PsiUtil.isAccessible(baseConstr, myClass, myClass)) {
                baseConstructors.add(new PsiMethodMember(baseConstr, substitutor));
            }
        }

        PsiMethodMember[] constructors = baseConstructors.toArray(new PsiMethodMember[0]);
        if (constructors.length == 0) {
            constructors = new PsiMethodMember[baseConstrs.length];
            for (int i = 0; i < baseConstrs.length; i++) {
                constructors[i] = new PsiMethodMember(baseConstrs[i], substitutor);
            }
        }

        boolean isCopyJavadoc = true;
        final PsiMethodMember[] constructors1 = constructors;
        ApplicationManager.getApplication().runWriteAction (
                () -> {
                    try {
                        if (myClass.getLBrace() == null) {
                            PsiClass psiClass = JavaPsiFacade.getInstance(myClass.getProject()).getElementFactory().createClass("X");
                            myClass.addRangeAfter(psiClass.getLBrace(), psiClass.getRBrace(), myClass.getLastChild());
                        }
                        JVMElementFactory factory = JVMElementFactories.getFactory(myClass.getLanguage(), project);
                        CodeStyleManager formatter = CodeStyleManager.getInstance(project);
                        PsiMethod derived = null;
                        for (PsiMethodMember candidate : constructors1) {
                            PsiMethod base = candidate.getElement();
                            derived = GenerateMembersUtil.substituteGenericMethod(base, candidate.getSubstitutor(), myClass);

                            if (!isCopyJavadoc) {
                                final PsiDocComment docComment = derived.getDocComment();
                                if (docComment != null) {
                                    docComment.delete();
                                }
                            }

                            final String targetClassName = myClass.getName();
                            derived.setName(targetClassName);

                            ConstructorBodyGenerator generator = ConstructorBodyGenerator.INSTANCE.forLanguage(derived.getLanguage());
                            if (generator != null) {
                                StringBuilder buffer = new StringBuilder();
                                generator.start(buffer, derived.getName(), PsiParameter.EMPTY_ARRAY);
                                generator.generateSuperCallIfNeeded(buffer, derived.getParameterList().getParameters());
                                generator.finish(buffer);
                                PsiMethod stub = factory.createMethodFromText(buffer.toString(), myClass);
                                derived.getBody().replace(stub.getBody());
                            }
                            derived = (PsiMethod)formatter.reformat(derived);
                            derived = (PsiMethod)JavaCodeStyleManager.getInstance(project).shortenClassReferences(derived);
                            PsiGenerationInfo<PsiMethod> info = OverrideImplementUtil.createGenerationInfo(derived);
                            info.insert(myClass, null, true);
                            derived = info.getPsiMember();
                        }
                        if (derived != null) {
                            editor.getCaretModel().moveToOffset(derived.getTextRange().getStartOffset());
                            editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
                        }
                    }
                    catch (IncorrectOperationException e) {
                        e.printStackTrace();
                    }
                    UndoUtil.markPsiFileForUndo(myClass.getContainingFile());
                }
        );
    }
}
