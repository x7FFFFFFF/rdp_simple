package my.project.rdp.server;

import java.io.DataInput;
import java.io.DataOutput;

public interface Event {

    void handle(DataInput input) throws Exception;
    void send(DataOutput out, int... params)  throws Exception;

  }
