package cn.ieclipse.smartim.idea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

public abstract class PsiFileTypeFactory {
    public static PsiFileTypeFactory create(PsiFile psiFile) {
        if (psiFile.getName().endsWith(".java") && test("com.intellij.psi.PsiJavaFile")) {
            return new PsiJavaFactory();
        }
        return null;
    }

    private static boolean test(String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public abstract String getQualifiedName(PsiFile psiFile);

    public abstract VirtualFile findFileByFQName(String file, Project project);

    public static class PsiFileDefaultFactory extends PsiFileTypeFactory {

        @Override public String getQualifiedName(PsiFile psiFile) {
            return null;
        }

        @Override public VirtualFile findFileByFQName(String file, Project project) {
            return null;
        }
    }
}
