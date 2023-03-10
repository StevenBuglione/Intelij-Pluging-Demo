package com.github.stevenbuglione.intelijplugingdemo.actions;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;


import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.*;


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

    public String getCodeBlockText(String statements) {
        StringBuilder sb = new StringBuilder();

        for (String statement : statements.split(";")) {
            statement = statement.trim();
            if (!statement.isEmpty()) {
                sb.append(statement).append(";");
            }
        }

        return sb.toString();
    }


    @Override
    public void navigate(MouseEvent e, PsiComment comment) {
        String searchText = comment.getText();
        if (searchText == null || searchText.isEmpty()) {
            return;
        }

        // Display the text from the comment in a JOptionPane message dialog
        int option = JOptionPane.showOptionDialog(
                null,                            // parent component
                searchText,                     // message
                "Insert Comment Text",           // title
                JOptionPane.YES_NO_CANCEL_OPTION,// option type
                JOptionPane.QUESTION_MESSAGE,   // message type
                null,                            // icon
                new String[]{"Yes", "No", "Cancel"},// options
                "Yes"                            // default option
        );

        // If the user chooses "Yes", insert the text into their function below the comment
        if (option == JOptionPane.YES_OPTION) {
            // Get the method that the comment is attached to
            PsiElement parent = comment.getParent();
            while (parent != null && !(parent instanceof PsiMethod)) {
                parent = parent.getParent();
            }
            if (parent instanceof PsiMethod) {
                PsiMethod oldMethod = (PsiMethod) parent;

                // Get the text of the method
                String methodText = oldMethod.getText();

                // Get the text of the comment
                String commentText = getCodeBlockText(cleanString(comment.getText()));

                // Get the index of the comment in the method text
                int commentIndex = methodText.indexOf(commentText);

                // Test exe
                String chatGPT = runDemoExe(commentText);

                // Get the index of the end of the comment
                int endIndex = commentIndex + commentText.length();

                // Get the method signature
                PsiType returnType = oldMethod.getReturnType();
                String methodName = oldMethod.getName();
                PsiParameterList parameterList = oldMethod.getParameterList();
                PsiElementFactory factory = JavaPsiFacade.getElementFactory(oldMethod.getProject());
                PsiMethod newMethod = factory.createMethod(methodName, returnType);
                newMethod.getParameterList().replace(parameterList);

                // Add statements to method body
                for (String statement : commentText.split(";")) {
                    statement = statement.trim();
                    if (!statement.isEmpty()) {
                        newMethod.getBody().add(factory.createStatementFromText(statement + ";", null));
                    }
                }
                newMethod.getModifierList().replace(oldMethod.getModifierList());

                // Replace the old method with the new one
                WriteCommandAction.runWriteCommandAction(oldMethod.getProject(), () -> {
                    oldMethod.replace(newMethod);
                });
            }
        }
    }


    public String runDemoExe(String arg) {
        String output = "";
        try {
            // Get the input stream for the demo.exe file in the resources folder
            InputStream inputStream = getClass().getResourceAsStream("/META-INF/demo.exe");
            // Create a temporary file to write the input stream to
            File tempFile = File.createTempFile("demo", ".exe");
            tempFile.deleteOnExit();
            // Write the input stream to the temporary file
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();

            // Construct the command to run the demo.exe file with the argument
            String command = "\"" + tempFile.getAbsolutePath() + "\" \"" + arg + "\"";

            // Run the command and read the output
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output += line;
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output;
    }
}



