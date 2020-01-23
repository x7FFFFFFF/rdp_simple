package my.project.rdp.client;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.model.Param;
import my.project.rdp.other.Utils;
import my.project.rdp.server.CommandRegistry;

import javax.swing.*;
import java.awt.*;

public class GuiClient extends JPanel {

    public GuiClient() throws Exception {
        super(new FlowLayout());
        final Command command = new Command(CommandRegistry.GET_SCREEN_SIZE, Param
                .ofInt(0, 0, 800, 600));
        final Answer answer = SimpleClient.INSTANCE.send(command);
        final Point point = answer.getDataObj();
        System.out.println("point = " + point);



    }

    private static void createAndShowGUI() throws Exception {
        //Create and set up the window.
        JFrame frame = new JFrame("GuiClient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new GuiClient();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Utils.rethrowVoid(GuiClient::createAndShowGUI );
            }
        });
    }
}
