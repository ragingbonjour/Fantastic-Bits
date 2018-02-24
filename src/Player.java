import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Grab Snaffles and try to throw them through the opponent's goal!
 * Move towards a Snaffle and use your team id to determine where you need to throw it
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int myTeamId = in.nextInt(); // if 0 you need to score on the right of the map, if 1 you need to score on the left

        // game loop
        while (true) {
        	
        	ArrayList<Wizard> Wizards = new ArrayList<>();
        	ArrayList<Snaffle> Snaffles = new ArrayList<>();
        	ArrayList<Opponent> Opponents = new ArrayList<>();
        	
            int myScore = in.nextInt();
            int myMagic = in.nextInt();
            int opponentScore = in.nextInt();
            int opponentMagic = in.nextInt();
            int entities = in.nextInt(); // number of entities still in game
            
            for (int i = 0; i < entities; i++) {
            	
                int entityId = in.nextInt(); // entity identifier
                String entityType = in.next(); // "WIZARD", "OPPONENT_WIZARD" or "SNAFFLE" (or "BLUDGER" after first league)
                int x = in.nextInt(); // position
                int y = in.nextInt(); // position
                int vx = in.nextInt(); // velocity
                int vy = in.nextInt(); // velocity
                int state = in.nextInt(); // 1 if the wizard is holding a Snaffle, 0 otherwise
                
                // public Entity(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
                if(entityType.equals("WIZARD")) Wizards.add(new Wizard(entityId, entityType, x, y, vx, vy, state));
                else if(entityType.equals("OPPONENT_WIZARD")) Opponents.add(new Opponent(entityId, entityType, x, y, vx, vy, state));
                else if(entityType.equals("SNAFFLE")) Snaffles.add(new Snaffle(entityId, entityType, x, y, vx, vy, state));

                
                
            }
            for (int i = 0; i < 2; i++) {

            	
                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");


                // Edit this line to indicate the action for each wizard (0 ≤ thrust ≤ 150, 0 ≤ power ≤ 500)
                // i.e.: "MOVE x y thrust" or "THROW x y power"
                System.out.println("MOVE 8000 3750 100");
            }
        }
    }
}

class Entity {
	
	protected static byte side_bias = 0;
	protected int entityID = 0;
	protected String entityType = "UNASSIGNED";
	// POSITION
	protected static int position_x = 0;
	protected static int position_y = 0;
	// VELOCITY
	protected static int velocity_x = 0;
	protected static int velocity_y = 0;
	
	protected static int state = 0; // 0 = NOT HOLDING, 1 = HOLDING
	
	final static int board_x = 16001;
	final int board_y = 7501;
	
	// Establish what side we should consider our goals to be, attempt to score on other side
	public static void entitySetup(int x_coord) {
		if(x_coord < board_x/2) { side_bias = 0; } 
		else { side_bias = 1; }
	}
	
	public static int getState() { return state; }
	public static int getPositionX() { return position_x; }
	public static int getPositionY() { return position_y; }
	public static int getVelocityX() { return velocity_x; }
	public static int getVelocityY() { return velocity_y; }
	
	public Entity(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
		this.entityID = entityID;
		this.entityType = entityType;
		position_x = x;
		position_y = y;
		velocity_x = vx;
		velocity_y = vy;
		this.state = state;
	}
}

class Wizard extends Entity{
	public Wizard(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
		super(entityID, entityType, x, y, vy, vx, state);
		System.out.println("Creating wizard state...");
	}
}

class Snaffle extends Entity{
	public Snaffle(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
		super(entityID, entityType, x, y, vy, vx, state);
		System.out.println("Creating snaffle state...");
	}	
}

class Opponent extends Entity {
	public Opponent(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
		super(entityID, entityType, x, y, vy, vx, state);
		System.out.println("Creating Opponent state...");
	}	
}
