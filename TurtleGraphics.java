import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import uk.ac.leedsbeckett.oop.OOPGraphics;

public class TurtleGraphics extends OOPGraphics{

	private Image dogImage;
	private int penWidth = 1;
	private final int MAX_Y = 400,MIN_Y = 0, MAX_X = 800, MIN_X = 0;
	
	private boolean changes = true;
	private List<String> commandHistory = new ArrayList<>();
	private final Color DEFAULT_PEN_COLOR = Color.BLACK; // Default pen color
	private Color penColor; // Variable to store the current pen color/
    private JFileChooser fileChooser;

    public TurtleGraphics() {

        penColor = DEFAULT_PEN_COLOR;
        fileChooser = new JFileChooser();
        commandHistory = new ArrayList<>();
        fileChooser.setDialogTitle("please, Select a File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
    }
    public void processCommand(String command) {

        if (command.isEmpty()){
            displayMessage("You didnt enter a command, please do :)");
            return; // No command provided
        }
        commandHistory.add(command);
        String[] parts = command.split(" ");

        String action = parts[0].toLowerCase(); // Convert action to lowercase for case-insensitive comparison
        String parameter = parts.length > 1 ? parts[1] : null; 

        switch (action) {
        
        case "penwidth":
            if (parameter != null) {
                try {
                    int width = Integer.parseInt(parameter);
                    if (width > 0) {
                        setPenWidth(width); // Call the setPenWidth() method
                        displayMessage("Pen width set to " + width);
                    } else {
                        displayMessage("Invalid pen width. Please enter a positive integer.");
                    }
                } catch (NumberFormatException e) {
                    displayMessage("Invalid parameter. Please enter a valid integer for pen width.");
                }
            } else {
                displayMessage("Missing parameter. Please specify pen width.");
            }
            break;

        case "square":
            if (parameter != null) {
                try {
                    int length = Integer.parseInt(parameter);
                    drawSquare(length);
                } catch (NumberFormatException e) {
                    displayMessage("Invalid parameter. Please enter a valid integer.");
                }
            } else {
                displayMessage("Missing parameter. Please specify length.");
            }
            break;
        case "triangle":
            if (parameter != null) {
                try {
                    int sideLength = Integer.parseInt(parameter);
                    if (sideLength > 0) {
                        drawTriangle(sideLength);
                    } else {
                        displayMessage("Invalid side length. Please enter a positive integer.");
                    }
                } catch (NumberFormatException e) {
                    displayMessage("Invalid parameter. Please enter a valid integer for the side length.");
                }
            } else {
                displayMessage("Missing parameter. Please specify the side length.");
            }
            break;
        case "customtri":
            if (parameter != null) {
                String[] sides = parameter.split(",");
                if (sides.length == 3) {
                    try {
                        int sideA = Integer.parseInt(sides[0].trim());
                        int sideB = Integer.parseInt(sides[1].trim());
                        int sideC = Integer.parseInt(sides[2].trim());
                        drawCustomTriangle(sideA, sideB, sideC);
                        displayMessage("Triangle drawn successfully!");
                    } catch (NumberFormatException e) {
                        displayMessage("Invalid parameter. Please enter valid integers for the side lengths.");
                    }
                } else {
                    displayMessage("Invalid parameter. Please specify three side lengths separated by commas.");
                }
            } else {
                displayMessage("Missing parameter. Please specify three side lengths separated by commas.");
            }
            break;
            case "exit":
            	exitApplication();
            	break;
            case "dogcursor":
            	setDogCursor();
            	displayMessage("You have switched to the dog cursor, ABIYE :)");
            	break;
            case "turtle":
                displayAbout();
                displayMessage("Here is the version Abiye :)");
                break;
            case "about":
            	about();
            	break;
            case "forward":
                if (parameter != null) {
                    try {
                        int distance = Integer.parseInt(parameter);
                        if (distance <= 0) {
                            displayMessage("Invalid parameter. Please enter a positive integer.");
                        } else {
                            forward(distance); // Move forward by the specified distance
                            displayMessage("Moved forward by " + distance + " pixels");
                            
                            // Check if the turtle is going out of bounds
                            if (getxPos() < MIN_X || getxPos() > MAX_X || getyPos() < MIN_Y || getyPos() > MAX_Y) {
                                displayMessage("Turtle going out of bounds.");
                                forward(-distance); // Move backward by the same distance to stay within bounds
                            }
                        }
                    } catch (NumberFormatException e) {
                        displayMessage("Invalid parameter. Please enter a valid integer.");
                    }
                } else {
                    displayMessage("Missing parameter. Please specify distance.");
                }
                break;
            case "backward":
                if (parameter != null) {
                    try {
                        int distance = Integer.parseInt(parameter);
                        if (distance <= 0) {
                            displayMessage("Invalid parameter. Please enter a positive integer.");
                        } else {
                            forward(-distance); // Move backward by the specified distance
                            displayMessage("Moved backward by " + distance + " pixels");
                            
                            // Check if the turtle is going out of bounds
                            if (getxPos() < MIN_X || getxPos() > MAX_X || getyPos() < MIN_Y || getyPos() > MAX_Y) {
                                displayMessage("Turtle going out of bounds.");
                                forward(distance); // Move forward by the same distance to stay within bounds
                            }
                        }
                    } catch (NumberFormatException e) {
                        displayMessage("Invalid parameter. Please enter a valid integer.");
                    }
                } else {
                    displayMessage("Missing parameter. Please specify distance.");
                }
                break;
                
            case "pencolour":
                if (parameter != null) {
                    try {
                        String[] rgb = parameter.split(",");
                        if (rgb.length == 3) {
                            int red = Integer.parseInt(rgb[0]);
                            int green = Integer.parseInt(rgb[1]);
                            int blue = Integer.parseInt(rgb[2]);

                            if (red >= 0 && red <= 255 && green >= 0 && green <= 255 && blue >= 0 && blue <= 255) {
                                setPenColour(new Color(red, green, blue));
                                displayMessage("Pen color set to RGB(" + red + ", " + green + ", " + blue + ")");
                            } else {
                                displayMessage("Invalid RGB values. Please enter values between 0 and 255.");
                            }
                        } else {
                            displayMessage("Invalid parameter format. Please provide comma-separated RGB values.");
                        }
                    } catch (NumberFormatException e) {
                        displayMessage("Invalid parameter. Please enter valid integer values for RGB.");
                    }
                } else {
                    displayMessage("Missing parameter. Please specify RGB values.");
                }
                break;
            case "turnleft":
            	if (parameter != null) {
                    try {
                        int angle = Integer.parseInt(parameter);
                        if (angle < 0 || angle > 360) {
                            displayMessage("Invalid parameter. Please enter an angle between 0 and 360.");
                        } else {
                            turnLeft(angle);
                            displayMessage("Turned left by " + angle + " degrees");
                        }
                    } catch (NumberFormatException e) {
                        displayMessage("Invalid parameter. Please enter a valid integer.");
                    }
                } else {
                    displayMessage("Missing parameter. Please specify angle.");
                }
                break;
            case "turnright":
                if (parameter != null) {
                    try {
                        int angle = Integer.parseInt(parameter);
                        if (angle < 0 || angle > 360) {
                            displayMessage("Invalid parameter. Please enter an angle between 0 and 360.");
                        } else {
                            turnRight(angle);
                            displayMessage("Turned right by " + angle + " degrees");
                        }
                    } catch (NumberFormatException e) {
                        displayMessage("Invalid parameter. Please enter a valid integer.");
                    }
                } else {
                    displayMessage("Missing parameter. Please specify angle.");
                }
                break;
            case "penup":
                penUp();
                displayMessage("Pen has been lifted up");
                break;
            case "pendown":
                penDown();
                displayMessage("Pen has now been put down");
                break;
            case "green":
                setPenColour(Color.GREEN);
                displayMessage("Pen Color Changed to Green Now");
                break;
            case "red":
                setPenColour(Color.RED);
                displayMessage("Pen Color Changed to Red Now");
                break;
            case "white":
                setPenColour(Color.WHITE);
                displayMessage("Pen Color Changed to white Now");
                break;
            case "blue":
                setPenColour(Color.BLUE);
                displayMessage("Pen Color Changed to Blue Now");
                break;
            case "circle":
                if (parameter != null) {
                    try {
                        int radius = Integer.parseInt(parameter);
                        circle(radius);
                    } catch (NumberFormatException e) {
                        displayMessage("Invalid parameter. Please enter a valid integer.");
                    }
                } else {
                    displayMessage("Missing parameter. Please specify radius.");
                }
                break;
            case "draw":
                setTurtleImage("C:/Users/abiye/git/oop-portfolio-2024-Abiye12/AssignmentTurtleGraphicsProgram/AssignmentTurtleGraphics/src/dog.png"); // Resize and set the turtle image
                displayMessage("Changed image"); 
                break;
            case "save":
                saveFile();
                break;
            case "load":
                loadFile();
                break;
            case "savecmd":
                commandHistory.add(command); // Add the command to the command history
                saveCommandsToFile();
                break;
            case "loadcmd":
                commandHistory.add(command); // Add the command to the command history
                loadCommandsFromFile();
                break;
            case "clear":
                clearCanvas();
                displayMessage("Canvas has been cleared");
                break;
            case "reset":
            	reset();
            	displayMessage("The turtle has been reseted");
            	break;
            default:
                displayMessage("Unknown command: " + action);
        }
        
    }


    public void setTurtleImage(String imagePath, JLabel label) {
        try {
            File file = new File(imagePath);
            BufferedImage image = ImageIO.read(file);

            ImageIcon icon = new ImageIcon(image);
            label.setIcon(icon);

            label.repaint();

            // ...
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage getCanvasImage() {
        
        int width = getWidth();
        int height = getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        paint(g2d);
        g2d.dispose();
        return image;
        
    }

    public void setDogCursor() {
        String imagePath = "C:/Users/abiye/git/oop-portfolio-2024-Abiye12/AssignmentTurtleGraphicsProgram/AssignmentTurtleGraphics/src/dog.png";
        try {
            File imageFile = new File(imagePath);
            dogImage = ImageIO.read(imageFile);
            setCursor(Toolkit.getDefaultToolkit().createCustomCursor(dogImage, new Point(0, 0), "dog"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void drawCustomTriangle(int sideA, int sideB, int sideC) {
        setPenColor(penColor);
        setPenWidth(penWidth);

        // Check if the given side lengths can form a valid triangle
        if (isValidTriangle(sideA, sideB, sideC)) {
            // Use the law of cosines to calculate the angle opposite to sideC
            double angleC = calculateAngle(sideA, sideB, sideC);

            // Draw the triangle
            forward(sideA);
            turnRight(180 - (int) angleC);
            forward(sideB);
            turnRight(180 - (int) calculateAngle(sideB, sideC, sideA));
            forward(sideC);
        } else {
            System.out.println("Invalid triangle: the given side lengths cannot form a triangle.");
        }
    }
  

    private boolean isValidTriangle(int sideA, int sideB, int sideC) {
        return (sideA + sideB > sideC) && (sideB + sideC > sideA) && (sideC + sideA > sideB);
    }

    private double calculateAngle(int sideA, int sideB, int sideC) {
        return Math.toDegrees(Math.acos((Math.pow(sideA, 2) + Math.pow(sideB, 2) - Math.pow(sideC, 2)) / (2.0 * sideA * sideB)));
    }

    private void drawTriangle(int sideLength) {
        int angle = 120; // Interior angle of an equilateral triangle

        setPenColor(penColor);
        setPenWidth(penWidth);

        turnRight(90); // Turn to start drawing the triangle

        for (int i = 0; i < 3; i++) {
            forward(sideLength);
            turnRight(angle);
    }
        
    }
       
    private void drawSquare(int length) {
    	Graphics2D g2d = (Graphics2D) getGraphics();
    	g2d.setColor(penColor);
    	g2d.setStroke(new BasicStroke(penWidth));
    	setPenColor(penColor);
        for (int i = 0; i < 4; i++) {
            forward(length);
            turnRight(90);
        }
    }
    private void loadCommandsFromFile() {
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            List<String> loadedCommands = new ArrayList<>(); // Create a new list to store the loaded commands

            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String command = scanner.nextLine();
                    loadedCommands.add(command); // Add the command to the loaded commands list
                }

                // Print the loaded commands to the console
                for (String command : loadedCommands) {
                    System.out.println(command);
                }

                displayMessage("Commands loaded successfully from file: " + file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                displayMessage("File not found: " + e.getMessage());
            }
        }
    }
    private void saveCommandsToFile() {
        int returnVal = fileChooser.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(file)) {
                for (String commandToAdd : commandHistory) {
                    writer.println(commandToAdd); // Write each command to the file
                }
                displayMessage("Commands saved successfully: " + file.getAbsolutePath());
            } catch (IOException e) {
                displayMessage("Error saving commands: " + e.getMessage());
            }
        }
    }

    private void setPenColor(Color color) {
        penColor = color;
    }
  

    public void setPenColour(int red, int green, int blue) {
        penColor = new Color(red, green, blue);
        displayMessage("Pen color set to RGB(" + red + ", " + green + ", " + blue + ")");
    }
    private void setPenWidth(int width) {
        penWidth = width;
        setStroke(penWidth);
    }
  

    private void saveFile() {
        int returnVal = fileChooser.showSaveDialog(this); // Show the save file dialog

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile(); // Get the selected file
            String fileName = file.getAbsolutePath(); // Get the file name
            BufferedImage image = getCanvasImage(); // Get the image of the canvas

            try {
                ImageIO.write(image, "png", file); // Save the image as PNG
                displayMessage("Canvas saved as " + fileName);
            } catch (IOException e) {
                displayMessage("Error saving file: " + e.getMessage());
            }
        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            // File saving was canceled
            displayMessage("File saving was canceled.");
        }
    }
    
    // Method to load an image file onto the canvas
    private void loadFile() {
        int returnVal = fileChooser.showOpenDialog(this); // Show the open file dialog

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(file); // Read the selected image file
                setBufferedImage(image); // Set the loaded image as the current drawing
                displayMessage("File loaded successfully: " + file.getAbsolutePath());
            } catch (IOException e) {
                displayMessage("Error loading file: " + e.getMessage());
            }
        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
          
            displayMessage("File loading was canceled.");
        }
    }
    private void clearCanvas() {
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        if (changes) { 
            int choice = JOptionPane.showConfirmDialog(mainFrame, 
                    "There are unsaved changes. Do you want to save the current image?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.CANCEL_OPTION) {
                return; 
            } else if (choice == JOptionPane.YES_OPTION) {
                saveFile(); 
            }
        }
        
        clear(); 
    }

@Override
public void about() {
	super.about();
    System.out.println("My name is Abiye Opusunnju and i aspire to be a software engineer");
}
private void displayAbout() {
    Graphics g = getGraphics(); // Get the graphics context to draw
    g.setColor(Color.GREEN); 
    g.drawOval(200, 100, 200, 200); 
    
    g.setColor(Color.BLUE); 
    g.drawOval(222, 120, 160, 160); 
    
    g.setColor(Color.RED); 
    g.drawOval(244, 140, 120, 120); 
    
    g.setColor(Color.WHITE); 
    g.drawString("Turtle Graphics v5.1", 270, 210); // Draw the version information
}
public void setxPos(int xPos) {
    // Set the x position of the turtle
    this.xPos = xPos;
}

public void setyPos(int yPos) {
    // Set the y position of the turtle
    this.yPos = yPos;
}


private void exitApplication() {
    int choice = JOptionPane.showConfirmDialog(null,
            "Are you sure you want to exit?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
       
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        mainFrame.dispose(); 

        
        System.exit(0);
    } else if (choice == JOptionPane.NO_OPTION) {
       
    }
}

}
