package net.auoeke.bsm;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
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
import net.auoeke.bsm.cp.InvokeDynamic;
import net.auoeke.bsm.cp.LongInfo;
import net.auoeke.bsm.cp.MethodHandleInfo;
import net.auoeke.bsm.cp.MethodTypeInfo;
import net.auoeke.bsm.cp.MethodrefInfo;
import net.auoeke.bsm.cp.ModuleInfo;
import net.auoeke.bsm.cp.NameAndTypeInfo;
import net.auoeke.bsm.cp.PackageInfo;
import net.auoeke.bsm.cp.StringInfo;
import net.auoeke.bsm.cp.UTF8Info;

public class ClassFile {
    public char minor;
    public char major;
    public CPInfo[] constantPool;

    public char access;
    public char name;
    public char superclass;

    public char interfaceCount;
    public char[] interfaces;

    public char fieldCount;
    public FieldInfo[] fields;

    public char methodCount;
    public MethodInfo[] methods;

    public char attributeCount;
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
        this(ByteBuffer.wrap(klass));
    }

    public ClassFile(ByteBuffer klass) {
        int magic = klass.getInt();
        assert magic == Constants.MAGIC : "Expected magic number 0xCAFEBABE as the first 4 bytes; found 0x%X (%1$d).".formatted(magic);

        this.minor = klass.getChar();
        this.major = klass.getChar();

        int cpCount = klass.getChar();
        this.constantPool = new CPInfo[cpCount - 1];

        for (int index = 1; index < cpCount; index++) {
            byte tag = klass.get();

            this.constantPool[index - 1] = switch (tag) {
                case Constants.Constant.UTF8 -> {
                    var info = new UTF8Info();
                    info.length = klass.getChar();
                    info.bytes = new byte[info.length];
                    klass.get(info.bytes);

                    yield info;
                }
                case Constants.Constant.INTEGER -> {
                    var info = new IntegerInfo();
                    info.value = klass.getInt();

                    yield info;
                }
                case Constants.Constant.FLOAT -> {
                    var info = new FloatInfo();
                    info.value = klass.getFloat();

                    yield info;
                }
                case Constants.Constant.LONG -> {
                    var info = new LongInfo();
                    info.value = klass.getLong();
                    ++index;
                    klass.get();
                    klass.getLong();

                    yield info;
                }
                case Constants.Constant.DOUBLE -> {
                    var info = new DoubleInfo();
                    info.value = klass.getDouble();
                    ++index;
                    klass.get();
                    klass.getDouble();

                    yield info;
                }
                case Constants.Constant.CLASS -> {
                    var info = new ClassInfo();
                    info.nameIndex = klass.getChar();

                    yield info;
                }
                case Constants.Constant.STRING -> {
                    var info = new StringInfo();
                    info.stringIndex = klass.getChar();

                    yield info;
                }
                case Constants.Constant.FIELDREF -> {
                    var info = new FieldrefInfo();
                    info.classIndex = klass.getChar();
                    info.nameAndTypeIndex = klass.getChar();

                    yield info;
                }
                case Constants.Constant.METHODREF -> {
                    var info = new MethodrefInfo();
                    info.classIndex = klass.getChar();
                    info.nameAndTypeIndex = klass.getChar();

                    yield info;
                }
                case Constants.Constant.INTERFACE_METHODREF -> {
                    var info = new InterfaceMethodrefInfo();
                    info.classIndex = klass.getChar();
                    info.nameAndTypeIndex = klass.getChar();

                    yield info;
                }
                case Constants.Constant.NAME_AND_TYPE -> {
                    var info = new NameAndTypeInfo();
                    info.nameIndex = klass.getChar();
                    info.descriptorIndex = klass.getChar();

                    yield info;
                }
                case Constants.Constant.METHOD_HANDLE -> {
                    var info = new MethodHandleInfo();
                    info.referenceKind = klass.get();
                    info.referenceIndex = klass.getChar();

                    yield info;
                }
                case Constants.Constant.METHOD_TYPE -> {
                    var info = new MethodTypeInfo();
                    info.descriptorIndex = klass.getChar();

                    yield info;
                }
                case Constants.Constant.DYNAMIC -> {
                    var info = new DynamicInfo();
                    info.bsmAttrIndex = klass.getChar();
                    info.nameAndTypeIndex = klass.getChar();

                    yield info;
                }
                case Constants.Constant.INVOKE_DYNAMIC -> {
                    var info = new InvokeDynamic();
                    info.bsmAttrIndex = klass.getChar();
                    info.nameAndTypeIndex = klass.getChar();

                    yield info;
                }
                case Constants.Constant.MODULE -> {
                    var info = new ModuleInfo();
                    info.nameIndex = klass.getChar();

                    yield info;
                }
                case Constants.Constant.PACKAGE -> {
                    var info = new PackageInfo();
                    info.nameIndex = klass.getChar();

                    yield info;
                }
                default -> throw new ClassFormatError("%d is not a known constant pool tag; see net.auoeke.bsm.Constants.Constant.".formatted(tag));
            };
        }

        this.access = klass.getChar();
        this.name = klass.getChar();
        this.superclass = klass.getChar();

        this.interfaceCount = klass.getChar();
        this.interfaces = read(klass, new char[this.interfaceCount]);

        var fieldCount = this.fieldCount = klass.getChar();
        var fields = this.fields = new FieldInfo[fieldCount];

        for (int index = 0; index < fieldCount; index++) {
            var field = fields[index] = new FieldInfo();
            field.access = klass.getChar();
            field.nameIndex = klass.getChar();
            field.descriptorIndex = klass.getChar();
            var attributeCount = field.attributeCount = klass.getChar();
            field.attributes = readAttributes(klass, attributeCount);
        }

        var methodCount = this.methodCount = klass.getChar();
        var methods = this.methods = new MethodInfo[this.methodCount];

        for (int index = 0; index < methodCount; index++) {
            var method = methods[index] = new MethodInfo();
            method.access = klass.getChar();
            method.nameIndex = klass.getChar();
            method.descriptorIndex = klass.getChar();
            var attributeCount = method.attributeCount = klass.getChar();
            method.attributes = readAttributes(klass, attributeCount);
        }

        var attributeCount = this.attributeCount = klass.getChar();
        this.attributes = readAttributes(klass, attributeCount);

        var bp = true;
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

    private static char[] read(ByteBuffer buffer, char[] array) {
        for (int index = 0, length = array.length; index < length; index++) {
            array[index] = buffer.getChar();
        }

        return array;
    }

    private static AttributeInfo[] readAttributes(ByteBuffer buffer, char count) {
        var attributes = new AttributeInfo[count];

        for (int index = 0; index < count; index++) {
            var attribute = attributes[index] = new AttributeInfo();
            attribute.nameIndex = buffer.getChar();
            var length = attribute.length = buffer.getInt();
            var info = attribute.info = new byte[length];
            buffer.get(info);
        }

        return attributes;
    }
}
