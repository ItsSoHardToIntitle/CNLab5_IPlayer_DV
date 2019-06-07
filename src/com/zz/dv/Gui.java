package com.zz.dv;
/**
*@author Zhang Zhen
*@time 2019��6��3�� ����8:24:00
*/

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

public class Gui {
    private static Routers routers = new Routers();
    private static int[][] configInfoArray = null;
    private static int[][] isNeighbor = null;
    private static final Object[] SEPERATOR = new Object[] { "", "", "" };

    private static JFrame jFrame;
    private static JTable jTable;
    private static JTable jTable2;
    private static JTable jTable3;
    private static DefaultTableModel defaultTableModel;
    private static DefaultTableModel defaultTableModel2;
    private static DefaultTableModel defaultTableModel3;

    // ����
    private static Gui gui = new Gui();

    public Gui() {
        init();
        addControls();
        jFrame.setVisible(true);
    }

    public void init() {
        jFrame = new JFrame("OS_lab3");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setResizable(true);
        jFrame.setSize(600, 500);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
    }

    // ���������ļ�·������̨���ж�ȡ���̶���ʼ��·�ɱ�
    public String createFileChooser() {
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(fileSystemView.getHomeDirectory());
        jFileChooser.setDialogTitle("��ѡ�������ļ�");
        SwingUtilities.updateComponentTreeUI(jFileChooser);
        int returnVal = jFileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return jFileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public void showAll() {
        Iterator<Router> iterator = routers.getRouters().iterator();
        defaultTableModel3.setRowCount(0);
        while (iterator.hasNext()) {
            showRouterTable(iterator.next(), true);
            defaultTableModel3.addRow(SEPERATOR);
        }
    }

    public void showRouterTable(Router router, boolean isAll) {
        if (!isAll) {
            defaultTableModel3.setRowCount(0);
        }
        int temp = (int) 'A';
        String tempStr = "";
        int nextHopId = -1;
        ArrayList<RouterTableItem> routerTableItems = router.getRouterTableItems();
        for (int i = 0; i < routerTableItems.size(); i++) {
            Object[] objects = new Object[3];
            objects[0] = (char) (temp++);
            objects[1] = routerTableItems.get(i).getDistance();
            nextHopId = routerTableItems.get(i).getNextHop();
            if (nextHopId == router.getId() || (int) objects[1] == 16) {
                tempStr = "-";
            } else {
                tempStr = Routers.getNoRouterMap().get(nextHopId);
            }
            objects[2] = tempStr;
            defaultTableModel3.addRow(objects);
        }
    }

    public void initTitle() {
        int length = configInfoArray.length + 1;
        String[] titile = new String[length];
        int temp = (int) 'A';
        titile[0] = "";
        for (int i = 1; i < length; i++) {
            titile[i] = String.valueOf((char) (temp++));
        }
        defaultTableModel.setColumnIdentifiers(titile);
    }

    public void initTable() {
        int temp = (int) 'A';
        int length = configInfoArray.length;
        Object[][] objects = new Object[length][length + 1];
        for (int k = 0; k < length; k++) {
            objects[k][0] = (char) (temp++);
        }
        for (int i = 0; i < length; i++) {
            for (int j = 1; j < length + 1; j++) {
                objects[i][j] = configInfoArray[i][j - 1];
            }
            defaultTableModel.addRow(objects[i]);
        }
    }

    public void initRouterConfigInfoTable() {
        defaultTableModel.setRowCount(0);
        initTitle(); // ��ͷ
        initTable(); // ����
    }

    public void initIterations() {
        defaultTableModel2.setRowCount(0);
        int temp = (int) 'A';
        int length = configInfoArray.length + 1;
        String[] titile = new String[length];
        Object[] data = new Object[length];
        titile[0] = "";
        data[0] = "��������";
        for (int i = 1; i < length; i++) {
            titile[i] = String.valueOf((char) (temp++));
            data[i] = 0;
        }
        defaultTableModel2.setColumnIdentifiers(titile);
        defaultTableModel2.addRow(data);
    }

    public void renewDistance(int row, int col, int newDistance) {
        configInfoArray[row][col - 1] = newDistance;
        routers.init();
    }

    public void initIsNeighbor() {
        int length = configInfoArray.length;
        isNeighbor = new int[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (i == j || configInfoArray[i][j] == 16) {
                    isNeighbor[i][j] = 0;
                } else {
                    isNeighbor[i][j] = 1;
                }
            }
        }
    }

    public void addControls() {
        // ����ĺ�״����

        // ��һ��ˮƽbox������ �����ļ�ѡ��ť + ����� + ���°�ť
        Box hBox = Box.createHorizontalBox();

        JButton fileChooseButoon = new JButton("ѡ�������ļ�");
        // ���º󵯳��ļ�ѡ���
        fileChooseButoon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String configFilePath = createFileChooser();
                try {
                    configInfoArray = ReadConfigInfo.readConfigInfo(configFilePath);
                    initIsNeighbor();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                // ��ʼ��·�ɱ�չʾ, ���ɶ�Ӧ��ţ�A - 0�� B - 1���� �� ����
                initRouterConfigInfoTable();
                initIterations();
                routers.init();
            }
        });
        hBox.add(fileChooseButoon);

        JTextField inputTextField = new JTextField();
        inputTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // �س���Ӧ
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        String routerNo = inputTextField.getText();
                        if (!"ALL".equals(routerNo)) {
                            int no = Routers.getRouterNoMap().get(routerNo);
                            // ��ʾ��Ӧ��ŵ�·�ɵ�·�ɱ�
                            Router router = routers.getRouters().get(no);
                            showRouterTable(router, false);
                        } else {
                            showAll();
                        }
                    } catch (NullPointerException nullPointerException) {
                        JOptionPane.showMessageDialog(null, "������Ҫ�鿴��·�����ı��!", "����", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        });
        hBox.add(inputTextField);

        JButton renewButton = new JButton("����");
        renewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                routers.update();
                String routerNo = inputTextField.getText();
                if (!"ALL".equals(routerNo)) {
                    int no = Routers.getRouterNoMap().get(routerNo);
                    // ��ʾ��Ӧ��ŵ�·�ɵ�·�ɱ�
                    Router router = routers.getRouters().get(no);
                    showRouterTable(router, false);
                } else {
                    showAll();
                }
            }
        });
        hBox.add(renewButton);

        // �ڶ���ˮƽbox�������� �����ļ���Ϣ + �������� �� + ��ʾĳ��·������·�ɱ���Ϣ
        Box hBox2 = Box.createHorizontalBox();

        // �����Ǹ�
        Box vBox = Box.createVerticalBox();

        jTable = new JTable();
        defaultTableModel = (DefaultTableModel) jTable.getModel();
        defaultTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    if (row != -1 && col != -1) {
                        int newDistance = Integer.parseInt((String) defaultTableModel.getValueAt(row, col));
                        // ���³�ʼ��
                        renewDistance(row, col, newDistance);
                        // ���µ�����������д�����������
                        initIterations();
                        String routerNo = inputTextField.getText();
                        if (!"ALL".equals(routerNo)) {
                            int no = Routers.getRouterNoMap().get(routerNo);
                            // ��ʾ��Ӧ��ŵ�·�ɵ�·�ɱ�
                            Router router = routers.getRouters().get(no);
                            showRouterTable(router, false);
                        } else {
                            showAll();
                        }
                    }
                }
            }
        });
        JScrollPane jScrollPane = new JScrollPane(jTable);
        vBox.add(jScrollPane);

        jTable2 = new JTable();
        jTable2.setEnabled(false);
        defaultTableModel2 = (DefaultTableModel) jTable2.getModel();
        // ��֪ʶ�㣺���粻��Table����ScrollPane�У���ͷ�޷���ʾ
        JScrollPane jScrollPane2 = new JScrollPane(jTable2);
        vBox.add(jScrollPane2);
        hBox2.add(vBox);

        String[] title3 = new String[] { "", "Distance", "Next-hop" };
        jTable3 = new JTable();
        jTable3.setEnabled(false);
        defaultTableModel3 = (DefaultTableModel) jTable3.getModel();
        JScrollPane jScrollPane3 = new JScrollPane(jTable3);
        defaultTableModel3.setColumnIdentifiers(title3);
        hBox2.add(jScrollPane3);

        Box vBox2 = Box.createVerticalBox();
        vBox2.add(hBox);
        vBox2.add(hBox2);

        jFrame.add(vBox2);
    }

    public static Gui getGuiInstance() {
        if (gui == null) {
            gui = new Gui();
        }
        return gui;
    }

    public static int[][] getConfigInfoArray() {
        return configInfoArray;
    }

    public static int[][] getIsNeighbor() {
        return isNeighbor;
    }

    // ǰ��˲��÷��밡
    public static DefaultTableModel getDefaultTableModel2() {
        return defaultTableModel2;
    }

}
