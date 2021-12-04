package domain;

public interface ProgramRepository {
    public void saveProgram(final Program program);

    public Program findProgramById(final int id);
}
