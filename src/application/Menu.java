package application;

import java.util.Scanner;

public class Menu {
	private MenuChoice[] menuChoices;
	
	public Menu(MenuChoice[] menuChoices) {
		this.menuChoices = menuChoices;
	}
	
	public void start() {
		int i;
		for (i = 0; i < menuChoices.length; i++) {
			System.out.println((i+1) + ". " + menuChoices[i].getDescription());
		}
		System.out.println((++i) + ". Quit");

		while (true) {
			Scanner uInput = new Scanner(System.in);
			String input = uInput.nextLine();
			int selection;
			
			try {
				selection = Integer.parseInt(input);
				
				if (selection <= 0 || selection > i) {
					throw new Exception("Selection out of range");
				}
			} catch (Exception nfe) {
				System.out.println("Error. Please enter an integer in the range [1, " + i + "]");
				continue;
			}			
			
			menuChoices[selection-1].execute();
		}
	}
}
