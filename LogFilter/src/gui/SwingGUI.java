package gui;

import java.awt.BorderLayout;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Toolkit;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import filters.Catalina;
import filters.Custom;
import filters.IrrigationSum;
import reporting.Analysis;


public class SwingGUI extends JFrame{
	/**
	 * 
	 */
	public static File selectedFolder;
	public static File outputFolder = new File("");
	public static boolean output = true;

	private static final long serialVersionUID = 6359649201512611820L;
	private static File[] traceLogFiles = new File[11];
	private static ArrayList<File> catFiles = new ArrayList<>();
	private static File[] ioLogFiles = new File[11];
	private JButton chooseFolderButton = new JButton("Select Folder");
	private JButton chooseOutputButton = new JButton("Output Folder");
	private JButton copyButton = new JButton("Copy results");
	private JButton analyzeButton = new JButton("Analyze Logs");
	private JButton clearAnalysis = new JButton("Clear Text");
	private JTextField folderText = new JTextField("");
	private JTextField outputFolderText = new JTextField("");
	private JLabel startTimeLabel = new JLabel("(time format - YYYY-MM-DDTHH:mm:SS) Start Time :");
	private JLabel ResultsLabel = new JLabel("Results:");
	private JLabel endTimeLabel = new JLabel("End Time:");
	private JTextArea filterDescription = new JTextArea("");
	
	private JTextField startTimeText = new JTextField("");
	private JTextField endTimeText = new JTextField("");
	public static DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	private final RecordTableModel model = new RecordTableModel();
    private JTable customFiltersTable = new JTable(model);
    private JButton add = new JButton("Add");
    private JButton remove = new JButton("Remove Selected");
    
	private JTextArea analyzeResult = new JTextArea("Results: ");
	private JPanel CustomerFiltersPanel = new JPanel();
	private JPanel analyzePanel = new JPanel();
	private JPanel chooseFolderPanel = new JPanel();
	private JPanel chooseFilters = new JPanel();
	public static LocalDateTime startTime = LocalDateTime.MIN;
	public static LocalDateTime endTime = LocalDateTime.MAX;

	public SwingGUI() {
		generateGUI();
	}
	    

	private int id = 0;

    

     public class Record {

        private int id;
        private String value;
        private String file;
        public Record(int id) {
            this.id = id;
        }


        public int getID() {
            return id;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
        public void setFile(String file) {
            this.file = file;
        }

        public String getFile() {
            return file;
        }
        
     }

     public class RecordTableModel extends AbstractTableModel {

    	 
		/**
		 * 
		 */
		private static final long serialVersionUID = -3004069309974289586L;
		private List<Record> lstRecords;
        public RecordTableModel() {
        	
            lstRecords = new ArrayList<>(24);
        }

        public void add(Record record) {
            lstRecords.add(record);
            fireTableRowsInserted(lstRecords.size() - 1, lstRecords.size() - 1);
        }

        public void remove(Record record) {
            if (lstRecords.contains(record)) {
                int index = lstRecords.indexOf(record);
                remove(index);
            }
        }

        public void remove(int index) {
            lstRecords.remove(index);
            fireTableRowsDeleted(index, index);
        }

        @Override
        public int getRowCount() {
            return lstRecords.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class clazz = String.class;
            switch (columnIndex) {
                case 0:
                    clazz = Integer.class;
                    break;
            }
            return clazz;
        }

        @Override
        public String getColumnName(int column) {
            String name = null;
            switch (column) {
                case 0:
                    name = "ID";
                    break;
                case 1:
                    name = "Value";
                    break;
                case 2:
                    name = "File";
                    break;
            }
            return name;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Record record = lstRecords.get(rowIndex);
            Object value = null;
            switch (columnIndex) {
                case 0:
                    value = record.getID();
                    break;
                case 1:
                    value = record.getValue();
                    break;
                case 2:
                    value = record.getFile();
                    break;
            }
            return value;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex >= 1;
        }
        
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            Record record = lstRecords.get(rowIndex);
            switch (columnIndex) {
                case 1:
                    record.setValue(aValue == null ? null : aValue.toString());
                    fireTableCellUpdated(rowIndex, columnIndex);
                    break;
                case 2:
                    record.setFile(aValue == null ? null : aValue.toString());
                    fireTableCellUpdated(rowIndex, columnIndex);
                    break;
            }
        }
    }
		//definition for filter list items
		class CheckListItem {

		  private String label;
		  private boolean isSelected = true;

		  public CheckListItem(String label) {
		    this.label = label;
		  }

		  public boolean isSelected() {
		    return isSelected;
		  }

		  public void setSelected(boolean isSelected) {
		    this.isSelected = isSelected;
		  }

		  @Override
		  public String toString() {
		    return label;
		  }
		}
		
		//rendering for filter list
		class CheckListRenderer extends JCheckBox implements ListCellRenderer<Object> {
			private static final long serialVersionUID = -4451658640631246278L;
			
			//customer component for filter list cell
			public Component getListCellRendererComponent(JList<?> list, Object value,
			      int index, boolean isSelected, boolean hasFocus) {
			    setEnabled(list.isEnabled());
			    setSelected(((CheckListItem) value).isSelected());
			    setFont(list.getFont());
			    setBackground(list.getBackground());
			    setForeground(list.getForeground());
			    setText(value.toString());
			    return this;
			  }
		}
		
	
		//scan selected folder for files
		public static void findFiles() throws IOException {
			Files.walk(Paths.get(selectedFolder.toURI()))
			  .filter(Files::isRegularFile)
			  .forEach(f -> {
				  int logNum = 0;
				  if (f.getFileName().toString().startsWith("catalina")){
					  
				  catFiles.add(f.toFile());
				  }
				  if (f.getFileName().toString().startsWith("IOBusActions")){
					  String[] log = f.toFile().getName().split("IOBusActions.txt.");
					  if (!log[log.length-1].startsWith("IOBusActions")) {
						  logNum =Integer.parseInt(log[log.length-1]);									  
					  }
					 
				  ioLogFiles[logNum]= f.toFile();
				  }	
				  if (f.getFileName().toString().startsWith("TraceToException")){
					  String[] log = f.toFile().getName().split("TraceToException.log.");
					  if (!log[log.length-1].startsWith("TraceToException")) {
						  logNum =Integer.parseInt(log[log.length-1]);								  
					  }
					 
				  traceLogFiles[logNum]= f.toFile();
				  }	  
			  }); 
		}
		
		//check start and end time input values
		public static void checkStartEndTime(JTextField start, JTextField end) {
			 try {
	    		  String requestPattern = "((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([0-2][0-9])(:[0-5][0-9]){2}.?[0-9]?[0-9]?[0-9]?";
	        	Pattern pattern = Pattern.compile(requestPattern);
	        	Matcher matcher = pattern.matcher(start.getText());
	    		  if (matcher.find())
   	    	  {
	    	    	   startTime = LocalDateTime.parse(start.getText(), format);
   	    	  }
	    		  else
	    		  {
	    			  if (!start.getText().isEmpty()) {
	    			  JOptionPane.showMessageDialog(null, "invalid DateTime Format, please use the YYYY-MM-DDTHH:mm:SS format, copy a date from the log or leave empty");
	    			  throw new Exception("invalid DateTimeFormat");
	    			  }
	    			  startTime = LocalDateTime.MIN;
	    		  }
		        	 matcher = pattern.matcher(end.getText());

   	    	  if (matcher.find())
   	    	  {
	    	    	   endTime = LocalDateTime.parse(end.getText(), format);
   	    	  }
   	    	  else
	    		  {
   	    		  if (!end.getText().isEmpty()) {
	    			  JOptionPane.showMessageDialog(null, "invalid DateTime Format, please use the YYYY-MM-DDTHH:mm:SS format, copy a date from the log or leave empty");
	    			  throw new Exception("invalid DateTimeFormat");
   	    		  }
   	    		  endTime = LocalDateTime.MAX;
	    		  }
			 }
			 catch (Exception e) {
				 e.printStackTrace();
		 	}
		}
		
		
	//goes through each line of the file and checks it with each active filter
	public  void scanFile(File file) {
		String line = "";
		System.out.println(file.getName());
        try (LineNumberReader  br = new LineNumberReader (new InputStreamReader(new DataInputStream(new FileInputStream(file))));) { 
            while ((line = br.readLine()) != null) {  	
            	if (IrrigationSum.activeFilter) {
            		if (file.getName().contains("IOBusActions"))
            		{
            		IrrigationSum.filterIO(line);
            		}
            		if (file.getName().contains("Trace"))
            		{
            		IrrigationSum.filterTrace(line);
            		}
            	}
            	if (Catalina.activeFilter) {
            		if (file.getName().contains("catalina"))
            		{
            		Catalina.filterCat(line);
            		}
            	}
            	for (int i=0; i< customFiltersTable.getRowCount(); i++) {
            		String filter = customFiltersTable.getValueAt(i, 1).toString();
            		String filterFile = customFiltersTable.getValueAt(i, 2).toString();
            		if (filter !="" && filterFile !="") {
            			if (file.getName().contains(filterFile))
            			Custom.customFilters(line, filter, br.getLineNumber());
            		}

            	}
            }
            
        } catch (Exception e) {
            System.err.println("Error matching line: " + line);
            System.err.println("Error: " + e.getMessage());
        }
	}
	
	
	//main UI creation and button behavior
	public void generateGUI() {
	       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       this.setSize(1220,680);
	       
	       analyzeResult.setLineWrap(true);
	       analyzeResult.setWrapStyleWord(true);
	       filterDescription.setLineWrap(true);
	       filterDescription.setWrapStyleWord(true);
	       
	       startTimeText.setPreferredSize(new Dimension(150, 30));
	       endTimeText.setPreferredSize(new Dimension(150, 30));
	       chooseFolderPanel.setPreferredSize(new Dimension(1220, 100));
	       analyzePanel.setPreferredSize(new Dimension(500, 600));
	       analyzeResult.setPreferredSize(new Dimension(500, 500));
	       filterDescription.setPreferredSize(new Dimension(300, 600));
	       chooseFilters.setPreferredSize(new Dimension(300, 600));
	       folderText.setPreferredSize(new Dimension(300, 30)); 
	       analyzeButton.setPreferredSize(new Dimension(150, 30));   
	       chooseFolderButton.setPreferredSize(new Dimension(150, 30));
	       outputFolderText.setPreferredSize(new Dimension(300, 30)); 
	       copyButton.setPreferredSize(new Dimension(150, 30));
	       chooseOutputButton.setPreferredSize(new Dimension(150, 30));
	       CustomerFiltersPanel.setPreferredSize(new Dimension(400, 660));
	       customFiltersTable.setPreferredSize(new Dimension(300, 600));
	       add.setPreferredSize(new Dimension(150, 30));
	       remove.setPreferredSize(new Dimension(150, 30));
	       customFiltersTable.getColumnModel().getColumn(0).setPreferredWidth(20);
	       clearAnalysis.setPreferredSize(new Dimension(150, 30));
	       
	       
	       //remove selected row from the table
	       remove.addActionListener(new ActionListener() {
	           @Override
	           public void actionPerformed(ActionEvent ae) {
	              // check for selected row first
	              if(customFiltersTable.getSelectedRow() != -1) {
	                 // remove selected row from the table
	                 model.remove(customFiltersTable.getSelectedRow());
	              }
	           }
	        });
	       
	       //add row to custom filter button
           add.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   model.add(new Record(++id));
               }
           });
	       
	       JScrollPane scrollPane = new JScrollPane(analyzeResult, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	       scrollPane.setPreferredSize(new Dimension(500, 400));	  
	       	
	       
	       //create filters list
	   		JList<CheckListItem> filtersList = new JList<CheckListItem>(new CheckListItem[] { 
	   				new CheckListItem("Irrigation Sum"),
	   		        new CheckListItem("IrrigationStart/Delay"),
	   		        new CheckListItem("Tile Disconnection"),
	   		        new CheckListItem("Catalina"),
	   		        new CheckListItem("Device start/stop"),
	   		        });
	   		filtersList.setCellRenderer(new CheckListRenderer());
	   		filtersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	   		filtersList.addMouseListener(new MouseAdapter() {

	   			//display filter description on click
	   			@Override
	   	      public void mouseClicked(MouseEvent event) {
	   	        JList<CheckListItem> list = (JList<CheckListItem>) event.getSource();
	   	        int index = list.locationToIndex(event.getPoint());// Get index of item clicked
	   	        CheckListItem item = (CheckListItem) list.getModel()
	   	            .getElementAt(index);
	   	        item.setSelected(!item.isSelected()); // Toggle selected state
	   	        switch (item.label)
	   	        {
	   	        case "Irrigation Sum" : {
	   	        		IrrigationSum.activeFilter = item.isSelected();
	   	        		IrrigationSum.displayDescription(filterDescription);
	   	        		}
	   	        case "Catalina" : {
	   	        	Catalina.activeFilter = item.isSelected();
	   	        	Catalina.displayDescription(filterDescription);
	        		}
	   	        };
	   	        list.repaint(list.getCellBounds(index, index));// Repaint cell
	   	      }
	   	    });
	   		
	   		filtersList.setPreferredSize(new Dimension(250, 600));
	       
	   		//choose logs folder button
	       chooseFolderButton.addActionListener(new ActionListener() {
	    	      public void actionPerformed(ActionEvent chooseFolder) {
	    	        JFileChooser fileChooser = new JFileChooser();
	    	        int returnValue = fileChooser.showOpenDialog(null);
	    	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	    	        	selectedFolder = fileChooser.getCurrentDirectory();
	    	        	folderText.setText(selectedFolder.toString());
	    	         // System.out.println(selectedFolder.getName());
	    	        }
	    	      }
	    	    });
	       
	       //copy results button
	       copyButton.addActionListener(new ActionListener() {
	    	      public void actionPerformed(ActionEvent copyAll) {
	    	    	  StringSelection selection = new StringSelection(analyzeResult.getText());
	    	    	  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    	    	  clipboard.setContents(selection, null);
	    	        }
	    	      
	    	    });
	       
	       //choose ouput folder button
	       chooseOutputButton.addActionListener(new ActionListener() {
	    	      public void actionPerformed(ActionEvent chooseFolder) {
	    	        JFileChooser fileChooser = new JFileChooser();
	    	        int returnValue = fileChooser.showOpenDialog(null);
	    	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	    	        	outputFolder = fileChooser.getCurrentDirectory();
	    	        	outputFolderText.setText(outputFolder.toString());
	    	         // System.out.println(selectedFolder.getName());
	    	        }
	    	      }
	    	    });
	      
	       //actions to perform on Analyze button click
	       analyzeButton.addActionListener(new ActionListener() {
	    	      public void actionPerformed(ActionEvent analyzeLogs) {	    	    	 
	    	    	  try {
	    	    		//locate all relevant files in sub\folders
							findFiles(); 
					
							
							//scan each file found
							for (int i = traceLogFiles.length-1; i>=0; i--) {
								if (traceLogFiles[i] != null) {
								scanFile(traceLogFiles[i]);
								}
							}
							for (int i = ioLogFiles.length-1; i>=0; i--) {
								if (ioLogFiles[i] != null) {
									scanFile(ioLogFiles[i]);
								}
							}
							for (File log : catFiles) {
								scanFile(log);
								
							}
							if (IrrigationSum.activeFilter){
								Analysis.analyzeIrrSum(analyzeResult, IrrigationSum.devices);									
								
								}
							if (Catalina.activeFilter){
								Analysis.analyzeCat(analyzeResult, Catalina.events);	
								}
	
						} 
	    	    	  	catch (Exception e) {
							e.printStackTrace();
						}
		    	    	  finally {
		    	    		  
		    	    		  catFiles.clear();
		    	    		  Analysis.irrSumAnalysis.clear();
								System.out.println(Analysis.irrSumAnalysis.size());
		    	    	  }
	    	      }
	    	    });
	       
	       //clear results button
	       clearAnalysis.addActionListener(new ActionListener() {
	    	      public void actionPerformed(ActionEvent clearLogs) {	      	
	    	
	    	    		  analyzeResult.setText("Results: ");
					
	       }
	       });
	       
	       //add 1 empty item to custom filters list
           model.add(new Record(++id));

	       analyzeResult.setEditable(false);
	       chooseFilters.add( new JLabel("Selected Filters:"), BorderLayout.NORTH);
	   		chooseFilters.add(new JScrollPane(filtersList), BorderLayout.CENTER);
	   		chooseFilters.add(filterDescription, BorderLayout.SOUTH);
	       
	   		chooseFolderPanel.add(chooseFolderButton, BorderLayout.WEST); 
	       chooseFolderPanel.add(folderText, BorderLayout.WEST);
	       chooseFolderPanel.add(startTimeLabel, BorderLayout.EAST);
	       chooseFolderPanel.add(startTimeText, BorderLayout.EAST);
	       chooseFolderPanel.add(endTimeLabel, BorderLayout.EAST);
	       chooseFolderPanel.add(endTimeText, BorderLayout.EAST);
	       chooseFolderPanel.add(chooseOutputButton, BorderLayout.SOUTH); 
	       chooseFolderPanel.add(outputFolderText, BorderLayout.SOUTH);
	       chooseFolderPanel.add(analyzeButton, BorderLayout.SOUTH);

	       CustomerFiltersPanel.add(new JScrollPane(customFiltersTable), BorderLayout.CENTER);
	       CustomerFiltersPanel.add(add, BorderLayout.SOUTH);
	       CustomerFiltersPanel.add(remove, BorderLayout.SOUTH);
	       
	       analyzePanel.add(ResultsLabel, BorderLayout.NORTH);
	       analyzePanel.add(scrollPane, BorderLayout.CENTER);
	       analyzePanel.add(clearAnalysis, BorderLayout.SOUTH);
	       analyzePanel.add(copyButton, BorderLayout.SOUTH);

	       this.add(chooseFilters, BorderLayout.EAST);
	       this.add(chooseFolderPanel, BorderLayout.NORTH);
	       this.add(analyzePanel, BorderLayout.CENTER);
	       this.add(CustomerFiltersPanel, BorderLayout.WEST);
	       this.setResizable(false);
	       this.setVisible(true);
	}
}
