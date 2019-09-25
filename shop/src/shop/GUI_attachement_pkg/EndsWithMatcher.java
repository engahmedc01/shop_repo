package shop.GUI_attachement_pkg;


public class EndsWithMatcher implements SuggestMatcher {
	@Override
	public boolean matches(String dataWord, String searchWord) {
		return dataWord.endsWith(searchWord);
	}
}