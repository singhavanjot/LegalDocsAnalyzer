package project.utils;

import project.models.Document;
import project.models.Paragraph;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParagraphMatcher {
    public static String compareDocuments(Document master, Document uploaded) {
        List<Paragraph> masterParas = master.getParagraphs();
        List<Paragraph> uploadParas = uploaded.getParagraphs();

        int matchCount = 0;
        List<Paragraph> unmatched = new ArrayList<>();

        for (Paragraph masterPara : masterParas) {
            boolean matched = false;
            for (Paragraph uploadedPara : uploadParas) {
                double fuzzyScore = computeFuzzySimilarity(masterPara.getText(), uploadedPara.getText());
                if (fuzzyScore >= 0.75) { // 75% similarity threshold
                    matched = true;
                    break;
                }
            }
            if (matched) matchCount++;
            else unmatched.add(masterPara);
        }

        StringBuilder result = new StringBuilder();
        result.append("Matched Paragraphs: ").append(matchCount).append("/").append(masterParas.size()).append("\n");
        if (!unmatched.isEmpty()) {
            result.append("Missing Paragraphs (not matched):\n");
            for (Paragraph p : unmatched) {
                result.append("- ").append(p.getText().substring(0, Math.min(80, p.getText().length()))).append("...\n");
            }
            result.append("❌ Result: NOT ACCEPTABLE\n");
        } else {
            result.append("✅ Result: ACCEPTABLE\n");
        }

        System.out.println(result);

        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            FileWriter fw = new FileWriter("comparison_result_" + timestamp + ".txt");
            fw.write(result.toString());
            fw.close();
        } catch (IOException e) {
            System.err.println("Error writing result to file.");
        }
        return null;
    }

    private static double computeFuzzySimilarity(String a, String b) {
        double tokenScore = tokenSetSimilarity(a, b);
        double levenshteinScore = 1.0 - (double) levenshtein(a, b) / Math.max(a.length(), b.length());
        return (tokenScore + levenshteinScore) / 2.0; // weighted average
    }

    private static double tokenSetSimilarity(String a, String b) {
        Set<String> setA = new HashSet<>(List.of(a.toLowerCase().split("\\s+")));
        Set<String> setB = new HashSet<>(List.of(b.toLowerCase().split("\\s+")));

        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);

        return (double) intersection.size() / union.size();
    }

    private static int levenshtein(String a, String b) {
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
}
