package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class MethodTypeInfo implements CPInfo {
    public int descriptorIndex;

    @Override
    public byte tag() {
        return Constants.Constant.METHOD_TYPE;
    }
}
