package froggerClient;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Obstacle extends Sprite implements Runnable {
	
	private Boolean car, moving, rightToLeft, detectCollision, moveFrog, server;
	private int speed;
	private Thread t;
	//protected String colImage, frogImage, frogColImage;
	
	private JLabel label;	
	private ServerService game;
	
	private Character frog;
	private JLabel frogLabel;

	public Boolean isCar() {
	    return car;
	}
	
	public int getSpeed() {
	    return speed;
	}
	
	public JLabel getLabel() {
	    return label;
	}
	/*
	public String getColImage() {
	    return colImage;
	}

	public String getFrogImage() {
	    return frogImage;
	}

	public String getFrogColImage() {
	    return frogColImage;
	}

	public void setColImage(String colImage) {
	    this.colImage = colImage;
	}

	public void setFrogImage(String frogImage) {
	    this.frogImage = frogImage;
	}

	public void setFrogColImage(String frogColImage) {
	    this.frogColImage = frogColImage;
	}*/
	
	public void setFrog(Character temp) {
		frog = temp;
	}
	
	public void setFrogLabel(JLabel temp) {
		frogLabel = temp;
	}

	public void setSpeed(int speed) {
	    this.speed = speed;
	}
	
	public void setLabel(JLabel temp) {
		label = temp;
	}

	public void setMoveFrog(Boolean move) {
	    this.moveFrog = move;
	}

	public void setCar(Boolean temp) {
	    this.car = temp;
	}

	public void setGame(ServerService temp) {
		game = temp;
	}

	public Boolean getMoving() {
		return moving;
	}

	public void setMoving(Boolean moving) {
		this.moving = moving;
	}

	public Obstacle() {
		super();
		// TODO Auto-generated constructor stub
		this.moving = false;
		this.car = false;
		this.moveFrog = false;
	}
	
	public Obstacle(int x, int y, int height, int width, int speed,
			String image, Boolean rightToLeft, Boolean car) {
		super(x, y, height, width, image);
		// TODO Auto-generated constructor stub
		this.moving = false;
		this.car = car;
		this.speed = speed;
		this.moveFrog = false;
		this.rightToLeft = rightToLeft;
		this.server = false;
	}

	public Obstacle(int x, int y, int height, int width, int speed,
			String image, Boolean rightToLeft, Boolean car, Character frog, Boolean server) {
		super(x, y, height, width, image);
		// TODO Auto-generated constructor stub
		this.moving = false;
		this.car = car;
		this.speed = speed;
		this.moveFrog = false;
		this.rightToLeft = rightToLeft;
		this.server = server;
		this.frog = frog;
	}
	
	public void startThread() {
		
		if ( !this.moving ) {
			this.moving = true;
			
			
			//System.out.println("Starting thread");
			t = new Thread(this, "Character2 thread");
			t.start(); //automatic call to the run method
		}
		
	}
	
	public void stopThread() {
		//will end the thread on next repeated cycle
		if (this.moving) {
			this.moving = false;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		while (this.moving) {
			
			int x = this.x;
			
			if(rightToLeft) {
				x -= this.speed;
				if(x+ this.width <= 0)
					x = GameProperties.SCREEN_WIDTH;
				
			} else {
				x += this.speed;
				
				if ( x >= GameProperties.SCREEN_WIDTH) {
					x = -1 * this.width;
				}
			}
			
			
			this.setX(x); //this.x = x; //rectangle doesn't update
			
			this.detectCollision();
			
			
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void detectCollision() {
		if ( this.r.intersects( frog.getRectangle() ) ) {

			game.handleCollision(this);
			
			/*if(car) {
				//collision detected
				System.out.println("BOOM!");
				//this.stopThread();
			
				game.handleCollision(this);
			} else {
				// logs
//
				System.out.println("OnBoard!");
				if(frog.getLog() == this) {
//					// Frog gets off the log if it gets carried to the border
					if( x >= GameProperties.SCREEN_WIDTH - frog.getWidth() || x <= 0) {
						game.handleCollision(this);
					} else {
						frog.setX(x);
						frog.setY(y);
					}
					//move label
					//frogLabel.setLocation(
					//		frog.getX(), frog.getY() );
				}
				
				
			}*/

		}
	}

}
