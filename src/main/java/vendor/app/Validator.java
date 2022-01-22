package vendor.app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Validator {
    private HashMap<String, String> errors = new HashMap<>();

    public AtomicBoolean validate(HashMap<String, String> data, HashMap<String, String> rules) {
        AtomicBoolean valid = new AtomicBoolean(true);

        rules.forEach((item, ruleset) -> {
            String[] rulesetTmp = ruleset.split("(\\|)");

            for (String rule : rulesetTmp) {
                int colon = rule.indexOf(':');

                String params = "";
                if (colon != -1) {
                    params = rule.substring(colon + 1);
                    rule = rule.substring(0, colon);
                }

                String methodName = "validate" + (rule.substring(0, 1).toUpperCase() + rule.substring(1));
                String value;

                if (data.get(item) != null) {
                    value = data.get(item);
                } else {
                    value = null;
                }

                Validator valida = new Validator();

                Method method = null;
                boolean methExist = true;

                /*
                 * Getting Methods */
                Class myClass = null;
                try {
                    myClass = Class.forName("vendor.app.Validator");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    method = myClass.getDeclaredMethod(methodName, String.class, String.class, String.class);
                } catch (SecurityException e) {
                    System.out.println("Security exception.");
                } catch (NoSuchMethodException e) {
                    methExist = false;
                    valid.set(false);
                }
                /*
                 * End of getting methods */

                if (methExist) {
                    try {
                        if ((boolean) method.invoke(this, item, value, params)) {
                            method.invoke(this, item, value, params);
                        } else {
                            valid.set(false);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("IllegalArgumentException");
                    } catch (IllegalAccessException e) {
                        System.out.println("IllegalAccessException");
                    } catch (InvocationTargetException e) {
                        System.out.println("InvocationTargetException");
                    }
                }
            }
        });

        return valid;
    }

    public HashMap<String, String> getErrors() {
        return this.errors;
    }

    private boolean validateRequired(String item, String value, String params) {
        if (value.isEmpty()) {
            this.errors.put(item, "The "+ item + " field is required.");
            return false;
        }

        return true;
    }

    private boolean validateEmail(String item, String value, String params) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(value);

        if(!mat.matches()) {
            this.errors.put(item, "The " + item + " field should be a valid email address.");
            return false;
        }

        return true;
    }

    private boolean validateMin(String item, String value, String params) {
        if (value.length() < parseInt(params)) {
            this.errors.put(item, "The " + item + " field should have minimum length of " + params + ".");
            return false;
        }

        return true;
    }
}
