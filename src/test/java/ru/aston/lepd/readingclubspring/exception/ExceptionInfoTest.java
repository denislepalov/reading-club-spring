package ru.aston.lepd.readingclubspring.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionInfoTest {


    @Test
    void testConstructorAndGetter() {
        String errorMessage = "An error occurred";
        ExceptionInfo exceptionInfo = new ExceptionInfo(errorMessage);

        assertEquals(errorMessage, exceptionInfo.errorInfo());
    }



    @Test
    void testEqualsAndHashCode() {
        ExceptionInfo info1 = new ExceptionInfo("Error 1");
        ExceptionInfo info2 = new ExceptionInfo("Error 1");
        ExceptionInfo info3 = new ExceptionInfo("Error 2");

        assertEquals(info1, info2); // Проверяем, что два объекта с одинаковыми данными равны
        assertNotEquals(info1, info3); // Проверяем, что два объекта с разными данными не равны

        assertEquals(info1.hashCode(), info2.hashCode()); // Проверяем, что хэш-коды для одинаковых данных совпадают
        assertNotEquals(info1.hashCode(), info3.hashCode()); // Проверяем, что хэш-коды для разных данных различаются
    }



    @Test
    void testToString() {
        ExceptionInfo exceptionInfo = new ExceptionInfo("Critical Error");
        assertEquals("ExceptionInfo[errorInfo=Critical Error]", exceptionInfo.toString());
    }



    @Test
    void testNullErrorInfo() {
        ExceptionInfo exceptionInfo = new ExceptionInfo(null);
        assertNull(exceptionInfo.errorInfo());
    }



}