package infrastructure;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;

import domain.Concert;
import domain.Play;
import domain.Program;
import domain.ProgramRepository;

/**
 * ???
 * 
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
public class ProgramRepositoryInFile implements ProgramRepository {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerSubtypes(new NamedType(Concert.class, "Concert"));
        MAPPER.registerSubtypes(new NamedType(Play.class, "Play"));
    }

    public void saveProgram(final Program program) {
        Writer w = null;

        try {
            w = new FileWriter("Week" + program.getId() + ".json");
            // json = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(program);
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(w, program);
            // w.flush();
            // w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Program findProgramById(final int programId) {
        Program program = null;
        Reader r = null;

        try {
            r = new FileReader("Week" + programId + ".json");
            program = MAPPER.readValue(r, Program.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return program;
    }
}
