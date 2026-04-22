package flashcard;

import java.util.List;
import java.util.Scanner;

/**
 * Handles the deck editing menu — add, edit, and delete cards.
 */
public class EditMode {

    private final List<FlashCard> cards;
    private final Scanner scanner;
    private final CardStorage storage;

    /**
     * Constructs an EditMode.
     *
     * @param cards   the current deck
     * @param scanner shared input scanner
     * @param storage storage to save after changes
     */
    public EditMode(List<FlashCard> cards, Scanner scanner, CardStorage storage) {
        this.cards = cards;
        this.scanner = scanner;
        this.storage = storage;
    }

    /**
     * Starts the edit menu loop.
     */
    public void start() {
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("  ---- Kart zasah ----");
            System.out.println("  Niit kart: " + cards.size());
            System.out.println("  1. Buh kartiig harah");
            System.out.println("  2. Shine kart nemeh");
            System.out.println("  3. Kart zasah");
            System.out.println("  4. Kart ustgah");
            System.out.println("  5. Butsah");
            System.out.print("  Songolt: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": listCards(); break;
                case "2": addCard(); break;
                case "3": editCard(); break;
                case "4": deleteCard(); break;
                case "5": running = false; break;
                default:
                    System.out.println("  Buruu songolt.");
            }
        }
    }

    // ── List ──────────────────────────────────────────────────────────────────

    private void listCards() {
        if (cards.isEmpty()) {
            System.out.println("  Kart baihgui.");
            return;
        }
        System.out.println();
        for (int i = 0; i < cards.size(); i++) {
            FlashCard c = cards.get(i);
            System.out.printf("  [%d] А: %s%n", i + 1, c.getQuestion());
            System.out.printf("      Х: %s%n", c.getAnswer());
        }
    }

    // ── Add ───────────────────────────────────────────────────────────────────

    private void addCard() {
        System.out.println();
        System.out.print("  Asuult: ");
        String question = scanner.nextLine().trim();
        if (question.isEmpty()) {
            System.out.println("  Asuult hooson baij bolohgui.");
            return;
        }

        System.out.print("  Hariult: ");
        String answer = scanner.nextLine().trim();
        if (answer.isEmpty()) {
            System.out.println("  Hariult hooosn baij bolohgui.");
            return;
        }

        int newId = cards.stream().mapToInt(FlashCard::getId).max().orElse(0) + 1;
        cards.add(new FlashCard(newId, question, answer));
        storage.save(cards);
        System.out.println("  Kart nemegdlee.");
    }

    // ── Edit ──────────────────────────────────────────────────────────────────

    private void editCard() {
        listCards();
        if (cards.isEmpty()) return;

        System.out.print("  Zasah kartiin dugaar: ");
        int idx = readIndex();
        if (idx < 0) return;

        FlashCard card = cards.get(idx);
        System.out.println("  Odoogiin asuult: " + card.getQuestion());
        System.out.print(" Shine asuult : ");
        String newQuestion = scanner.nextLine().trim();

        System.out.println("  Odoogiin hariult: " + card.getAnswer());
        System.out.print("  Shine hariult: ");
        String newAnswer = scanner.nextLine().trim();

        if (!newQuestion.isEmpty()) card.setQuestion(newQuestion);
        if (!newAnswer.isEmpty()) card.setAnswer(newAnswer);

        storage.save(cards);
        System.out.println("  Kart shinechlegdlee.");
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    private void deleteCard() {
        listCards();
        if (cards.isEmpty()) return;

        System.out.print("  Ustgah kartiin dugaar: ");
        int idx = readIndex();
        if (idx < 0) return;

        FlashCard card = cards.get(idx);
        System.out.println("  \"" + card.getQuestion() + "\" ustgah uu? (y/n): ");
        System.out.print("  ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("y")) {
            cards.remove(idx);
            storage.save(cards);
            System.out.println("  Kart ustgagdlaa.");
        } else {
            System.out.println("  Bolison.");
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private int readIndex() {
        try {
            int num = Integer.parseInt(scanner.nextLine().trim());
            if (num < 1 || num > cards.size()) {
                System.out.println("  Буруу дугаар.");
                return -1;
            }
            return num - 1;
        } catch (NumberFormatException e) {
            System.out.println("  Too oruulna uu.");
            return -1;
        }
    }
}
