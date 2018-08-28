package org.springnext.builder.windows;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springnext.builder.database.DatabaseReader;
import org.springnext.builder.entity.ProjectInfo;
import org.springnext.builder.entity.Table;
import org.springnext.builder.generator.BuilderService;

@Component
public class MainFrame extends JFrame {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;

	@Autowired
	public BuilderService builderService;

	// 老项目包名输入框
	public JTextField oldPackageText;
	// 老项目GroupID输入框
	public JTextField oldGroupIDText;
	// 老项目ArtifactID输入框
	public JTextField oldArtifactIDText;
	// 老项目version输入框
	public JTextField oldVersionText;
	// 老项目名输入框
	public JTextField oldNameText;
	// 老项目URL输入框
	public JTextField oldURLText;
	// 老项目Description输入框
	public JTextField oldDescriptionText;
	// 新项目包名输入框
	public JTextField newPackageText;
	// 新项目GroupID输入框
	public JTextField newGroupIDText;
	// 新项目ArtifactID输入框
	public JTextField newArtifactIDText;
	// 新项目version输入框
	public JTextField newVersionText;
	// 新项目名输入框
	public JTextField newNameText;
	// 新项目URL输入框
	public JTextField newURLText;
	// 新项目Description输入框
	public JTextField newDescriptionText;
	// 项目路径
	public JTextField projectSrcText;
	public JTextField dbURL;
	public JTextField dbUserName;
	public JTextField dbPassword;
	public JTable table;
	
	public List<Table> tableList;
	public ProjectInfo projectInfo;

	/*
	 * 设置JTable的列名
	 */
	public String[] columnNames = { "表名","备注" };
	public JTextField dbName;
	public JTextField menuModelName;
	public JTextField modelName;
	public JTextField tablePrefix;
	private JTextField author;

	/**
	 * Create the panel.
	 */
	public MainFrame() {

		initMainFrame();

		initNorthPane();

		initCenterPane();

	}

	/**
	 * 主窗体参数设置
	 */
	public void initMainFrame() {
		// 设置窗体图标与标题
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(MainFrame.class.getResource("/org/springnext/builder/image/favicon.png")));
		setTitle("SpringNext Builder");
		// 设置UI界面整体风格为当前操作系统的风格
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		// 设置关闲按钮事件
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 设置窗体大小并锁定大小
		setSize(800, 450);
		setResizable(false);
		// 设置窗体初始化位置
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int x = (int) (toolkit.getScreenSize().getWidth() - getWidth()) / 2;
		int y = (int) (toolkit.getScreenSize().getHeight() - getHeight()) / 2;
		setLocation(x, y);
		// 设置窗体内的布局样式
		getContentPane().setLayout(new BorderLayout(0, 0));
	}

	/**
	 * 主窗体上半部份布局设置
	 */
	public void initNorthPane() {
		JPanel panelTop = new JPanel();
		getContentPane().add(panelTop, BorderLayout.NORTH);

		GridBagLayout gbl_panelTop = new GridBagLayout();
		gbl_panelTop.columnWidths = new int[] { 100, 300, 50, 50, 0 };
		gbl_panelTop.rowHeights = new int[] { 30 };
		gbl_panelTop.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		gbl_panelTop.rowWeights = new double[] { 0.0 };
		panelTop.setLayout(gbl_panelTop);

		JLabel projectSrcLbl = new JLabel("项目pom文件");
		projectSrcLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_projectSrcLbl = new GridBagConstraints();
		gbc_projectSrcLbl.fill = GridBagConstraints.BOTH;
		gbc_projectSrcLbl.insets = new Insets(0, 0, 0, 5);
		gbc_projectSrcLbl.gridx = 0;
		gbc_projectSrcLbl.gridy = 0;
		panelTop.add(projectSrcLbl, gbc_projectSrcLbl);

		projectSrcText = new JTextField();
		projectSrcText.setColumns(10);
		GridBagConstraints gbc_projectSrcText = new GridBagConstraints();
		gbc_projectSrcText.insets = new Insets(0, 0, 0, 5);
		gbc_projectSrcText.anchor = GridBagConstraints.NORTH;
		gbc_projectSrcText.fill = GridBagConstraints.BOTH;
		gbc_projectSrcText.gridx = 1;
		gbc_projectSrcText.gridy = 0;
		panelTop.add(projectSrcText, gbc_projectSrcText);

		JButton btnNewButton = new JButton("浏览...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser(); // 设置选择器
				chooser.setMultiSelectionEnabled(false); // 设为单选
				int returnVal = chooser.showOpenDialog(btnNewButton); // 打开文件选择框并接收选择框返回值
				// 判断用户是选择取消还是确定
				if (returnVal == JFileChooser.APPROVE_OPTION) { // 如果符合文件类型

					String filePath = chooser.getSelectedFile().getAbsolutePath(); // 获取绝对路径
					projectSrcText.setText(filePath);

					readFile(filePath);
				}
			}

		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 0;
		panelTop.add(btnNewButton, gbc_btnNewButton);

		JButton btnNewButton_1 = new JButton("加载");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!StringUtils.isEmpty(projectSrcText.getText())) {
					readFile(projectSrcText.getText());
				}
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.gridx = 3;
		gbc_btnNewButton_1.gridy = 0;
		panelTop.add(btnNewButton_1, gbc_btnNewButton_1);
	}

	public void readFile(String filePath) {
		projectInfo = builderService.loadProjectInfo(filePath);
		projectInfo.setProjectSrc(projectSrcText.getText());
		
		oldPackageText.setText(projectInfo.getPackageName());
		oldGroupIDText.setText(projectInfo.getGroupId());
		oldArtifactIDText.setText(projectInfo.getArtifactId());
		oldVersionText.setText(projectInfo.getVersion());
		oldNameText.setText(projectInfo.getName());
		oldURLText.setText(projectInfo.getUrl());
		oldDescriptionText.setText(projectInfo.getDescription());

		newPackageText.setText(projectInfo.getPackageName());
		newGroupIDText.setText(projectInfo.getGroupId());
		newArtifactIDText.setText(projectInfo.getArtifactId());
		newVersionText.setText(projectInfo.getVersion());
		newNameText.setText(projectInfo.getName());
		newURLText.setText(projectInfo.getUrl());
		newDescriptionText.setText(projectInfo.getDescription());
	}

	/**
	 * 主窗体中件部份设置
	 */
	public void initCenterPane() {
		JTabbedPane panelCenterTab = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(panelCenterTab, BorderLayout.CENTER);

		initTabPane1(panelCenterTab);

		initTabPane2(panelCenterTab);

		initTabPane3(panelCenterTab);
	}

	public void initTabPane3(JTabbedPane tabbedPane) {
		JPanel tabPanel_3 = new JPanel();
		tabbedPane.addTab("生成Microservice项目代码", null, tabPanel_3, null);
	}

	public void initTabPane2(JTabbedPane tabbedPane) {
		JPanel tabPanel_2 = new JPanel();
		tabbedPane.addTab("生成Manager项目代码", null, tabPanel_2, null);
		GridBagLayout gbl_tabPanel_2 = new GridBagLayout();
		gbl_tabPanel_2.columnWidths = new int[] { 400, 400 };
		gbl_tabPanel_2.rowHeights = new int[] { 400 };
		gbl_tabPanel_2.columnWeights = new double[] { 1.0, 1.0 };
		gbl_tabPanel_2.rowWeights = new double[] { 1.0 };
		tabPanel_2.setLayout(gbl_tabPanel_2);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "模板信息", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		tabPanel_2.add(panel, gbc_panel_1);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {150, 250};
		gbl_panel.rowHeights = new int[] {30, 30, 30, 30, 30, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel label_1 = new JLabel("菜单模块");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 0;
		panel.add(label_1, gbc_label_1);
		
		menuModelName = new JTextField();
		GridBagConstraints gbc_menuModelName = new GridBagConstraints();
		gbc_menuModelName.insets = new Insets(0, 0, 5, 0);
		gbc_menuModelName.fill = GridBagConstraints.HORIZONTAL;
		gbc_menuModelName.gridx = 1;
		gbc_menuModelName.gridy = 0;
		panel.add(menuModelName, gbc_menuModelName);
		menuModelName.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("ModelName");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 1;
		panel.add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		modelName = new JTextField();
		GridBagConstraints gbc_modelName = new GridBagConstraints();
		gbc_modelName.insets = new Insets(0, 0, 5, 0);
		gbc_modelName.fill = GridBagConstraints.HORIZONTAL;
		gbc_modelName.gridx = 1;
		gbc_modelName.gridy = 1;
		panel.add(modelName, gbc_modelName);
		modelName.setColumns(10);
		
		JLabel lblTable = new JLabel("TablePrefix");
		lblTable.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblTable = new GridBagConstraints();
		gbc_lblTable.anchor = GridBagConstraints.EAST;
		gbc_lblTable.insets = new Insets(0, 0, 5, 5);
		gbc_lblTable.gridx = 0;
		gbc_lblTable.gridy = 2;
		panel.add(lblTable, gbc_lblTable);
		
		tablePrefix = new JTextField();
		GridBagConstraints gbc_tablePrefix = new GridBagConstraints();
		gbc_tablePrefix.insets = new Insets(0, 0, 5, 0);
		gbc_tablePrefix.fill = GridBagConstraints.HORIZONTAL;
		gbc_tablePrefix.gridx = 1;
		gbc_tablePrefix.gridy = 2;
		panel.add(tablePrefix, gbc_tablePrefix);
		tablePrefix.setColumns(10);
		
		JButton btnNewButton_4 = new JButton("生成");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectIndexs = table.getSelectedRows();
				List<Table> tables = new ArrayList<Table>();
				
				for (int i = 0; i < selectIndexs.length; i++) {
					tables.add(tableList.get(selectIndexs[i]));
				}
				projectInfo.setMenuModelName(menuModelName.getText());
				projectInfo.setTablePrefix(tablePrefix.getText());
				projectInfo.setModelName(modelName.getText());
				projectInfo.setAuthor(author.getText());
				
				builderService.generatorManager(projectInfo, tables);
			}
		});
		
		JLabel label_2 = new JLabel("作者");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.fill = GridBagConstraints.VERTICAL;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 3;
		panel.add(label_2, gbc_label_2);
		
		author = new JTextField();
		GridBagConstraints gbc_author = new GridBagConstraints();
		gbc_author.insets = new Insets(0, 0, 5, 0);
		gbc_author.fill = GridBagConstraints.HORIZONTAL;
		gbc_author.gridx = 1;
		gbc_author.gridy = 3;
		panel.add(author, gbc_author);
		author.setColumns(10);
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.gridx = 1;
		gbc_btnNewButton_4.gridy = 4;
		panel.add(btnNewButton_4, gbc_btnNewButton_4);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "数据库信息", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		tabPanel_2.add(panel_1, gbc_panel);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 400 };
		gbl_panel_1.rowHeights = new int[] { 200, 183 };
		gbl_panel_1.columnWeights = new double[] { 1.0 };
		gbl_panel_1.rowWeights = new double[] { 0.0, 1.0 };
		panel_1.setLayout(gbl_panel_1);

		JScrollPane panel_2 = new JScrollPane();
		panel_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 1;
		panel_1.add(panel_2, gbc_panel_2);

		/*
		 * 初始化JTable里面各项的值，设置两个一模一样的实体"赵匡义"学生。
		 */
		Object[][] obj = new Object[0][0];
		table = new JTable(obj, columnNames);
		table.setModel(new DefaultTableModel(
				obj,
				columnNames
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(240);
		table.getColumnModel().getColumn(0).setMinWidth(240);
		table.getColumnModel().getColumn(0).setMaxWidth(240);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		/*
		 * 设置JTable自动调整列表的状态，此处设置为关闭
		 */
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		panel_2.setSize(125, 85);
		panel_2.setViewportView(table);

		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 0;
		panel_1.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[] { 150, 250 };
		gbl_panel_3.rowHeights = new int[] {30, 30, 30, 30, 30};
		gbl_panel_3.columnWeights = new double[] { 0.0, 1.0 };
		gbl_panel_3.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel_3.setLayout(gbl_panel_3);
		
				JLabel lblNewLabel_2 = new JLabel("连接URL");
				GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
				gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_2.gridx = 0;
				gbc_lblNewLabel_2.gridy = 0;
				panel_3.add(lblNewLabel_2, gbc_lblNewLabel_2);

		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 5, 0);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 1;
		gbc_panel_4.gridy = 0;
		panel_3.add(panel_4, gbc_panel_4);
		panel_4.setLayout(new GridLayout(1, 2, 0, 0));
		
				dbURL = new JTextField();
				panel_4.add(dbURL);
				dbURL.setColumns(10);
		
				JLabel lblNewLabel_3 = new JLabel("数据库用户名");
				GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
				gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_3.gridx = 0;
				gbc_lblNewLabel_3.gridy = 1;
				panel_3.add(lblNewLabel_3, gbc_lblNewLabel_3);
				
						dbUserName = new JTextField();
						GridBagConstraints gbc_dbUserName = new GridBagConstraints();
						gbc_dbUserName.insets = new Insets(0, 0, 5, 0);
						gbc_dbUserName.fill = GridBagConstraints.HORIZONTAL;
						gbc_dbUserName.gridx = 1;
						gbc_dbUserName.gridy = 1;
						panel_3.add(dbUserName, gbc_dbUserName);
						dbUserName.setColumns(10);
		
				JLabel lblNewLabel_4 = new JLabel("连接密码");
				GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
				gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_4.gridx = 0;
				gbc_lblNewLabel_4.gridy = 2;
				panel_3.add(lblNewLabel_4, gbc_lblNewLabel_4);
		
				dbPassword = new JTextField();
				GridBagConstraints gbc_dbPassword = new GridBagConstraints();
				gbc_dbPassword.insets = new Insets(0, 0, 5, 0);
				gbc_dbPassword.fill = GridBagConstraints.HORIZONTAL;
				gbc_dbPassword.gridx = 1;
				gbc_dbPassword.gridy = 2;
				panel_3.add(dbPassword, gbc_dbPassword);
				dbPassword.setColumns(10);
		
		JLabel label = new JLabel("数据库名");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 3;
		panel_3.add(label, gbc_label);
		
		dbName = new JTextField();
		GridBagConstraints gbc_dbName = new GridBagConstraints();
		gbc_dbName.insets = new Insets(0, 0, 5, 0);
		gbc_dbName.fill = GridBagConstraints.HORIZONTAL;
		gbc_dbName.gridx = 1;
		gbc_dbName.gridy = 3;
		panel_3.add(dbName, gbc_dbName);
		dbName.setColumns(10);
		
				JButton btnNewButton_3 = new JButton("加载数据库");
				btnNewButton_3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						try {
							String jarFilePath = "";
							String driverClass = "";
							String url = dbURL.getText();
							String username = dbUserName.getText();
							String password = dbPassword.getText();
							String database = "";
							tableList = new DatabaseReader(jarFilePath,driverClass,url,username, password, database).readTables();

							
							
							String[][] rows = new String[tableList.size()][2];
							for (int i = 0; i < tableList.size(); i++) {
								rows[i][0] = tableList.get(i).getName();
								rows[i][1] = tableList.get(i).getRemark();
							}
							table.setModel(new DefaultTableModel(rows, columnNames));

						} catch (Exception e1) {
							e1.printStackTrace();
						}

					}
				});
				btnNewButton_3.setHorizontalAlignment(SwingConstants.LEFT);
				GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
				gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 0);
				gbc_btnNewButton_3.gridx = 1;
				gbc_btnNewButton_3.gridy = 4;
				panel_3.add(btnNewButton_3, gbc_btnNewButton_3);
	}

	public void initTabPane1(JTabbedPane tabbedPane) {
		JPanel tabPanel_1 = new JPanel();
		tabbedPane.addTab("项目重命名", null, tabPanel_1, null);
		tabbedPane.setEnabledAt(0, true);

		GridBagLayout gbl_tabPanel_1 = new GridBagLayout();
		gbl_tabPanel_1.columnWidths = new int[] { 500 };
		gbl_tabPanel_1.rowHeights = new int[] { 350, 50 };
		gbl_tabPanel_1.columnWeights = new double[] { 1.0 };
		gbl_tabPanel_1.rowWeights = new double[] { 1.0, 0.0 };
		tabPanel_1.setLayout(gbl_tabPanel_1);

		JPanel tab_1_panel_center = new JPanel();
		GridBagLayout gbl_tab_1_panel_center = new GridBagLayout();
		gbl_tab_1_panel_center.columnWidths = new int[] { 400, 400 };
		gbl_tab_1_panel_center.rowHeights = new int[] { 350 };
		gbl_tab_1_panel_center.columnWeights = new double[] { 1.0, 1.0 };
		gbl_tab_1_panel_center.rowWeights = new double[] { 1.0 };
		tab_1_panel_center.setLayout(gbl_tab_1_panel_center);
		GridBagConstraints gbc_tab_1_panel_center = new GridBagConstraints();
		gbc_tab_1_panel_center.gridwidth = 2;
		gbc_tab_1_panel_center.insets = new Insets(0, 0, 5, 5);
		gbc_tab_1_panel_center.fill = GridBagConstraints.BOTH;
		gbc_tab_1_panel_center.gridx = 0;
		gbc_tab_1_panel_center.gridy = 0;
		tabPanel_1.add(tab_1_panel_center, gbc_tab_1_panel_center);

		JPanel tab_1_panel_bottom = new JPanel();
		GridBagConstraints gbc_tab_1_panel_bottom = new GridBagConstraints();
		gbc_tab_1_panel_bottom.gridwidth = 2;
		gbc_tab_1_panel_bottom.insets = new Insets(0, 0, 5, 5);
		gbc_tab_1_panel_bottom.fill = GridBagConstraints.BOTH;
		gbc_tab_1_panel_bottom.gridx = 0;
		gbc_tab_1_panel_bottom.gridy = 1;
		tabPanel_1.add(tab_1_panel_bottom, gbc_tab_1_panel_bottom);

		JPanel tab_1_panel_center_left = new JPanel();
		tab_1_panel_center_left
				.setBorder(new TitledBorder(null, "原工程信息", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_tab_1_panel_center_left = new GridBagConstraints();
		gbc_tab_1_panel_center_left.insets = new Insets(0, 0, 5, 5);
		gbc_tab_1_panel_center_left.fill = GridBagConstraints.BOTH;
		gbc_tab_1_panel_center_left.gridx = 0;
		gbc_tab_1_panel_center_left.gridy = 0;
		tab_1_panel_center.add(tab_1_panel_center_left, gbc_tab_1_panel_center_left);

		JPanel tab_1_panel_center_right = new JPanel();
		tab_1_panel_center_right
				.setBorder(new TitledBorder(null, "新工程信息", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_tab_1_panel_center_right = new GridBagConstraints();
		gbc_tab_1_panel_center_right.insets = new Insets(0, 0, 5, 5);
		gbc_tab_1_panel_center_right.fill = GridBagConstraints.BOTH;
		gbc_tab_1_panel_center_right.gridx = 1;
		gbc_tab_1_panel_center_right.gridy = 0;
		tab_1_panel_center.add(tab_1_panel_center_right, gbc_tab_1_panel_center_right);
		GridBagLayout gbl_tab_1_panel_center_left = new GridBagLayout();
		gbl_tab_1_panel_center_left.columnWidths = new int[] { 90, 266, 0 };
		gbl_tab_1_panel_center_left.rowHeights = new int[] { 30, 30, 30, 30, 30, 30, 30, 0 };
		gbl_tab_1_panel_center_left.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_tab_1_panel_center_left.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		tab_1_panel_center_left.setLayout(gbl_tab_1_panel_center_left);

		JLabel oldPackageLbl = new JLabel("packageName");
		oldPackageLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_oldPackageLbl = new GridBagConstraints();
		gbc_oldPackageLbl.fill = GridBagConstraints.BOTH;
		gbc_oldPackageLbl.insets = new Insets(0, 0, 5, 5);
		gbc_oldPackageLbl.gridx = 0;
		gbc_oldPackageLbl.gridy = 0;
		tab_1_panel_center_left.add(oldPackageLbl, gbc_oldPackageLbl);
		oldPackageText = new JTextField();
		oldPackageText.setEditable(false);
		oldPackageText.setColumns(10);
		GridBagConstraints gbc_oldPackageText = new GridBagConstraints();
		gbc_oldPackageText.fill = GridBagConstraints.BOTH;
		gbc_oldPackageText.insets = new Insets(0, 0, 5, 0);
		gbc_oldPackageText.gridx = 1;
		gbc_oldPackageText.gridy = 0;
		tab_1_panel_center_left.add(oldPackageText, gbc_oldPackageText);

		JLabel oldGroupIDLbl = new JLabel("GroupID");
		oldGroupIDLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_oldGroupIDLbl = new GridBagConstraints();
		gbc_oldGroupIDLbl.fill = GridBagConstraints.BOTH;
		gbc_oldGroupIDLbl.insets = new Insets(0, 0, 5, 5);
		gbc_oldGroupIDLbl.gridx = 0;
		gbc_oldGroupIDLbl.gridy = 1;
		tab_1_panel_center_left.add(oldGroupIDLbl, gbc_oldGroupIDLbl);
		oldGroupIDText = new JTextField();
		oldGroupIDText.setEditable(false);
		oldGroupIDText.setColumns(10);
		GridBagConstraints gbc_oldGroupIDText = new GridBagConstraints();
		gbc_oldGroupIDText.fill = GridBagConstraints.BOTH;
		gbc_oldGroupIDText.insets = new Insets(0, 0, 5, 0);
		gbc_oldGroupIDText.gridx = 1;
		gbc_oldGroupIDText.gridy = 1;
		tab_1_panel_center_left.add(oldGroupIDText, gbc_oldGroupIDText);

		JLabel oldArtifactIDLbl = new JLabel("ArtifactID");
		oldArtifactIDLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_oldArtifactIDLbl = new GridBagConstraints();
		gbc_oldArtifactIDLbl.fill = GridBagConstraints.BOTH;
		gbc_oldArtifactIDLbl.insets = new Insets(0, 0, 5, 5);
		gbc_oldArtifactIDLbl.gridx = 0;
		gbc_oldArtifactIDLbl.gridy = 2;
		tab_1_panel_center_left.add(oldArtifactIDLbl, gbc_oldArtifactIDLbl);
		oldArtifactIDText = new JTextField();
		oldArtifactIDText.setEditable(false);
		oldArtifactIDText.setColumns(10);
		GridBagConstraints gbc_oldArtifactIDText = new GridBagConstraints();
		gbc_oldArtifactIDText.fill = GridBagConstraints.BOTH;
		gbc_oldArtifactIDText.insets = new Insets(0, 0, 5, 0);
		gbc_oldArtifactIDText.gridx = 1;
		gbc_oldArtifactIDText.gridy = 2;
		tab_1_panel_center_left.add(oldArtifactIDText, gbc_oldArtifactIDText);

		JLabel oldVersionLbl = new JLabel("Version");
		oldVersionLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_oldVersionLbl = new GridBagConstraints();
		gbc_oldVersionLbl.fill = GridBagConstraints.BOTH;
		gbc_oldVersionLbl.insets = new Insets(0, 0, 5, 5);
		gbc_oldVersionLbl.gridx = 0;
		gbc_oldVersionLbl.gridy = 3;
		tab_1_panel_center_left.add(oldVersionLbl, gbc_oldVersionLbl);
		oldVersionText = new JTextField();
		oldVersionText.setEditable(false);
		oldVersionText.setColumns(10);
		GridBagConstraints gbc_oldVersionText = new GridBagConstraints();
		gbc_oldVersionText.fill = GridBagConstraints.BOTH;
		gbc_oldVersionText.insets = new Insets(0, 0, 5, 0);
		gbc_oldVersionText.gridx = 1;
		gbc_oldVersionText.gridy = 3;
		tab_1_panel_center_left.add(oldVersionText, gbc_oldVersionText);

		JLabel oldNameLbl = new JLabel("Name");
		oldNameLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_oldNameLbl = new GridBagConstraints();
		gbc_oldNameLbl.fill = GridBagConstraints.BOTH;
		gbc_oldNameLbl.insets = new Insets(0, 0, 5, 5);
		gbc_oldNameLbl.gridx = 0;
		gbc_oldNameLbl.gridy = 4;
		tab_1_panel_center_left.add(oldNameLbl, gbc_oldNameLbl);
		oldNameText = new JTextField();
		oldNameText.setEditable(false);
		oldNameText.setColumns(10);
		GridBagConstraints gbc_oldNameText = new GridBagConstraints();
		gbc_oldNameText.fill = GridBagConstraints.BOTH;
		gbc_oldNameText.insets = new Insets(0, 0, 5, 0);
		gbc_oldNameText.gridx = 1;
		gbc_oldNameText.gridy = 4;
		tab_1_panel_center_left.add(oldNameText, gbc_oldNameText);

		JLabel oldURLLbl = new JLabel("URL");
		oldURLLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_oldURLLbl = new GridBagConstraints();
		gbc_oldURLLbl.fill = GridBagConstraints.BOTH;
		gbc_oldURLLbl.insets = new Insets(0, 0, 5, 5);
		gbc_oldURLLbl.gridx = 0;
		gbc_oldURLLbl.gridy = 5;
		tab_1_panel_center_left.add(oldURLLbl, gbc_oldURLLbl);
		oldURLText = new JTextField();
		oldURLText.setEditable(false);
		oldURLText.setColumns(10);
		GridBagConstraints gbc_oldURLText = new GridBagConstraints();
		gbc_oldURLText.fill = GridBagConstraints.BOTH;
		gbc_oldURLText.insets = new Insets(0, 0, 5, 0);
		gbc_oldURLText.gridx = 1;
		gbc_oldURLText.gridy = 5;
		tab_1_panel_center_left.add(oldURLText, gbc_oldURLText);

		JLabel oldDescriptionLbl = new JLabel("Description");
		oldDescriptionLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_oldDescriptionLbl = new GridBagConstraints();
		gbc_oldDescriptionLbl.fill = GridBagConstraints.BOTH;
		gbc_oldDescriptionLbl.insets = new Insets(0, 0, 0, 5);
		gbc_oldDescriptionLbl.gridx = 0;
		gbc_oldDescriptionLbl.gridy = 6;
		tab_1_panel_center_left.add(oldDescriptionLbl, gbc_oldDescriptionLbl);
		oldDescriptionText = new JTextField();
		oldDescriptionText.setEditable(false);
		oldDescriptionText.setColumns(10);
		GridBagConstraints gbc_oldDescriptionText = new GridBagConstraints();
		gbc_oldDescriptionText.fill = GridBagConstraints.BOTH;
		gbc_oldDescriptionText.gridx = 1;
		gbc_oldDescriptionText.gridy = 6;
		tab_1_panel_center_left.add(oldDescriptionText, gbc_oldDescriptionText);
		GridBagLayout gbl_tab_1_panel_center_right = new GridBagLayout();
		gbl_tab_1_panel_center_right.columnWidths = new int[] { 90, 266, 0 };
		gbl_tab_1_panel_center_right.rowHeights = new int[] { 30, 30, 30, 30, 30, 30, 30, 0 };
		gbl_tab_1_panel_center_right.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_tab_1_panel_center_right.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		tab_1_panel_center_right.setLayout(gbl_tab_1_panel_center_right);

		JLabel newPackageLbl = new JLabel("packageName");
		newPackageLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_newPackageLbl = new GridBagConstraints();
		gbc_newPackageLbl.fill = GridBagConstraints.BOTH;
		gbc_newPackageLbl.insets = new Insets(0, 0, 5, 5);
		gbc_newPackageLbl.gridx = 0;
		gbc_newPackageLbl.gridy = 0;
		tab_1_panel_center_right.add(newPackageLbl, gbc_newPackageLbl);
		newPackageText = new JTextField();
		newPackageText.setColumns(10);
		GridBagConstraints gbc_newPackageText = new GridBagConstraints();
		gbc_newPackageText.fill = GridBagConstraints.BOTH;
		gbc_newPackageText.insets = new Insets(0, 0, 5, 0);
		gbc_newPackageText.gridx = 1;
		gbc_newPackageText.gridy = 0;
		tab_1_panel_center_right.add(newPackageText, gbc_newPackageText);

		JLabel newGroupIDLbl = new JLabel("GroupID");
		newGroupIDLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_newGroupIDLbl = new GridBagConstraints();
		gbc_newGroupIDLbl.fill = GridBagConstraints.BOTH;
		gbc_newGroupIDLbl.insets = new Insets(0, 0, 5, 5);
		gbc_newGroupIDLbl.gridx = 0;
		gbc_newGroupIDLbl.gridy = 1;
		tab_1_panel_center_right.add(newGroupIDLbl, gbc_newGroupIDLbl);
		newGroupIDText = new JTextField();
		newGroupIDText.setColumns(10);
		GridBagConstraints gbc_newGroupIDText = new GridBagConstraints();
		gbc_newGroupIDText.fill = GridBagConstraints.BOTH;
		gbc_newGroupIDText.insets = new Insets(0, 0, 5, 0);
		gbc_newGroupIDText.gridx = 1;
		gbc_newGroupIDText.gridy = 1;
		tab_1_panel_center_right.add(newGroupIDText, gbc_newGroupIDText);

		JLabel newArtifactIDLbl = new JLabel("ArtifactID");
		newArtifactIDLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_newArtifactIDLbl = new GridBagConstraints();
		gbc_newArtifactIDLbl.fill = GridBagConstraints.BOTH;
		gbc_newArtifactIDLbl.insets = new Insets(0, 0, 5, 5);
		gbc_newArtifactIDLbl.gridx = 0;
		gbc_newArtifactIDLbl.gridy = 2;
		tab_1_panel_center_right.add(newArtifactIDLbl, gbc_newArtifactIDLbl);
		newArtifactIDText = new JTextField();
		newArtifactIDText.setColumns(10);
		GridBagConstraints gbc_newArtifactIDText = new GridBagConstraints();
		gbc_newArtifactIDText.fill = GridBagConstraints.BOTH;
		gbc_newArtifactIDText.insets = new Insets(0, 0, 5, 0);
		gbc_newArtifactIDText.gridx = 1;
		gbc_newArtifactIDText.gridy = 2;
		tab_1_panel_center_right.add(newArtifactIDText, gbc_newArtifactIDText);

		JLabel newVersionLbl = new JLabel("Version");
		newVersionLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_newVersionLbl = new GridBagConstraints();
		gbc_newVersionLbl.fill = GridBagConstraints.BOTH;
		gbc_newVersionLbl.insets = new Insets(0, 0, 5, 5);
		gbc_newVersionLbl.gridx = 0;
		gbc_newVersionLbl.gridy = 3;
		tab_1_panel_center_right.add(newVersionLbl, gbc_newVersionLbl);
		newVersionText = new JTextField();
		newVersionText.setColumns(10);
		GridBagConstraints gbc_newVersionText = new GridBagConstraints();
		gbc_newVersionText.fill = GridBagConstraints.BOTH;
		gbc_newVersionText.insets = new Insets(0, 0, 5, 0);
		gbc_newVersionText.gridx = 1;
		gbc_newVersionText.gridy = 3;
		tab_1_panel_center_right.add(newVersionText, gbc_newVersionText);

		JLabel newNameLbl = new JLabel("Name");
		newNameLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_newNameLbl = new GridBagConstraints();
		gbc_newNameLbl.fill = GridBagConstraints.BOTH;
		gbc_newNameLbl.insets = new Insets(0, 0, 5, 5);
		gbc_newNameLbl.gridx = 0;
		gbc_newNameLbl.gridy = 4;
		tab_1_panel_center_right.add(newNameLbl, gbc_newNameLbl);
		newNameText = new JTextField();
		newNameText.setColumns(10);
		GridBagConstraints gbc_newNameText = new GridBagConstraints();
		gbc_newNameText.fill = GridBagConstraints.BOTH;
		gbc_newNameText.insets = new Insets(0, 0, 5, 0);
		gbc_newNameText.gridx = 1;
		gbc_newNameText.gridy = 4;
		tab_1_panel_center_right.add(newNameText, gbc_newNameText);

		JLabel newURLLbl = new JLabel("URL");
		newURLLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_newURLLbl = new GridBagConstraints();
		gbc_newURLLbl.fill = GridBagConstraints.BOTH;
		gbc_newURLLbl.insets = new Insets(0, 0, 5, 5);
		gbc_newURLLbl.gridx = 0;
		gbc_newURLLbl.gridy = 5;
		tab_1_panel_center_right.add(newURLLbl, gbc_newURLLbl);
		newURLText = new JTextField();
		newURLText.setColumns(10);
		GridBagConstraints gbc_newURLText = new GridBagConstraints();
		gbc_newURLText.fill = GridBagConstraints.BOTH;
		gbc_newURLText.insets = new Insets(0, 0, 5, 0);
		gbc_newURLText.gridx = 1;
		gbc_newURLText.gridy = 5;
		tab_1_panel_center_right.add(newURLText, gbc_newURLText);

		JLabel newDescriptionLbl = new JLabel("Description");
		newDescriptionLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_newDescriptionLbl = new GridBagConstraints();
		gbc_newDescriptionLbl.fill = GridBagConstraints.BOTH;
		gbc_newDescriptionLbl.insets = new Insets(0, 0, 0, 5);
		gbc_newDescriptionLbl.gridx = 0;
		gbc_newDescriptionLbl.gridy = 6;
		tab_1_panel_center_right.add(newDescriptionLbl, gbc_newDescriptionLbl);
		newDescriptionText = new JTextField();
		newDescriptionText.setColumns(10);
		GridBagConstraints gbc_newDescriptionText = new GridBagConstraints();
		gbc_newDescriptionText.fill = GridBagConstraints.BOTH;
		gbc_newDescriptionText.gridx = 1;
		gbc_newDescriptionText.gridy = 6;
		tab_1_panel_center_right.add(newDescriptionText, gbc_newDescriptionText);

		JButton button = new JButton("修改");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProjectInfo newProjectInfo = new ProjectInfo();
				newProjectInfo.setPackageName(newPackageText.getText());
				newProjectInfo.setGroupId(newGroupIDText.getText());
				newProjectInfo.setArtifactId(newArtifactIDText.getText());
				newProjectInfo.setVersion(newVersionText.getText());
				newProjectInfo.setName(newNameText.getText());
				newProjectInfo.setUrl(newURLText.getText());
				newProjectInfo.setDescription(newDescriptionText.getText());
				newProjectInfo.setProjectSrc(projectInfo.getProjectSrc());

				builderService.editProjectInfo(projectInfo, newProjectInfo);
				
				projectInfo = newProjectInfo;
			}
		});
		tab_1_panel_bottom.add(button);
	}

}
