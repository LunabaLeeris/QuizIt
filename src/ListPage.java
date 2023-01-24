import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class ListPage extends JLayeredPane implements MouseListener, ActionListener {
    ArrayList<JPanel> panels = new ArrayList<>();
    ArrayList<JLabel> labels = new ArrayList<>();
    ArrayList<String> definitions = new ArrayList<>();
    ArrayList<String> answers = new ArrayList<>();
    JTextArea definition = new JTextArea();
    JTextArea answer = new JTextArea();
    JScrollPane definitionScroll;
    JScrollPane answerScroll;
    JScrollPane scroll;
    JButton saveButton = new JButton();
    JButton backButton = new JButton();
    JButton editButton = new JButton();
    JPanel saveBShadow = new JPanel();
    JPanel editBShadow = new JPanel();
    JPanel listArea = new JPanel();
    JPanel editArea = new JPanel();
    JPanel backShadow = new JPanel();
    int width;
    int height;
    int amount = 0;
    int panelIndex;
    boolean added = false;
    boolean editLayerOn = false;
    ListPage(int width, int height){
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        this.setLayout(null);

        JPanel background = new JPanel();
        background.setBackground(new Color(217, 232, 211));
        background.setBounds(0, 0, width, height);
        this.add(background, JLayeredPane.DEFAULT_LAYER);

        adjustComponents();
        addComponents();


        this.setVisible(true);
    }
    public void adjustButton(JButton button, int x, int y, int width, int height, String title, Color color, int fontSize){
        button.setBounds(x, y, width, height);
        button.setText(title);
        button.setFocusable(false);
        button.setFont(new Font("Times New Roman", Font.BOLD, fontSize));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.addActionListener(this);
    }
    public void adjustPanel(JPanel panel, int x, int y, int width, int height, Color color){
        panel.setBounds(x, y, width, height);
        panel.setBackground(color);
    }
    public void adjustComponents(){
        int backBWidth = (int) (width*.2);
        int backBHeight = (int) (height*.1);
        int increment = (int) (height*.02);
        int definitionWidth = (int) (width*.8);
        int definitionHeight = (int) (height*.45);
        int answerHeight = (int) (height*.15);

        adjustButton(saveButton, (int) (width*.1), (int) (height*.05), backBWidth, backBHeight, "SAVE",
                new Color(141, 205, 113), 20);

        adjustPanel(saveBShadow, saveButton.getX(), saveButton.getY()+increment, backBWidth, backBHeight,
                new Color(63, 82, 54, 230));

        adjustButton(backButton, (int) (width*.7), (int) (height*.05), backBWidth, backBHeight,
                "BACK", new Color(223, 120, 120), 20);

        adjustPanel(backShadow, backButton.getX(), backButton.getY()+increment, backBWidth, backBHeight,
                new Color(109, 10, 10));

        listArea.setLayout(new GridLayout(3, 3, 5, 5));
        listArea.setBackground(new Color(217, 232, 211));
        listArea.setBounds(0, 0, width, height);

        scroll = new JScrollPane(listArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(0, (int) (height*.2), (int)(width*.98), (int) (height - height*.27));
        scroll.setVisible(true);

        adjustPanel(editArea, (int)(width*.1), (int)(height*.08), (int)(width*.8), (int)(height*.7), new Color(63, 82, 54, 230));
        editArea.setLayout(null);

        definition.setBounds(getCenter(definitionWidth, true),
                (int) (height*.05), (int) (width*.6), definitionHeight);
        definition.setText("Input definitions here...");
        definition.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        definition.setLineWrap(true);

        definitionScroll = new JScrollPane(definition, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        definitionScroll.setBounds(definition.getBounds());

        answer.setBounds(definition.getX(), (int) (height*.52), (int) (width*.37), answerHeight);
        answer.setFont(new Font("Times New Roman", Font.PLAIN, 10));
        answer.setText("Input answer/s here...");
        answer.setLineWrap(true);

        answerScroll = new JScrollPane(answer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        answerScroll.setBounds(answer.getBounds());

        adjustButton(editButton, answer.getX()+answer.getWidth()+increment, answer.getY(), (int)(width*.2), (int)(height*.13),
                "EDIT", new Color(141, 205, 113), 15);

        adjustPanel(editBShadow, editButton.getX(), editButton.getY()+increment, editButton.getWidth(), editButton.getHeight(), new Color(23, 66, 4));

    }
    public void addComponents(){
        this.add(backButton, JLayeredPane.MODAL_LAYER);
        this.add(backShadow, JLayeredPane.PALETTE_LAYER);
        this.add(saveButton, JLayeredPane.MODAL_LAYER);
        this.add(saveBShadow, JLayeredPane.PALETTE_LAYER);
        this.add(scroll, JLayeredPane.POPUP_LAYER);
        editArea.add(definitionScroll);
        editArea.add(answerScroll);
        editArea.add(editButton);
        editArea.add(editBShadow);
    }

    public void updateList(){
        JPanel newPanel = new JPanel();
        newPanel.setPreferredSize(new Dimension((int)(width/5),(int)(height/5)));
        newPanel.setBackground(new Color(176, 190, 170));
        newPanel.setLayout(new BorderLayout());
        newPanel.addMouseListener(this);

        JLabel definitionText = new JLabel();
        definitionText.setText(definitions.get(definitions.size()-1));
        newPanel.add(definitionText);
        definitionText.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        definitionText.setForeground(Color.WHITE);
        definitionText.setHorizontalAlignment(JLabel.CENTER);

        labels.add(definitionText);
        panels.add(newPanel);
        listArea.add(newPanel);
    }
    public void createEditArea(){
        definition.setText(definitions.get(panelIndex));
        answer.setText(answers.get(panelIndex));
        this.add(editArea, JLayeredPane.DRAG_LAYER);
    }
    public void editDefAns(){
        definitions.set(panelIndex, definition.getText());
        answers.set(panelIndex, answer.getText());
        labels.get(panelIndex).setText(definition.getText());
        panels.get(panelIndex).setBackground(new Color(176, 190, 170));
    }

//------------------------------MOUSE LISTENER-----------------------------------

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (added && !editLayerOn) {
            for (int i = 0; i < panels.size(); i++) {
                if (e.getSource() == panels.get(i)) {
                    editLayerOn = true;
                    panelIndex = i;
                    createEditArea();
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (added && !editLayerOn) {
            for (JPanel panel : panels) {
                if (e.getSource() == panel) {
                    panel.setBackground(new Color(196, 232, 181));
                }
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (added && !editLayerOn) {
            for (JPanel panel : panels) {
                if (e.getSource() == panel) {
                    panel.setBackground(new Color(176, 190, 170));
                }
            }
        }
    }
    public int getCenter(int size, boolean isWidth) {
        if (isWidth) {
            return ((width / 2) - (size / 2));
        } else {
            return ((height / 2) - (size / 2));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == editButton){
            this.remove(editArea);
            editLayerOn = false;
            editDefAns();
            this.repaint();
        }
    }
}
