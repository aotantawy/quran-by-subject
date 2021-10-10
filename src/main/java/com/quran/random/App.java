package com.quran.random;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class App {

	public static final String URL = "https://quranbysubject.com/";

	public static boolean isBlankLine(String line) {
		if (line.isBlank())
			return true;
		return false;
	}

	public static boolean isSurahName(Element line) {
		if (line.tagName().equals("h4"))
			return true;
		return false;
	}

	public static String getPureSubcategory(String subcategory) {
		int colonIndex = subcategory.indexOf(':');
		return subcategory.substring(colonIndex + 1);
	}

	public static List<Verse> scrapeVersesPage(String category, String pageURL) throws IOException {
		List<Verse> versesList = new LinkedList<>();

		Document doc = Jsoup.connect(pageURL).get();

		Elements pageData = doc.select("#Quran a[onclick*=\"تفسير الآية\"], #Quran .ayanum, #Quran h4, #Quran br");
		Elements subcategory = doc.select(".wrapper .row .panel h3");

		String subcategoryStr = getPureSubcategory(subcategory.get(0).text().trim());
		String surahName = "";
		String verses = "";

		for (Element element : pageData) {
			String lineStr = element.text().trim();

			if (isSurahName(element)) {
				surahName = lineStr;
				continue;
			}

			if (!isBlankLine(verses) && isBlankLine(lineStr)) {
				Verse verseObj = new Verse(category, subcategoryStr, surahName, verses);
				versesList.add(verseObj);
				verses = "";
				continue;
			}

			verses += lineStr;
		}

		return versesList;

	}

	public static void writeObjectsToFile(List<Verse> verseList, String fileName) {

		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

		try {
			writer.writeValue(new FileWriter(fileName), verseList);
		} catch (Exception e) {
			System.out.println("Error while writing objects " + e.getMessage());
		}
	}

	public static void main(String[] args) throws IOException {

		List<Verse> versesList = new LinkedList<>();
		int userCounter = 1;

		Document index = Jsoup.connect(URL + "categories.php").get();

		Elements categories = index.select("h4");

		for (Element element : categories) {
			String category = element.text().trim();

			System.out.println("Scannning ...");

			Elements subcategoryLinks = index.select("#users" + Integer.toString(userCounter) + " .name");

			for (Element subcategory : subcategoryLinks) {
				String pageURL = URL + subcategory.attr("href");

				versesList.addAll(scrapeVersesPage(category, pageURL));
			}

			userCounter++;

		}

		System.out.println("writing objects to the file");
		writeObjectsToFile(versesList, "output.json");
		System.out.println("Done");

	}
}