package top.dpdaidai.mn.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import top.dpdaidai.mn.test.v1.V1AllTest;
import top.dpdaidai.mn.test.v2.V2AllTests;
import top.dpdaidai.mn.test.v3.V3AllTests;
import top.dpdaidai.mn.test.v4.V4AllTests;
import top.dpdaidai.mn.test.v5.V5AllTests;

/**
 * @Author chenpantao
 * @Date 9/13/21 5:48 PM
 * @Version 1.0
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        V1AllTest.class,
        V2AllTests.class,
        V3AllTests.class,
        V4AllTests.class,
        V5AllTests.class})
public class AllTests {
}
