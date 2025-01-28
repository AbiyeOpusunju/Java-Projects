import javax.swing.*;
import java.awt.FlowLayout;

public class MainClass {
    public static void main(String[] args) {
        new MainClass();
    }

    public MainClass() {
        TurtleGraphics turtle = new TurtleGraphics();
        

        JFrame mainFrame = new JFrame("Turtle Graphics"); // Create a frame to display the turtle panel on
        mainFrame.setLayout(new FlowLayout()); // Set layout (not strictly necessary)
        mainFrame.add(turtle); // Add this object (turtle graphics panel) to the frame
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set close operation
        mainFrame.pack(); // Set the frame size
        mainFrame.setVisible(true); // Display the frame
        

        turtle.about(); // Call the OOPGraphics about method to display
    }
}