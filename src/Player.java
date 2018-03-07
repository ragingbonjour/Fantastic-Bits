import java.util.*;

/**
 * Grab Snaffles and try to throw them through the opponent's goal! Move towards
 * a Snaffle and use your team id to determine where you need to throw it
 **/
class Player {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int myTeamId = in.nextInt(); // if 0 you need to score on the right of the map, if 1 you need to score on the
		// left

		// game loop
        do {

            ArrayList<Wizard> Wizards = new ArrayList<>();
            ArrayList<Snaffle> Snaffles = new ArrayList<>();
            ArrayList<Opponent> Opponents = new ArrayList<>();
            ArrayList<Bludger> Bludgers = new ArrayList<>();

            int myScore = in.nextInt();
            int myMagic = in.nextInt();
            int opponentScore = in.nextInt();
            int opponentMagic = in.nextInt();
            int entities = in.nextInt(); // number of entities still in game

            // Grab the input from the game regarding the position of where each of the
            // elements are
            for (int i = 0; i < entities; i++) {

                int entityId = in.nextInt(); // entity identifier
                String entityType = in.next(); // "WIZARD", "OPPONENT_WIZARD" or "SNAFFLE" (or "BLUDGER" after first
                // league)
                int x = in.nextInt(); // position
                int y = in.nextInt(); // position
                int vx = in.nextInt(); // velocity
                int vy = in.nextInt(); // velocity
                int state = in.nextInt(); // 1 if the wizard is holding a Snaffle, 0 otherwise

                switch (entityType) {
                    case "WIZARD":
                        Wizards.add(new Wizard(entityId, entityType, x, y, vx, vy, state));
                        break;
                    case "OPPONENT_WIZARD":
                        Opponents.add(new Opponent(entityId, entityType, x, y, vx, vy, state));
                        break;
                    case "SNAFFLE":
                        System.err.println(entityId);
                        Snaffles.add(new Snaffle(entityId, entityType, x, y, vx, vy, state));
                        break;
                    case "BLUDGER":
                        Bludgers.add(new Bludger(entityId, entityType, x, y, vx, vy, state));
                        break;
                }
            }

//			for (int i = 0 ; i < Snaffles.size() ; i++) {
//			    System.err.print("Snaffle: " + i + " with SnaffleID: " + Snaffles.get(i).getEntityID() + " | ");
//            }
//
//			for (int i = 0 ; i < Wizards.size() ; i++) {
//			    System.err.print("Wizard: " + i + " with WizardID: " + Wizards.get(i).getEntityID() + " | ");
//            }

            // ACTIONS FOR JUST THE FIRST TWO WIZARDS - THE ONES WE CONTROL
            for (int i = 0; i < 2; i++) {

                String action = null;
                int x_coordinate = 0;
                int y_coordinate = 0;
                short throw_or_power = 0;

                /** Implement logic to get the hyponenuse of where to go for each of the bludgers, maybe take into
                 * account the velocity of each as well. */

                if (Wizards.get(i).getState() == 1) { // The wizard is holding a snaffle and is prepared to
                    // move/fire
                    action = "THROW";
                    throw_or_power = 500;
                    int[] targetCoordinates = EVALUATE_DISTANCE_TO_GOAL(myTeamId);

                    // If the wizard is within the Y range of the goal, proceed to give the rest of
                    // the throw instructions to print

                    x_coordinate = targetCoordinates[0];
                    y_coordinate = targetCoordinates[1];

                }


                // HUNT NEAREST SNAFFLE
                if (Wizards.get(i).getState() == 0) {
                    action = "MOVE";
                    throw_or_power = 150;

                    double distanceEvaluation = 100000.0;
                    int snaffleIndex = 0;

                    for (int j = 0; j < Snaffles.size(); j++) {
                        // As long as the snaffle isn't already taken OR TARGETED ALREADY
                        if (Snaffles.get(j).getState() == 0 && !Snaffles.get(j).getIsTargeted()) {
                            double temp = EVALUATE_OBJECT_TO_OBJECT(Wizards.get(i), Snaffles.get(j));
                            if (temp < distanceEvaluation) {
                                distanceEvaluation = temp;
                                snaffleIndex = j;
                                Snaffles.get(j).setIsTargeted(true);
                            }
                        }
                    }


                    x_coordinate = Snaffles.get(snaffleIndex).getX();
                    y_coordinate = Snaffles.get(snaffleIndex).getY();

                }

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");

                // Edit this line to indicate the action for each wizard (0 ≤ thrust ≤ 150, 0 ≤
                // power ≤ 500)
                // i.e.: "MOVE x y thrust" or "THROW x y power"
                System.out.println(action + " " + x_coordinate + " " + y_coordinate + " " + throw_or_power);
                // System.out.println("MOVE 8000 3750 100");
            }
        } while (true);
	}

	private static int[] EVALUATE_DISTANCE_TO_GOAL(int teamID) {
		// Make priority depending on which goal we require to score on
		int[] targetCoordinates = null;
//		[0] = x, [1] = y.
		
		// START ON RIGHT - AIM LEFT
		if (teamID == 0) {
			targetCoordinates = new int[] {16000, 3750};
		} else if(teamID == 1) {
			targetCoordinates = new int[] {0, 3750};
		}
		
		return targetCoordinates;
	}

	private static double EVALUATE_OBJECT_TO_OBJECT(Wizard WIZARD, Snaffle SNAFFLE) {
		/*
		 * Use in a for loop so it iterates through all the available snaffles(or
		 * whatever objects are needed) with this method and compares the distance
		 * between the current object and the node you wish to track down first.
		 */

		double hyp = (Math.sqrt(

				Math.pow(SNAFFLE.getX() - WIZARD.getX(), 2) + Math.pow(SNAFFLE.getY() - WIZARD.getY(), 2)));

//		if (hyp < 0)
//			hyp = hyp * (-1);

		return hyp;
	}

}

class Entity {

	private int entityID;
	private String entityType = "UNASSIGNED";
	// POSITION
	private int x;
	private int y;
	// VELOCITY
	private int vx;
	private int vy;

	private int state = 0; // 0 = NOT HOLDING, 1 = HOLDING

	// Modifications to the primary board
	private final static int BOARD_X = 16001;
	private final static int BOARD_Y = 7501;
	private final static short DISK_RADIUS = 400;
	private final static short POLE_RADIUS = 300;
	private final static int POLE_CENTER_Y = 3750;

	public int getEntityID() {
	    return entityID;
    }

	public static int getDISK_RADIUS() {
		return DISK_RADIUS;
	}

	public int getPOLE_RADIUS() {
	    return POLE_RADIUS;
    }

	public int getPOLE_CENTER_Y() {
		return POLE_CENTER_Y;
	}

	public int getBOARD_X() {
		return BOARD_X;
	}

	public int getBOARD_Y() {
	    return BOARD_Y;
	}

	public int getState() {
		return state;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getVelocityX() {
		return vx;
	}

	public int getVelocityY() {
		return vy;
	}

	public Entity(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
		this.entityID = entityID;
		this.entityType = entityType;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.state = state;
	}
}

class Wizard extends Entity {
	public Wizard(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
		super(entityID, entityType, x, y, vy, vx, state);
		// System.out.println("Creating wizard state...");
	}
}

class Snaffle extends Entity {
	// Use this to prioritize where to aim and collectively split up the load on the bots.
	private boolean isTargeted = false;
	
	public void setIsTargeted(boolean isTargeted) {
		this.isTargeted = isTargeted;
	}
	
	public boolean getIsTargeted() {
		return isTargeted;
	}
	
	public Snaffle(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
		super(entityID, entityType, x, y, vy, vx, state);
		// System.out.println("Creating snaffle state...");
	}
}

class Opponent extends Entity {
	public Opponent(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
		super(entityID, entityType, x, y, vy, vx, state);
		// System.out.println("Creating Opponent state...");
	}
}

class Bludger extends Entity {
//	From the documentation, -1 is the default if no one has been hit with it yet - SENT BY GAME IN OBJECT INFORMATION
	private int lastVictimID = -1;
//	Last victim information is supplied already in the state field on ititialization, so a set method isn't necessary here
	public int getLastVictimID() { return lastVictimID; }
	
	public int getBludgerRadius() { return 200; }
	
	public Bludger(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
		super(entityID, entityType, x, y, vy, vx, state);
		lastVictimID = state;
	}
	
}
