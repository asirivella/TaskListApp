package TaskListApp;

import java.util.Scanner;
import java.util.ArrayList;
import console.JavaConsole;
import console.CustomCaret;
import menu.*;

public class TaskListApp {

	private static boolean fContinue = true;
	private static JavaConsole console;
	private static Scanner scanner;
	private ArrayList<task> tasks = new ArrayList<task>();
	
	public class task{
		private String _text;
		
		public task(String text){
			_text = text;
		}
		
		public String get_text(){
			return _text;
		}
	}

	public void handler(String key){
		menu menu = new menu(this, console);
		scanner.nextLine();
		switch (key) {
			case "A" :
				addItemHandler();
				break;
			case "D" :
				deleteItemHandler();
				break;
			case "L" :
				showTaskList();
				
				break;
			case "E" :
				exitHandler();
				break;
		}
		
		scanner.nextLine();

		while(fContinue)
		{
			console.clear();
			menu.show();
		}
	}
	
	public void showTaskList(){
		for(int i=0; i < tasks.size(); i++){
			task t = tasks.get(i);
			System.out.println((i+1) + ". " + t.get_text());	
		}
	}
	
	public void addItemHandler(){
		String inputLine = scanner.nextLine();
		
		if(inputLine != ""){
			tasks.add(new task(inputLine));
			System.out.println("Added  with id# " + tasks.size());
		}
	}
	
	public void deleteItemHandler(){
		int id = scanner.nextInt();
		
		if(id > 0){
			tasks.remove(id-1);
			System.out.println("Added  with id# " + tasks.size());
		}
		showTaskList();
	}

	public void exitHandler(){
		fContinue = false;
	}
	
	public static void main(String[] args) {

		TaskListApp app = new TaskListApp();
		console = new JavaConsole();
		scanner = new Scanner(System.in);
		menu menu = new menu(app, console);

		menu.add("A", "Add tasks to the list");
		menu.add("D", "Mark the task done");
		menu.add("L", "Display list of tasks");
		menu.add("E", "Exit");

		while(fContinue){
			console.clear();
			System.out.println("Please choose an option.");
			menu.show();
		}
		System.exit(0);
	}

}
