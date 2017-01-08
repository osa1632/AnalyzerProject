package Analyzer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class DataExtractor {
	private final String token;
	private final String diffBotUrl = "http://api.diffbot.com/v3/article";
	
	public DataExtractor(String token) {
		this.token = token;
	}
	
	private String buildDiffBotUrl(String urlRequested)
	{
		String url = diffBotUrl;
		url+="?";
		url+="token="+token;
		String encodedUrlRequested;
		
		try {
			encodedUrlRequested = URLEncoder.encode(urlRequested,"UTF-8");

		} catch (UnsupportedEncodingException e) {
			encodedUrlRequested = "";
		}
		url+="&url="+encodedUrlRequested;

		return url;
	}


	
	public String getText(String url)
	{
		String urlData= Url.getUrl(buildDiffBotUrl(url));

		JsonParser jp = new JsonParser(); //from json
		JsonElement root = jp.parse(urlData); //Convert the input stream to a json element
		JsonObject rootobj = root.getAsJsonObject();
		JsonArray jsonArray = rootobj.get("objects").getAsJsonArray();

		String title=null;
		String text=null;

		for (int i=0;i<jsonArray.size();i++)
		{
			JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
			JsonElement jsonTitle = jsonObject.get("title");
			JsonElement jsonText = jsonObject.get("text");

			if (!jsonTitle.isJsonNull())
			{
				title=jsonTitle.getAsString();
			}

			if (!jsonText.isJsonNull())
			{
				text=jsonText.getAsString();
			}

		}

		return title+"\n"+text;
	}
	
	
}

