import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;

import javax.swing.JDialog;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JList;

public class NewBarCode extends JDialog {

	private final long serialVersionUID = 1L;
	private JPanel jContentPane = null;  //  @jve:decl-index=0:visual-constraint="18,36"
	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JTextField CodeEdit = null;
	private JButton oKButton = null;
	private String sklad;  //  @jve:decl-index=0:
	private String tovar;
	private JList CodeList = null;
	private JButton DeleteButton = null;
	private void setSklad(String sklad) {
		this.sklad = sklad;
	}

	private String getSklad() {
		return sklad;
	}

	public NewBarCode(String sklad, String tovar) {
		// TODO Auto-generated constructor stub
		setSklad(sklad);
		setTovar(tovar);
		initialize();
		try{
			ResultSet rs=DataSet.QueryExec("select bar_code from bar_code where id_tovar=(select id_tovar from tovar where name='"+getTovar()+
					"') and id_skl=(select id_skl from sklad where name='"+getSklad()+"') order by bar_code", false);
			while (rs.next()){
				((DataListModel)CodeList.getModel()).add(rs.getString(1));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		setTitle(tovar);
		MainWindow.Scaner.init(2, "", "", this);
	}



	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(595, 338);
		this.setModal(true);
		this.setContentPane(getJPanel());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(17, 28, 82, 16));
			jLabel.setText("Штрих-код:");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(jLabel, null);
			jPanel.add(getCodeEdit(), null);
			jPanel.add(getOKButton(), null);
			jPanel.add(getCodeList(), null);
			jPanel.add(getDeleteButton(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes CodeEdit	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCodeEdit() {
		if (CodeEdit == null) {
			CodeEdit = new JTextField();
			CodeEdit.setBounds(new Rectangle(100, 26, 332, 21));
			CodeEdit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (CodeEdit.getText().trim().length()>0) {
						inputCod(CodeEdit.getText().trim());
						CodeEdit.setText("");
					}
				}
			});
		}
		return CodeEdit;
	}

	/**
	 * This method initializes oKButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOKButton() {
		if (oKButton == null) {
			oKButton = new JButton();
			oKButton.setBounds(new Rectangle(135, 267, 149, 23));
			oKButton.setText("OK!");
			oKButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try{
						ResultSet rs=DataSet.QueryExec("Select id_skl from sklad where name='"+getSklad()+"'", false);
						rs.next();
						int skl=rs.getInt(1);
						rs=DataSet.QueryExec("Select id_tovar from tovar where name='"+getTovar()+"'", false);
						rs.next();
						int id=rs.getInt(1);
						DataSet.UpdateQuery("savepoint point1");
						DataSet.UpdateQuery("delete from bar_code where id_tovar="+id+" and id_skl="+skl);
						for (int i=0;i<((DataListModel)CodeList.getModel()).getSize();i++)
							DataSet.UpdateQuery("insert into bar_code (id_tovar,id_skl,bar_code) values ("+id+", "+skl+", '"+((DataListModel)CodeList.getModel()).getElementAt(i)+"')");
						close();
					}catch(Exception exc){
			            JOptionPane.showMessageDialog(null, "Запись не удалась. Повторите попытку.", "Ошибка записи", JOptionPane.ERROR_MESSAGE);
			            try {
			                DataSet.UpdateQuery("rollback to point1");
			            } catch (SQLException ex) {
			                ex.printStackTrace();
			            }
			            exc.printStackTrace();
					}

				}
			});
		}
		return oKButton;
	}
	public void inputCod(String kod){
        try
        {
            ResultSet rs=DataSet.QueryExec("Select count(*) from bar_code where bar_code='"+kod.trim()+"' and id_skl=(select id_skl from sklad where name='"+getSklad()+"')", false);
            rs.next();
            if (rs.getInt(1)>0)
                if (JOptionPane.showConfirmDialog(null, "Такой штрих-код имееться в базе для этого склада, ввести повторный?", "Повторный штрих-код", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.NO_OPTION)
                    return;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        if (((DataListModel)CodeList.getModel()).pos(kod)==-1){
            ((DataListModel)CodeList.getModel()).add(kod);
        }
        

	}

	private String getTovar() {
		return tovar;
	}

	private void setTovar(String tovar) {
		this.tovar = tovar;
	}

	/**
	 * This method initializes CodeList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getCodeList() {
		if (CodeList == null) {
			CodeList = new JList(new DataListModel());
			CodeList.setBounds(new Rectangle(20, 60, 411, 187));
		}
		return CodeList;
	}

	/**
	 * This method initializes DeleteButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDeleteButton() {
		if (DeleteButton == null) {
			DeleteButton = new JButton();
			DeleteButton.setBounds(new Rectangle(450, 76, 123, 21));
			DeleteButton.setText("Удалить");
			DeleteButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
			        ((DataListModel)CodeList.getModel()).remove(CodeList.getSelectedIndex());
				}
			});
		}
		return DeleteButton;
	}
	private void close(){
		this.setVisible(false);
	}

}  //  @jve:decl-index=0:visual-constraint="195,28"
