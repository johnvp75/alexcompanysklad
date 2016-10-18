
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;
public class AutoComplete extends JComboBox	implements JComboBox.KeySelectionManager
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String searchFor;
	private long lap;
	private boolean selected=false;
	public class CBDocument extends PlainDocument
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void insertString(int offset, String str, AttributeSet a) throws BadLocationException
		{
			if (str==null) return;
			super.insertString(offset, str, a);
//			System.out.println(str);
			if(!isPopupVisible() && str.length() != 0) fireActionEvent();
		}
	}
	public AutoComplete()
	{
		super();
		lap = new java.util.Date().getTime();
		setKeySelectionManager(this);
		JTextField tf;
		if(getEditor() != null)
		{
			tf = (JTextField)getEditor().getEditorComponent();
			if(tf != null)
			{
				tf.setDocument(new CBDocument());
				addKeyListener(new KeyAdapter(){
					public void keyPressed(KeyEvent event){
						int keyCode=event.getKeyCode();
						if (keyCode==107){
							event.setKeyCode(KeyEvent.VK_UNDEFINED);
						}
					}
				});
				addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						JTextField tf = (JTextField)getEditor().getEditorComponent();
						String text = tf.getText();
/*						if (text.equals("+")){
							tf.setText(previos);
							return;
						}else{
							previos=text;
						}
*/
						ComboBoxModel aModel = getModel();
						String current;
						boolean found=false;
						for(int i = 0; i < aModel.getSize(); i++)
						{
							
							current = aModel.getElementAt(i).toString();
							if(current.toLowerCase().startsWith(text.toLowerCase()))
							{
								((JComboBox)evt.getSource()).setSelectedItem(current);
								
								tf.setText(current);
								tf.setSelectionStart(text.length());
								tf.setSelectionEnd(current.length());
								selected=tf.getSelectionStart()>=tf.getSelectionEnd();
								found=true;
								fireActionEvent();
								break;
							}else{
								found=false;
							}
						}
						if (!found) selected=true;
					}
					
				});
			}
		}
	}
	public int selectionForKey(char aKey, ComboBoxModel aModel)
	{
		long now = new java.util.Date().getTime();
		if (searchFor!=null && aKey==KeyEvent.VK_BACK_SPACE &&	searchFor.length()>0)
		{
			searchFor = searchFor.substring(0, searchFor.length() -1);
		}
		else
		{
			//System.out.println(lap);
			// Kam nie hier vorbei.
			if(lap + 1000 < now)
				searchFor = "" + aKey;
			else
				searchFor = searchFor + aKey;
		}
		lap = now;
		String current;
		for(int i = 0; i < aModel.getSize(); i++)
		{
			current = aModel.getElementAt(i).toString().toLowerCase();
			if (current.toLowerCase().startsWith(searchFor.toLowerCase())) {
				selected=current.toLowerCase().equals(aModel.getElementAt(i).toString().toLowerCase());
				return i;
			}
		}
		selected=true;
		return -1;
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public void fireActionEvent()
	{
		super.fireActionEvent();
	}
}
