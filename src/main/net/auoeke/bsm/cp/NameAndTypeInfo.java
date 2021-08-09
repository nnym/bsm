package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class NameAndTypeInfo implements CPInfo {
    public char nameIndex;
    public char descriptorIndex;

    @Override
    public byte tag() {
        return Constants.Constant.NAME_AND_TYPE;
    }
}
