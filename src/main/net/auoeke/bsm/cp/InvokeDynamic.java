package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class InvokeDynamic implements CPInfo {
    public char bsmAttrIndex;
    public char nameAndTypeIndex;

    @Override
    public byte tag() {
        return Constants.Constant.INVOKE_DYNAMIC;
    }
}
