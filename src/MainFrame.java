import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainFrame extends JFrame implements ActionListener {
    int width;
    int height;
    Image icon = Toolkit.getDefaultToolkit().getImage("Icon.png");
    File Dir;
    File newQuizFile;
    HomePage homePage;
    FrontPage frontPage;
    NewQuizPage quizPage;
    QuestionSelectionPage qsPage;
    QuestionsPage qPage;

    JLayeredPane lastPage;

    MainFrame(int width, int height){
        checkIfDirectoryExists();
        this.width = width;
        this.height = height;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("QUIZ.IT");
        this.setIconImage(icon);
        reset();
        this.add(frontPage);
        this.setVisible(true);
    }
    public void reset(){
        homePage = new HomePage(width, height);
        frontPage = new FrontPage(width, height);
        quizPage = new NewQuizPage(width, height);
        qsPage = new QuestionSelectionPage(width, height);
        qPage = new QuestionsPage(width, height);

        this.setSize(width, height);

        addActionListeners();
    }
    public void checkIfDirectoryExists(){
        Dir = new File("SavedFiles");
        if (!Dir.exists())
            if (!Dir.mkdir()){
                JOptionPane.showMessageDialog(null, "There's a problem parsing the saved files",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
    }
    public void addActionListeners(){
        homePage.addNewButton.addActionListener(this);
        homePage.listPage.backButton.addActionListener(this);
        homePage.listPage.saveButton.addActionListener(this);
        homePage.deleteButton.addActionListener(this);
        homePage.useButton.addActionListener(this);
        frontPage.startButton.addActionListener(this);
        quizPage.addButton.addActionListener(this);
        quizPage.listButton.addActionListener(this);
        quizPage.finishButton.addActionListener(this);
        quizPage.listPage.backButton.addActionListener(this);
        quizPage.listPage.editButton.addActionListener(this);
        quizPage.listPage.saveButton.addActionListener(this);
        qsPage.listButton.addActionListener(this);
        qsPage.startButton.addActionListener(this);
    }
    public void addActionListenersQPage(){
        qPage.resultPage.exitButton.addActionListener(this);
        qPage.resultPage.newQuizButton.addActionListener(this);
        qPage.resultPage.tryAgainButton.addActionListener(this);
        qPage.resultPage.listButton.addActionListener(this);
    }
    public void initializeQPage(){
        qPage.definitions = quizPage.listPage.definitions;
        qPage.answers = quizPage.listPage.answers;
        qPage.choices = qsPage.choices;
        qPage.min = qsPage.min;
        qPage.max = qsPage.max;
        qPage.applyToAll = qsPage.questionTypes.get("al");
        qPage.randomizeTime = qsPage.questionTypes.get("aatrm");
        addActionListenersQPage();
    }
    public void createSavedFile(String fileName, ListPage listPageToLookAt){
        String path = "SavedFiles\\" + fileName;
        File newFile = new File(path);

        if (newFile.exists()){
            String[] options = {"REWRITE", "CANCEL"};
            int choice = JOptionPane.showOptionDialog(null, "FILE ALREADY EXISTS",
                    "", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, 0);

            if (choice == 1){
                return;
            }
            else{
                if (!newFile.delete()){
                    JOptionPane.showMessageDialog(null, "FAILED TO SAVE FILE", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        try {
            if (newFile.createNewFile()){
                FileWriter write = new FileWriter(path);
                write.append("D\n");
                for (String definitions : listPageToLookAt.definitions){
                    write.append("'").append(definitions).append("'\n");
                }
                write.append("A\n");
                for (String answers : listPageToLookAt.answers){
                    write.append("'").append(answers).append("'\n");
                }
                write.close();
            }
            else{
                JOptionPane.showMessageDialog(null, "FAILED TO SAVE FILE", "", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(null, "FAILED TO SAVE FILE", "", JOptionPane.ERROR_MESSAGE);
        }
    }

// -------------------------- MOUSE LISTENER STUFF----------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == frontPage.startButton){
            this.remove(frontPage);
            this.add(homePage);
            homePage.revalidate();
            lastPage = homePage;
        }
        else if (e.getSource() == homePage.addNewButton){
            String fileName = JOptionPane.showInputDialog("Input Quiz Name");
            if (fileName.length() == 0){
                JOptionPane.showMessageDialog(null,
                        "Please Write A Name", "", JOptionPane.ERROR_MESSAGE);
            }
            else {
                newQuizFile = new File("SavedFiles\\" + fileName);
                this.remove(homePage);
                this.add(quizPage);
                quizPage.revalidate();
                lastPage = homePage;
            }
        }
        else if (e.getSource() == quizPage.addButton){
            String definitionText = quizPage.definition.getText();
            String answerText = quizPage.answer.getText();

            if (definitionText.length() == 0 || definitionText.equals("Input definitions here...")){
                JOptionPane.showMessageDialog(null, "Please Input A Definition", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else if (answerText.length() == 0 || answerText.equals("Input answer/s here...")){
                JOptionPane.showMessageDialog(null, "Please Input An Answer", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else {
                quizPage.listPage.definitions.add(definitionText);
                quizPage.listPage.answers.add(answerText);
                quizPage.definition.setText("");
                quizPage.answer.setText("");
                quizPage.listPage.amount++;
                quizPage.listPage.updateList();
            }
        }
        else if (e.getSource() == quizPage.listButton){
                this.remove(quizPage);
                this.add(quizPage.listPage);
                quizPage.listPage.added = true;
                quizPage.listPage.revalidate();
                lastPage = quizPage;
        }
        else if (e.getSource() == homePage.listPage.saveButton){
            String[] buttons = {"Rewrite", "Create New File"};

            int choice = JOptionPane.showOptionDialog(null, "Rewrite Existing File?",
                    "SAVE", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, buttons, 0);
            if (choice == 0){
                if (homePage.myClickedFile.exists()){
                    if (!homePage.myClickedFile.delete()){
                        JOptionPane.showMessageDialog(null, "Failed To Perform Action", "", JOptionPane.ERROR_MESSAGE);
                    }
                }
                createSavedFile(homePage.myClickedFile.getName(), homePage.listPage);
            }
            else if (choice == 1){
                String fileName = JOptionPane.showInputDialog("Input Quiz Name");
                if (fileName.length() == 0){
                    JOptionPane.showMessageDialog(null,
                            "Please Write A Name", "", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    createSavedFile(fileName, homePage.listPage);
                }
            }
        }
        else if (e.getSource() == homePage.deleteButton){
            int choice = JOptionPane.showConfirmDialog(null, "Delete This Quiz?", "", JOptionPane.YES_NO_OPTION);
            if (choice == 0){
                if (homePage.myClickedFile.delete()){
                    homePage.savedFiles.remove(homePage.fileLabel);
                    homePage.listArea.remove(homePage.fileLabel);
                    homePage.remove(homePage.listPage);
                    JOptionPane.showMessageDialog(null, "Quiz Successfully Deleted", "", JOptionPane.INFORMATION_MESSAGE);
                    homePage.listArea.revalidate();
                    homePage.repaint();
                }
            }
        }
        else if (e.getSource() == homePage.listPage.backButton){
            homePage.remove(homePage.listPage);
            homePage.listPage.added = false;
        }
        else if (e.getSource() == homePage.useButton){
            int choice = JOptionPane.showConfirmDialog(null , "Are You Sure?", "", JOptionPane.YES_NO_OPTION);
            if (choice == 0) {
                for (int i = 0; i < homePage.listPage.definitions.size(); i++){
                    quizPage.listPage.definitions.add(homePage.listPage.definitions.get(i));
                    quizPage.listPage.answers.add(homePage.listPage.answers.get(i));
                    quizPage.listPage.updateList();
                }

                this.remove(homePage);
                newQuizFile = homePage.myClickedFile;
                this.remove(homePage);
                this.add(quizPage);
                quizPage.revalidate();
                lastPage = homePage;
            }
        }
        else if (e.getSource() == quizPage.listPage.saveButton){
            String[] buttons = {"Rewrite", "Create New File"};

            int choice = JOptionPane.showOptionDialog(null, "Rewrite Existing File?",
                    "SAVE", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, buttons, 0);
            if (choice == 0){
                if (newQuizFile.exists()){
                    if (!newQuizFile.delete()){
                        JOptionPane.showMessageDialog(null, "Failed To Perform Action", "", JOptionPane.ERROR_MESSAGE);
                    }

                }
                createSavedFile(newQuizFile.getName(), quizPage.listPage);
            }
            else if (choice == 1){
                String fileName = JOptionPane.showInputDialog("Input Quiz Name");
                if (fileName.length() == 0){
                    JOptionPane.showMessageDialog(null,
                            "Please Write A Name", "", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    createSavedFile(fileName, homePage.listPage);
                }
            }
        }
        else if (e.getSource() == quizPage.listPage.backButton){
            this.remove(quizPage.listPage);
            quizPage.listPage.added = false;
            this.add(lastPage);
        }
        else if (e.getSource() == quizPage.finishButton){
            if (quizPage.listPage.definitions.size() < 4){
                JOptionPane.showMessageDialog(null, "Please Input At Least Four", "", JOptionPane.WARNING_MESSAGE);
            }
            else {
                int verify = JOptionPane.showConfirmDialog(null, "Are You Sure?", "", JOptionPane.YES_NO_OPTION);
                if (verify == 0) {
                    this.remove(quizPage);
                    this.add(qsPage);
                    qsPage.added = true;
                    qsPage.revalidate();
                    lastPage = quizPage;
                }
            }
        }
        else if (e.getSource() == qsPage.listButton){
            this.remove(qsPage);
            this.add(quizPage.listPage);
            quizPage.listPage.added = true;
            quizPage.listPage.revalidate();
            lastPage = qsPage;
        }
        else if (e.getSource() == qsPage.startButton){
            qsPage.fillChoice();
            if (qsPage.choices.isEmpty()){
                JOptionPane.showMessageDialog(null, "Please Select A Specification", "", JOptionPane.ERROR_MESSAGE);
            }
            else if(qsPage.questionTypes.get("tt") && !qsPage.questionTypes.get("al") && !qsPage.questionTypes.get("aatrm")){
                JOptionPane.showMessageDialog(null, "Please Select A Type Of Time Trial", "", JOptionPane.ERROR_MESSAGE);
            }
            else {
                initializeQPage();
                this.remove(qsPage);
                this.add(qPage);
                qPage.runGame();
                qPage.revalidate();
                qPage.added = true;
                lastPage = qsPage;
            }
        }
        else if (e.getSource() == qPage.resultPage.exitButton){
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "",JOptionPane.YES_NO_OPTION);
            if (choice == 0){
                this.dispose();
            }
        }
        else if (e.getSource() == qPage.resultPage.newQuizButton) {
            this.remove(qPage);
            reset();
            this.add(homePage);
            homePage.revalidate();
        }
        else if (e.getSource() == qPage.resultPage.listButton) {
            this.remove(qPage);
            this.add(quizPage.listPage);
            quizPage.listPage.added = true;
            quizPage.listPage.revalidate();
            this.lastPage = qPage;
        }
        else if (e.getSource() == qPage.resultPage.tryAgainButton) {
            qPage.remove(qPage.resultPage);
            qPage = new QuestionsPage(width, height);
            initializeQPage();
            this.add(qPage);
            qPage.runGame();
            qPage.added = true;
            qPage.revalidate();
        }
        this.repaint();
    }
}
