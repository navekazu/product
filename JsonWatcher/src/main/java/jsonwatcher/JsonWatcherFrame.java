package jsonwatcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonWatcherFrame extends JFrame {
	
	private JTextArea lineArea;
	private JTextArea textArea;
	private JPanel validStatus;
	private JLabel validMessage;
	private Color defaultColor;

	public JsonWatcherFrame(String jsonPath) {
		initContents(jsonPath);
	}
	
	private void initContents(String jsonPath) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        setContentPane(panel);

        lineArea = new JTextArea();
        
        textArea = new JTextArea(30, 100);
        Document doc = textArea.getDocument();
        doc.addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				validJson();
				lineChange();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				validJson();
				lineChange();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				validJson();
				lineChange();
			}
        });
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.add(lineArea, BorderLayout.WEST);
        textPanel.add(textArea, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(textPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout(5, 5));
    	validStatus = new JPanel();
    	validMessage = new JLabel();
    	validStatus.setPreferredSize(new Dimension(50, 30));
    	defaultColor = validStatus.getBackground();
    	southPanel.add(validStatus, BorderLayout.WEST);
    	southPanel.add(validMessage, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        setTitle("Json Watcher - " + jsonPath);
        loadJson(jsonPath, textArea);
        
        validJson();
		lineChange();

        pack();
	}
	
	private void loadJson(String jsonPath, JTextArea textArea) {
		Path file = Paths.get(jsonPath);
		String text;
		try {
			text = Files.readString(file);
			textArea.setText(text);		
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			textArea.setText(e.getMessage());		
		}
	}

    private void validJson() {
    	validStatus.setBackground(defaultColor);
    	validMessage.setText("");

    	String text = textArea.getText();
    	if (text.isEmpty()) {
    		return ;
    	}

    	ObjectMapper mapper = new ObjectMapper();
    	try {
			mapper.readValue(text, new TypeReference<>() {});
	    	validStatus.setBackground(new Color(200, 255, 200));
		} catch (JsonProcessingException e) {
	    	validStatus.setBackground(new Color(255, 200, 200));
	    	validMessage.setText(e.getMessage());
			e.printStackTrace();
		}
    }

    private void lineChange() {
        lineArea.setText("");

        String text = textArea.getText();
    	if (text.isEmpty()) {
    		return ;
    	}

        long count = text.chars()
                .filter(c -> c == '\n')
                .count();
        count += text.charAt(text.length()-1)=='\n'? 0: 1;

        StringBuilder sb = new StringBuilder();
        for (long l = 0; l < count; l++) {
        	if (l!=0) {
            	sb.append("\n");
        	}
        	sb.append(l);
        }
        lineArea.setText(sb.toString());
    }
}
