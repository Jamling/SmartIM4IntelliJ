package cn.ieclipse.smartim.idea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.search.GlobalSearchScope;

public class PsiJavaFactory extends PsiFileTypeFactory {

    @Override public String getQualifiedName(PsiFile psiFile) {
        PsiJavaFile psiJavaFile = (PsiJavaFile)psiFile;
        assert psiJavaFile != null;
        final PsiClass[] classes = psiJavaFile.getClasses();
        if (classes.length > 0) {
            return classes[0].getQualifiedName();
        }
        return null;
    }

    @Override public VirtualFile findFileByFQName(String file, Project project) {
        VirtualFile result = null;
        if (file != null) {
            PsiClass aClass = JavaPsiFacade.getInstance(project).findClass(file, GlobalSearchScope.allScope(project));
            if (aClass != null && aClass.getNavigationElement().getContainingFile().getName().endsWith(".java")) {
                result = aClass.getNavigationElement().getContainingFile().getVirtualFile();
            }
        }
        return result;
    }
}
