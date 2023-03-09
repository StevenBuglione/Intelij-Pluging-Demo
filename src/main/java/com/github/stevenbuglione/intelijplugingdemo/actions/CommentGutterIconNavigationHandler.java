package com.github.stevenbuglione.intelijplugingdemo.actions;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.psi.PsiComment;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

class CommentGutterIconNavigationHandler implements GutterIconNavigationHandler<PsiComment> {

    public static String cleanString(String input) {
        String result = input.trim();

        // Remove starting "/*"
        if (result.startsWith("/*")) {
            result = result.substring(2);
        }

        // Remove ending "*/"
        if (result.endsWith("*/")) {
            result = result.substring(0, result.length() - 2);
        }

        // Remove the first instance of "//"
        int index = result.indexOf("//");
        if (index != -1) {
            result = result.substring(0, index) + result.substring(index + 2);
        }

        // Remove any new line characters
        result = result.replaceAll("\\r|\\n", "");

        return result;
    }



    @Override
    public void navigate(MouseEvent e, PsiComment comment) {
        String searchText = comment.getText();
        if (searchText == null || searchText.isEmpty()) {
            return;
        }
        String searchUrl = "https://www.google.com/search?q=" + cleanString(searchText);
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(searchUrl));
            } else {
                throw new UnsupportedOperationException("Desktop not supported");
            }
        } catch (IOException | URISyntaxException | UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
    }
}


