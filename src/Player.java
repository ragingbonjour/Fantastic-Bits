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
		while (true) {

			ArrayList<Wizard> Wizards = new ArrayList<>();
			ArrayList<Snaffle> Snaffles = new ArrayList<>();
			ArrayList<Opponent> Opponents = new ArrayList<>();

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

				/*
				 * public Entity(int entityID, String entityType, int x, int y, int vy, int vx,
				 * int state) {
				 */
				switch (entityType) {
					case "WIZARD":
						Wizards.add(new Wizard(entityId, entityType, x, y, vx, vy, state));
						break;
					case "OPPONENT_WIZARD":
						Opponents.add(new Opponent(entityId, entityType, x, y, vx, vy, state));
						break;
					case "SNAFFLE":
						Snaffles.add(new Snaffle(entityId, entityType, x, y, vx, vy, state));
						break;
				}
			}

			// ACTIONS FOR JUST THE FIRST TWO WIZARDS - THE ONES WE CONTROL
			for (int i = 0; i < 2; i++) {

				String action = null;
				int x_coordinate = 0;
				int y_coordinate = 0;
				short throw_or_power = 0;

				// Implement logic to get the hyponenuse of where to go for each of the
				// bludgers, maybe take into account
				// the velocity of each as well.
				try {
					if (Wizards.get(i).getState() == 1) { // The wizard is holding a snaffle and is prepared to
															// move/fire
						action = "THROW";
						throw_or_power = 500;
						int[] targetCoordinates = EVALUATE_DISTANCE_TO_GOAL(Wizards.get(i), (byte) myTeamId);

						// If the wizard is within the Y range of the goal, proceed to give the rest of
						// the throw instructions to print
						if (Wizards.get(i).getPositionY() >= targetCoordinates[1]
								&& Wizards.get(i).getPositionY() <= targetCoordinates[2]) {
							y_coordinate = Wizards.get(i).getPositionY();
							x_coordinate = Wizards.get(i).getPositionX();
						} else {
							if (Wizards.get(i).getPositionY() < targetCoordinates[1]) {
								x_coordinate = targetCoordinates[0];
								y_coordinate = targetCoordinates[1] + Entity.getDISK_RADIUS();
							} else if (Wizards.get(i).getPositionY() > targetCoordinates[2]) {
								x_coordinate = targetCoordinates[0];
								y_coordinate = targetCoordinates[1] - Entity.getDISK_RADIUS();
							}
						}
					}
				} catch (Exception e) {
					System.err.print("Impossible to throw a snaffle without holding one.");
				}

				// HUNT NEAREST SNAFFLE
				if (Wizards.get(i).getState() == 0) {
					action = "MOVE";
					throw_or_power = 150;

					double distanceEvaluation = 0.0;
					int snaffleIndex = 0;

					for (int j = 0; j < Snaffles.size(); j++) {
						// As long as the snaffle isn't already taken
						if (Snaffles.get(j).getState() == 0) {
							double temp = EVALUATE_OBJECT_TO_OBJECT(Wizards.get(i), Snaffles.get(j));
							if (temp < distanceEvaluation) {
								distanceEvaluation = temp;
								snaffleIndex = j;
							}
						}
					}

					x_coordinate = Snaffles.get(snaffleIndex).getPositionX();
					y_coordinate = Snaffles.get(snaffleIndex).getPositionY();

				}

				// Write an action using System.out.println()
				// To debug: System.err.println("Debug messages...");

				// Edit this line to indicate the action for each wizard (0 ≤ thrust ≤ 150, 0 ≤
				// power ≤ 500)
				// i.e.: "MOVE x y thrust" or "THROW x y power"
				System.out.println(action + " " + x_coordinate + " " + y_coordinate + " " + throw_or_power);
				// System.out.println("MOVE 8000 3750 100");
			}
		}
	}

	private static int[] EVALUATE_DISTANCE_TO_GOAL(Entity entity, byte teamID) {
		// Make priority depending on which goal we require to score on
		int[] targetCoordinates = null;

		if (teamID == 0) {
			// Defines the range for where is needed to score (both here and the other loop
			// below)
			targetCoordinates = new int[] { 0, (entity.getPOLE_CENTER_Y() - 2000), (entity.getPOLE_CENTER_Y() + 2000) };
		} else if (teamID == 1) {
			// [ goal_center_coordinate, lowest_point, highest_point ]
			targetCoordinates = new int[] { entity.getBOARD_X(), (entity.getPOLE_CENTER_Y() - 2000), (entity.getPOLE_CENTER_Y() + 2000) };
		}

		return targetCoordinates;
	}

	private static double EVALUATE_OBJECT_TO_OBJECT(Wizard WIZARD, Snaffle SNAFFLE) {
		/*
		 * Use in a for loop so it iterates through all the available snaffles(or
		 * whatever objects are needed) with this method and compares the distance
		 * between the current object and the node you wish to track down first.
		 */

		double temp = (Math.sqrt(Math.pow(SNAFFLE.getPositionX() - WIZARD.getPositionX(), 2)
				+ Math.pow(SNAFFLE.getPositionY() - WIZARD.getPositionY(), 2)));
		if (temp < 0)
			temp = temp * (-1);

		return temp;
	}

}

class Entity {

	private static int entityID;
	private static String entityType = "UNASSIGNED";
	// POSITION
	private static int position_x;
	private static int position_y;
	// VELOCITY
	private int velocity_x;
	private static int velocity_y;

	private static int state; // 0 = NOT HOLDING, 1 = HOLDING

	// Modifications to the primary board
	final static int BOARD_X = 16001;
	// final static int BOARD_Y = 7501;
	final static short DISK_RADIUS = 400;
	// final static short POLE_RADIUS = 300;
	final static int POLE_CENTER_Y = 3750;

	public static int getDISK_RADIUS() { return DISK_RADIUS; }

	public int getPOLE_CENTER_Y() { return POLE_CENTER_Y; }

	public int getBOARD_X() { return BOARD_X; }

	public int getState() {
		return state;
	}

	public int getPositionX() {
		return position_x;
	}

	public int getPositionY() {
		return position_y;
	}
	// public static int getVelocityX() { return velocity_x; }
	// public static int getVelocityY() { return velocity_y; }

	public Entity(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
		Entity.entityID = entityID;
		Entity.entityType = entityType;
		position_x = x;
		position_y = y;
		velocity_x = vx;
		velocity_y = vy;
		Entity.state = state;
	}
}

class Wizard extends Entity {
	public Wizard(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
		super(entityID, entityType, x, y, vy, vx, state);
		// System.out.println("Creating wizard state...");
	}
}

class Snaffle extends Entity {
	Snaffle(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
		super(entityID, entityType, x, y, vy, vx, state);
		// System.out.println("Creating snaffle state...");
	}
}

class Opponent extends Entity {
	Opponent(int entityID, String entityType, int x, int y, int vy, int vx, int state) {
		super(entityID, entityType, x, y, vy, vx, state);
		// System.out.println("Creating Opponent state...");
	}
}
