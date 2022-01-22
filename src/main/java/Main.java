import vendor.app.Router;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Router router = new Router();
        router.renderView("index", null);
    }
}
