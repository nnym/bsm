package net.auoeke.bsm;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class BSMTest {
    @Test
    void test() {
        new ClassFile("out/production/classes/net/auoeke/bsm/BSM.class");
    }
}
