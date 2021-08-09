package net.auoeke.bsm.cp;

import net.auoeke.bsm.Constants;

public class UTF8Info implements CPInfo {
    public String value;

    @Override
    public byte tag() {
        return Constants.Constant.UTF8;
    }
}
