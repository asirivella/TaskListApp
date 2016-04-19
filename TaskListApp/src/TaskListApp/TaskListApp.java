package TaskListApp;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;

import console.JavaConsole;


public class TaskListApp {

	private boolean fContinue = true;
	private static JavaConsole console;
	private ArrayList<task> tasks = new ArrayList<task>();
	private Map<String, MenuItem> menu = new HashMap<String, MenuItem>();
	
	public class task{
		private String _text;
		
		public task(String text){
			_text = text;
		}
		
		public String get_text(){
			return _text;
		}
	}
	
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

	public void addMenuItem(String key, String text) {
		menu.put(key, new MenuItem(key, text));
	}

	public void addTask(String text) {
		tasks.add(new task(text));
	}
	
	public void clearConsole(){
		console.clear();
	}

	public void showMenu() {
		console.clear();
		String input = "";
		String choosen = "";
		String theRest = "";
		String[] inputArr;
		if(!fContinue) return;
		Scanner in = new Scanner(System.in);
		
		System.out.println("Please choose an option :");

		for(Map.Entry<String, MenuItem> item : menu.entrySet()){
			System.out.printf(" [%s] %s \n", 
					item.getKey(), item.getValue().get_text());
		}

		System.out.println();
		System.out.printf("Input: ");

		try {
			input = in.nextLine(); 
		} catch (Exception e1) { 
			e1.printStackTrace();
		}
		
		inputArr = input.split("\\s+", 2);
		if(inputArr.length > 0) choosen = inputArr[0];
		if(inputArr.length > 1) theRest = inputArr[1];
		
		if (choosen != "" && menu.containsKey(choosen)) {	
			handler(choosen, theRest);	
		}else {	
			System.out.println("Invalid option.");	
		}
		
		System.out.print("\nPress enter to continue... ");
		in.nextLine();

		while(fContinue)
		{
			console.clear();
			showMenu();
		}
		in.close();
	}

	public void handler(String key, String value){
		
		switch (key) {
			case "A" :
				addItemHandler(value);
				break;
			case "D" :
				deleteItemHandler(value);
				break;
			case "L" :
				showTaskList();
				break;
			case "E" :
				exitHandler();
				break;
		}
	}
	
	public void showTaskList(){
		System.out.println();
		System.out.println("Output: ");
		for(int i=0; i < tasks.size(); i++){
			task t = tasks.get(i);
			System.out.println((i+1) + ". " + t.get_text());	
		}
	}
	
	public void addItemHandler(String val){
		String str = val;
		if(str != ""){
			tasks.add(new task(str));
			System.out.println("Output: Added  with id# " + tasks.size());
		}
	}
	
	public void deleteItemHandler(String val){
		String str = val;
		int id = Integer.parseInt(str);
		
		if(id > 0){
			tasks.remove(id-1);
		}
		showTaskList();
	}

	public void exitHandler(){
		System.out.println("Bye! See you :) ");
		fContinue = false;
		
		// Adding Sleep just to make the end message visible to user.
		try {
			Thread.sleep(500);
	    } catch (InterruptedException ie) {
	    	ie.printStackTrace();
	    }
		System.exit(0);
	}
	
	public static void main(String[] args) {

		TaskListApp app = new TaskListApp();
		console = new JavaConsole();

		app.addMenuItem("A", "Add tasks to the list");
		app.addMenuItem("D", "Mark the task done");
		app.addMenuItem("L", "Display list of tasks");
		app.addMenuItem("E", "Exit");
		
		// Sample tasks
		app.addTask("Sample Task-1");
		app.addTask("Sample Task-2");
		app.addTask("Sample Task-3");
		app.addTask("Sample Task-4");

		console.clear();
		app.showMenu();
		System.exit(0);
	}
}