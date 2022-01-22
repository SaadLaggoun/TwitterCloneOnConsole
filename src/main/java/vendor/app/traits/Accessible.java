package vendor.app.traits;

import java.lang.reflect.Field;
import java.util.Arrays;

public interface Accessible {
    String[] fillable = {};
    String[] accessible = {};

    default boolean set(String name, String value) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        if (this.fillable.length == 0) return false;

        if (doesObjectContainField(this, name)) {
            Class c = Class.forName("vendor.app.traits.Accessible");
            Field field = c.getField(name);
            field.set(this, value);
            return true;
        }

        return false;
    }

    default Object get(String name) throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException {
        if (this.accessible.length == 0) return null;

        if (doesObjectContainField(this, name)) {
            Class c = Class.forName("vendor.app.traits.Accessible");
            Field field = c.getField(name);
            return field.get(this);
        } else {
            return null;
        }
    }

    static boolean doesObjectContainField(Object object, String fieldName) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .anyMatch(f -> f.getName().equals(fieldName));
    }
}
