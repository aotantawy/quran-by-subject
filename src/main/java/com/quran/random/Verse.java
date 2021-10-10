package com.quran.random;

public class Verse {

	private String category;
	private String subcategory;
	private String surah;
	private String verse;

	public Verse(String category, String subcategory, String surah, String verse) {
		this.category = category;
		this.subcategory = subcategory;
		this.surah = surah;
		this.verse = verse;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public String getSurah() {
		return surah;
	}

	public void setSurah(String surah) {
		this.surah = surah;
	}

	public String getVerse() {
		return verse;
	}

	public void setVerse(String verse) {
		this.verse = verse;
	}

	@Override
	public String toString() {
		return "Verse [category=" + category + ", subcategory=" + subcategory + ", surah=" + surah + ", verse=" + verse
				+ "]";
	}

}
