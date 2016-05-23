package Analyzer;


public class Analyzer {

	public static void main(String[] args) {
		String token =args[0];
		String url = args[1];
		String text = (new DataExtractor(token).getText(url));

		FrequenyDistribution frequenyDistribution = new FrequenyDistribution(text);

		for (String result:frequenyDistribution.cleanResult(frequenyDistribution.getSortedOccurencies()))
		{
			System.out.println(result);
		}
	}
}
