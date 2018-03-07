package CodeInspections;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import org.junit.Assert;

import java.util.List;

public class ReturnPrivateMutableFieldTest extends LightPlatformCodeInsightFixtureTestCase {

    @Override
    public String getTestDataPath() {
        return "/home/yhuang/git/MyPlugin/TestCode/src/";
    }

    protected void doTest(String testName, String hint) throws Throwable {
        myFixture.configureByFile(testName + ".java");
        myFixture.enableInspections(new CodeInspectionProvider());
        List<HighlightInfo> highlightInfos = myFixture.doHighlighting();
        Assert.assertTrue(!highlightInfos.isEmpty());

        final IntentionAction action = myFixture.findSingleIntention(hint);

        Assert.assertNotNull(action);
        myFixture.launchAction(action);
        myFixture.checkResultByFile(testName + "Fixed.java");
    }

    public void test_Eqeq() throws Throwable {
        doTest("ReturnPrivateMutableMemberSample", "Use defensive copy to avoid return direct reference to private field");
    }
}
