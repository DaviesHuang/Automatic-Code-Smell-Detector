package Actions;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateBuilderFactory;
import com.intellij.codeInsight.template.TemplateBuilderImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;

public class CreateSubclassFromSuperAction {

    public static PsiClass createSubclass(final PsiClass psiClass, final PsiDirectory targetDirectory, final String className, boolean showChooser) {
        final Project project = psiClass.getProject();
        final PsiClass[] targetClass = new PsiClass[1];
        WriteCommandAction.writeCommandAction(project).withName(getTitle(psiClass)).withGroupId(getTitle(psiClass)).run(() -> {
            IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace();

            final PsiTypeParameterList oldTypeParameterList = psiClass.getTypeParameterList();

            try {
                targetClass[0] = JavaDirectoryService.getInstance().createClass(targetDirectory, className);
            }
            catch (final IncorrectOperationException e) {
                ApplicationManager.getApplication().invokeLater(
                        () -> Messages.showErrorDialog(project, CodeInsightBundle.message("intention.error.cannot.create.class.message", className) +
                                        "\n" + e.getLocalizedMessage(),
                                CodeInsightBundle.message("intention.error.cannot.create.class.title")));
                return;
            }
            startTemplate(oldTypeParameterList, project, psiClass, targetClass[0], false);
        });
        if (targetClass[0] == null) return null;
        if (!ApplicationManager.getApplication().isUnitTestMode() && !psiClass.hasTypeParameters()) {
            final Editor editor = CodeInsightUtil.positionCursorAtLBrace(project, targetClass[0].getContainingFile(), targetClass[0]);
            if (editor == null) return targetClass[0];
        }
        return targetClass[0];
    }

    private static String getTitle(PsiClass psiClass) {
        return psiClass.getName();
    }

    private static void startTemplate(PsiTypeParameterList oldTypeParameterList,
                                      final Project project,
                                      final PsiClass psiClass,
                                      final PsiClass targetClass,
                                      final boolean includeClassName) {
        final PsiElementFactory elementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiJavaCodeReferenceElement ref = elementFactory.createClassReferenceElement(psiClass);
        try {
            if (psiClass.isInterface()) {
                ref = (PsiJavaCodeReferenceElement)targetClass.getImplementsList().add(ref);
            }
            else {
                ref = (PsiJavaCodeReferenceElement)targetClass.getExtendsList().add(ref);
            }
            if (psiClass.hasTypeParameters() || includeClassName) {
                final Editor editor = CodeInsightUtil.positionCursorAtLBrace(project, targetClass.getContainingFile(), targetClass);
                final TemplateBuilderImpl templateBuilder = editor != null
                        ? (TemplateBuilderImpl)TemplateBuilderFactory.getInstance().createTemplateBuilder(targetClass) : null;

                if (includeClassName && templateBuilder != null) {
                    templateBuilder.replaceElement(targetClass.getNameIdentifier(), targetClass.getName());
                }

                if (oldTypeParameterList != null) {
                    for (PsiTypeParameter parameter : oldTypeParameterList.getTypeParameters()) {
                        final PsiElement param = ref.getParameterList().add(elementFactory.createTypeElement(elementFactory.createType(parameter)));
                        if (templateBuilder != null) {
                            templateBuilder.replaceElement(param, param.getText());
                        }
                    }
                }

                replaceTypeParamsList(targetClass, oldTypeParameterList);
                if (templateBuilder != null) {
                    templateBuilder.setEndVariableBefore(ref);
                    final Template template = templateBuilder.buildTemplate();
                    template.addEndVariable();

                    PsiClassOwner containingFile = (PsiClassOwner)targetClass.getContainingFile();
                    int idxInFile = ArrayUtil.find(containingFile.getClasses(), targetClass);

                    PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(editor.getDocument());

                    final TextRange textRange = targetClass.getTextRange();
                    final RangeMarker startClassOffset = editor.getDocument().createRangeMarker(textRange.getStartOffset(), textRange.getEndOffset());
                    editor.getDocument().deleteString(textRange.getStartOffset(), textRange.getEndOffset());
                }
            }
        }
        catch (IncorrectOperationException e) {
            e.printStackTrace();
        }
    }

    private static PsiElement replaceTypeParamsList(PsiClass psiClass, PsiTypeParameterList oldTypeParameterList) {
        final PsiTypeParameterList typeParameterList = psiClass.getTypeParameterList();
        assert typeParameterList != null;
        return typeParameterList.replace(oldTypeParameterList);
    }

}
