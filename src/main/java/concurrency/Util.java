package concurrency;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class Util {

  // processing work simulation
  public static void doWork(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static void generateTestFiles() {
    Random random = new Random();
    List<String> filePaths = Util.generateFilePaths("/tmp/concurrency/example/files/file_", 10);
    String[] words = {
      "apple",
      "banana",
      "cherry",
      "date",
      "elderberry",
      "fig",
      "grape",
      "honeydew",
      "kiwi",
      "lemon",
      "mango",
      "nectarine",
      "orange",
      "papaya",
      "quince",
      "raspberry",
      "strawberry",
      "tangerine",
      "ugli",
      "vanilla"
    };
    Supplier<String> wordSupplier = () -> words[random.nextInt(words.length)];
    filePaths.forEach(
        file ->
            Util.generateFile(
                file,
                random.nextInt(6) + 15, // Random number of lines between 15 and 20
                random.nextInt(6) + 10, // Random word count between 10 and 15
                wordSupplier));
  }

  public static void generateFile(
      String filePath, int lines, int wordCount, Supplier<String> wordSupplier) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      for (int i = 0; i < lines; i++) {
        StringBuilder line = new StringBuilder();
        for (int j = 0; j < wordCount; j++) {
          String word = wordSupplier.get();
          line.append(word).append(" ");
        }
        writer.write(line.toString().trim());
        writer.newLine();
      }
      System.out.println("File generated at: " + filePath);
    } catch (IOException e) {
      System.err.println("Error writing file: " + e.getMessage());
    }
  }

  public static List<String> generateFilePaths(String prefix, int files) {
    List<String> filePaths = new ArrayList<>();
    for (int suffix = 0; suffix < files; suffix++) {
      filePaths.add(prefix + "_" + suffix);
    }
    return filePaths;
  }
}
