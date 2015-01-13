package mnuber.wordgraph;

/**
 * @author mnuber
 * Used to demonstrate WordCloud, a word graph creator.
 */
public class WordCloudCreator {

	public static void main (String[] args){
		WordCloud cloud = new WordCloud();
		cloud.excludedWords += "Applause";
		cloud.renderCloud(330*24,330*36, 1600, 200);
	}
}

