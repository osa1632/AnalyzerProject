package Analyzer;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FrequenyDistribution {
	String tokens[];
	List<String> blackList;
	Map<String, List<Integer>> patterns;
	Map<String, List<Integer>> newPatterns;
	Map<String, AbstractMap.SimpleEntry<Integer,List<Integer>>> allPatterns;

	public FrequenyDistribution(String text) {
		tokens = text.split("\\s+");
		allPatterns = new HashMap<String, AbstractMap.SimpleEntry<Integer,List<Integer>>>();
	}

	public  List<String> getSortedOccurencies() {

		initPatternsMap();

		boolean hasChange = true;
		int seqLength = 1;
		while (hasChange)
		{
			generateLongerDistribionMap(seqLength);
			seqLength++;
			hasChange = (patterns.size()>0);
		}

		insertToAllPatternsAndReinit(seqLength);

		return getSortedDistribution();
	}

	private void generateLongerDistribionMap(int seqLength) {
		insertToAllPatternsAndReinit(seqLength);

		for (String key:patterns.keySet())
		{
			List<Integer> indices = patterns.get(key);

			for (Integer index:indices)
			{
				if (index+1>=tokens.length)
				{
					continue;
				}
				String newKey = key+" "+tokens[index+1];
				List<Integer> newIndices =  newPatterns.getOrDefault(newKey, new LinkedList<Integer>());

				newIndices.add(index+1);

				newPatterns.put(newKey, newIndices);
			}
		}

		removeOneTimesPatterns();
	}

	private void insertToAllPatternsAndReinit(int seqLength) {

		for (String key:newPatterns.keySet())
		{
			allPatterns.put(key, new AbstractMap.SimpleEntry<Integer, List<Integer>>(seqLength, newPatterns.get(key)));
		}
		//allPatterns.putAll(newPatterns);
		patterns = new HashMap<String,List<Integer>>();
		patterns.putAll(newPatterns);
		newPatterns = new HashMap<String,List< Integer>>();
	}

	private List<String> getSortedDistribution()
	{
		List<String> sortedList = new LinkedList<String>();
		sortedList.addAll(allPatterns.keySet());

		sortedList.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				AbstractMap.SimpleEntry<Integer, List<Integer>> valO1;
				AbstractMap.SimpleEntry<Integer, List<Integer>> valO2;

				valO1 = allPatterns.get(o1);
				valO2 = allPatterns.get(o2);


				if (valO1.getKey() == valO2.getKey())
				{
					return -Integer.compare(valO1.getValue().size(),valO2.getValue().size());
				}
				else 
				{
					return  -Integer.compare(valO1.getKey() , valO2.getKey());
				}

			}
		});

		return sortedList;
	}

	private void initPatternsMap() {
		newPatterns = new HashMap<String,List<Integer>>();

		for (int i=0;i<tokens.length;i++)	
		{
			String key = tokens[i];
			if (key=="")
			{
				continue;
			}
			List<Integer> indices =  newPatterns.getOrDefault(key, new LinkedList<Integer>());

			indices.add(i);

			newPatterns.put(key, indices);
		}
		
		List<String> tempList = new LinkedList<String>();
		tempList.addAll(newPatterns.keySet());
		
		tempList.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				
				return Integer.compare(newPatterns.get(o2).size(), newPatterns.get(o1).size());
			}
		});
		
		
		removeOneTimesPatterns();
	}

	private void removeOneTimesPatterns() {
		List<String> toDeleteList = new LinkedList<String>();
		for (String key:newPatterns.keySet())
		{
			if (newPatterns.get(key).size()<=1)
			{
				toDeleteList.add(key);
			}
		}

		for (String key:toDeleteList)
		{
			newPatterns.remove(key);
		}
	}


	public List<String> cleanResult(List<String> results)
	{
		List<String> tokensKey = new LinkedList<String>();

		List<String> blackList = new LinkedList<String>();
		for (String key:allPatterns.keySet())
		{
			if (allPatterns.get(key).getKey() ==1)
			{
				tokensKey.add(key);
			}
		}


		tokensKey.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {

				return -Integer.compare(
						allPatterns.get(o1).getValue().size(), 
						allPatterns.get(o2).getValue().size());
			}
		});
		
		for (int i=0;i<tokensKey.size()/20;i++)
		{
			blackList.add(tokensKey.get(i));
		}

		List<String> toDeleteList = new LinkedList<String>();
		for (String key:results)
		{
			boolean toDelete=false;
			for (String token:blackList)
			{
				if (allPatterns.containsKey(key+" "+token))
				{
					toDeleteList.add(key+" "+token);
				}
				if (allPatterns.containsKey(token+" "+key))
				{
					toDeleteList.add(token+" "+key);
				}
				if (key.startsWith(token+" ") ||key.endsWith(" "+token))
				{
					toDelete=true;
				}
			}
			if (!toDelete)
			{
				for (String result:results)
				{
					if (!result.equals(key) && (result.startsWith(key+" ")||result.endsWith(" "+key)))
					{
						toDelete=true;
					}
				}
			}
			if (toDelete){toDeleteList.add(key);}
			
		}
		results.removeAll(toDeleteList);
		results.removeAll(tokensKey);

		return results;
	}
}
