/* SERVER INSTANCE*/

package demo;



public class Character2 extends Sprite implements Runnable{
	
	private Boolean visible, moving;
	private Thread t;
	
	//declare the label from the main program
	//DO NOT INSTANTIATE IT!!!!!!!!!!!!!!!!! (no = new JLabel)
	
	
	private Character1 character1;     // I would need it 
	
	
	public void setCharacter1 (Character1 temp) {
		character1 = temp;
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
	
	public void startThread() {
		//run will be triggered
		System.out.println("Current moving: " + this.moving);
		
		//if already moving, do not start again
		if ( !this.moving ) {
			this.moving = true;
			
			
			
			this.setImage("tardis.png");


			character1.setImage("dw12.png");


			
			System.out.println("Starting thread");
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
		
		System.out.println("run triggered");
		
		while (this.moving) {
			
			int x = this.x;
			
			x += GameProperties.CHARACTER_STEP;
			
			if ( x >= GameProperties.SCREEN_WIDTH) {
				x = -1 * this.width;
			}
			
			this.setX(x); //this.x = x; //rectangle doesn't update
			
			
			
			//detect collisions between character1 r and char2
			if (this.visible) this.detectCollision();
			
			System.out.println("x, y: " + this.x + " " + this.y);
			
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		System.out.println("Thread Stopped");
		
	}
	
	private void detectCollision() {
		if ( this.r.intersects( character1.getRectangle() ) ) {
			//collision detected
			System.out.println("BOOM!");
			this.stopThread();
			
			this.setImage("redTardis.png");


			character1.setImage("redDw12.png");



		}
	}
	
	public void hide() {
		this.visible = false;
		
	}

	public void show() {
		this.visible = true;
		
	}

}
