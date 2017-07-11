package convalida.compiler.internal.scanners;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Wellington Costa on 10/07/2017.
 */

public class RClassScanner extends TreeScanner {

    private final Map<String, Set<String>> rClasses = new LinkedHashMap<>();
    private String currentPackageName;

    @Override
    public void visitSelect(JCTree.JCFieldAccess jcFieldAccess) {
        Symbol symbol = jcFieldAccess.sym;
        if (symbol != null
                && symbol.getEnclosingElement() != null
                && symbol.getEnclosingElement().getEnclosingElement() != null
                && symbol.getEnclosingElement().getEnclosingElement().enclClass() != null) {
            Set<String> rClassSet = rClasses.get(currentPackageName);
            if (rClassSet == null) {
                rClassSet = new HashSet<>();
                rClasses.put(currentPackageName, rClassSet);
            }
            rClassSet.add(symbol.getEnclosingElement().getEnclosingElement().enclClass().className());
        }
    }

    public Map<String, Set<String>> getRClasses() {
        return rClasses;
    }

    public void setCurrentPackageName(String respectivePackageName) {
        this.currentPackageName = respectivePackageName;
    }
}