package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class InterfaceMethodrefInfo implements CPInfo {
    public int classIndex;
    public int nameAndTypeIndex;

    @Override
    public byte tag() {
        return Constants.Constant.INTERFACE_METHODREF;
    }
}
