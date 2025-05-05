package project.utils;

import project.models.Document;
import project.models.Paragraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DocumentLoader {

    public static Document loadDocument(String path) {
        ArrayList<Paragraph> paragraphs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder currentPara = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (currentPara.length() > 0) {
                        paragraphs.add(new Paragraph(currentPara.toString().trim()));
                        currentPara.setLength(0);
                    }
                } else {
                    currentPara.append(line).append(" ");
                }
            }
            if (currentPara.length() > 0) {
                paragraphs.add(new Paragraph(currentPara.toString().trim()));
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading file: " + path);
            return null;
        }

        return new Document(paragraphs);
    }
}
