import java.awt.Color;
import java.awt.Font;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;


public class MenuItem {
	private Boolean mouseOverItem;

	  private Boolean mouseClickingItem;

	  private UnicodeFont uFont;

	  private String text;

	  private Color textColor;

	  private Color backgroundColor;
	  
	  private Color mouseOverBackgroundColor;

	  private Color clickedColor;
	  
	  private float x;
	  
	  private float y;
	  
	  private float width;
	  
	  private float height;

	  public MenuItem() {
	  }

	  /** 
	   *  Used for loading java fonts
	   */
	  public MenuItem(String text, Color textColor, Color backgroundColor, Color mouseOverBackgroundColor, Color clickedColor, float x, float y, float width, float height, Font font, int size, boolean bold,boolean italic) {
		  this.text = text;
		  this.textColor = textColor;
		  this.backgroundColor = backgroundColor;
		  this.mouseOverBackgroundColor = mouseOverBackgroundColor;
		  this.clickedColor = clickedColor;
		  this.x = x;
		  this.y = y;
		  this.width = width;
		  this.height = height;
		  this.uFont = new UnicodeFont(font,size,bold,italic);
		  this.uFont.addAsciiGlyphs();
		  uFont.getEffects().add(new ColorEffect(textColor)); 
		  try {
			this.uFont.loadGlyphs();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  this.mouseClickingItem = false;
		  this.mouseOverItem = false;
	  }

	  /**
	   * Used for load ttf font from file
	   */
	  public MenuItem(String text, Color textColor, Color backgroundColor, Color mouseOverBackgroundColor, Color clickedColor, float x, float y, float width, float height, String fontFileName, int size,boolean bold,boolean italic) {
		  this.text = text;
		  this.textColor = textColor;
		  this.backgroundColor = backgroundColor;
		  this.mouseOverBackgroundColor = mouseOverBackgroundColor;
		  this.clickedColor = clickedColor;
		  this.x = x;
		  this.y = y;
		  this.width = width;
		  this.height = height;
		  try {
			this.uFont = new UnicodeFont(fontFileName,size,bold,italic);
			this.uFont.addAsciiGlyphs();
			uFont.getEffects().add(new ColorEffect(textColor)); 
			this.uFont.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		  this.mouseClickingItem = false;
		  this.mouseOverItem = false;
	  }

	  public Boolean isMouseOver() {
		  return mouseOverItem;
	  }

	  public Boolean isClicked() {
		  return mouseClickingItem;
	  }

	  public void events() {
	     int mouseX = Mouse.getX();
	     
		 int mouseY = Mouse.getY();
			
		 mouseY = (Mouse.getY() + 600) - (Mouse.getY()*2);
		 
			
		 if((mouseX >= x) && (mouseX <= (x + width)) && (mouseY > y) && (mouseY < (y + height))) {
	        mouseOverItem = true;
	        //System.out.println("j");
	        if(Mouse.next()) {
	        	if(Mouse.getEventButton() == 0) {
	        		if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState() == true) {
	        			mouseClickingItem = true;
	        		}
	        	} else {
	        		mouseClickingItem = false;
	        	}
	        } else {
				mouseClickingItem = false;
	        }
		}
		else {
			mouseOverItem = false;
			mouseClickingItem = false;
		}
	  }

	  public void render() {
		  
			if(mouseOverItem == true) {
				
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_FILL);
				GL11.glColor3f((float)this.mouseOverBackgroundColor.getRed()/255.0f,(float)this.mouseOverBackgroundColor.getGreen()/255.0f,(float)this.mouseOverBackgroundColor.getBlue()/255.0f);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(x , y);
				GL11.glVertex2f(x , y + height);
				GL11.glVertex2f(x + width , y + height);
				GL11.glVertex2f(x + width,y);
				GL11.glEnd();
			}
			
			if(mouseClickingItem == true) {
				
			}
			
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_LINE);
			GL11.glColor3f(0.0f,0.0f,0.0f);
			
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(x , y);
			GL11.glVertex2f(x , y + height);
			GL11.glVertex2f(x + width , y + height);
			GL11.glVertex2f(x + width,y);
			GL11.glEnd();
			
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_FILL);
	     
	     int textWidth = uFont.getWidth(text);
	     int textHeight = uFont.getHeight(text);
	     
	     GL11.glEnable(GL11.GL_TEXTURE_2D);
	     GL11.glEnable(GL11.GL_BLEND);
		 GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	   
	     uFont.drawString(x+((width-textWidth)/2),y+((height-textHeight)/2),text);
	     
	     GL11.glDisable(GL11.GL_BLEND);
	     GL11.glDisable(GL11.GL_TEXTURE_2D); 
	  }

	  public void setText(String text) {
		  this.text = text;
	  }

	  public void setTextColor(Color textColor) {
		  this.textColor = textColor;
	  }

	  public void setBackgroundColor(Color backgroundColor) {
		  this.backgroundColor = backgroundColor;
	  }
	  
	  public float getX() {
		  return x;
	  }
	  
	  public float getY() {
		  return y;
	  }
	  
	  public float getWidth() {
		  return width;
	  }
	  
	  public float getHeight() {
		  return height;
	  }
}
