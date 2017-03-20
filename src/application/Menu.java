package application;

public class Menu {
	public static final MenuChoice[] PLAYER_MENU = {new StartSession(), new ViewLeaderboard(), new ViewFollowings(), new FollowToggle(), new ChangePassword()};
	public static final MenuChoice[] SIGNIN_SIGNUP_MENU = {new SignUp(), new SignIn()};

	private MenuChoice[] menuChoices;
	private boolean forever; 
	
	public Menu(MenuChoice[] menuChoices) {
		this.menuChoices = menuChoices;
		this.forever = true;
	}
	
	public Menu(MenuChoice[] menuChoices, boolean forever) {
		this.menuChoices = menuChoices;
		this.forever = forever;
	}
	
	public void start() {
		int i;
		for (i = 0; i < menuChoices.length; i++) {
			System.out.println((i+1) + ". " + menuChoices[i].getDescription());
		}
		System.out.println((++i) + ". Quit");

		while (true) {
			System.out.print("Selection: ");
			int selection;
			
			try {
				selection = UserInput.getInstance().getInt();
				
				if (selection == i) {
					UserInput.getInstance().close();
					System.exit(0);
				} else if (selection <= 0 || selection > i) {
					throw new Exception("Selection out of range");
				}
			} catch (Exception nfe) {
				System.out.println("Error. Please enter an integer in the range [1, " + i + "]");
				continue;
			}			
			
			menuChoices[selection-1].execute();
			
			if (!forever) {
				break;
			}
			
			for (i = 0; i < menuChoices.length; i++) {
				System.out.println((i+1) + ". " + menuChoices[i].getDescription());
			}
			System.out.println((++i) + ". Quit");
		}
	}
}
