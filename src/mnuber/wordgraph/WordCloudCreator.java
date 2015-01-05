package mnuber.wordgraph;

/**
 * @author mnuber
 * Used to demonstrate WordCloud, a word graph creator.
 */
public class WordCloudCreator {

	public static void main (String[] args){
		WordCloud cloud = new WordCloud();
		cloud.renderCloud(800, 600, 120, 80);
	}
}
