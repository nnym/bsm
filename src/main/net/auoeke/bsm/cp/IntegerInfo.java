package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class IntegerInfo implements CPInfo {
    public int value;

    @Override
    public byte tag() {
        return Constants.Constant.INTEGER;
    }
}
