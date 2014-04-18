import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class TextBox {

	private String text;
	
	private ArrayList<String> keysToPoll;
	
	private int x;
	
	private int y;
	
	private int width;
	
	private int height;
	
	private UnicodeFont uFont;
	
	private boolean isSelected;
	
	private Color textColor;
	
	private int characterLimit;
	
	
	public TextBox() {
		text = "";
	}
	
	public TextBox(Color textColor,String text,int x,int y,int width,int height,Font font,int size,boolean bold,boolean italic,ArrayList<String> keysToPoll,
			int characterLimit) {
		this.textColor = textColor;
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isSelected = false;
		this.keysToPoll = keysToPoll;
		this.characterLimit = characterLimit;
		this.uFont = new UnicodeFont(font,size,bold,italic);
		  this.uFont.addAsciiGlyphs();
		  uFont.getEffects().add(new ColorEffect(textColor)); 
		  try {
			this.uFont.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
	}
	}
	
	public void events() {
		int mouseX = Mouse.getX();
		
		 int mouseY = Mouse.getY();
			
		 mouseY = (Mouse.getY() + 600) - (Mouse.getY()*2);
		
		 if(Mouse.isButtonDown(0)) {
	        if((mouseX >= x) && (mouseX <= (x + width)) && (mouseY >= y) && (mouseY <= (y + height))) {
			   isSelected = true;	
			}
	        else {
	           isSelected = false;
	        }
		 } 
		 
		 if(isSelected == true) {
			 while(Keyboard.next()){
				 if(Keyboard.getEventKeyState()) {
					 for(String b : keysToPoll) {
						 if(!b.equals("BACK")) {
							 if(Keyboard.getEventKey() == Keyboard.getKeyIndex(b)) {
								 if((text.length() + 1) <= characterLimit) {
									 text += b;
								 }
							 }
						 } else {
							 if(Keyboard.getEventKey() == Keyboard.getKeyIndex(b)) {
								 if((text.length() >= 1)) {
									 text = text.substring(0, text.length() - 1);
								 }
							 }
						 }
					 }
				 }
			 }
		 }
	}
	
	public void render() {
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_LINE);
		GL11.glColor3f(0.0f,0.0f,0.0f);
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x , y);
		GL11.glVertex2f(x , y + height);
		GL11.glVertex2f(x + width , y + height);
		GL11.glVertex2f(x + width,y);
		GL11.glEnd();
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK,GL11.GL_FILL);
	    
		int textHeight = uFont.getHeight(text);
	     
	     GL11.glEnable(GL11.GL_TEXTURE_2D);
	     GL11.glEnable(GL11.GL_BLEND);
		 GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	   
	     uFont.drawString(x,y + height - ((height + textHeight)/2),text);
	     
	     GL11.glDisable(GL11.GL_BLEND);
	     GL11.glDisable(GL11.GL_TEXTURE_2D); 
	     
		if(isSelected == true) {
			//render cursor
			int textWidth = uFont.getWidth(text);
			GL11.glColor3f(0.0f,0.0f,0.0f);
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2f((x+2)+textWidth,y + 5);
			GL11.glVertex2f((x+2)+textWidth,y + (height - 5));
			GL11.glEnd();
		}
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public int getHeight() {
		return height;
	}
}
