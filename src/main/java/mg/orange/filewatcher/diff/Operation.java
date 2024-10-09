package mg.orange.filewatcher.diff;

/**
 * The data structure representing a diff is a Linked list of Diff objects:
 * {Diff(Operation.DELETE, "Hello"), Diff(Operation.INSERT, "Goodbye"),
 *  Diff(Operation.EQUAL, " world.")}
 * which means: delete "Hello", add "Goodbye" and keep " world."
 */
/**
 * Class representing one diff operation.
 */
public enum Operation {
    DELETE, INSERT, EQUAL
}
