package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class PackageInfo implements CPInfo {
    public int nameIndex;

    @Override
    public byte tag() {
        return Constants.Constant.PACKAGE;
    }
}
