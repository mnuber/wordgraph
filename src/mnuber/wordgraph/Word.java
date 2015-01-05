package mnuber.wordgraph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Calendar;
import java.util.Random;

/**
 * Word is a basic data structure for handling words in AbstractWordGraph
 * Word contains the text and counter properties that are required
 * for sorting and displaying a word graph.
 * Word also has a Rectangle object that can be manipulated to change
 * the location of text glyphs on a graphics context. 
 * 
 * @author mnuber
 */

public class Word implements Comparable<Word>{
	
	private String text = "";
	private int count = 0;
	private Color color;
	private Font font;
	private Rectangle bounds;
	
	/**
	 * Constructor for creating a new instance of Word
	 * 
	 * @param text the text of the word
	 */
	public Word(String text){
		this(text, 1);
	}

	/**
	 * Constructor for creating a new instance of Word with text and count.
	 * The constructor also generates a random color to prevent a NPE at render.
	 * 
	 *  @param text the text of the word
	 *  @param count the initial count of words
	 */
	public Word(String text, int count) {
		this.text = text;
		this.count = count;
		setDefaultColor();
	}

	/*
	 * Set the color of the Word for rendering.
	 */
	public void setDefaultColor(){
		Random rng = new Random(Calendar.getInstance().getTimeInMillis());
		setColor(new Color(5 + rng.nextInt(250), 
				5 + rng.nextInt(250), 
				5 + rng.nextInt(250)));
	}

	/*
	 * Mutators for color, text, font, etc.
	 */
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String string) {
		text = string;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public void addCount() {
		count++;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
		System.out.println(bounds);
	}
	
	public int getX() {
		if(bounds!=null)
			return (int)bounds.getX();
		return -1; 
	}
	
	public int getY() {
		if(bounds!=null)
			return (int)bounds.getY();
		return -1; 
	}
	
	/*
	 * Functions to move, translate, and otherwise manipulate the position 
	 * of the Word for changing the layout.
	 */
	public void translate(int x, int y) {
		bounds.setLocation(getX() + x, getY() + y);
	}
	
	public void moveTo(int x, int y) {
		bounds.setLocation(x, y);
	}

	public int getWidth() {
		return (int) bounds.getWidth();
	}
	
	public int getHeight() {
		return (int) bounds.getHeight();
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Word o) {
		/*
		 * Sort by most frequent word
		 */
		Word other = (Word)(o);
		
		if(count < other.getCount())
			return 1;
		else if (count > other.getCount())
			return -1;
		else
			return 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (other instanceof Word) {
			return getText().equals(((Word) other).getText());
		}
		else {
			return true;
		}
	}
	
	public String toString() {
		return text;
	}
	
}
