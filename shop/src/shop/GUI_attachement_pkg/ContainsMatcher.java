package shop.GUI_attachement_pkg;



public class ContainsMatcher implements SuggestMatcher {
	@Override
	public boolean matches(String dataWord, String searchWord) {
		return dataWord.contains(searchWord);
	}
}