FLASHCARD
Хэрэглэгч асуулт,хариулт бүхий картууд үүсгэж программ хариултаас хамаарч зөв буруу гэсэн хариу буцаана.
Програм юу хийдэг вэ?
  Программ нь сурах,карт засварлах,хадгалалт,гаргасан амжилтаа харах асуултуудаа гурван төрлөөр сонгон харж болно.
  RecentmistakesFirstSorter:Асуултыг их алдсан дарааллаар нь харуулна.
  RandomSorter:Асуултуудыг программ хэрэглэгчдэд санаандгүй байдлаар харуулна.
  WorstFirstSorter:Программыг дахин ажиллуулахад хэрэглэгчийн хамгийн сүүлийн алдсан асуулт эхэнд гарч ирнэ.
Шинэ асуулт нэмэхэд асуулт cards.json-д хадгалагдана.
 private void addCard() {
        System.out.println();
        System.out.print("  Asuult: ");
        String question = scanner.nextLine().trim();
        if (question.isEmpty()) {
            System.out.println("  Asuult hooson baij bolohgui.");
            return;
        }
Хадгалахад:
public void save(List<FlashCard> cards) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(toJson(cards));
        } catch (IOException e) {
            System.err.println("JSON файл хадгалахад алдаа: " + e.getMessage());
        }
    }

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
Программыг ажиллуулах команд:
Java суусан эсэхийг шалгана-java -version
Maven-аар build хийнэ-mvn clean compile 
Программыг ажиллуулна- java "-Dfile.encoding=UTF-8" -cp target/classes flashcard.Main
