package mnuber.wordgraph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.util.Calendar;	
import java.util.Random;

/**
 * WordCloud is a concrete implementation of AbstractWordGraph, a class used to create word graphs
 * WordCloud parses a text file selected by the user and lays them out in a cloud formation,
 * which is then rendered to an image and exported to the directory of the text file.
 * Examples are included in the samples folder.
 * 
 * @author mnuber
 */
public class WordCloud extends AbstractWordGraph {
	
	//Red, white, gray.
	//Color[] colors = {Color.red, Color.white, Color.gray};
	//Green, brown, blue (earth colors)
	//Color[] colors = {new Color(60, 126, 17), new Color(101, 67, 30),new Color(36, 102, 202) };
	//Red, white and, blue
	Color[] colors = {new Color(30, 90, 240), Color.white, Color.red};
	
	private int maxWords, height, width;
	

	
	/**
	 * Constructs a WordCloud object that creates a seeded random object
	 */
	public WordCloud() {
		super();
		//Expand on the excluded words from the parent class.
		this.excludedWords += "i,am,as,it,we,for,our,s,i,i'm,it's,its,do";
		random = new Random(Calendar.getInstance().getTimeInMillis());		
	}
	
	/** Renders a word cloud from a text file
	 * @param width the width of the final image
	 * @param height the height of the final image
	 * @param fontsize the maximum fontsize
	 * @param maxWords the maximum amount of words to be rendered
	 */
	public void renderCloud(int height, int width, int fontsize, int maxWords) {
		this.height = height;
		this.width = width;
		System.out.println(maxWords);
		parseFile(openFile("./"));
		this.maxWords = (wordMapList.size() < maxWords ? wordMapList.size() : maxWords);

		normalizeFontSizes(maxWords, fontsize);
		System.out.println(this.maxWords);

		output(width,height);
		System.out.println(this.maxWords);

	}

	/**
	 * Sets the color of each word and assigns it a font.
	 * Each font is custom because it generates a unique TextLayout object
	 * Which allows us to use the exact bounding box of the words when placing 
	 */
	private void setColorAndFont() {
		for(int i = 0; i < maxWords; i++) {
			Word word = wordMapList.get(i);
			Color color;
			if(i%3 == 0)
				color = colors[0];
			else if(i%2 == 0)
				color = colors[1];
			else
				color = colors[2];
			word.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255*(maxWords - i)/maxWords));
			word.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255));

			Font font = new Font("Times New Roman", Font.PLAIN, word.getCount());
			
			TextLayout textLayout = new TextLayout(word.getText(), font, new FontRenderContext(new AffineTransform(), false, false));
			word.setFont(font);
			word.setBounds(textLayout.getBounds().getBounds());
		}
	}

	/**This method creates a layout for rendering
	 * Each word is placed as close as possible
	 * to the center of the image without overlapping
	 */
	@Override
	protected void createLayout() {
		setColorAndFont();

		Point middle = new Point(width/2,height/2);
		for(int i = 0; i < maxWords; i++) {
			//System.out.println(i);
			Word word = wordMapList.get(i);
			if(i == 0) {
				word.moveTo(width/2 - word.getWidth()/2, height/2 - word.getHeight()/2);
			}
			else {
				boolean freespace = false;
				word.moveTo(middle.x, middle.y);
	
				int attempts = 0;
				while(!freespace) {
					attempts++;
					word.translate(random.nextInt()%9, random.nextInt()%9);
					freespace = true;
					//Sort through the placed words until we find an opening
					if(new Rectangle(0,0,width,height).contains(word.getBounds())) {
						for(int j = 0; j < i; j++) {
							if(word.getBounds().intersects( wordMapList.get(j).getBounds()) 
									&& word!= wordMapList.get(j)) {
										freespace = false;
							}	
						}
					}
					else
						freespace = false;
					if(attempts > 1000000) {
						word.moveTo(middle.x, middle.y);
						attempts = 0;
					}
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see mnuber.wordgraph.AbstractWordGraph#render(java.awt.Graphics2D, int, int)
	 */
	@Override
	protected void render(Graphics2D graphics, int width, int height) {
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, width, height);
		
		//Write the words

		System.out.println(wordMapList.size());
		for(int i = 0; i < maxWords; i++) {
			Word word = wordMapList.get(i);
			graphics.setColor(word.getColor());
			graphics.setFont(word.getFont()); 		
			graphics.drawString(word.getText(), word.getX(), word.getY()+word.getHeight());
		}
	}
}
