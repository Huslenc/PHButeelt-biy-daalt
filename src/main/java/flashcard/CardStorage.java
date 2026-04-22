package flashcard;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads flashcards from a JSON file.
 * Uses manual JSON parsing — no external libraries needed.
 *
 * JSON format:
 * [
 *   {"id":1,"question":"...","answer":"...","correctCount":0,"incorrectCount":0},
 *   ...
 * ]
 */
public class CardStorage {

    private final String filePath;

    /**
     * Constructs a CardStorage pointing to the given file.
     *
     * @param filePath path to the JSON file
     */
    public CardStorage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads cards from the JSON file.
     * If the file does not exist, returns a default deck.
     *
     * @return list of FlashCards
     */
    public List<FlashCard> load() {
        if (!Files.exists(Paths.get(filePath))) {
            return defaultDeck();
        }
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return parseJson(content.trim());
        } catch (IOException e) {
            System.err.println("JSON файл уншихад алдаа: " + e.getMessage());
            return defaultDeck();
        }
    }

    /**
     * Saves cards to the JSON file.
     *
     * @param cards list of FlashCards to save
     */
    public void save(List<FlashCard> cards) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(toJson(cards));
        } catch (IOException e) {
            System.err.println("JSON файл хадгалахад алдаа: " + e.getMessage());
        }
    }

    // ── JSON serialization ────────────────────────────────────────────────────

    private String toJson(List<FlashCard> cards) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < cards.size(); i++) {
            FlashCard c = cards.get(i);
            sb.append("  {");
            sb.append("\"id\":").append(c.getId()).append(",");
            sb.append("\"question\":").append(jsonString(c.getQuestion())).append(",");
            sb.append("\"answer\":").append(jsonString(c.getAnswer())).append(",");
            sb.append("\"correctCount\":").append(c.getCorrectCount()).append(",");
            sb.append("\"incorrectCount\":").append(c.getIncorrectCount());
            sb.append("}");
            if (i < cards.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    private String jsonString(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"")
                       .replace("\n", "\\n").replace("\r", "\\r") + "\"";
    }

    // ── JSON deserialization ──────────────────────────────────────────────────

    private List<FlashCard> parseJson(String json) {
        List<FlashCard> cards = new ArrayList<>();
        // Strip outer [ ]
        json = json.trim();
        if (json.startsWith("[")) json = json.substring(1);
        if (json.endsWith("]")) json = json.substring(0, json.length() - 1);

        // Split by },{
        String[] objects = json.split("\\},\\s*\\{");
        for (String obj : objects) {
            obj = obj.trim().replaceAll("^\\{", "").replaceAll("\\}$", "");
            if (obj.isBlank()) continue;
            FlashCard card = parseObject(obj);
            if (card != null) cards.add(card);
        }
        return cards;
    }

    private FlashCard parseObject(String obj) {
        try {
            int id = Integer.parseInt(extractValue(obj, "id"));
            String question = extractStringValue(obj, "question");
            String answer = extractStringValue(obj, "answer");
            int correctCount = Integer.parseInt(extractValue(obj, "correctCount"));
            int incorrectCount = Integer.parseInt(extractValue(obj, "incorrectCount"));

            FlashCard card = new FlashCard(id, question, answer);
            card.setCorrectCount(correctCount);
            card.setIncorrectCount(incorrectCount);
            return card;
        } catch (Exception e) {
            return null;
        }
    }

    private String extractValue(String obj, String key) {
        String pattern = "\"" + key + "\":\\s*";
        int idx = obj.indexOf("\"" + key + "\":");
        if (idx < 0) return "0";
        int start = idx + key.length() + 3;
        while (start < obj.length() && obj.charAt(start) == ' ') start++;
        int end = start;
        while (end < obj.length() && obj.charAt(end) != ',' && obj.charAt(end) != '}') end++;
        return obj.substring(start, end).trim();
    }

    private String extractStringValue(String obj, String key) {
        int idx = obj.indexOf("\"" + key + "\":");
        if (idx < 0) return "";
        int start = obj.indexOf("\"", idx + key.length() + 3) + 1;
        int end = start;
        while (end < obj.length()) {
            if (obj.charAt(end) == '"' && obj.charAt(end - 1) != '\\') break;
            end++;
        }
        return obj.substring(start, end)
                  .replace("\\\"", "\"")
                  .replace("\\n", "\n")
                  .replace("\\\\", "\\");
    }

    // ── Default deck ──────────────────────────────────────────────────────────

    private List<FlashCard> defaultDeck() {
        List<FlashCard> cards = new ArrayList<>();
        cards.add(new FlashCard(1, "Mongol ulsiin niislel yu ve?", "UB"));
        cards.add(new FlashCard(2, "2 + 2 = ?", "4"));
        cards.add(new FlashCard(3, "Mongolchuud ymar heleer yridag ve?", "Mongol hel"));
        cards.add(new FlashCard(4, "Zairmag haluun baidag uu huiten baidag uu?", "Huiten"));
        return cards;
    }
}
