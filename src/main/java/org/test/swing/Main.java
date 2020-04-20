package org.test.swing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        start();
    }

    public static void start() {

        final JFrame window = new JFrame("Swing");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        Object[][] rowData = {{"id", "name", "age"}};
        Object[] columnNames = {"id", "name", "age"};
        final DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);
        final JTable sqlDataTable = new JTable(tableModel);


        JButton sqlButtonRequest = new JButton("Запрос SQL");
        sqlButtonRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

//                try {
//                    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }

                Connection connection = null;
                try {
                    connection = DriverManager.getConnection("jdbc:postgres://localhost/testdb?", "postgres", "postgres");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    String query = "select * from cat;";
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet rs = statement.executeQuery();
                    sqlDataTable.setModel(tableModel);

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        String age = rs.getString("age");
                        tableModel.addRow(new Object[]{id, name, age});

                    }
                    connection.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        panel.add(sqlButtonRequest);
        panel.add(sqlDataTable);

        window.getContentPane().add(panel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }
}