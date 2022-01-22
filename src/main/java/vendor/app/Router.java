package vendor.app;

import com.sun.istack.internal.Nullable;
import vendor.app.views.Views;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class Router {

    public void renderView(String url, @Nullable HashMap<String, String> params) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Views viewsClass = new Views();
        if (Views.viewsList.contains(url)) {
            Method viewMethod = viewsClass.getClass().getDeclaredMethod(url);
            viewMethod.invoke(viewsClass);
        } else {
            System.out.println("Invalid url.");
            System.exit(-1);
        }
    }
}
