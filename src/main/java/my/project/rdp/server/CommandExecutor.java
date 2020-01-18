package my.project.rdp.server;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;

public interface CommandExecutor {
    Answer execute(Command  command) throws Exception;
    default Object decryptAnsver(byte[] data) throws Exception {
        return data;
    }

}
