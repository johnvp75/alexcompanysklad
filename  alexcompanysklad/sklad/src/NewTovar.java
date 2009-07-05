import java.awt.GridLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewTovar extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JLabel inBoxLabel = null;
	private JTextField inBoxTextField = null;
	private JLabel TovarNameLabel = null;
	private JTextField TovarNameTextField = null;
	private JCheckBox AkciaCheckBox = null;
	private JLabel DiscLabel = null;
	private JTextField DiscTextField = null;
	private JPanel ButtonPanel = null;
	private JButton BarCodeButton = null;
	private JButton okButton = null;
	private JButton CancelButton = null;
	private boolean ok;
	private String Sklad;
	private String Price;
	private JDialog dialog;
	/**
	 * This is the default constructor
	 */
	public NewTovar() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(582, 180);
		this.setLayout(new BorderLayout());
		this.add(getJPanel(), BorderLayout.CENTER);
		this.add(getButtonPanel(), BorderLayout.SOUTH);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			DiscLabel = new JLabel();
			DiscLabel.setBounds(new Rectangle(175, 85, 53, 22));
			DiscLabel.setText("Скидка");
			TovarNameLabel = new JLabel();
			TovarNameLabel.setBounds(new Rectangle(7, 22, 142, 22));
			TovarNameLabel.setText("Наименование товара:");
			inBoxLabel = new JLabel();
			inBoxLabel.setBounds(new Rectangle(7, 53, 136, 22));
			inBoxLabel.setText("Кол-во шт. в упаковке");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(inBoxLabel, null);
			jPanel.add(getInBoxTextField(), null);
			jPanel.add(TovarNameLabel, null);
			jPanel.add(getTovarNameTextField(), null);
			jPanel.add(getAkciaCheckBox(), null);
			jPanel.add(DiscLabel, null);
			jPanel.add(getDiscTextField(), null);
			jPanel.setBounds(0, 0, 582, 150);
		}
		return jPanel;
	}

	/**
	 * This method initializes inBoxTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getInBoxTextField() {
		if (inBoxTextField == null) {
			inBoxTextField = new JTextField();
			inBoxTextField.setBounds(new Rectangle(151, 53, 73, 22));
			inBoxTextField.setText("1");
			inBoxTextField.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					DiscTextField.requestFocus();
				}
			});
		}
		return inBoxTextField;
	}

	/**
	 * This method initializes TovarNameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTovarNameTextField() {
		if (TovarNameTextField == null) {
			TovarNameTextField = new JTextField();
			TovarNameTextField.setBounds(new Rectangle(151, 22, 416, 22));
			TovarNameTextField.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					 inBoxTextField.requestFocus();
				}
			});
		}
		return TovarNameTextField;
	}

	/**
	 * This method initializes AkciaCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getAkciaCheckBox() {
		if (AkciaCheckBox == null) {
			AkciaCheckBox = new JCheckBox();
			AkciaCheckBox.setBounds(new Rectangle(7, 85, 148, 21));
			AkciaCheckBox.setText("Акционный товар");
		}
		return AkciaCheckBox;
	}

	/**
	 * This method initializes DiscTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDiscTextField() {
		if (DiscTextField == null) {
			DiscTextField = new JTextField();
			DiscTextField.setBounds(new Rectangle(243, 85, 56, 22));
			DiscTextField.setText("0");
			DiscTextField.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					BarCodeButton.requestFocus();
				}
			});
		}
		return DiscTextField;
	}

	/**
	 * This method initializes ButtonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (ButtonPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(0, 0, 0, 29);
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.CENTER;
			gridBagConstraints.insets = new Insets(0, 1, 0, 29);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.NONE;
			ButtonPanel = new JPanel();
			ButtonPanel.setLayout(new GridBagLayout());
			ButtonPanel.add(getBarCodeButton(), gridBagConstraints);
			ButtonPanel.add(getOkButton(), gridBagConstraints1);
			ButtonPanel.add(getCancelButton(), gridBagConstraints2);
		}
		return ButtonPanel;
	}

	/**
	 * This method initializes BarCodeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBarCodeButton() {
		if (BarCodeButton == null) {
			BarCodeButton = new JButton();
			BarCodeButton.setText("Ввести штрих-код");
		}
		return BarCodeButton;
	}

	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("Добавить");
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (TovarNameTextField.getText().trim().equals("")){
						JOptionPane.showMessageDialog(dialog,"Наименование не может быть пустой строкой! \n Повторите ввод!","Неверное название",JOptionPane.ERROR_MESSAGE);
						return;
					}
					ResultSet rs=DataSet.QueryExec("select count(*) from tovar where name like '%"+TovarNameTextField.getText().trim()+"%'", false);
					try {
						rs.next();
						if (rs.getInt(1)>0){
							JOptionPane.showMessageDialog(dialog,"Такой товар в базе существует! \n Будьте внимательней!","Повторное название",JOptionPane.ERROR_MESSAGE);
							return;
						}
						else{
							rs = DataSet.QueryExec("Select max(id_tovar)+1 from tovar", false);
							rs.next();
							int id_tovar=rs.getInt(1);
							String SQL="insert into tovar (id_tovar,name,kol) values ("+id_tovar+",'"+TovarNameTextField.getText().trim()+"', "+inBoxTextField.getText()+")"; 
							DataSet.UpdateQuery(SQL);
							rs = DataSet.QueryExec("Select max(id_nom)+1 from kart", false);
							rs.next();
							int id_nom=rs.getInt(1);
							SQL="insert into kart (id_tovar, id_group, id_nom, id_skl) select "+id_tovar+", -1, "+id_nom+", id_skl from sklad where name='"+Sklad+"'";
							DataSet.UpdateQuery(SQL);
							int isakcia=0;
							if (AkciaCheckBox.isSelected())
								isakcia=1;
							SQL="insert into price (id_tovar, cost, akciya, isakcia, id_skl, id_price) select "+id_tovar+" ,0, "+DiscTextField.getText()+", "+isakcia+", (select id_skl from sklad where name='"+Sklad+"'), id_price from type_price where name='"+Price+"'";
							DataSet.UpdateQuery(SQL);
							ok=true;
							DataSet.commit();
							dialog.setVisible(false);
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
				}
			});
		}
		return okButton;
	}

	/**
	 * This method initializes CancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (CancelButton == null) {
			CancelButton = new JButton();
			CancelButton.setText("Отмена");
			CancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ok=false;
					dialog.setVisible(false);
				}
			});
		}
		return CancelButton;
	}
	public boolean showDialog(Component parent, String title){
		ok=false;
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame)parent;
		else
			owner = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, parent);
		if (dialog==null || dialog.getOwner()!=owner){
			dialog=new JDialog(owner,true);
			dialog.add(this);
			dialog.pack();
		}
		dialog.setTitle(title);
//		dialog.setLocation(400-dialog.getWidth()/2, 300-dialog.getHeight()/2);
		dialog.setSize(582, 200);
		dialog.setLocationRelativeTo(parent);
		initform();
		dialog.setVisible(true);
		
		return ok;
	}
	public String getTovar(){
		return TovarNameTextField.getText();
	}
	public void setTovar(String aValue){
		TovarNameTextField.setText(aValue);
	}
	private void initform(){
		inBoxTextField.setText("1");
		DiscTextField.setText("0");
		AkciaCheckBox.setSelected(false);
	}
	public void setSklad(String aValue){
		Sklad=aValue;
	}
	public void setPrice(String aValue){
		Price=aValue;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
