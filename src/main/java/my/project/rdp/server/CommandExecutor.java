package my.project.rdp.server;

import java.io.DataInput;
import java.io.DataOutput;

public interface CommandExecutor {
    void handle(DataInput input) throws Exception;
    void send(DataOutput out)  throws Exception;

}
