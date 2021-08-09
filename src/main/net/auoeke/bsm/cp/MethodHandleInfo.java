package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class MethodHandleInfo implements CPInfo {
    public int referenceKind;
    public int referenceIndex;

    @Override
    public byte tag() {
        return Constants.Constant.METHOD_HANDLE;
    }
}
