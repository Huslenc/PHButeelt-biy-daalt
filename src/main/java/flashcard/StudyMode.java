package flashcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles a study session — asks questions and checks answers.
 */
public class StudyMode {

    // REMOVED: private static final int REPETITIONS = 1;  // Энийг хасах

    private final List<FlashCard> cards;
    private final Scanner scanner;
    private final CardStorage storage;

    /**
     * Constructs a StudyMode.
     *
     * @param cards   the deck to study
     * @param scanner shared input scanner
     * @param storage storage to save progress after session
     */
    public StudyMode(List<FlashCard> cards, Scanner scanner, CardStorage storage) {
        this.cards = cards;
        this.scanner = scanner;
        this.storage = storage;
    }

    /**
     * Starts the study session.
     * Runs until all cards have been answered correctly REPETITIONS times.
     */
    public void start() {
        if (cards.isEmpty()) {
            System.out.println("Kart baihgui bn. Ehleed kart nemne uu");
            return;
        }

        // ========== ШИНЭ: Давталтын тоо сонгох ==========
        System.out.println();
        System.out.println("  ================================");
        System.out.println("  Hed udaa zov hariulj duusgah ve?");
        System.out.println("  ================================");
        System.out.println("  1. Neg udaa (1 toirog)");
        System.out.println("  2. Hoyor udaa (2 toirog)");
        System.out.print("  Songolt (1-2): ");
        
        int repetitions;  // Шинэ хувьсагч
        String repChoice = scanner.nextLine().trim();
        if (repChoice.equals("1")) {
            repetitions = 1;
            System.out.println("  -> Neg toirogoor duusgana.");
        } else {
            repetitions = 2;
            System.out.println("  -> Hoyor toirogoor duusgana.");
        }
        System.out.println();
        // ========== ШИНЭ хэсэг дууслаа ==========

        System.out.println("  Daraalal songono uu:");
        System.out.println("  1. Sanamsargui (random)");
        System.out.println("  2. Hamgiin muu ehend (worst-first)");
        System.out.println("  3. Suuliin buruu ehend (recent-mistakes-first)");
        System.out.print("  Songolt (1-3): ");

        String choice = scanner.nextLine().trim();
        CardOrganizer organizer;
        RecentMistakesFirstSorter recentSorter = null;

        switch (choice) {
            case "2":
                organizer = new WorstFirstSorter();
                break;
            case "3":
                recentSorter = new RecentMistakesFirstSorter();
                organizer = recentSorter;
                break;
            default:
                organizer = new RandomSorter();
                break;
        }

        AchievementTracker achievements = new AchievementTracker();
        Map<FlashCard, Integer> correctCounts = new HashMap<>();
        for (FlashCard card : cards) {
            correctCounts.put(card, 0);
        }

        List<FlashCard> remaining = new ArrayList<>(cards);
        int round = 1;

        while (!remaining.isEmpty()) {
            System.out.println();
            System.out.println("  ================================");
            System.out.println("  Toirog " + round + "  |  Uldsen: " + remaining.size() + " kart");
            System.out.println("  ================================");

            List<FlashCard> roundCards = organizer.organize(remaining);
            if (recentSorter != null) {
                recentSorter.clearMistakes();
            }
            achievements.startRound();

            for (FlashCard card : roundCards) {
                System.out.println();
                System.out.println("  Asuult: " + card.getQuestion());
                System.out.print("  Hariult: ");

                long start = System.currentTimeMillis();
                String answer = scanner.nextLine().trim();
                double elapsed = (System.currentTimeMillis() - start) / 1000.0;

                boolean correct = answer.equalsIgnoreCase(card.getAnswer().trim());

                if (correct) {
                    card.recordCorrect();
                    correctCounts.put(card, correctCounts.get(card) + 1);
                    System.out.printf("  Zov! (%.1fс)%n", elapsed);
                } else {
                    card.recordIncorrect();
                    System.out.println("  ✗ Buruu. Zov hariult: " + card.getAnswer());
                    if (recentSorter != null) {
                        recentSorter.addMistake(card);
                    }
                }

                achievements.recordAnswer(card, correct, elapsed);
            }

            achievements.endRound();

            // 🔽 ЭНД ӨӨРЧЛӨЛТ: REPETITIONS биш repetitions ашиглах
            remaining.clear();
            for (FlashCard card : cards) {
                if (correctCounts.get(card) < repetitions) {  // repetitions ашиглаж байна
                    remaining.add(card);
                }
            }
            // 🔼 ӨӨРЧЛӨЛТ ДУУССАН

            round++;
        }

        System.out.println();
        System.out.println("  ================================");
        System.out.println("  Bayr hurgey! Buh kartiig davlaa!");
        System.out.println("  ================================");

        List<String> earned = achievements.getEarned();
        if (!earned.isEmpty()) {
            System.out.println();
            System.out.println("  Taны амжилтууд:");
            for (String a : earned) {
                System.out.println("  " + a);
            }
        }

        // Save updated stats
        storage.save(cards);
    }
}