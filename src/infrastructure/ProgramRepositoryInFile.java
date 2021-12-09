package infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import domain.Concert;
import domain.Play;
import domain.Program;
import domain.ProgramRepository;

import java.io.*;

/**
 * Program repository storing programs in different files.
 *
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
public class ProgramRepositoryInFile implements ProgramRepository {
    private static final String PATH = "saves/";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.registerSubtypes(new NamedType(Concert.class, "Concert"));
        MAPPER.registerSubtypes(new NamedType(Play.class, "Play"));
    }

    // @Override
    public void saveProgram(final Program program) {
        Writer w;

        try {
            w = new FileWriter(PATH + "Week" + program.getId() + ".json");
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(w, program);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @Override
    public Program findProgramById(final int programId) {
        Program program = null;
        Reader r;

        try {
            r = new FileReader(PATH + "Week" + programId + ".json");
            program = MAPPER.readValue(r, Program.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return program;
    }
}

// We also tried to use Gson, but we've met some difficulties to serialize a sub-object dealing with polymorphism…
/*public class ProgramRepositoryInFile implements ProgramRepository {
    final Gson gson = new Gson();

    // @Override
    public void saveProgram(final Program program) {
        Writer w = null;
        try {
            w = new FileWriter("Week" + program.getId() + ".json");
            gson.toJson(program, w);
            w.flush()
            w.close()
        } catch (JsonIOException | IOException e) {
            e.printStackTrace();
        }
    }

    // @Override
    public Program findProgramById(final int programId) {
        Program program = null;
        Reader r;

        try {
            r = new FileReader("Week" + programId + ".json");
            program = gson.fromJson(r, Program.class);
            r.close();
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            e.printStackTrace();
        } 

        return program;
    }
}*/
