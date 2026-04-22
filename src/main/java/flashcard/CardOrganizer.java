package flashcard;

import java.util.List;

/**
 * Interface for organizing/sorting flashcards before a study round.
 */
public interface CardOrganizer {

    /**
     * Organizes the given list of flashcards.
     *
     * @param cards the cards to organize
     * @return reordered list
     */
    List<FlashCard> organize(List<FlashCard> cards);
}
