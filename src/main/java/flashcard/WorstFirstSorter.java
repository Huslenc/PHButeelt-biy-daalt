package flashcard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Organizes flashcards so the most-failed cards appear first.
 */
public class WorstFirstSorter implements CardOrganizer {

    @Override
    public List<FlashCard> organize(List<FlashCard> cards) {
        List<FlashCard> sorted = new ArrayList<>(cards);
        sorted.sort(Comparator.comparingInt(FlashCard::getIncorrectCount).reversed());
        return sorted;
    }
}
