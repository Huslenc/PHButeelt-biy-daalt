package flashcard;

/**
 * Represents a single flashcard with a question and answer.
 */
public class FlashCard {
    private int id;
    private String question;
    private String answer;
    private int correctCount;
    private int incorrectCount;

    /** Required for JSON deserialization. */
    public FlashCard() {}

    /**
     * Constructs a FlashCard.
     *
     * @param id       unique identifier
     * @param question question text
     * @param answer   answer text
     */
    public FlashCard(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.correctCount = 0;
        this.incorrectCount = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public int getCorrectCount() { return correctCount; }
    public void setCorrectCount(int correctCount) { this.correctCount = correctCount; }

    public int getIncorrectCount() { return incorrectCount; }
    public void setIncorrectCount(int incorrectCount) { this.incorrectCount = incorrectCount; }

    public int getTotalAnswered() { return correctCount + incorrectCount; }

    /** Records a correct answer. */
    public void recordCorrect() { correctCount++; }

    /** Records an incorrect answer. */
    public void recordIncorrect() { incorrectCount++; }
}
