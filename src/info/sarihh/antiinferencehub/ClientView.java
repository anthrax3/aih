package info.sarihh.antiinferencehub;

import info.sarihh.antiinferencehub.AboutBox;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import jsyntaxpane.DefaultSyntaxKit;
/*
 * Author: Sari Haj Hussein
 */
public class ClientView extends FrameView implements Runnable {

    public ClientView(SingleFrameApplication app) {
        super(app);
        DefaultSyntaxKit.initKit();
        initComponents();
    }

    @Action
    public final void clearInput() {
        inputEditorPane.setText("");
    }

    @Action
    public final void clearOutput() {
        outputTextArea.setText("");
    }

    @Action
    public final void connect() {
        if (hostTextField.getText().trim().length() < 1) {
            JOptionPane.showMessageDialog(
                    App.getApplication().getMainFrame(),
                    "Missing hub host name!", "Error", JOptionPane.ERROR_MESSAGE);
            hostTextField.setFocusable(true);
            hostTextField.requestFocusInWindow();
            return;
        }
        try {
            socket = new Socket(InetAddress.getByName(hostTextField.getText()), 12345);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            enableComponents(true);
            new Thread(this).start();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(
                    App.getApplication().getMainFrame(),
                    "Cannot connect to hub!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Action
    public final void disconnect() {
        try {
            out.writeObject("QUIT");
            out.flush();
            socket.close();
            enableComponents(false);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Action
    public final void execute() {
        try {
            out.writeObject(inputEditorPane.getText().toUpperCase());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action
    public final void showAboutBox() {
        if (antiInferenceHubAboutBox == null) {
            JFrame mainFrame = App.getApplication().getMainFrame();
            antiInferenceHubAboutBox = new AboutBox(mainFrame);
            antiInferenceHubAboutBox.setLocationRelativeTo(mainFrame);
        }
        App.getApplication().show(antiInferenceHubAboutBox);
    }

    @Action
    public final void showUsersGuide() throws IOException {
        Desktop.getDesktop().open(new File("documentation/users-guide-1.0-revision-1.pdf"));
    }

    @Override
    public final void run() {
        try {
            while (true) {
                String message = (String) in.readObject();
                if (message.equals("QUIT")) {
                    disconnect();
                    break;
                }
                setOutputText(message);
            }
            JOptionPane.showMessageDialog(
                    App.getApplication().getMainFrame(),
                    "Hub is down!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    private final void enableComponents(final boolean b) {
        SwingUtilities.invokeLater(new Runnable() {

            private boolean c = !b;

            public void run() {
                connectButton.setEnabled(c);
                disconnectButton.setEnabled(b);
                executeButton.setEnabled(b);
                clearInputButton.setEnabled(b);
                clearOutputButton.setEnabled(b);
                inputEditorPane.setEditable(b);
            }
        });
    }

    private final void setOutputText(final String text) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                outputTextArea.append(text + "\n");
                outputTextArea.setCaretPosition(outputTextArea.getText().length());
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        centerPanel = new javax.swing.JPanel();
        hostPanel = new javax.swing.JPanel();
        hostLabel = new javax.swing.JLabel();
        hostTextField = new javax.swing.JTextField();
        inputScrollPane = new javax.swing.JScrollPane(inputEditorPane);
        outputScrollPane = new javax.swing.JScrollPane();
        outputTextArea = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem usersGuideMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        southPanel = new javax.swing.JPanel();

        centerPanel.setBackground(new java.awt.Color(255, 255, 255));
        centerPanel.setName("centerPanel"); // NOI18N
        centerPanel.setLayout(new java.awt.BorderLayout());

        hostPanel.setBackground(new java.awt.Color(255, 255, 255));
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(info.sarihh.antiinferencehub.App.class).getContext().getResourceMap(ClientView.class);
        hostPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("hostPanel.border.title"))); // NOI18N
        hostPanel.setName("hostPanel"); // NOI18N

        hostLabel.setBackground(new java.awt.Color(255, 255, 255));
        hostLabel.setText(resourceMap.getString("hostLabel.text")); // NOI18N
        hostLabel.setName("hostLabel"); // NOI18N
        hostPanel.add(hostLabel);

        hostTextField.setName("hostTextField"); // NOI18N
        hostTextField.setPreferredSize(new java.awt.Dimension(500, 20));
        hostPanel.add(hostTextField);

        centerPanel.add(hostPanel, java.awt.BorderLayout.NORTH);

        inputScrollPane.setBackground(new java.awt.Color(255, 255, 255));
        inputScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("inputScrollPane.border.title"))); // NOI18N
        inputScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        inputScrollPane.setName("inputScrollPane"); // NOI18N

        inputEditorPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        inputEditorPane.setContentType("text/sql"); // NOI18N
        inputEditorPane.setEditable(false);
        inputEditorPane.setFont(new java.awt.Font("Tahoma", 0, 12));
        inputEditorPane.setName("inputEditorPane"); // NOI18N
        inputEditorPane.setPreferredSize(new java.awt.Dimension(106, 150));
        inputEditorPane.setSelectionColor(new java.awt.Color(212, 208, 200));
        inputScrollPane.setViewportView(inputEditorPane);

        centerPanel.add(inputScrollPane, java.awt.BorderLayout.CENTER);

        outputScrollPane.setBackground(new java.awt.Color(255, 255, 255));
        outputScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("outputScrollPane.border.title"))); // NOI18N
        outputScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        outputScrollPane.setName("outputScrollPane"); // NOI18N

        outputTextArea.setColumns(60);
        outputTextArea.setEditable(false);
        outputTextArea.setFont(new java.awt.Font("Tahoma", 0, 12));
        outputTextArea.setLineWrap(true);
        outputTextArea.setRows(9);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        outputTextArea.setName("outputTextArea"); // NOI18N
        outputTextArea.setSelectionColor(new java.awt.Color(212, 208, 200));
        outputScrollPane.setViewportView(outputTextArea);

        centerPanel.add(outputScrollPane, java.awt.BorderLayout.SOUTH);

        menuBar.setBackground(new java.awt.Color(255, 255, 255));
        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setBackground(new java.awt.Color(255, 255, 255));
        fileMenu.setMnemonic('F');
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(info.sarihh.antiinferencehub.App.class).getContext().getActionMap(ClientView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setBackground(new java.awt.Color(255, 255, 255));
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setBackground(new java.awt.Color(255, 255, 255));
        helpMenu.setMnemonic('H');
        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        usersGuideMenuItem.setAction(actionMap.get("showUsersGuide")); // NOI18N
        usersGuideMenuItem.setBackground(new java.awt.Color(255, 255, 255));
        usersGuideMenuItem.setName("usersGuideMenuItem"); // NOI18N
        helpMenu.add(usersGuideMenuItem);

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setBackground(new java.awt.Color(255, 255, 255));
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        southPanel.setBackground(new java.awt.Color(255, 255, 255));
        southPanel.setName("southPanel"); // NOI18N

        connectButton.setAction(actionMap.get("connect")); // NOI18N
        connectButton.setBackground(new java.awt.Color(255, 255, 255));
        connectButton.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), javax.swing.BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        connectButton.setName("connectButton"); // NOI18N
        connectButton.setPreferredSize(new java.awt.Dimension(100, 21));
        southPanel.add(connectButton);

        disconnectButton.setAction(actionMap.get("disconnect")); // NOI18N
        disconnectButton.setBackground(new java.awt.Color(255, 255, 255));
        disconnectButton.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), javax.swing.BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        disconnectButton.setName("disconnectButton"); // NOI18N
        disconnectButton.setPreferredSize(new java.awt.Dimension(100, 21));
        southPanel.add(disconnectButton);

        executeButton.setAction(actionMap.get("execute")); // NOI18N
        executeButton.setBackground(new java.awt.Color(255, 255, 255));
        executeButton.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), javax.swing.BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        executeButton.setName("executeButton"); // NOI18N
        executeButton.setPreferredSize(new java.awt.Dimension(100, 21));
        southPanel.add(executeButton);

        clearInputButton.setAction(actionMap.get("clearInput")); // NOI18N
        clearInputButton.setBackground(new java.awt.Color(255, 255, 255));
        clearInputButton.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), javax.swing.BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        clearInputButton.setName("clearInputButton"); // NOI18N
        clearInputButton.setPreferredSize(new java.awt.Dimension(100, 21));
        southPanel.add(clearInputButton);

        clearOutputButton.setAction(actionMap.get("clearOutput")); // NOI18N
        clearOutputButton.setBackground(new java.awt.Color(255, 255, 255));
        clearOutputButton.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), javax.swing.BorderFactory.createEmptyBorder(2, 10, 2, 10)));
        clearOutputButton.setName("clearOutputButton"); // NOI18N
        clearOutputButton.setPreferredSize(new java.awt.Dimension(100, 21));
        southPanel.add(clearOutputButton);

        setComponent(centerPanel);
        setMenuBar(menuBar);
        setStatusBar(southPanel);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel centerPanel;
    private final javax.swing.JButton clearInputButton = new javax.swing.JButton();
    private final javax.swing.JButton clearOutputButton = new javax.swing.JButton();
    private final javax.swing.JButton connectButton = new javax.swing.JButton();
    private final javax.swing.JButton disconnectButton = new javax.swing.JButton();
    private final javax.swing.JButton executeButton = new javax.swing.JButton();
    private javax.swing.JLabel hostLabel;
    private javax.swing.JPanel hostPanel;
    private javax.swing.JTextField hostTextField;
    private final javax.swing.JEditorPane inputEditorPane = new javax.swing.JEditorPane();
    private javax.swing.JScrollPane inputScrollPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JScrollPane outputScrollPane;
    private static javax.swing.JTextArea outputTextArea;
    private javax.swing.JPanel southPanel;
    // End of variables declaration//GEN-END:variables
    private AboutBox antiInferenceHubAboutBox;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
}
