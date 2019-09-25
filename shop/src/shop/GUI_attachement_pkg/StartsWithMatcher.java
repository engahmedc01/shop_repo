package shop.GUI_attachement_pkg;


public class StartsWithMatcher implements SuggestMatcher {
	@Override
	public boolean matches(String dataWord, String searchWord) {
		return dataWord.startsWith(searchWord);
	}
}