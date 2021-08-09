package net.auoeke.bsm;

public interface Constants {
    int MAGIC = 0xCAFEBABE;

    interface Constant {
        byte UTF8 = 1;
        byte INTEGER = 3;
        byte FLOAT = 4;
        byte LONG = 5;
        byte DOUBLE = 6;
        byte CLASS = 7;
        byte STRING = 8;
        byte FIELDREF = 9;
        byte METHODREF = 10;
        byte INTERFACE_METHODREF = 11;
        byte NAME_AND_TYPE = 12;
        byte METHOD_HANDLE = 15;
        byte METHOD_TYPE = 16;
        byte DYNAMIC = 17;
        byte INVOKE_DYNAMIC = 18;
        byte MODULE = 19;
        byte PACKAGE = 20;
    }
}
