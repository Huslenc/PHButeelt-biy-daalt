package flashcard;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks and awards achievements during a flashcard session.
 */
public class AchievementTracker {

    private static final double SPEED_THRESHOLD_SECONDS = 5.0;
    private static final int REPEAT_THRESHOLD = 5;
    private static final int CONFIDENT_THRESHOLD = 3;

    private boolean allCorrectThisRound;
    private final List<String> earned = new ArrayList<>();

    /** Call at the start of each round. */
    public void startRound() {
        allCorrectThisRound = true;
    }

    /**
     * Records the result of one answer.
     *
     * @param card           the card that was answered
     * @param correct        whether the answer was correct
     * @param elapsedSeconds time taken to answer
     */
    public void recordAnswer(FlashCard card, boolean correct, double elapsedSeconds) {
        if (!correct) {
            allCorrectThisRound = false;
        }

        if (correct && elapsedSeconds < SPEED_THRESHOLD_SECONDS) {
            awardOnce("SPEED: 5 sekunded bagtaj hariullaaa!");
        }
        if (card.getTotalAnswered() > REPEAT_THRESHOLD) {
            awardOnce("REPEAT: Neg kartad 5aas olon udaa hariullaa!");
        }
        if (card.getCorrectCount() >= CONFIDENT_THRESHOLD) {
            awardOnce("CONFIDENT: Neg kartiig dor hayj 3udaa!");
        }
    }

    /** Call at the end of each round. */
    public void endRound() {
        if (allCorrectThisRound) {
            awardOnce("CORRECT: Buh kartiig zov hariullaa!");
        }
    }

    private void awardOnce(String achievement) {
        if (!earned.contains(achievement)) {
            earned.add(achievement);
            System.out.println("  Amjilt: " + achievement);
        }
    }

    /**
     * Returns all earned achievements.
     *
     * @return list of achievement strings
     */
    public List<String> getEarned() {
        return new ArrayList<>(earned);
    }
}
