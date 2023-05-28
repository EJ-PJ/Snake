import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    int bodyParts = 6;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int appleEaten = 0;
    int appleX;
    int appleY;
    char direction = 'D';
    boolean runnig = false;
    Timer timer;
    Random random;

    GamePanel(){
	//System.out.println("hey3");
	random = new Random();
	this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
	this.setBackground(Color.black);
	//setFocusable sirve para que JPanel pueda recibir eventos del teclado
	this.setFocusable(true);
	this.addKeyListener(new MyKeyAdapter());
	startGame();

    }

    public void	startGame(){
	newApple();
	runnig = true;
	//El timer representa que el delay (que tan rapido ira el juego)
	//con this hacemos referencia a ActionListener interface
	timer = new Timer(DELAY, this);
	timer.start();

    }

    public void paintComponent(Graphics g){
	super.paintComponent(g);
	draw(g);
    }

    public void newApple(){
	appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE; 
	appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;

    }
    

    public void draw(Graphics g){
	//System.out.println("test2");
	//Draw lines on x  and y axis
	if(runnig){
	    for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
		g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
		g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE );
	    }
	    //Draw Apple
	    g.setColor(Color.red);
	    g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
	    

	    //Draw the body parts of the snake
	    for(int i = 0; i < bodyParts; i++){
		//System.out.println("---------------------------");
		//if i == 0, that means draw the head
		if(i == 0){
		    g.setColor(Color.green);
		    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
		//Else its gonna draw the body
		}else{
		    g.setColor(new Color(45,180,0));
		    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
		}
	    }
	    scoreCounter(g);
	}else{
	    gameOver(g);
	}
    }

    public void move(){

	//System.out.println("---------------------------");
	for(int i = bodyParts; i > 0; i--){
	    x[i] = x[i - 1];
	    y[i] = y[i - 1];
	}

	switch(direction){
	    case 'U':
		y[0] = y[0] - UNIT_SIZE;
		break;
	    case 'D':
		y[0] = y[0] + UNIT_SIZE;
		break;
	    case 'L':
		x[0] = x[0] - UNIT_SIZE;
		break;
	    case 'R':
		x[0] = x[0] + UNIT_SIZE;
		break;
	}
    
    }

    public void checkApple(){
	if((x[0] == appleX) && (y[0] == appleY)){
	    bodyParts ++;
	    appleEaten ++;
	    newApple();
	}
    }

    public void checkCollision(){

	//This checks if head collides whit body
	for(int i = bodyParts; i > 0; i--){
	    if((x[0] == x[i]) && (y[0] == y[i])){
		runnig = false;
	    }
	}
	//check if head touch left and right border
	if(x[0] < 0 || x[0] > SCREEN_WIDTH){
	    runnig = false;
	}
	//check if head touch up and down border
	if(y[0] < 0 || y[0] > SCREEN_HEIGHT){
	    runnig = false;
	}

	if(runnig == false){
	    timer.stop();
	}
    }

    public void gameOver(Graphics g){
	//Gameover text
	g.setColor(Color.red);
	g.setFont(new Font("Arial",Font.BOLD, 75));
	FontMetrics metric = getFontMetrics(g.getFont());
	g.drawString("Game Over", (SCREEN_WIDTH - metric.stringWidth("Game Over") / 2), SCREEN_HEIGHT / 2);
    }

    public void scoreCounter(Graphics g){
	g.setColor(Color.red);
	g.setFont(new Font("Arial",Font.BOLD, 20));
	FontMetrics metric = getFontMetrics(g.getFont());
	g.drawString("Score: " + appleEaten, (SCREEN_WIDTH - metric.stringWidth("Score: " + appleEaten) / 2), g.getFont().getSize());
    
    }

    @Override
    public void actionPerformed(ActionEvent e){
	if(runnig){
	    move();
	    checkApple();
	    checkCollision();
	    //minuto de video 26:34
	}

	repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
	@Override
	public void keyPressed(KeyEvent e){
	    switch(e.getKeyCode()){
		case KeyEvent.VK_LEFT:
		    if(direction != 'R'){
			direction = 'L';
		    }
		    break;
		case KeyEvent.VK_RIGHT:
		    if(direction != 'L'){
			direction = 'R';
		    }
		    break;

		case KeyEvent.VK_UP:
		    if(direction != 'D'){
			direction = 'U';
		    }
		    break;

		case KeyEvent.VK_DOWN:
		    if(direction != 'U'){
			direction = 'D';
		    }
		    break;
	    }
	
	}
    }

}


