package menu;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import console.JavaConsole;
import TaskListApp.*;

public class menu {

	private TaskListApp app;
	private JavaConsole console;
	
	public menu(TaskListApp ap, JavaConsole c) {
		app = ap;
		console = c;
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

	private Map<String, MenuItem> Items = new HashMap<String, MenuItem>();
	
	public void add(String key, String text) {
		Items.put(key, new MenuItem(key, text));
	}
	
	public void clearConsole(){
		console.clear();
	}

	public void show() {
		console.clear();
		String choosen = "";
		Scanner in = new Scanner(System.in);

		for(Map.Entry<String, MenuItem> item : Items.entrySet()){
			System.out.printf(" [%s] %s \n", item.getKey(), item.getValue().get_text());
		}

		System.out.println();

		try {
			choosen = in.next(); 
		} catch (Exception e1) { /* Ignore non numeric and mixed */ }

		if (Items.containsKey(choosen)) {
			MenuItem mi = Items.get(choosen);
			app.handler(choosen);
		} else {
			System.out.println("Invalid option.\nPress enter to continue...");
			in.nextLine();
			show();
		}
		in.close();
	}
}