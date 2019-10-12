package cn.ieclipse.smartim.views;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Jamling on 2017/7/12.
 */
public class ContactTreeNodeDescriptor extends AbstractTreeNode {

    protected ContactTreeNodeDescriptor(Project project, Object value) {
        super(project, value);
    }

    @NotNull @Override public Collection<? extends AbstractTreeNode> getChildren() {
        return null;
    }

    @Override protected void update(PresentationData presentationData) {

    }
}
