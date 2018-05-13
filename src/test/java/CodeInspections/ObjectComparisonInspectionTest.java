package CodeInspections;

import CodeInspections.ObjectComparison.ObjectComparisonInspection;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.*;
import org.junit.Assert;

import java.util.List;

public class ObjectComparisonInspectionTest extends UsefulTestCase {

    final String dataPath = "/home/yhuang/git/MyPlugin/TestCode/src/";
    protected CodeInsightTestFixture fixture;

    public void setUp() throws Exception {
        super.setUp();
        final IdeaTestFixtureFactory fixtureFactory = IdeaTestFixtureFactory.getFixtureFactory();
        final TestFixtureBuilder<IdeaProjectTestFixture> testFixtureBuilder =
                fixtureFactory.createFixtureBuilder(getName());
        fixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(testFixtureBuilder.getFixture());
        fixture.setTestDataPath(dataPath);
        final JavaModuleFixtureBuilder builder = testFixtureBuilder.addModule(JavaModuleFixtureBuilder.class);

        builder.addContentRoot(fixture.getTempDirPath()).addSourceRoot("");
        builder.setMockJdkLevel(JavaModuleFixtureBuilder.MockJdkLevel.jdk15);
        fixture.setUp();
    }

    public void tearDown() throws Exception {
        fixture.tearDown();
        fixture = null;
    }

    protected void doTest(String testName, String hint) throws Throwable {
        fixture.configureByFile(testName + ".java");
        fixture.enableInspections(ObjectComparisonInspection.class);
        List<HighlightInfo> highlightInfos = fixture.doHighlighting();
        Assert.assertTrue(!highlightInfos.isEmpty());

        final IntentionAction action = fixture.findSingleIntention(hint);

        Assert.assertNotNull(action);
        fixture.launchAction(action);
        fixture.checkResultByFile(testName + "Fixed.java");
    }

    public void test_Eqeq() throws Throwable {
        doTest("ObjectComparisonSample", "Use equals()");
    }

}
