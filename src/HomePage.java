import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HomePage extends JLayeredPane implements MouseListener {
    int width;
    int height;
    int increment;
    JLabel fileLabel;
    ImageIcon delete = new ImageIcon("delete.png");
    JButton useButton = new JButton();
    JPanel useBShadow = new JPanel();
    JButton deleteButton = new JButton();
    JPanel delBShadow = new JPanel();
    ListPage listPage;
    File myClickedFile;
    File[] files;
    ArrayList<JLabel> savedFiles;
    JPanel background;
    JPanel listArea = new JPanel();
    JPanel addNewShadow = new JPanel();
    JButton addNewButton = new JButton();
    JScrollPane scroll;

    HomePage(int width, int height) {
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        this.setLayout(null);

        background = new JPanel();
        background.setBackground(new Color(217, 232, 211));
        background.setBounds(0, 0, width, height);
        this.add(background, JLayeredPane.DEFAULT_LAYER);

        fillFiles(new File("SavedFiles"));
        adjustComponents();
        addComponents();
        displayFilesOnScreen();

        this.setVisible(true);
    }

    public void adjustComponents() {
        int addNewWidth = (int) (width * .25);
        int addNewHeight = (int) (height * .1);
        increment = (int) (height * .02);

        resizeImageIcons();

        listPage = new ListPage(width, height);

        addNewButton.setBounds((int) (width * .65), (int) (height * .05), addNewWidth, addNewHeight);
        addNewButton.setText("ADD NEW");
        addNewButton.setFocusable(false);
        addNewButton.setBackground(new Color(141, 205, 113));
        addNewButton.setForeground(Color.WHITE);

        addNewShadow.setBounds(addNewButton.getX(), addNewButton.getY() + increment, addNewWidth, addNewHeight);
        addNewShadow.setBackground(new Color(23, 66, 4));

        listArea.setLayout(new GridLayout(3, 1, increment, increment));
        listArea.setBackground(new Color(217, 232, 211));

        scroll = new JScrollPane(listArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(0, (int) (height * .2), (int) (width * .98), (int) (height - height * .27));
        scroll.setVisible(true);

        listPage.adjustButton(useButton, (int) (width*.48), (int) (height*.05), (int)(width*.2), (int)(height*.1),
                "USE", new Color(170, 139, 86), 20);
        listPage.adjustPanel(useBShadow, useButton.getX(), useButton.getY()+increment, (int)(width*.2), (int)(height*.1),
                new Color(75, 60, 36, 255));

        listPage.adjustButton(deleteButton, (int) (width*.27), (int) (height*.05), (int)(width*.1), (int)(height*.1),
                "", new Color(243, 156, 156), 20);

        listPage.adjustPanel(delBShadow, deleteButton.getX(), deleteButton.getY()+increment, (int)(width*.1), (int)(height*.1),
                new Color(109, 10, 10, 255));
        deleteButton.setIcon(delete);
    }

    public void addComponents() {
        this.add(addNewButton, JLayeredPane.MODAL_LAYER);
        this.add(addNewShadow, JLayeredPane.PALETTE_LAYER);
        this.add(scroll, JLayeredPane.POPUP_LAYER);
        listPage.add(useButton, MODAL_LAYER);
        listPage.add(useBShadow, PALETTE_LAYER);
        listPage.add(deleteButton, MODAL_LAYER);
        listPage.add(delBShadow, PALETTE_LAYER);
    }

    public void fillFiles(File directory) {
        files = directory.listFiles();
    }

    public void displayFilesOnScreen() {
        savedFiles = new ArrayList<>();
        for (File file : files) { // fills the panels
            JLabel newLabel = new JLabel();
            newLabel.setText(file.getName());
            newLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            newLabel.setHorizontalAlignment(JLabel.CENTER);
            newLabel.setForeground(Color.WHITE);
            newLabel.setBackground(new Color(63, 82, 54));
            newLabel.setPreferredSize(new Dimension((int) (width * .2), (int) (height * .2)));
            newLabel.addMouseListener(this);
            newLabel.setOpaque(true);
            listArea.add(newLabel);
            listArea.repaint();
            savedFiles.add(newLabel);
        }
    }

    public void readFile(String fileName) {
        myClickedFile = new File("SavedFiles\\" + fileName);
        try {
            FileReader myReader = new FileReader(myClickedFile);
            StringBuilder line = new StringBuilder();
            char state = 'D';
            int newChar = 0;
            while (newChar != -1) {
                newChar = myReader.read();
                if ((char) newChar == '\n') {
                    if (line.toString().equals("D")) {
                        state = 'D';
                    } else if (line.toString().equals("A")) {
                        state = 'A';
                    } else {
                        line.deleteCharAt(0);
                        line.deleteCharAt(line.length() - 1);
                        if (state == 'D') {
                            listPage.definitions.add(line.toString());
                            listPage.updateList();
                        } else {
                            listPage.answers.add(line.toString());
                        }
                    }

                    line = new StringBuilder();
                } else {
                    line.append((char) newChar);
                }
            }
            myReader.close();
        } catch (IOException e) {
            return;
        }
        this.add(listPage, DRAG_LAYER);
        listPage.added = true;
    }
    public void resetListPage(){
        listPage.definitions = new ArrayList<>();
        listPage.answers = new ArrayList<>();
        listPage.panels = new ArrayList<>();
        listPage.listArea.removeAll();
        listPage.listArea.repaint();
    }
    public void resizeImageIcons(){
        Image image1 = delete.getImage();
        Image img1 = image1.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
        delete = new ImageIcon(img1);
    }

    // ---------------- MOUSE LISTENERS ---------------------

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (JLabel label : savedFiles) {
            if (e.getSource() == label) {
                resetListPage();
                readFile(label.getText());
                fileLabel = label;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        for (JLabel label : savedFiles) {
            if (e.getSource() == label) {
                label.setBackground(new Color(104, 141, 96, 255));
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        for (JLabel label : savedFiles) {
            if (e.getSource() == label) {
                label.setBackground(new Color(63, 82, 54));
            }
        }
    }
}
