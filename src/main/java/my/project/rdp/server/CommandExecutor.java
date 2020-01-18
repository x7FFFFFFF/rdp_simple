package my.project.rdp.server;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;

public interface CommandExecutor {
    Answer execute(Command  command) throws Exception;

}
