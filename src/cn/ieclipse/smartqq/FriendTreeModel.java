package cn.ieclipse.smartqq;

import org.junit.experimental.categories.Categories;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.List;

/**
 * Created by Jamling on 2017/7/1.
 */
public class FriendTreeModel extends DefaultTreeModel {

    public FriendTreeModel(TreeNode root, List<Categories> categories) {
        super(root);
        if (categories != null) {
            for (Categories c : categories) {
                //insertNodeInto(new DefaultMutableTreeNode(c), root);
            }
        }
    }


}

class CateTreeNode extends DefaultMutableTreeNode {

}

