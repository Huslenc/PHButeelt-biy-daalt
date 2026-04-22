package flashcard;

import java.util.List;
import java.util.Scanner;

/**
 * Main entry point for the Flashcard application.
 *
 * <p>Menu:
 * <pre>
 *   1. Study   - study the deck
 *   2. Edit    - add / edit / delete cards
 *   3. Quit    - exit
 * </pre>
 *
 * <p>Cards are loaded from (and saved to) cards.json automatically.
 */
public class Main {

    private static final String DATA_FILE = "cards.json";

    /**
     * Application entry point.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        CardStorage storage = new CardStorage(DATA_FILE);
        List<FlashCard> cards = storage.load();
        Scanner scanner = new Scanner(System.in);

        System.out.println("==============================");
        System.out.println("      FLASHCARD SISTEM        ");
        System.out.println("==============================");

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("  1. Surah  (Study)");
            System.out.println("  2. Kartaa zasah (Edit deck)");
            System.out.println("  3. Garah  (Quit)");
            System.out.print("  Songolt(1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    new StudyMode(cards, scanner, storage).start();
                    break;
                case "2":
                    new EditMode(cards, scanner, storage).start();
                    break;
                case "3":
                    System.out.println("  Bayartai!");
                    running = false;
                    break;
                default:
                    System.out.println("  1, 2, esvel 3 gej oruulna uu.");
            }
        }

        scanner.close();
    }
}
