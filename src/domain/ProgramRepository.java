package domain;

/**
 * Program repository interface (used for serialization in infrastructure).
 *
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
public interface ProgramRepository {
    void saveProgram(final Program program);

    Program findProgramById(final int id);
}
