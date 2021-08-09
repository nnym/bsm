package net.auoeke.bsm;

public interface Constants {
    int MAGIC = 0xCAFEBABE;

    interface Access {
        int PUBLIC = 0x1;
        int PRIVATE = 0x2;
        int PROTECTED = 0x4;
        int STATIC = 0x8;
        int FINAL = 0x10;
        int INTERFACE = 0x200;
        int ABSTRACT = 0x400;
        int SYNTHETIC = 0x1000;
        int ANNOTATION = 0x2000;
        int ENUM = 0x4000;
    }

    interface Constant {
        int UTF8 = 1;
        int INTEGER = 3;
        int FLOAT = 4;
        int LONG = 5;
        int DOUBLE = 6;
        int CLASS = 7;
        int STRING = 8;
        int FIELDREF = 9;
        int METHODREF = 10;
        int INTERFACE_METHODREF = 11;
        int NAME_AND_TYPE = 12;
        int METHOD_HANDLE = 15;
        int METHOD_TYPE = 16;
        int DYNAMIC = 17;
        int INVOKE_DYNAMIC = 18;
        int MODULE = 19;
        int PACKAGE = 20;
    }
}
