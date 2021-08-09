package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class StringInfo implements CPInfo {
    public char stringIndex;

    @Override
    public byte tag() {
        return Constants.Constant.STRING;
    }
}
