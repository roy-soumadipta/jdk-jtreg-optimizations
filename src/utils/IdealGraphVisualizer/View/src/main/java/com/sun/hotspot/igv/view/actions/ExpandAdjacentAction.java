/*
 * Copyright (c) 2022, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 */
package com.sun.hotspot.igv.view.actions;

import com.sun.hotspot.igv.graph.Figure;
import com.sun.hotspot.igv.view.DiagramViewModel;
import com.sun.hotspot.igv.view.EditorTopComponent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.openide.util.HelpCtx;


public abstract class ExpandAdjacentAction extends ModelAwareAction {

    protected void expandFigures(Function<Figure, List<Figure>> getAdjacentFigures) {
        EditorTopComponent editor = EditorTopComponent.getActive();
        if (editor != null) {
            Set<Figure> selectedFigured = editor.getModel().getSelectedFigures();
            Set<Figure> expandedFigures = new HashSet<>(selectedFigured);
            for (Figure selectedFigure : selectedFigured) {
                expandedFigures.addAll(getAdjacentFigures.apply(selectedFigure));
            }
            editor.getModel().showFigures(expandedFigures);
        }
    }

    public abstract String getName();

    @Override
    public boolean isEnabled(DiagramViewModel model) {
        return model != null && !model.getSelectedNodes().isEmpty();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
