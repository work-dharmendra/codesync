package com.codesync.senderui;

import com.codesync.sender.injector.AppInjector;
import com.codesync.sender.listener.FileWatcher;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

/**
 * Created by dharmendra.singh on 4/20/2015.
 */
public class OpenGUI {
    public static void main(String []args){
        OpenGUI openGUI = new OpenGUI();
        openGUI.createUI();
    }

    public void createUI(){
        JFrame main = new JFrame("CodeSync");
        // Don't use this nonsense!
        //main.setBounds(550,550,500,500);
        main.getContentPane().setPreferredSize(new Dimension(300, 300));
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JTextField filePath = new JTextField();
        main.setLayout(new GridLayout(3,2));
        JPanel pane = new JPanel(new GridLayout(3,2));
        main.add(pane);
        pane.add(new JLabel("Path of codesync.xml: "));
        pane.add(filePath);
        JButton syncButton = new JButton("Sync");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 2;
        pane.add(syncButton, gbc);
        JTextArea textArea = new JTextArea ("Test");

        JScrollPane scroll = new JScrollPane (textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.add(scroll, gbc);

        syncButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Injector injector = Guice.createInjector(new AppInjector(Paths.get(filePath.getText())));

                FileWatcher fileWatcher = injector.getInstance(FileWatcher.class);
                fileWatcher.watchDir();
            }
        });
        main.pack();
        main.setVisible(true);
    }
}
