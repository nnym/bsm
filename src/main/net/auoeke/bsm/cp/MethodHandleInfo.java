package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class MethodHandleInfo implements CPInfo {
    public byte referenceKind;
    public char referenceIndex;

    @Override
    public byte tag() {
        return Constants.Constant.METHOD_HANDLE;
    }
}
