package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Application {	
			
	public static void main(String args[]) {				
		new Application().start();
	}
	
	public Application() {
		init();
	}
	
	public void start() {
		new Menu(Menu.SIGNIN_SIGNUP_MENU).start();
	}
	
	private static void init() {
	}	
}
