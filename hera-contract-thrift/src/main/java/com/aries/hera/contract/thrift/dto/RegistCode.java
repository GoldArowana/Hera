/**
 * Autogenerated by Thrift Compiler (0.12.0)
 * <p>
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *
 * @generated
 */
package com.aries.hera.contract.thrift.dto;


@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.12.0)", date = "2019-06-19")
public enum RegistCode implements org.apache.thrift.TEnum {
    SUCCESS(1),
    NOT_CHANGE(0),
    FAILED(-1);

    private final int value;

    private RegistCode(int value) {
        this.value = value;
    }

    /**
     * Get the integer value of this enum value, as defined in the Thrift IDL.
     */
    public int getValue() {
        return value;
    }

    /**
     * Find a the enum type by its integer value, as defined in the Thrift IDL.
     *
     * @return null if the value is not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static RegistCode findByValue(int value) {
        switch (value) {
            case 1:
                return SUCCESS;
            case 0:
                return NOT_CHANGE;
            case -1:
                return FAILED;
            default:
                return null;
        }
    }
}
