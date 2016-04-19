package TaskListApp;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;

import console.JavaConsole;


public class TaskListApp {

	private static boolean fContinue = true;
	private static JavaConsole console;
	private static Scanner scanner;
	private ArrayList<task> tasks = new ArrayList<task>();
	private Map<String, MenuItem> menu = new HashMap<String, MenuItem>();
	
	private class MenuItem {
		
		private String _key;
		private String _text;
		
		public MenuItem(String key, String text) {
			_key = key;
			_text = text;
		}

		public String get_text() {
			return _text;
		}
	}

	public void showMenu() {
		console.clear();
		String choosen = "";
		Scanner in = new Scanner(System.in);
		
		System.out.println("Please choose an option :");

		for(Map.Entry<String, MenuItem> item : menu.entrySet()){
			System.out.printf(" [%s] %s \n", item.getKey(), item.getValue().get_text());
		}

		System.out.println();

		try {
			choosen = in.next(); 
		} catch (Exception e1) { /* Ignore non numeric and mixed */ }

		if (menu.containsKey(choosen)) {
			MenuItem mi = menu.get(choosen);
			handler(choosen);
		} else {
			System.out.println("Invalid option.\nPress enter to continue...");
			in.nextLine();
			showMenu();
		}
		in.close();
	}
	
	public class task{
		private String _text;
		
		public task(String text){
			_text = text;
		}
		
		public String get_text(){
			return _text;
		}
	}

	public void addMenuItem(String key, String text) {
		menu.put(key, new MenuItem(key, text));
	}

	public void addTask(String text) {
		tasks.add(new task(text));
	}
	
	public void clearConsole(){
		console.clear();
	}
	
	public void handler(String key){
		
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
			showMenu();
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
		String inputLine = scanner.nextLine();
		System.out.println("delete input line : " + inputLine);
		int id = Integer.parseInt(inputLine);
		System.out.println("delete item handler : " + id);
		
		if(id > 0){
			tasks.remove(id-1);
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

		app.addMenuItem("A", "Add tasks to the list");
		app.addMenuItem("D", "Mark the task done");
		app.addMenuItem("L", "Display list of tasks");
		app.addMenuItem("E", "Exit");
		
		// Sample tasks
		app.addTask("Task-1");
		app.addTask("Task-2");
		app.addTask("Task-3");
		app.addTask("Task-4");

		while(fContinue){
			console.clear();
			app.showMenu();
		}
		System.exit(0);
	}
}