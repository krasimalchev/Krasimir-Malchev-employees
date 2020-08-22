package com.sirma.inteview.findteammaxduration.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;

import com.sirma.inteview.findteammaxduration.model.TeamDataController;
import com.sirma.inteview.findteammaxduration.model.TeamExperience;



public class TeamFrame implements ActionListener{
    JTextArea output;
    JScrollPane scrollPane;
    JFileChooser fc;
    JPanel contentPane;
    JMenuItem openFileItem;
    JMenuItem exitItem;
    JTable table = new JTable();
    JLabel statusString;
    
    
    public void updateTableData(TeamExperience team) {
    	List<EmployeeTabelRow> rows = new ArrayList<EmployeeTabelRow>(team.getProjectsTime().size());
    	Iterator<Entry<Integer, Long>> it = team.getProjectsTime().entrySet().iterator();
    	while(it.hasNext()) {
    		Entry<Integer, Long> project = it.next();
    		rows.add(new EmployeeTabelRow(team.getOneEmployee(),
    				team.getOtherEmployee(), project.getKey(), project.getValue()));
    	}
    	table.setModel(new MyTableModel(rows));
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
        JCheckBoxMenuItem cbMenuItem;

        menuBar = new JMenuBar();

        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription(
                "File");
        menuBar.add(menu);

        openFileItem = new JMenuItem("Open File...",
                                 KeyEvent.VK_O);
        openFileItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openFileItem.getAccessibleContext().setAccessibleDescription(
                "Opens a File");
        openFileItem.addActionListener(this);
        menu.add(openFileItem);
        menu.addSeparator();

        cbMenuItem = new JCheckBoxMenuItem("Include end date");
        cbMenuItem.setMnemonic(KeyEvent.VK_I);
        cbMenuItem.setEnabled(false);
        menu.add(cbMenuItem);
        menu.addSeparator();
       
        exitItem = new JMenuItem("Exit");
        exitItem.getAccessibleContext().setAccessibleDescription(
    		   	"Exits the application");
        exitItem.addActionListener(this);
        menu.add(exitItem);


        return menuBar;
    }
    
    class EmployeeTabelRow{
    	private Integer employee1ID;
    	private Integer employee2ID;
    	private Integer projectID;
    	private Long duration;
    	
    	public EmployeeTabelRow(Integer employee1ID, Integer employee2ID,
    			Integer projectID, Long duration) {
    		this.employee1ID = employee1ID;
    		this.employee2ID = employee2ID;
    		this.projectID = projectID;
    		this.duration = duration;
    	}
    	
    	public Long getDuration() {
			return duration;
		}
    	
    	public Integer getEmployee1ID() {
			return employee1ID;
		}
    	
    	public Integer getEmployee2ID() {
			return employee2ID;
		}
    	
    	public Integer getProjectID() {
			return projectID;
		}
    	
    	public void setDuration(Long duration) {
			this.duration = duration;
		}
    	
    	public void setEmployee1ID(Integer employee1id) {
			employee1ID = employee1id;
		}
    	
    	public void setEmployee2ID(Integer employee2id) {
			employee2ID = employee2id;
		}
    	
    	public void setProjectID(Integer projectID) {
			this.projectID = projectID;
		}
    }
    
    class MyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		private static final int EMP1_IDX = 0;
		private static final int EMP2_IDX = 1;
		private static final int PRJ_IDX = 2;
		private static final int DAYS_IDX = 3;

		private String[] columnNames = { "Employee ID #1",
								 		 "Employee ID #2",
								 		 "Project ID",
										 "Days worked" };
		
        private List<EmployeeTabelRow> rows;
         
         public MyTableModel() {
        	 
        	 rows = new ArrayList<EmployeeTabelRow>(25);
         }
         
         public MyTableModel(List<EmployeeTabelRow> rows) {
        	this.rows = rows;
         }
         
        @Override
 		public Object getValueAt(int rowIndex, int columnIndex) {
 			EmployeeTabelRow employee = rows.get(rowIndex);
 			switch(columnIndex) {
 			case EMP1_IDX:
 				return employee.getEmployee1ID();
 			case EMP2_IDX:
 				return employee.getEmployee2ID();
 			case PRJ_IDX:
 				return employee.getProjectID();
 			case DAYS_IDX:
 				return employee.getDuration();
 			}
 			return null;
 		}
  
         public int getColumnCount() {
             return columnNames.length;
         }
  
         public int getRowCount() {
             return rows.size();
         }
  
         public String getColumnName(int col) {
             return columnNames[col];
         }
         
         public Class<?> getColumnClass(int columnIndex) {
             return getValueAt(0, columnIndex).getClass();
         }
  
         public boolean isCellEditable(int row, int col) {
             return false;
         }
         
         public void setValueAt(Object value, int row, int col) {
        	 EmployeeTabelRow employee = rows.get(row);
             switch(col) {
  			case EMP1_IDX:
  				employee.setEmployee1ID((Integer)value);
  				break;
  			case EMP2_IDX:
  				employee.setEmployee2ID((Integer)value);
  				break;
  			case PRJ_IDX:
  				employee.setProjectID((Integer)value);
  				break;
  			case DAYS_IDX:
  				employee.setDuration((Long)value);
  				break;
  			}
             fireTableCellUpdated(row, col);
         }
        
     }

    private Container createContentPane() {
        contentPane = new JPanel(/*new GridLayout(1,0)*/new BorderLayout());
        contentPane.setOpaque(true);

        table = new JTable(new MyTableModel());
       
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
 
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane);
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY),
            new EmptyBorder(4, 4, 4, 4)));
        statusString = new JLabel();
        statusBar.add(statusString);

        contentPane.add(statusBar, BorderLayout.SOUTH);
        
        fc = new JFileChooser();
    
        return contentPane;
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = TeamFrame.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Find Maximum Team Duration");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TeamFrame tf = new TeamFrame();
        frame.setJMenuBar(tf.createMenuBar());
        frame.setContentPane(tf.createContentPane());
        

        frame.setSize(450, 260);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
    	
    	TeamFrame frame = new TeamFrame();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	frame.createAndShowGUI();
            }
        });
    }

	@Override
	public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.openFileItem) {
            int returnVal = fc.showOpenDialog(contentPane);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                TeamDataController controller = new TeamDataController(file);
    			controller.calculateTeams();
    			TeamExperience team = controller.findMaximumDuration();
    			updateTableData(team);
    			statusString.setText("Total Duration: " 
    								+ team.getTotalDuration().toString()
    								+ " days");
               
            }
        }
        
        if (e.getSource() == this.exitItem) {
        	System.exit(0);
        }
	}
}