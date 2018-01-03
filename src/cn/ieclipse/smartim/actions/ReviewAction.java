package cn.ieclipse.smartim.actions;

import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

/**
 * Created by Jamling on 2018/1/2.
 */
public class ReviewAction extends EditorAction {

    public ReviewAction() {
        this(new ReviewHandler());
    }

    protected ReviewAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
        setupHandler(new ReviewHandler());
    }
}
