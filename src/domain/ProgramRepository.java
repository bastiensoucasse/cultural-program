package domain;

public interface ProgramRepository {
    void saveProgram(final Program program);

    Program findProgramById(final int id);
}
