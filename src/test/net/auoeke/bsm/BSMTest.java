package net.auoeke.bsm;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class BSMTest {
    private static final double test = 1.4;

    @Test
    void test() {
        new ClassFile("out/test/classes/net/auoeke/bsm/BSMTest.class");
    }
}
