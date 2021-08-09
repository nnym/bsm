package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class FieldrefInfo implements CPInfo {
    public char classIndex;
    public char nameAndTypeIndex;

    @Override
    public byte tag() {
        return Constants.Constant.FIELDREF;
    }
}
