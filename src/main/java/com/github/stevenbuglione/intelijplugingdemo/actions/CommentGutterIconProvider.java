package com.github.stevenbuglione.intelijplugingdemo.actions;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.*;
import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.openapi.editor.markup.GutterIconRenderer;

import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.ConstantFunction;
import com.intellij.util.Function;
import com.intellij.util.IconUtil;
import com.intellij.util.ui.ColorIcon;
import com.intellij.util.ui.EmptyIcon;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

