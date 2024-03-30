package cn.ieclipse.smartim.idea;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jamling on 2018/1/2.
 */
public final class VFSUtils {
    private VFSUtils() {

    }

    public static String getPath(VirtualFile file) {
        String result = null;
        Document document = FileDocumentManager.getInstance().getDocument(file);

        Project[] openProjects = VFSUtils.getOpenProjects();
        for (int i = 0; i < openProjects.length && result == null; i++) {
            Project openProject = openProjects[i];
            if (!openProject.isInitialized() && !ApplicationManager.getApplication().isUnitTestMode())
                continue;
            if (document != null) {
                PsiFile psiFile = PsiDocumentManager.getInstance(openProject).getPsiFile(document);
                // result = PsiFileTypeFactory.create(psiFile).getQualifiedName(psiFile);
            }
            ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(openProject).getFileIndex();
            if (projectFileIndex.isInSource(file)) {
                VirtualFile sourceRoot = projectFileIndex.getSourceRootForFile(file);
                result = (getRelativePath(file, sourceRoot));
            }

            if (projectFileIndex.isInContent(file)) {
                VirtualFile contentRoot = projectFileIndex.getContentRootForFile(file);
                result = (getRelativePath(file, contentRoot));
            }

        }
        return result;
    }

    private static String getRelativePath(VirtualFile file, VirtualFile rootForFile) {
        if (file == null || rootForFile == null)
            return null;
        return file.getPath().substring(rootForFile.getPath().length());
    }

    public static VirtualFile _getVirtualFile(Project project, String file) {
        VirtualFile result = findFileByFQName(file, project);

        if (result == null) {
            final Set<VirtualFile> candidates = new HashSet<>();

            Module[] modules = ModuleManager.getInstance(project).getModules();
            for (Module module : modules) {
                findFileInModule(candidates, module, file);
            }

            int currWeight = 0;
            for (VirtualFile candidate : candidates) {
                final int newWeight = weight(candidate, file);
                if (result == null || newWeight > currWeight) {
                    currWeight = newWeight;
                    result = candidate;
                }
            }
        }

        return result;
    }

    private static int weight(final VirtualFile candidate, final String file) {
        final String pattern = file.replace('\\', '/');
        final String candidatePath = candidate.getPath();

        int weight = 0;
        while (weight < pattern.length() && weight < candidatePath.length()
            && pattern.charAt(pattern.length() - weight - 1) == candidatePath
            .charAt(candidatePath.length() - weight - 1))
            weight++;

        return weight;
    }

    private static VirtualFile findFileByFQName(String file, Project project) {
        return null;
    }

    private static void findFileInModule(final Set<VirtualFile> found, Module module, String path) {
        ModuleRootManager rootManager = ModuleRootManager.getInstance(module);
        findInRoots(found, rootManager.getContentRoots(), path);
    }

    private static void findInRoots(final Set<VirtualFile> found, VirtualFile[] roots, String relativePath) {
        if (relativePath == null)
            return;

        for (VirtualFile root : roots) {
            String probeName;
            if (isArchive(root)) {
                probeName = root.getPath() + '!' + relativePath;
            } else {
                probeName = root.getPath() + relativePath;
            }
            VirtualFile virtualFile = root.getFileSystem().findFileByPath(probeName);
            if (virtualFile != null) {
                found.add(virtualFile);
            }
        }
    }

    public static boolean isArchive(VirtualFile sourceRoot) {
        return sourceRoot.getFileType() == getArchiveFileType();
    }

    private static FileType getArchiveFileType() {
        FileType fileType = FileTypeManager.getInstance().getFileTypeByExtension(".zip");
        if (fileType == FileTypeManager.getInstance().getFileTypeByExtension(".kokoko")) {
            fileType = FileTypeManager.getInstance().getFileTypeByExtension("zip");
        }
        return fileType;
    }

    public static Project getProjectByName(String projectName) {
        if (projectName == null)
            return null;

        for (Project openProject : getOpenProjects()) {
            if (openProject.getName().equals(projectName)) {
                return openProject;
            }
        }
        return null;
    }

    public static Project[] getOpenProjects() {
        Project[] openProjects = null;
        if (openProjects == null) {
            openProjects = ProjectManager.getInstance().getOpenProjects();
        }
        return openProjects;
    }
}
