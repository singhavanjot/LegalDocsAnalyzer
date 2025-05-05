package project.utils;

import project.models.Document;
import project.models.Paragraph;

import java.util.List;
import java.util.ArrayList;

public class ClauseMatcher {
    public static void compareClauses(Document master, Document uploaded) {
        List<Paragraph> masterParas = master.getParagraphs();
        List<Paragraph> uploadParas = uploaded.getParagraphs();

        int matchCount = 0;
        List<Paragraph> unmatched = new ArrayList<>();

        for (Paragraph masterPara : masterParas) {
            boolean matched = false;
            for (Paragraph uploadedPara : uploadParas) {
                if (masterPara.getText().equalsIgnoreCase(uploadedPara.getText())) {
                    matched = true;
                    break;
                }
            }
            if (matched) matchCount++;
            else unmatched.add(masterPara);
        }

        System.out.println("Matched Clauses: " + matchCount + "/" + masterParas.size());
        if (!unmatched.isEmpty()) {
            System.out.println("Missing Clauses (not matched):");
            for (Paragraph p : unmatched) {
                System.out.println("- " + p.getText().substring(0, Math.min(80, p.getText().length())) + "...");
            }
            System.out.println("❌ Result: NOT ACCEPTABLE");
        } else {
            System.out.println("✅ Result: ACCEPTABLE");
        }
    }
}
