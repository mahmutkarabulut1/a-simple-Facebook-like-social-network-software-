/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aoopproject;

/**
 *
 * @author mahmut,umut,bengi,ece
 */
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        UserManager userManager = UserManager.getInstance();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame(userManager);
                mainFrame.setVisible(true);
            }
        });
    }
}
