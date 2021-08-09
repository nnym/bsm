package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class PackageInfo implements CPInfo {
    public char nameIndex;

    @Override
    public byte tag() {
        return Constants.Constant.PACKAGE;
    }
}
