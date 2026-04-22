package flashcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Organizes flashcards so that cards answered incorrectly
 * in the previous round appear first.
 * The relative order within each group is preserved.
 */
public class RecentMistakesFirstSorter implements CardOrganizer {

    private final Set<FlashCard> recentMistakes = new HashSet<>();

    /**
     * Marks a card as a recent mistake.
     *
     * @param card the card answered incorrectly
     */
    public void addMistake(FlashCard card) {
        recentMistakes.add(card);
    }

    /**
     * Clears recorded mistakes (call after organize, before next round).
     */
    public void clearMistakes() {
        recentMistakes.clear();
    }

    @Override
    public List<FlashCard> organize(List<FlashCard> cards) {
        List<FlashCard> mistakes = new ArrayList<>();
        List<FlashCard> others = new ArrayList<>();

        for (FlashCard card : cards) {
            if (recentMistakes.contains(card)) {
                mistakes.add(card);
            } else {
                others.add(card);
            }
        }
        Collections.reverse(mistakes);
        List<FlashCard> result = new ArrayList<>();
        result.addAll(mistakes);
        result.addAll(others);
        return result;
    }
}
