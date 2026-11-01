package com.caooc.forms;
import javax.swing.*;import java.awt.*;
public class FrmModule extends JFrame {
 protected final JTabbedPane tabs=new JTabbedPane();
 public FrmModule(String title,String... tables){super(title+" - Quản lý cho thuê văn phòng");setDefaultCloseOperation(DISPOSE_ON_CLOSE);setSize(1100,690);setLocationRelativeTo(null);setLayout(new BorderLayout());
  for(String table:tables)tabs.addTab(table,new FrmTable(table));add(tabs,BorderLayout.CENTER);
 }
 protected void action(String label,int tabIndex,Runnable runnable){JPanel p=(JPanel)tabs.getComponentAt(tabIndex);JButton b=new JButton(label);b.addActionListener(e->runnable.run());((JPanel)p.getComponent(0)).add(b);p.revalidate();p.repaint();}
 protected void reload(){for(int i=0;i<tabs.getTabCount();i++){Component c=tabs.getComponentAt(i);if(c instanceof FrmTable t)t.load();}}
}
