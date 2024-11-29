package demo;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Character2 extends Sprite  {
	
	private Boolean visible, moving;
//	private Thread t;            [MOVING HAPPENS ON THE SERVER]
	
	
	//declare the label from the main program
	//DO NOT INSTANTIATE IT!!!!!!!!!!!!!!!!! (no = new JLabel)
	private JLabel character2Label;	
	private JButton startButton, visibiltyButton;
	
	private Character1 character1;
	private JLabel character1Label;
	
	public void setCharacter1 (Character1 temp) {
		character1 = temp;
	}
	
	public void setCharacter1Label(JLabel temp) {
		character1Label = temp;
	}
	
	public void setCharacter2Label(JLabel temp) {
		character2Label = temp;
	}

	public void setStartButton(JButton temp) {
		startButton = temp;
	}

	public void setVisibilityButton(JButton temp) {
		visibiltyButton = temp;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getMoving() {
		return moving;
	}

	public void setMoving(Boolean moving) {
		this.moving = moving;
	}

	public Character2() {
		super();
		// TODO Auto-generated constructor stub
		this.moving = false;
		this.visible = true;
	}

	public Character2(int x, int y, int height, int width, String image) {
		super(x, y, height, width, image);
		// TODO Auto-generated constructor stub
		this.moving = false;
		this.visible = true;
	}
	

	
	// you might wanna send the moving , x and a y 

}
