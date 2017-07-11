package convalida.compiler.internal.scanners;

import com.squareup.javapoet.ClassName;
import com.sun.source.tree.ClassTree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;

import java.util.Map;

import convalida.compiler.internal.Id;
import convalida.compiler.internal.QualifiedId;

/**
 * @author Wellington Costa on 10/07/2017.
 */
public class IdScanner extends TreeScanner {

    private final Map<QualifiedId, Id> ids;
    private final String rPackageName;
    private final String respectivePackageName;

    public IdScanner(Map<QualifiedId, Id> ids, String rPackageName, String respectivePackageName) {
        this.ids = ids;
        this.rPackageName = rPackageName;
        this.respectivePackageName = respectivePackageName;
    }

    @Override
    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
        for (JCTree tree : jcClassDecl.defs) {
            if (tree instanceof ClassTree) {
                ClassTree classTree = (ClassTree) tree;
                String className = classTree.getSimpleName().toString();
                if (className.equals("string")) {
                    ClassName rClassName = ClassName.get(rPackageName, "R", className);
                    VarScanner scanner = new VarScanner(ids, rClassName, respectivePackageName);
                    ((JCTree) classTree).accept(scanner);
                }
            }
        }
    }

}
