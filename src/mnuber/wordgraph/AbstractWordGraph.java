package mnuber.wordgraph;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;


/**
 * AbstractWordGraph is an abstract class used to create concrete implementations
 * of word graph generators.  WordCloud is a concrete implementation included 
 * in this package.
 * 
 * @author mnuber
 */
public abstract class AbstractWordGraph {
	
	protected static Random random;
	protected ArrayList<Word> wordMapList;
	protected int totalWords = 0;
	protected int totalUniqueWords = 0;

	/*Exclude small words that often have a very high count*/
	protected String excludedWords = " ,\n,at,the,than,a,then,in,on,off,this"
			+ ",that,of,or,and,but,too,to,was,were,by,as,also";
	
	/**
	 * Constructor 
	 */
	public AbstractWordGraph(){
		wordMapList = new ArrayList<Word>();
	}
	
	/**
	 * @param path initial path for the file chooser dialog  
	 * @return file chosen by the user.
	 */
	protected File openFile(String path) {
		JFileChooser chooser = new JFileChooser(path);
		chooser.showOpenDialog(null);
		return chooser.getSelectedFile();
	}
	
	/**
	 * Parses a file for words, which are sorted 
	 * and counted and stored as Word objects in wordMapList
	 * 
	 * @param file a text file to be parsed for words
	 */
	protected void parseFile(File file) {
		ArrayList<String> wordList = new ArrayList<String>();
		String regexPattern = "[a-zA-z']+";
		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = null;
		Scanner fileInput = null;
		
		try {
			fileInput = new Scanner(file);
			while(fileInput.hasNextLine()) {
				matcher = pattern.matcher(fileInput.nextLine());
				while(matcher.find())
					wordList.add(matcher.group().toUpperCase().replace("'", ""));
			}
		} catch (FileNotFoundException e) {
			System.out.println(file.toString() + " was not found.\nMake sure it exists.");
		}
		finally {
			fileInput.close();
		}


		/*		Remove all the excluded words before sorting */		
		wordList.removeAll(new ArrayList<String>(
				Arrays.asList(excludedWords.toUpperCase().split(","))));
		
		Collections.sort(wordList);
		totalWords = wordList.size();
 
		/*
		 * The words are sorted alphabetically so that text such as this:
		 * 'I will not and I should not'
		 * Is sorted: and,I,I,not,not,should,will
		 * Then finding the word count for each is as simple as checking
		 * if the next word matches the previous word. If so, raise the word count,
		 * otherwise, store the word and count and continue.
		 * 
		 */
		
		if(totalWords > 0) {
			String currentWord = wordList.get(0);
			int count = 0;
			for(String word : wordList) {
				if(word.equals(currentWord)) {
					count++;
					currentWord = word;
				}
				else {
					if(!currentWord.equals(""))
						wordMapList.add(new Word(currentWord, count));
					count = 1;
					currentWord = word;
				}

			}
			wordMapList.add(new Word(currentWord, count));
			totalUniqueWords = wordMapList.size();
			//Change the sort from alpha to count
			Collections.sort(wordMapList);

		}
		else {
			System.out.println("No words were found in this file.");
			System.out.println("The following: " + excludedWords + " were all excluded.");
		}
	}

	/**
	 * Outputs an image that is laid out and rendered in the concrete class.
	 * A BufferedImage graphics context is created, overridden by the concrete class,
	 * then exported as a PNG with a random file name.  
	 * 
	 * @param imageWidth the width of the image to be saved
	 * @param imageHeight the height of the image to be saved
	 */
	protected void output(int imageWidth, int imageHeight) {
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_BGR);
		Graphics2D graphics = image.createGraphics();
		createLayout();
		graphics.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		render(graphics, imageHeight, imageWidth);
		try {
			String randomFileName = "img" + Calendar.getInstance().getTimeInMillis() + ".png";
			ImageIO.write(image, "png", new File(randomFileName));
			System.out.println("File: " + randomFileName + " created." );
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	

	/**
	 * Normalizes font size so that they can be scaled to the maximum fontsize.
	 * This guarantees that the largest font used will always be known.
	 * 
	 * @param threshold the amount of words used in normalization
	 * @param multiplier the maximum fontsize after normalization
	 */
	protected void normalizeFontSizes(int threshold, int multiplier) {
		if (threshold < wordMapList.size()) {
			double normalMax = wordMapList.get(0).getCount();
			double normalMin = wordMapList.get(threshold).getCount();
			for (int i = 0; i < threshold; i++) {
				wordMapList.get(i).setCount((int)
						((wordMapList.get(i).getCount() - normalMin)/
						(normalMax - normalMin) * (multiplier - 1) + 1));
			}
		}
			
	}
	
	
	/**
	 * Create layout is called by the output function.  It should be overridden by the
	 * concrete implementation to change each Word object's bounds so that the words
	 * are rendered in a functional way.  The layout should also include 
	 * any coloring or scaling that needs to be done before rendering.
	 * 
	 */
	protected abstract void createLayout();
	
	/**
	 * Render is called during output.  The function receives an empty graphics context 
	 * Everything rendered onto the context is exported as a PNG after it completes.
	 * @param graphics a graphics2d context
	 * @param width the width of the image to be output
	 * @param height the height of the image to be output
	 */
	protected abstract void render(Graphics2D graphics, int width, int height);
	


}
