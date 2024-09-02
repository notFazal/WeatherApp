import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

// child class of JFrame
public class WeatherApp extends JFrame{
	

	
	public WeatherApp() {
		// constructor
		setTitle("Weather"); // sets title of the window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // actually exit application where default is hide_on_close
		//setResizable(false); // prevent window from being resized
		setSize(420,420); // sets the x and y dimensions of the this
		setVisible(true); // make this visible, w/o you can't see it run
		setLayout(null); // use absolute positioning
		setLocationRelativeTo(null); // sets it to center of the screen
		ImageIcon logo = new ImageIcon("weatherLogo.png"); // create an Image Icon for the application
		setIconImage(logo.getImage()); // changes the icon on the application
		//getContentPane().setBackground(new Color(52,171,235)); // change color of application background using RGB 
		getContentPane().setBackground(new Color(0x34abeb)); // change color of application background using hex
		buttons();
	}
	
	private void buttons() {
		JButton searchButton = new JButton(loadImage("541346-20.png")); // create button
		searchButton.setBounds(320, 15, 75, 50); // x, y, width, height
		
		add(searchButton); // add it in
		text();
	}
	
	private void text() {
		JTextField searchField = new JTextField(); // create text field
		searchField.setBounds(15, 15, 300, 50); // x, y, width, height
		searchField.setFont(new Font("Dialog", Font.PLAIN, 24));
		add(searchField); // add it in
	}
	
	
	// create images in GUI applications
    private ImageIcon loadImage(String resourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't find the image file: " + resourcePath);
            return null; // return null or a default image up to you
        }
    }
	
}
