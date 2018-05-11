package Dialogs.CyclomaticComplexity;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class IdentifyComplexElementDialog extends DialogWrapper {

    private JPanel myMainPane;
    private JLabel elementLabel;

    public IdentifyComplexElementDialog(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
        setTitle("Extract Element into Method");
        init();
    }

    protected void init() {
        super.init();
    }

    public void setElement(PsiElement element) {
        setLabelContent(element.getText());
    }

    public void setElement(PsiElement[] elements) {
        StringBuilder contentBuilder = new StringBuilder();
        for (PsiElement element : elements) {
            contentBuilder.append(element.getText()).append(" ");
        }
        setLabelContent(contentBuilder.toString());
    }

    private void setLabelContent(String content) {
        if (content.length() > 80) {
            content = "<html><body>" +
                    content.substring(0, 40) +
                    "<br><br>...<br><br>" +
                    content.substring(content.length() - 40) +
                    "</body></html>";
        }
        elementLabel.setText(content);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return myMainPane;
    }
}
