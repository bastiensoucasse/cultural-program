package infrastructure;

import domain.Program;
import domain.ProgramRepository;

import java.util.HashMap;
import java.util.Map;

public class ProgramRepositoryInMemory implements ProgramRepository {
    private final Map<Integer, Program> programMap = new HashMap<>();

    @Override
    public void saveProgram(final Program program) {
        programMap.put(program.getId(), program);
    }

    @Override
    public Program findProgramById(final int id) {
        return programMap.get(id);
    }
}
