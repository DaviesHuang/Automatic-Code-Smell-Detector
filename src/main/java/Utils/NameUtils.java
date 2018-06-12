package Utils;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import org.apache.commons.lang.StringUtils;

public class NameUtils {

    public static String generateSubclassName(String superClassName, String caseName) {
        if (caseName.equals("Default")) {
            return caseName + superClassName;
        }

        if (StringUtils.isNumeric(caseName)) {
            return superClassName + caseName;
        }

        String convertedCaseName = caseName.replaceAll("\\s","_");
        if (CharMatcher.javaUpperCase().matchesAllOf(convertedCaseName)) {
            convertedCaseName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, convertedCaseName);
        } else {
            convertedCaseName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, convertedCaseName.toLowerCase());
        }

        return convertedCaseName;
    }
}
