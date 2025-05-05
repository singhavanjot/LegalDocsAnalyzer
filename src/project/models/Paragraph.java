package project.models;

public class Paragraph {
    private String text;

    public Paragraph(String text) {
        this.text = text.trim();
    }

    public String getText() {
        return text;
    }
}
