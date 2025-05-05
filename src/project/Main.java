package project;

import project.models.Document;
import project.utils.DocumentLoader;
import project.utils.ParagraphMatcher;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    private static final String TEMPLATE_FOLDER = "templates/";
    private static final String UPLOAD_FOLDER = "uploads/";
    private static final String RESULT_FOLDER = "results/";
    private static Document masterDoc = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("📄 Welcome to Legal Document Checker (Paragraph Mode)");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Load master document");
            System.out.println("2. Compare uploaded document");
            System.out.println("3. Exit");
            System.out.print("> ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter master document filename (in templates/): ");
                    String masterFilename = scanner.nextLine().trim();
                    try {
                        masterDoc = DocumentLoader.loadDocument(TEMPLATE_FOLDER + masterFilename);
                        System.out.println("✅ Master document loaded successfully.");
                    } catch (Exception e) {
                        System.out.println("❌ Failed to load master document: " + e.getMessage());
                    }
                    break;

                case "2":
                    if (masterDoc == null) {
                        System.out.println("⚠️ Please load the master document first.");
                        break;
                    }
                    System.out.print("Enter uploaded document filename (in uploads/): ");
                    String uploadFilename = scanner.nextLine().trim();
                    try {
                        Document uploadedDoc = DocumentLoader.loadDocument(UPLOAD_FOLDER + uploadFilename);
                        String result = ParagraphMatcher.compareDocuments(masterDoc, uploadedDoc);
                        saveResultToFile(result);
                        System.out.println("Would you like to continue? (yes/no)");
                        String again = scanner.nextLine().trim().toLowerCase();
                        if (again.equals("no")) {
                            System.out.println("👋 Exiting. Thank you!");
                            return;
                        }
                    } catch (Exception e) {
                        System.out.println("❌ Failed to load uploaded document: " + e.getMessage());
                    }
                    break;

                case "3":
                    System.out.println("👋 Exiting. Goodbye!");
                    return;

                default:
                    System.out.println("⚠️ Invalid option. Please enter 1, 2 or 3.");
            }
        }
    }

    private static void saveResultToFile(String content) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = RESULT_FOLDER + "result_" + timestamp + ".txt";
            FileWriter writer = new FileWriter(filename);
            writer.write(content);
            writer.close();
            System.out.println("📁 Result saved to " + filename);
        } catch (IOException e) {
            System.out.println("❌ Error saving result: " + e.getMessage());
        }
    }
}

