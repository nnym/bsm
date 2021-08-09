package net.auoeke.bsm;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import net.auoeke.bsm.cp.CPInfo;
import net.auoeke.bsm.cp.ClassInfo;
import net.auoeke.bsm.cp.DoubleInfo;
import net.auoeke.bsm.cp.DynamicInfo;
import net.auoeke.bsm.cp.FieldrefInfo;
import net.auoeke.bsm.cp.FloatInfo;
import net.auoeke.bsm.cp.IntegerInfo;
import net.auoeke.bsm.cp.InterfaceMethodrefInfo;
import net.auoeke.bsm.cp.InvokeDynamicInfo;
import net.auoeke.bsm.cp.LongInfo;
import net.auoeke.bsm.cp.MethodHandleInfo;
import net.auoeke.bsm.cp.MethodTypeInfo;
import net.auoeke.bsm.cp.MethodrefInfo;
import net.auoeke.bsm.cp.ModuleInfo;
import net.auoeke.bsm.cp.NameAndTypeInfo;
import net.auoeke.bsm.cp.PackageInfo;
import net.auoeke.bsm.cp.StringInfo;
import net.auoeke.bsm.cp.UTF8Info;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ClassFile {
    public int minor;
    public int major;
    public CPInfo[] constantPool;

    public int access;
    public int name;
    public int superclass;

    public int interfaceCount;
    public int[] interfaces;

    public int fieldCount;
    public FieldInfo[] fields;

    public int methodCount;
    public MethodInfo[] methods;

    public int attributeCount;
    public AttributeInfo[] attributes;

    public ClassFile(URL url) {
        this(uri(url));
    }

    public ClassFile(URI uri) {
        this(Path.of(uri));
    }

    public ClassFile(String path) {
        this(Path.of(path));
    }

    public ClassFile(Path path) {
        this(read(path));
    }

    public ClassFile(byte[] klass) {
        this(new DataInputStream(new ByteArrayInputStream(klass)));
    }

    public ClassFile(DataInputStream klass) {
        try {
            int magic = klass.readInt();
            assert magic == Constants.MAGIC : "Expected magic number 0xCAFEBABE as the first 4 bytes; found 0x%X (%1$d).".formatted(magic);

            this.minor = klass.readUnsignedShort();
            this.major = klass.readUnsignedShort();

            int cpCount = klass.readUnsignedShort() - 1;
            this.constantPool = new CPInfo[cpCount];

            for (int index = 0; index < cpCount; index++) {
                int tag = klass.readUnsignedByte();

                this.constantPool[index] = switch (tag) {
                    case Constants.Constant.UTF8 -> {
                        var info = new UTF8Info();
                        info.value = klass.readUTF();

                        yield info;
                    }
                    case Constants.Constant.INTEGER -> {
                        var info = new IntegerInfo();
                        info.value = klass.readInt();

                        yield info;
                    }
                    case Constants.Constant.FLOAT -> {
                        var info = new FloatInfo();
                        info.value = klass.readFloat();

                        yield info;
                    }
                    case Constants.Constant.LONG -> {
                        var info = new LongInfo();
                        info.value = klass.readLong();
                        ++index;

                        yield info;
                    }
                    case Constants.Constant.DOUBLE -> {
                        var info = new DoubleInfo();
                        info.value = klass.readDouble();
                        ++index;

                        yield info;
                    }
                    case Constants.Constant.CLASS -> {
                        var info = new ClassInfo();
                        info.nameIndex = readIndex(klass);

                        yield info;
                    }
                    case Constants.Constant.STRING -> {
                        var info = new StringInfo();
                        info.stringIndex = readIndex(klass);

                        yield info;
                    }
                    case Constants.Constant.FIELDREF -> {
                        var info = new FieldrefInfo();
                        info.classIndex = readIndex(klass);
                        info.nameAndTypeIndex = readIndex(klass);

                        yield info;
                    }
                    case Constants.Constant.METHODREF -> {
                        var info = new MethodrefInfo();
                        info.classIndex = readIndex(klass);
                        info.nameAndTypeIndex = readIndex(klass);

                        yield info;
                    }
                    case Constants.Constant.INTERFACE_METHODREF -> {
                        var info = new InterfaceMethodrefInfo();
                        info.classIndex = readIndex(klass);
                        info.nameAndTypeIndex = readIndex(klass);

                        yield info;
                    }
                    case Constants.Constant.NAME_AND_TYPE -> {
                        var info = new NameAndTypeInfo();
                        info.nameIndex = klass.readUnsignedShort();
                        info.descriptorIndex = readIndex(klass);

                        yield info;
                    }
                    case Constants.Constant.METHOD_HANDLE -> {
                        var info = new MethodHandleInfo();
                        info.referenceKind = klass.read();
                        info.referenceIndex = readIndex(klass);

                        yield info;
                    }
                    case Constants.Constant.METHOD_TYPE -> {
                        var info = new MethodTypeInfo();
                        info.descriptorIndex = readIndex(klass);

                        yield info;
                    }
                    case Constants.Constant.DYNAMIC -> {
                        var info = new DynamicInfo();
                        info.bsmAttrIndex = readIndex(klass);
                        info.nameAndTypeIndex = readIndex(klass);

                        yield info;
                    }
                    case Constants.Constant.INVOKE_DYNAMIC -> {
                        var info = new InvokeDynamicInfo();
                        info.bsmAttrIndex = readIndex(klass);
                        info.nameAndTypeIndex = readIndex(klass);

                        yield info;
                    }
                    case Constants.Constant.MODULE -> {
                        var info = new ModuleInfo();
                        info.nameIndex = readIndex(klass);

                        yield info;
                    }
                    case Constants.Constant.PACKAGE -> {
                        var info = new PackageInfo();
                        info.nameIndex = readIndex(klass);

                        yield info;
                    }
                    default -> throw new ClassFormatError("%d is not a known constant pool tag; see net.auoeke.bsm.Constants.Constant.".formatted(tag));
                };
            }

            this.access = klass.readUnsignedShort();
            this.name = readIndex(klass);
            this.superclass = readIndex(klass);

            this.interfaceCount = klass.readUnsignedShort();
            this.interfaces = readIndexes(klass, this.interfaceCount);

            var fieldCount = this.fieldCount = klass.readUnsignedShort();
            var fields = this.fields = new FieldInfo[fieldCount];

            for (int index = 0; index < fieldCount; index++) {
                var field = fields[index] = new FieldInfo();
                field.access = klass.readUnsignedShort();
                field.nameIndex = readIndex(klass);
                field.descriptorIndex = readIndex(klass);
                var attributeCount = field.attributeCount = klass.readUnsignedShort();
                field.attributes = readAttributes(klass, attributeCount);
            }

            var methodCount = this.methodCount = klass.readUnsignedShort();
            var methods = this.methods = new MethodInfo[this.methodCount];

            for (int index = 0; index < methodCount; index++) {
                var method = methods[index] = new MethodInfo();
                method.access = klass.readUnsignedShort();
                method.nameIndex = readIndex(klass);
                method.descriptorIndex = readIndex(klass);
                var attributeCount = method.attributeCount = klass.readUnsignedShort();
                method.attributes = readAttributes(klass, attributeCount);
            }

            var attributeCount = this.attributeCount = klass.readUnsignedShort();
            this.attributes = readAttributes(klass, attributeCount);

            var bp = true;
        } catch (IOException exception) {
            throw BSM.rethrow(exception);
        }
    }

    private static byte[] read(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException exception) {
            throw BSM.rethrow(exception);
        }
    }

    private static URI uri(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException exception) {
            throw BSM.rethrow(exception);
        }
    }

    private static int readIndex(DataInputStream klass) throws IOException {
        return klass.readUnsignedShort() - 1;
    }

    private static int[] readIndexes(DataInputStream klass, int length) throws IOException {
        int[] indexes = new int[length];

        for (int index = 0; index < length; index++) {
            indexes[index] = klass.readUnsignedShort() - 1;
        }

        return indexes;
    }

    private static AttributeInfo[] readAttributes(DataInputStream klass, int count) throws IOException {
        var attributes = new AttributeInfo[count];

        for (int index = 0; index < count; index++) {
            var attribute = attributes[index] = new AttributeInfo();
            attribute.nameIndex = readIndex(klass);
            var length = attribute.length = klass.readInt();
            var info = attribute.info = new byte[length];
            klass.read(info);
        }

        return attributes;
    }
}
