package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class FloatInfo implements CPInfo {
    public float value;

    @Override
    public byte tag() {
        return Constants.Constant.INTEGER;
    }
}
