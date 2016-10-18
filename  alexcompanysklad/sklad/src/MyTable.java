import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;


public class MyTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private int oldRow=-1, oldCol=-1;
	public MyTable(TableModel model)   
    {   
        super(model);   
        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);   
        setDefaultRenderer(String.class, new Renderer());   
        DefaultCellEditor dce = new DefaultCellEditor(new JTextField());   
        dce.setClickCountToStart(1);   
        setDefaultEditor(String.class, dce);   
        setOpaque(false);   
//        setShowGrid(false);   
        configure();
    }   
    
    public void changeSelection(int rowIndex,int columnIndex,boolean toggle,boolean extend){
//    	if (getSelectedRow()==rowIndex && getSelectedColumn()==columnIndex)
    		super.changeSelection(rowIndex, columnIndex, toggle, extend);
//    	if ()
    }
    private class Renderer implements TableCellRenderer   
    {   
        DefaultTableCellRenderer renderer;   
        JTextField textField;   
        protected Border border = new EmptyBorder(1, 1, 1, 1);   

        public Renderer()   
        {   
            renderer = new DefaultTableCellRenderer();   
            textField = new JTextField();   
            textField.setHorizontalAlignment(SwingConstants.RIGHT);   
        }   

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)   
        {   
            if (!isCellEditable(row, column))   
            {   
                renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);   
                renderer.setHorizontalAlignment(column == 0 ? SwingConstants.LEFT : SwingConstants.RIGHT);   
                renderer.setBackground(Color.GRAY.brighter());   
                renderer.setOpaque(false);   
                renderer.setFont(table.getFont().deriveFont(9f).deriveFont(Font.BOLD));   
                renderer.setForeground(Color.BLACK);   
                renderer.setBorder(border);   
                return renderer;   
            }   
            textField.setText(value.toString());   
            return textField;   
        }   
    }   
    private void configure()   
    {   
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);   
        setCellSelectionEnabled(true);   
        // Add SelectionListeners to track selection changes   
        // across columns.   
        getColumnModel().getSelectionModel().addListSelectionListener(   
                    new ExploreSelectionListener());   
    }   

    // Add this class to the body of MyTable class.   
    private class ExploreSelectionListener implements ListSelectionListener   
    {   
        public void valueChanged(ListSelectionEvent e)   
        {   
            
        	if(!e.getValueIsAdjusting())   
            {   
            	if (!((naklTableModel)getModel()).getEditable())
            		return;
            	int row = getEditingRow(); 
                if (row==-1)
                	row = getSelectedRow();
                int col = getEditingColumn();
                if (col==-1)
                	col=getSelectedColumn();
                // Make sure we start with legal values.   
                while(col < 0) col++;   
                while(row < 0) row++;
                if (!isCellEditable(row, col)){
                	col=3;
                }
                // Find the next editable cell.
/*                while(!isCellEditable(row, col))   
                {   
                    row++;   
                    if(row > getRowCount()-1)   
                    {   
                        row = 1;   
                        col = (col == getColumnCount()-1) ? 1 : col+1;   
                    }   
                }
                while(!isCellEditable(row, col))   
                {   
                    col++;   
                    if(col > getColumnCount()-1)   
                    {   
                        col = 1;   
                        row = (row == getRowCount()-1) ? 1 : row+1;   
                    }   
                }   
*/                // Select the cell in the table.   
                final int r = row, c = col;   
                EventQueue.invokeLater(new Runnable()   
                {   
                    public void run()   
                    {   
                        changeSelection(r, c, false, false);   
                    }   
                });   
                // Edit.   
/*                if(isCellEditable(row, col))   
                {   
                    editCellAt(row, col);   
                    ((JTextField)editorComp).selectAll();   
                    editorComp.requestFocusInWindow();   
                }   
  */          }   
        }   
    }   
} 

