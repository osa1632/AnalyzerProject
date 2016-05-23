package Analyzer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Url {
	public static String getUrl(String url) {
		URL urlObj;
		try {
			urlObj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));

			String inputLine;
			String response = "";

			while ((inputLine = in.readLine()) != null) {
				response+=inputLine;
			}

			in.close();
			return response.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
