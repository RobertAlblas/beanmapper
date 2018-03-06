package io.beanmapper.core.inspector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import io.beanmapper.exceptions.BeanGetFieldException;
import io.beanmapper.exceptions.BeanSetFieldException;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class FieldPropertyAccessorTest {

    @Mocked
    private Field field;

    @Test
    public void getValueShouldWrapIllegalAccessExceptionInBeanGetFieldException()
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        new Expectations() {{
            field.getName();
            result = "length";

            field.get("Instance");
            result = new IllegalAccessException();
        }};

        FieldPropertyAccessor accessor = new FieldPropertyAccessor(field);

        try {
            accessor.getValue("Instance");
            fail();
        } catch (BeanGetFieldException e) {
            assertEquals("Not possible to get field java.lang.String.length", e.getMessage());
        }
    }

    @Test
    public void setValueShouldWrapIllegalAccessExceptionInBeanSetFieldException()
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        new Expectations() {{
            field.getName();
            result = "length";

            field.set("Instance", "value");
            result = new IllegalAccessException();
        }};

        FieldPropertyAccessor accessor = new FieldPropertyAccessor(field);

        try {
            accessor.setValue("Instance", "value");
            fail();
        } catch (BeanSetFieldException e) {
            assertEquals("Not possible to set field java.lang.String.length", e.getMessage());
        }
    }
}
