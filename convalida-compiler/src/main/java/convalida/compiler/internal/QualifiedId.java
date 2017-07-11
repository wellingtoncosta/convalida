package convalida.compiler.internal;

/**
 * @author Wellington Costa on 10/07/2017.
 */
public final class QualifiedId {

    private final String packageName;
    public final int id;

    public QualifiedId(String packageName, int id) {
        this.packageName = packageName;
        this.id = id;
    }

    @Override
    public String toString() {
        return "QualifiedId{packageName='" + packageName + "', id=" + id + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QualifiedId)) return false;
        QualifiedId other = (QualifiedId) o;
        return id == other.id
                && packageName.equals(other.packageName);
    }

    @Override
    public int hashCode() {
        int result = packageName.hashCode();
        result = 31 * result + id;
        return result;
    }
}
