/*
 * Copyright 2014-2017 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.smartim.common;

import javax.swing.SizeRequirements;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.GlyphView;
import javax.swing.text.View;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.InlineView;
import javax.swing.text.html.ParagraphView;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年12月27日
 *       
 */
public class WrapHTMLFactory extends HTMLEditorKit.HTMLFactory {
    public static final String SEPARATOR = System.getProperty("line.separator");
    
    @Override
    public View create(Element elem) {
        View v = super.create(elem);
        if (v instanceof InlineView) {
            return new WrapInlineView(elem);
        }
        else if (v instanceof ParagraphView) {
            return new WrapParagraphView(elem);
        }
        return v;
    }
    
    class WrapInlineView extends InlineView {
        
        public WrapInlineView(Element elem) {
            super(elem);
        }
        
        @Override
        public int getBreakWeight(int axis, float pos, float len) {
            // return GoodBreakWeight;
            if (axis == View.X_AXIS) {
                checkPainter();
                int p0 = getStartOffset();
                int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos,
                        len);
                if (p1 == p0) {
                    // can't even fit a single character
                    return View.BadBreakWeight;
                }
                try {
                    // if the view contains line break char return forced break
                    if (getDocument().getText(p0, p1 - p0)
                            .indexOf(WrapHTMLFactory.SEPARATOR) >= 0) {
                        return View.ForcedBreakWeight;
                    }
                } catch (BadLocationException ex) {
                    // should never happen
                }
                
            }
            return super.getBreakWeight(axis, pos, len);
        }
        
        public View breakView(int axis, int p0, float pos, float len) {
            if (axis == View.X_AXIS) {
                checkPainter();
                int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos,
                        len);
                try {
                    // if the view contains line break char break the view
                    int index = getDocument().getText(p0, p1 - p0)
                            .indexOf(WrapHTMLFactory.SEPARATOR);
                    if (index >= 0) {
                        GlyphView v = (GlyphView) createFragment(p0,
                                p0 + index + 1);
                        return v;
                    }
                } catch (BadLocationException ex) {
                    // should never happen
                }
                
            }
            return super.breakView(axis, p0, pos, len);
        }
    }
    
    class WrapParagraphView extends ParagraphView {
        
        public WrapParagraphView(Element elem) {
            super(elem);
        }
        
        @Override
        protected SizeRequirements calculateMinorAxisRequirements(int axis,
                SizeRequirements r) {
            if (r == null) {
                r = new SizeRequirements();
            }
            float pref = layoutPool.getPreferredSpan(axis);
            float min = layoutPool.getMinimumSpan(axis);
            // Don't include insets, Box.getXXXSpan will include them.
            r.minimum = (int) min;
            r.preferred = Math.max(r.minimum, (int) pref);
            r.maximum = Integer.MAX_VALUE;
            r.alignment = 0.5f;
            return r;
        }
        
    };
}
