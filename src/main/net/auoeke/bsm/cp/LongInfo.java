package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class LongInfo implements CPInfo {
    public long value;

    @Override
    public byte tag() {
        return Constants.Constant.LONG;
    }
}
