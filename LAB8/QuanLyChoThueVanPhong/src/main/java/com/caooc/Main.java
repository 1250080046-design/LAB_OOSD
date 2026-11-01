package com.caooc;
import javax.swing.*;import com.caooc.forms.FrmMain;
public class Main{public static void main(String[]args){SwingUtilities.invokeLater(()->{try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}catch(Exception ignored){}new FrmMain().setVisible(true);});}}
