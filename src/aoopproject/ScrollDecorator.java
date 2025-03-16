
package aoopproject;

/*
  This class uses the Decorator design pattern to enhance the functionality of a JComponent.
 */
import javax.swing.*;

public class ScrollDecorator {
    /**
     * Adds a JScrollPane around the given JComponent.
     * This method decorates the given component with scrolling capability.
     */
    public static JScrollPane decorate(JComponent component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }
}

