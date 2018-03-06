package io.beanmapper.core.inspector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.beanmapper.exceptions.BeanGetFieldException;
import io.beanmapper.exceptions.BeanSetFieldException;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class PropertyDescriptorPropertyAccessorTest {

    @Mocked
    private PropertyDescriptor mockDescriptor;

    @Test
    public void getValueShouldThrowBeanGetFieldExceptionWhenFieldIsNotReadable() {
        PropertyDescriptorPropertyAccessor accessor = new PropertyDescriptorPropertyAccessor(mockDescriptor);

        new Expectations() {{
            mockDescriptor.getReadMethod();
            result = null;

            mockDescriptor.getName();
            result = "length";
        }};

        try {
            accessor.getValue("Instance");
            fail();
        } catch (BeanGetFieldException e) {
            assertEquals("Not possible to get field java.lang.String.length", e.getMessage());
        }
    }


    @Test
    public void getValueShouldWrapIllegalAccessExceptionInBeanGetFieldException()
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        PropertyDescriptorPropertyAccessor accessor = new PropertyDescriptorPropertyAccessor(mockDescriptor);

        Method readMethod = String.class.getDeclaredMethod("length");

        new Expectations() {{
            mockDescriptor.getReadMethod();
            result = readMethod;
            result = new IllegalAccessException();

            mockDescriptor.getName();
            result = "length";
        }};

        try {
            accessor.getValue("Instance");
            fail();
        } catch (BeanGetFieldException e) {
            assertEquals("Not possible to get field java.lang.String.length", e.getMessage());
        }
    }

    @Test
    public void setValueShouldThrowBeanGetFieldExceptionWhenFieldIsNotReadable() {
        PropertyDescriptorPropertyAccessor accessor = new PropertyDescriptorPropertyAccessor(mockDescriptor);

        new Expectations() {{
            mockDescriptor.getWriteMethod();
            result = null;

            mockDescriptor.getName();
            result = "length";
        }};

        try {
            accessor.setValue("Instance", "value");
            fail();
        } catch (BeanSetFieldException e) {
            assertEquals("Not possible to set field java.lang.String.length", e.getMessage());
        }
    }

    @Test
    public void setValueShouldWrapIllegalAccessExceptionInBeanSetFieldException()
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        PropertyDescriptorPropertyAccessor accessor = new PropertyDescriptorPropertyAccessor(mockDescriptor);

        Method writeMethod = String.class.getDeclaredMethod("length");

        new Expectations() {{
            mockDescriptor.getWriteMethod();
            result = writeMethod;
            result = new IllegalAccessException();

            mockDescriptor.getName();
            result = "length";
        }};

        try {
            accessor.setValue("Instance", "value");
            fail();
        } catch (BeanSetFieldException e) {
            assertEquals("Not possible to set field java.lang.String.length", e.getMessage());
        }
    }
}