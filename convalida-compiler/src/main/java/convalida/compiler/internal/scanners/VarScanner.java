package convalida.compiler.internal.scanners;

import com.squareup.javapoet.ClassName;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;

import java.util.Map;

import convalida.compiler.internal.Id;
import convalida.compiler.internal.QualifiedId;

/**
 * @author Wellington Costa on 10/07/2017.
 */
class VarScanner extends TreeScanner {

    private final Map<QualifiedId, Id> ids;
    private final ClassName className;
    private final String respectivePackageName;

    VarScanner(Map<QualifiedId, Id> ids, ClassName className,
               String respectivePackageName) {
        this.ids = ids;
        this.className = className;
        this.respectivePackageName = respectivePackageName;
    }

    @Override
    public void visitVarDef(JCTree.JCVariableDecl jcVariableDecl) {
        if ("int".equals(jcVariableDecl.getType().toString())) {
            int id = Integer.valueOf(jcVariableDecl.getInitializer().toString());
            String resourceName = jcVariableDecl.getName().toString();
            QualifiedId qualifiedId = new QualifiedId(respectivePackageName, id);
            ids.put(qualifiedId, new Id(id, className, resourceName));
        }
    }
}
