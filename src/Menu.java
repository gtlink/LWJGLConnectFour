import java.util.ArrayList;


public class Menu {
	private ArrayList<MenuItem> itemList;

	  public Menu() {
		  itemList = new ArrayList<MenuItem>(10);
	  }

	  public void addItem(MenuItem m) {
		  itemList.add(m);
	  }

	  public void addItem(Integer index, MenuItem m) {
		  itemList.add(index, m);
	  }

	  public void removeItem(Integer index) {
		  itemList.remove(index);
	  }

	  public MenuItem getItem(Integer index) {
		  return itemList.get(index);
	  }

	  public void events() {
		  for(MenuItem m: itemList) {
			  m.events();
		  }
	  }

	  public void render() {
		  for(MenuItem m: itemList) {
			  m.render();
		  }
	  }
}
