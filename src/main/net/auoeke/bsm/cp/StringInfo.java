package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class StringInfo implements CPInfo {
    public int stringIndex;

    @Override
    public byte tag() {
        return Constants.Constant.STRING;
    }
}
