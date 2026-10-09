package com.tourcompany.forms;

import com.tourcompany.services.KetQuaXuLy;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class Ui {
    private Ui() {}
    public static void showResult(Component parent, KetQuaXuLy r){ if(r==null)return; JOptionPane.showMessageDialog(parent,r.getThongBao(),r.isThanhCong()?"Thông báo":"Không thể thực hiện",r.isThanhCong()?JOptionPane.INFORMATION_MESSAGE:JOptionPane.WARNING_MESSAGE); }
    public static void error(Component parent,String msg){JOptionPane.showMessageDialog(parent,msg,"Lỗi",JOptionPane.ERROR_MESSAGE);}
    public static boolean confirm(Component parent,String msg){return JOptionPane.showConfirmDialog(parent,msg,"Xác nhận",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;}
    public static String ask(Component parent,String label,String initial){Object v=JOptionPane.showInputDialog(parent,label,"Nhập thông tin",JOptionPane.QUESTION_MESSAGE,null,null,initial==null?"":initial);return v==null?null:String.valueOf(v).trim();}
    public static String[] fields(Component parent,String title,String[] labels,String[] initial){
        JPanel p=new JPanel(new GridBagLayout()); GridBagConstraints c=new GridBagConstraints(); c.insets=new Insets(4,6,4,6); c.fill=GridBagConstraints.HORIZONTAL;
        JTextField[] inputs=new JTextField[labels.length];
        for(int i=0;i<labels.length;i++){c.gridx=0;c.gridy=i;c.weightx=0; p.add(new JLabel(labels[i]),c); c.gridx=1;c.weightx=1;inputs[i]=new JTextField(initial!=null&&i<initial.length&&initial[i]!=null?initial[i]:"",24);p.add(inputs[i],c);}
        int res=JOptionPane.showConfirmDialog(parent,p,title,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);if(res!=JOptionPane.OK_OPTION)return null;String[] out=new String[inputs.length];for(int i=0;i<inputs.length;i++)out[i]=inputs[i].getText().trim();return out;
    }
    public static LocalDate parseDate(Component parent,String text,String label){try{return LocalDate.parse(text);}catch(DateTimeParseException e){error(parent,label+" phải theo định dạng yyyy-MM-dd.");return null;}}
    public static BigDecimal parseMoney(Component parent,String text,String label){try{BigDecimal d=new BigDecimal(text.replace(",","").trim());if(d.signum()<0)throw new NumberFormatException();return d;}catch(Exception e){error(parent,label+" phải là số tiền không âm.");return null;}}
    public static int parseInt(Component parent,String text,String label){try{return Integer.parseInt(text.trim());}catch(Exception e){error(parent,label+" phải là số nguyên.");return Integer.MIN_VALUE;}}
    public static JFrame frame(String title,Component content){JFrame f=new JFrame(title);f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);f.setContentPane((Container)content);f.setSize(1050,650);f.setLocationRelativeTo(null);return f;}
}
