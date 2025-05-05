package project.models;

import java.util.List;

public class Document {
    private List<Paragraph> paragraphs;

    public Document(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }
}
