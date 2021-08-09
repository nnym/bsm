package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class DoubleInfo implements CPInfo {
    public double value;

    @Override
    public byte tag() {
        return Constants.Constant.DOUBLE;
    }
}
