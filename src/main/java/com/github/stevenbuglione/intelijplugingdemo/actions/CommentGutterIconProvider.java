package com.github.stevenbuglione.intelijplugingdemo.actions;
import com.intellij.codeInsight.daemon.*;
import com.intellij.openapi.editor.markup.GutterIconRenderer;

import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.util.Function;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.util.function.Supplier;

public class CommentGutterIconProvider implements LineMarkerProvider {

    private static final Icon ICON = IconLoader.getIcon("/icons/cherryPick.svg", CommentGutterIconProvider.class);

    @Override
    public LineMarkerInfo<? extends PsiElement> getLineMarkerInfo(PsiElement element) {
        if (!(element instanceof PsiComment)) {
            return null;
        }

        PsiComment comment = (PsiComment) element;
        String text = comment.getText();

        if (text == null || text.isEmpty()) {
            return null;
        }

        Function<? super PsiComment, @NlsContexts.Tooltip String> commentTooltipProvider = comment1 -> "Search on Google";
        Supplier<@NotNull @Nls String> accessibleNameProvider = () -> "Google search";
        return new LineMarkerInfo<>(
                comment,
                comment.getTextRange(),
                ICON,
                commentTooltipProvider,
                new CommentGutterIconNavigationHandler(),
                GutterIconRenderer.Alignment.LEFT,
                accessibleNameProvider
        );
    }
}

