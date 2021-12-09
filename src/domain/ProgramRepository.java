package domain;

/**
 * ???
 * 
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
public interface ProgramRepository {
    void saveProgram(final Program program);

    Program findProgramById(final int id);
}
