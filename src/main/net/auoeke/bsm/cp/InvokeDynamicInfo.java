package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class InvokeDynamicInfo implements CPInfo {
    public int bsmAttrIndex;
    public int nameAndTypeIndex;

    @Override
    public byte tag() {
        return Constants.Constant.INVOKE_DYNAMIC;
    }
}
