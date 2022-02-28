package GroceryShopBillingSystem;

import java.awt.*;

public class Constraint {

    public static final int LEFT = GridBagConstraints.WEST;
    public static final int RIGHT = GridBagConstraints.EAST;

    public static GridBagConstraints setPosition(int x, int y) {
        return new GridBagConstraints(x, y, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
    }

    public static GridBagConstraints setPosition(int x, int y, int xwidth, int ywidth) {
        return new GridBagConstraints(x, y, xwidth, ywidth, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
    }

    public static GridBagConstraints setPosition(int x, int y, int xwidth, int ywidth, int anchor) {
        return new GridBagConstraints(x, y, xwidth, ywidth, 0, 0, anchor, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
    }

    public static GridBagConstraints setPosition(int x, int y, int anchor) {
        return new GridBagConstraints(x, y, 1, 1, 0, 0, anchor, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 5, 5);
    }

    public static boolean isValidPassword(String password) {
        boolean hasUpperCase = false, hasLowerCase = false, hasNumber = false;
        int i = 0;

        while (!(hasUpperCase && hasLowerCase && hasNumber) && i < password.length()) {
            if (Character.isUpperCase(password.charAt(i))) {
                hasUpperCase = true;
            }

            if (Character.isLowerCase(password.charAt(i))) {
                hasLowerCase = true;
            }

            if (Character.isDigit(password.charAt(i))) {
                hasNumber = true;
            }

            i++;
        }

        return (hasUpperCase && hasLowerCase && hasNumber);
    }
}

