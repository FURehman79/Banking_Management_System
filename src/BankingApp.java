import gui.LoginFrame;
import javax.swing.SwingUtilities;
import java.io.File;

public class BankingApp {
    
    public static void main(String[] args) {
        System.out.println("Banking Management System Starting...");
        
        // Create data directory
        createDataDirectory();
        
        // Start the application - NO LOOK AND FEEL SETTING
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Create and show login window directly
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                
                System.out.println("Banking Management System is ready!");
            }
        });
    }
    
    private static void createDataDirectory() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            boolean created = dataDir.mkdirs();
            System.out.println("Data directory created: " + created);
            System.out.println("Data directory path: " + dataDir.getAbsolutePath());
        }
    }
}