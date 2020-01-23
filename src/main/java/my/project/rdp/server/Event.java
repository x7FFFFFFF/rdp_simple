package my.project.rdp.server;

import java.awt.event.MouseEvent;
import java.io.DataInput;
import java.io.DataOutput;

public interface Event {

    void handle(DataInput input) throws Exception;
    void send(DataOutput out, MouseEvent e )  throws Exception;
    //void send(MouseEvent mouseEvent);

  }
